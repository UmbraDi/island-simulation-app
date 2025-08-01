package com.island.model.entities.animals;

import com.island.model.config.AnimalConfig;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Herbivore extends Animal {

    public Herbivore(AnimalConfig config) {
        super(config);
    }

    public double calculateMoveProbability() {
        boolean hasPredator = this.currentLocation.hasAnimalType(Predator.class);
        boolean hasFood = this.currentLocation.hasPlant();
        if (hasPredator) {
            return 0.9;
        } else if (hasFood) {
            return 0.25;
        } else {
            return 0.7;
        }
    }
}
