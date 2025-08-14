package com.island.util;

import com.island.model.config.SimulationConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.Herbivore;
import com.island.model.entities.animals.Predator;
import com.island.model.services.Simulation;

import java.util.*;
import java.util.stream.Collectors;


public class StatisticsManager {
    private final Simulation simulation;
    private final Map<Class<? extends Animal>, Long> previousPopulation = new HashMap<>();
    private final Map<Class<? extends Animal>, Long> currentBirths = new HashMap<>();
    private final Map<Class<? extends Animal>, Long> currentDeaths = new HashMap<>();
    private final Map<Class<? extends Animal>, Long> starvationDeaths = new HashMap<>();
    private final Map<Class<? extends Animal>, Long> eatenDeaths = new HashMap<>();
    private final Map<Class<? extends Animal>, Long> otherDeaths = new HashMap<>();
    private int lastReportDay = 0;

    public StatisticsManager(Simulation simulation) {
        this.simulation = simulation;
    }

    public void updateDailyStats(int currentDay) {
        // Собираем текущую статистику
        Map<Class<? extends Animal>, Long> currentPopulation = getCurrentPopulations();

        if (currentDay - lastReportDay >= SimulationConfig.STATISTICS_PRINT_INTERVAL_DAYS) {
            printStatistics(currentDay);

            // Сохраняем данные для следующего периода
            previousPopulation.clear();
            previousPopulation.putAll(currentPopulation);
            currentBirths.clear();
            currentDeaths.clear();
            starvationDeaths.clear();
            eatenDeaths.clear();
            lastReportDay = currentDay;
        }
    }

    public void recordBirth(Animal animal) {
        currentBirths.merge(animal.getClass(), 1L, Long::sum);
    }

    public void recordDeath(Animal animal, DeathCause cause) {
        currentDeaths.merge(animal.getClass(), 1L, Long::sum);
        switch (cause) {
            case STARVATION -> starvationDeaths.merge(animal.getClass(), 1L, Long::sum);
            case EATEN -> eatenDeaths.merge(animal.getClass(), 1L, Long::sum);
            case OTHER -> otherDeaths.merge(animal.getClass(), 1L, Long::sum);
        }
    }

    public enum DeathCause {
        STARVATION, // Смерть от голода
        EATEN,      // Было съедено
        OTHER       // Другие причины
    }

    private void printDeathStatistics() {
        System.out.println("\nПРИЧИНЫ СМЕРТНОСТИ:");
        System.out.println("-".repeat(60));
        System.out.printf("%-15s | %-12s | %-12s | %-12s | %-10s%n",
                "Вид", "От голода", "Съедено", "Другие", "Всего");
        System.out.println("-".repeat(60));

        // Собираем все виды, у которых были смерти
        Set<Class<? extends Animal>> allDeadTypes = new HashSet<>();
        allDeadTypes.addAll(starvationDeaths.keySet());
        allDeadTypes.addAll(eatenDeaths.keySet());
        allDeadTypes.addAll(otherDeaths.keySet());

        allDeadTypes.stream()
                .sorted(Comparator.comparing(Class::getSimpleName))
                .forEach(type -> {
                    String name = getRussianName(type.getSimpleName());
                    long starved = starvationDeaths.getOrDefault(type, 0L);
                    long eaten = eatenDeaths.getOrDefault(type, 0L);
                    long other = otherDeaths.getOrDefault(type, 0L);
                    long total = starved + eaten + other;

                    System.out.printf("%-15s | %-12d | %-12d | %-12d | %-10d%n",
                            name, starved, eaten, other, total);
                });

        // Итоговая статистика
        long totalStarved = starvationDeaths.values().stream().mapToLong(Long::longValue).sum();
        long totalEaten = eatenDeaths.values().stream().mapToLong(Long::longValue).sum();
        long totalOther = otherDeaths.values().stream().mapToLong(Long::longValue).sum();

        System.out.println("-".repeat(60));
        System.out.printf("%-15s | %-12d | %-12d | %-12d | %-10d%n",
                "ВСЕГО",
                totalStarved,
                totalEaten,
                totalOther,
                totalStarved + totalEaten + totalOther);
    }

    private Map<Class<? extends Animal>, Long> getCurrentPopulations() {
        return simulation.getIsland().getAllAnimals().stream()
                .collect(Collectors.groupingBy(
                        Animal::getClass,
                        Collectors.counting()
                ));
    }

    private void printStatistics(int currentDay) {
        System.out.println("\n" + "=".repeat(60));
        System.out.printf("СТАТИСТИКА ЗА ПЕРИОД ДНИ %d-%d%n", lastReportDay + 1, currentDay);
        System.out.println("=".repeat(60));

        printSummaryStats();
        printPopulationDetails();
        printDeathStatistics();
        printFoodChainBalance();
    }

    private void printSummaryStats() {
        long totalAnimals = getCurrentPopulations().values().stream().mapToLong(Long::longValue).sum();
        long totalBirths = currentBirths.values().stream().mapToLong(Long::longValue).sum();
        long totalDeaths = currentDeaths.values().stream().mapToLong(Long::longValue).sum();

        System.out.println("\nОБЩАЯ СТАТИСТИКА:");
        System.out.println("-".repeat(40));
        System.out.printf("Всего животных: %d (%+d)%n",
                totalAnimals,
                totalAnimals - previousPopulation.values().stream().mapToLong(Long::longValue).sum());

        System.out.printf("Хищники: %d | Травоядные: %d%n",
                countByType(Predator.class),
                countByType(Herbivore.class));

        System.out.printf("Рождений: %d | Смертей: %d | Коэффициент: %.1f%%%n",
                totalBirths,
                totalDeaths,
                totalBirths + totalDeaths > 0 ? (totalBirths * 100.0 / (totalBirths + totalDeaths)) : 0);
    }

    private void printPopulationDetails() {
        System.out.println("\nДЕТАЛИ ПО ВИДАМ:");
        System.out.println("-".repeat(90));
        System.out.printf("%-15s | %-8s | %-8s | %-8s | %-10s | %-8s | %-8s | %-10s%n",
                "Вид", "Текущее", "Было", "Изменение", "Рождено", "Умерло", "Баланс", "Выживаемость");
        System.out.println("-".repeat(90));

        getCurrentPopulations().entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getSimpleName()))
                .forEach(entry -> {
                    Class<? extends Animal> type = entry.getKey();
                    String name = getRussianName(type.getSimpleName());
                    long current = entry.getValue();
                    long previous = previousPopulation.getOrDefault(type, 0L);
                    long births = currentBirths.getOrDefault(type, 0L);
                    long deaths = currentDeaths.getOrDefault(type, 0L);
                    long balance = births - deaths;

                    System.out.printf("%-15s | %-8d | %-8d | %-8s | %-10d | %-8d | %-8s | %-10s%n",
                            name,
                            current,
                            previous,
                            formatChange(current - previous),
                            births,
                            deaths,
                            formatChange(balance),
                            calculateSurvivalRate(births, deaths));
                });
    }

    private void printFoodChainBalance() {
        System.out.println("\nБАЛАНС ЭКОСИСТЕМЫ:");
        System.out.println("-".repeat(50));

        long predators = countByType(Predator.class);
        long herbivores = countByType(Herbivore.class);
        double ratio = predators > 0 ? (double) herbivores / predators : 0;

        System.out.printf("Соотношение жертва/хищник: %.1f:1%n", ratio);
        System.out.printf("Средний уровень сытости хищников: %.1f%%%n",
                getAveragePredatorSatiety());
        System.out.printf("Растений: %d (%.1f/клетку)%n",
                simulation.getIsland().getAllPlants().size(),
                (double) simulation.getIsland().getAllPlants().size() / simulation.getIsland().getIslandSize());
    }

    // Вспомогательные методы
    private long countByType(Class<?> animalType) {
        return simulation.getIsland().getAllAnimals().stream()
                .filter(animalType::isInstance)
                .count();
    }

    private double getAveragePredatorSatiety() {
        return simulation.getIsland().getAllAnimals().stream()
                .filter(Predator.class::isInstance)
                .mapToDouble(p -> p.getCurrentSatiety() / p.config.satietyLimit * 100)
                .average()
                .orElse(0);
    }

    private String formatChange(long change) {
        return change > 0 ? "+" + change : String.valueOf(change);
    }

    private String calculateSurvivalRate(long births, long deaths) {
        if (births + deaths == 0) return "н/д";
        return String.format("%.1f%%", births * 100.0 / (births + deaths));
    }

    private String getRussianName(String className) {
        return switch (className) {
            case "Wolf" -> "Волк";
            case "Bear" -> "Медведь";
            case "Fox" -> "Лиса";
            case "Rabbit" -> "Кролик";
            case "Deer" -> "Олень";
            case "Mouse" -> "Мышь";
            case "Goat" -> "Коза";
            case "Sheep" -> "Овца";
            case "Boar" -> "Кабан";
            case "Buffalo" -> "Буйвол";
            case "Duck" -> "Утка";
            case "Caterpillar" -> "Гусеница";
            case "Eagle" -> "Орел";
            case "Boa" -> "Удав";
            default -> className;
        };
    }
}
