package com.island.model.entities.animals;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.Entity;
import com.island.model.locations.Location;

public abstract class Animal extends Entity {
    protected Location location;

    protected final AnimalConfig config;

    public Animal(AnimalConfig config) {
        this.config = config;
    }

    public int getMaxSpeed() {
        return config.maxSpeed;
    }

    public void eat() {

    };

    public void move() {

    };

    public void reproduce() {

    };



}
