package com.island.model.services;

import com.island.model.Island;
import com.island.model.config.SimulationConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.locations.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation {
    private final Island island;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    private final ExecutorService animalExecutor = Executors.newWorkStealingPool();

    private volatile boolean isRunning = false;

    private final AtomicInteger dayCounter = new AtomicInteger(0);

    private final AtomicInteger monthCounter = new AtomicInteger(0);

    public Simulation(Island island) {
        this.island = island;
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
        int currentDay = dayCounter.incrementAndGet();
        int totalPlants = 0;
        for (Location[] row : island.getLocations()) {
            for (Location location : row) {
                location.growPlants();
                totalPlants += location.getPlants().size();
            }
        }

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


                        }
                        return null;
                    });
                }
            }
        }
        try {
            animalExecutor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stopSimulation() {
        isRunning = false;
        scheduler.shutdownNow();
        animalExecutor.shutdownNow();
        System.out.println("\nИтоговое количество дней: " + dayCounter.get());
    }
}
