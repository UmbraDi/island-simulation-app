package com.island.model.entities.animals;

import com.island.model.config.AnimalConfig;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Predator extends Animal {

    public Predator(AnimalConfig config) {
        super(config);
    }

    public double calculateMoveProbability() {
        boolean hasFood = this.currentLocation.hasAnimalType(Predator.class);
        if (hasFood) {
            return 0.9;
        } else {
            return 0.2;
        }
    }
}
