package com.island.model.entities.animals;

import com.island.model.config.AnimalConfig;
import com.island.model.locations.Location;


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

    @Override
    public void eat() {
        eatPlants(currentLocation);
    }

    protected boolean eatPlants(Location location) {
        double foodNeeded = config.satietyLimit - currentSatiety;
        double eaten = location.eatPlants(foodNeeded);
        currentSatiety += eaten;
        return eaten > 0;
    }
}
