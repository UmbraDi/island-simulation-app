package com.island.model.entities.animals;

import com.island.model.entities.Entity;

public abstract class Animal extends Entity {
    protected static int maxMovementSpeed;
    protected static double starvationRate;
    protected final double satietyLimit;
    protected final double hungerThreshold;
    protected final Gender gender;

    public Animal(double weight, double satietyLimit, double hungerThreshold) {
        super(weight);
        this.satietyLimit = satietyLimit;
        this.hungerThreshold = hungerThreshold;
        this.gender = Gender.getRandomGender();
    }

    public boolean canReproduceWith(Animal other) {
        return this.gender != other.gender && this.getClass() == other.getClass();
    }


}
