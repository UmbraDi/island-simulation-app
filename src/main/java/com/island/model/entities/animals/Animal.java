package com.island.model.entities.animals;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.Entity;
import com.island.model.locations.Location;

public abstract class Animal extends Entity {

    protected final AnimalConfig config;

    public Animal(AnimalConfig config) {
        this.config = config;
    }

    public void eat() {

    };

    public void move() {

    };

    public void reproduce() {

    };

    @Override
    public void update() {

    }

    //    //Возможность размножения
//    public boolean canReproduceWith(Animal other) {
//        return this.gender != other.gender && this.getClass() == other.getClass();
//    }


}
