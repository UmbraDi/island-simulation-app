package com.island.model.entities;

import com.island.model.locations.Location;

public abstract class Entity {
    protected Location location;

    protected boolean isAlive = true;

    public Location getLocation() {
        return location;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void die() {
        isAlive = false;

    }

    public abstract void update();

}
