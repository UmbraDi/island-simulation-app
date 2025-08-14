package com.island.model.services;

import com.island.model.Island;
import com.island.model.config.SimulationConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.predators.Wolf;
import com.island.model.locations.Location;
import com.island.util.StatisticsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation {
    public final StatisticsManager statsManager;

    private final Island island;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    private final ExecutorService animalExecutor = Executors.newWorkStealingPool();

    private volatile boolean isRunning = false;

    private final AtomicInteger dayCounter = new AtomicInteger(0);

    //private final AtomicInteger monthCounter = new AtomicInteger(0);

    public Simulation(Island island) {
        this.island = island;
        this.statsManager = new StatisticsManager(this);
    }

    public Island getIsland() {
        return island;
    }


    public void startSimulation() {
        if (isRunning) return;
        isRunning = true;
        scheduler.scheduleAtFixedRate(this::processDay,
                0,
                SimulationConfig.DAY_DURATION_MS,
                TimeUnit.MILLISECONDS);
    }

    private void processDay() {
        //System.out.println("[DEBUG] Processing day... Animals count: " + island.getAllAnimals().size());
        int currentDay = dayCounter.incrementAndGet();
        //int totalPlants = 0;
        for (Location[] row : island.getLocations()) {
            for (Location location : row) {
                location.growPlants();
                //totalPlants += location.getPlants().size();
            }
        }

        List<Callable<Void>> tasks = getCallables();
        try {
            animalExecutor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
//        Animal tracedAnimal = null;
//
//        if (tracedAnimal == null) {
//            tracedAnimal = island.getAllAnimals().stream()
//                    .filter(a -> a.getClass() == Wolf.class)
//                    .findFirst()
//                    .orElse(null);
//        }
//
//        if (tracedAnimal != null) {
//            System.out.printf("Трассировка %s: [%d,%d] сытость=%.1f%n",
//                    tracedAnimal.getClass().getSimpleName(),
//                    tracedAnimal.getLocation().getX(),
//                    tracedAnimal.getLocation().getY(),
//                    tracedAnimal.getCurrentSatiety());
//        }

        statsManager.updateDailyStats(currentDay);
    }

    private List<Callable<Void>> getCallables() {
        List<Callable<Void>> tasks = new ArrayList<>();
        for (Location[] row : island.getLocations()) {
            for (Location location : row) {
                for (Animal animal : location.getAnimalsList()) {
                    tasks.add(() -> {
                        if (animal.isAlive()) {
                            animal.movement();
                            if (animal.getCurrentSatiety() <
                                    animal.config.satietyLimit / animal.config.hungerThreshold) {
                                animal.eat();
                            }
                            animal.reproduce();
                            animal.decreaseSatiety();
                            if (animal.getCurrentSatiety() <= 0) {
                                statsManager.recordDeath(animal, StatisticsManager.DeathCause.STARVATION);
                                animal.die();
                            }



                        }
                        return null;
                    });
                }
            }
        }
        return tasks;
    }

    public void stopSimulation() {
        isRunning = false;
        scheduler.shutdownNow();
        animalExecutor.shutdownNow();
        System.out.println("\nИтоговое количество дней: " + dayCounter.get());
    }
}
