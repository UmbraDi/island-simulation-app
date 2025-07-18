package com.island.model.entities;

import com.island.model.locations.Location;

public abstract class Entity {
    protected Location location;
    protected final double weight;
    protected static int maxPerLocation;
    protected boolean isAlive = true;

    protected Entity(double weight) {
        this.weight = weight;
    }

    public Location getLocation() {
        return location;
    }

    public double getWeight() {
        return weight;
    }

    public static int getMaxPerLocation() {
        return maxPerLocation;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void die() {
        isAlive = false;

    }

    public abstract void update();

    public abstract boolean isEdibleBy(Entity other);

    public abstract int getEdibilityProbability();
}
