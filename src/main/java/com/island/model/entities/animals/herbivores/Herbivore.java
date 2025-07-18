package com.island.model.entities.animals.herbivores;

import com.island.model.entities.Entity;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.predators.Predator;

public abstract class Herbivore extends Animal {

    public Herbivore(double weight, double satietyLimit, double hungerThreshold) {
        super(weight, satietyLimit, hungerThreshold);
    }

    @Override
    public boolean isEdibleBy(Entity other) {
        return other instanceof Predator;
    }


}
