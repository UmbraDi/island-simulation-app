package com.island.model.entities.animals;

import com.island.model.Island;
import com.island.model.config.AnimalConfig;
import com.island.model.entities.Entity;
import com.island.model.locations.Location;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends Entity {
    protected Location currentLocation;
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    protected final AnimalConfig config;

    public Animal(AnimalConfig config) {
        this.config = config;
    }

    public int getMaxSpeed() {
        return config.maxSpeed;
    }

    public void eat() {

    };

    public boolean move() {
        Island island = Island.getIsland();
        List<Location> neighbors = island.getNeighbors(currentLocation);
        if (neighbors.isEmpty()) return false;
        Location target = neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
        return performMove(target);
    };

    private boolean performMove(Location target) {
        currentLocation.removeAnimal(this);
        target.addAnimal(this);
        this.currentLocation = target;
        return true;
    }

    private void movement(int steps) {
        if (steps <= 0) return;
        if (random.nextDouble() < calculateMoveProbability()) {
            this.move();
            movement(steps - 1);
        }

    }

    public void movement() {
        int steps = this.config.maxSpeed;
        movement(steps);
    }

    protected abstract double calculateMoveProbability();

    public void reproduce() {

    };



}
