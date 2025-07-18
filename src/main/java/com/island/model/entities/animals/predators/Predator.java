package com.island.model.entities.animals.predators;

import com.island.model.entities.Entity;
import com.island.model.entities.animals.Animal;

public abstract class Predator extends Animal {

    public Predator(double weight, double satietyLimit, double hungerThreshold) {
        super(weight, satietyLimit, hungerThreshold);
    }

    @Override
    public boolean isEdibleBy(Entity other) {
        return false;
    }
}
