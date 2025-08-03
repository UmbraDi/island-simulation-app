package com.island.util;

import com.island.model.entities.animals.Animal;
import com.island.model.entities.plants.Plant;
import com.island.model.services.Simulation;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class StatisticsManager {
    private final Simulation simulation;
    private int lastReportDay = 0;

    public StatisticsManager(Simulation simulation) {
        this.simulation = simulation;
    }

    public void printStatistics(int currentDay) {
        if (currentDay - lastReportDay >= 30) {
            printPopulationStats();
            printPlantStats();
            printSurvivalRates();
            lastReportDay = currentDay;
        }
    }

    private void printPopulationStats() {
        Map<Class<? extends Animal>, Long> populations = simulation.getIsland().getAllAnimals().stream()
                .collect(Collectors.groupingBy(
                        Animal::getClass,
                        Collectors.counting()
                ));

        System.out.println("\n=== POPULATION STATISTICS ===");
        System.out.printf("%-20s | %-6s | %s%n", "Species", "Count", "Trend");

        populations.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(Class::getSimpleName)))
                .forEach(entry -> {
                    String species = entry.getKey().getSimpleName();
                    long count = entry.getValue();
                    String trend = getPopulationTrend(entry.getKey(), count);
                    System.out.printf("%-20s | %-6d | %s%n", species, count, trend);
                });
    }

    private String getPopulationTrend(Class<? extends Animal> animalClass, long currentCount) {
        // Реализация сравнения с предыдущим периодом
        Long previousCount = simulation.getPreviousPopulationStats().get(animalClass);
        if (previousCount == null) return "-";

        long difference = currentCount - previousCount;
        if (difference > 0) return String.format("↑ +%d", difference);
        if (difference < 0) return String.format("↓ %d", difference);
        return "→";
    }

    private void printPlantStats() {
        List<Plant> allPlants = simulation.getIsland().getAllPlants();
        int totalPlants = allPlants.size();
        int totalLocations = simulation.getIsland().getIslandSize();

        // Рассчитываем плотность
        double density = totalLocations > 0 ? (double) totalPlants / totalLocations : 0;

        System.out.println("\n=== PLANT STATISTICS ===");
        System.out.printf("Total plants: %d | Density: %.2f plants/cell%n", totalPlants, density);
    }

    private void printSurvivalRates() {
        System.out.println("\n=== SURVIVAL RATES ===");

        // Статистика рождений
        System.out.println("Births (last 30 days):");
        simulation.getBirthRecords().entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .forEach(entry -> System.out.printf("%-15s: %d%n",
                        entry.getKey().getSimpleName(), entry.getValue()));

        // Статистика смертей
        System.out.println("\nDeaths (last 30 days):");
        simulation.getDeathRecords().entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .forEach(entry -> System.out.printf("%-15s: %d%n",
                        entry.getKey().getSimpleName(), entry.getValue()));

        // Коэффициент выживаемости
        System.out.println("\nSurvival Rate:");
        simulation.getAnimalClasses().forEach(animalClass -> {
            long births = simulation.getBirthRecords().getOrDefault(animalClass, 0L);
            long deaths = simulation.getDeathRecords().getOrDefault(animalClass, 0L);
            if (births + deaths > 0) {
                double rate = (double) births / (births + deaths) * 100;
                System.out.printf("%-15s: %.1f%%%n", animalClass.getSimpleName(), rate);
            }
        });
    }


}
