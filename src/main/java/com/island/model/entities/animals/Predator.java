package com.island.model.entities.animals;

import com.island.model.config.AnimalConfig;
import com.island.util.StatisticsManager;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Predator extends Animal {


    public Predator(AnimalConfig config) {
        super(config);
    }



    public double calculateMoveProbability() {
        if (hasPrey()) {
            return 0.9;
        } else {
            return 0.4;
        }
    }

    protected boolean hasPrey(){
        return this.location.getAnimals().keySet().stream().anyMatch(diet::containsKey);
    };

    protected Map<Class<? extends Animal>, Set<Animal>> getPreys() {
        return location.getAnimals().entrySet().stream()
                .filter(entry -> diet.containsKey(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    protected Animal getPrey() {
        Map<Class<? extends Animal>, Set<Animal>> filteredAnimals = getPreys();
        Optional<Animal> result = diet.entrySet().stream()
                .filter(entry -> filteredAnimals.containsKey(entry.getKey()))
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .flatMap(entry -> filteredAnimals.get(entry.getKey()).stream())
                .findFirst();
        return result.orElse(null);
    }



    @Override
    public void eat() {
        if (!hasPrey()) return;
        Animal prey = getPrey();
        if (prey == null) return;
        if (getProbability(diet.get(prey.getClass()))) {
            double nutrition = Math.min(prey.config.weight, config.satietyLimit - currentSatiety);
            currentSatiety += nutrition;
            simulation.statsManager.recordDeath(prey, StatisticsManager.DeathCause.EATEN);
            prey.die();
        }
        //System.out.println("Скушал " + prey.getClass());

    }
}
