package com.island.model.services;

import com.island.model.Island;
import com.island.model.config.SimulationConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.locations.Location;
import com.island.util.StatisticsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Simulation {
    private final StatisticsManager statsManager;

    private final Island island;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    private final ExecutorService animalExecutor = Executors.newWorkStealingPool();

    private volatile boolean isRunning = false;

    private final AtomicInteger dayCounter = new AtomicInteger(0);

    private final AtomicInteger monthCounter = new AtomicInteger(0);

    private final Map<Class<? extends Animal>, Long> birthRecords = new ConcurrentHashMap<>();
    private final Map<Class<? extends Animal>, Long> deathRecords = new ConcurrentHashMap<>();
    private final Map<Class<? extends Animal>, Long> previousPopulationStats = new ConcurrentHashMap<>();

    public Map<Class<? extends Animal>, Long> getPreviousPopulationStats() {
        return previousPopulationStats;
    }

    public Simulation(Island island) {
        this.island = island;
        this.statsManager = new StatisticsManager(this);
    }


    public void recordBirth(Animal animal) {
        birthRecords.merge(animal.getClass(), 1L, Long::sum);
    }

    public void recordDeath(Animal animal) {
        deathRecords.merge(animal.getClass(), 1L, Long::sum);
    }

    public void updatePopulationStats() {
        previousPopulationStats.clear();
        previousPopulationStats.putAll(getCurrentPopulationStats());
        birthRecords.clear();
        deathRecords.clear();
    }

    public Island getIsland() {
        return island;
    }

    private Map<Class<? extends Animal>, Long> getCurrentPopulationStats() {
        return island.getAllAnimals().stream()
                .collect(Collectors.groupingBy(
                        Animal::getClass,
                        Collectors.counting()
                ));
    }

    // Геттеры
    public Map<Class<? extends Animal>, Long> getBirthRecords() { return birthRecords; }
    public Map<Class<? extends Animal>, Long> getDeathRecords() { return deathRecords; }
    public Set<Class<? extends Animal>> getAnimalClasses() { return birthRecords.keySet(); }

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
        int totalPlants = 0;
        for (Location[] row : island.getLocations()) {
            for (Location location : row) {
                location.growPlants();
                totalPlants += location.getPlants().size();
            }
        }

        List<Callable<Void>> tasks = getCallables();
        try {
            animalExecutor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        updatePopulationStats();
        statsManager.printStatistics(currentDay);
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
