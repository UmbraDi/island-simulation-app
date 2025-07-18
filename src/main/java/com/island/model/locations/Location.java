package com.island.model.locations;

import com.island.model.entities.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Location {
    private final int x, y;
    private final TerrainType terrain;
    private final List<Entity> entities = new ArrayList<>();

    public Location(int x, int y, TerrainType terrain) {
        this.x = x;
        this.y = y;
        this.terrain = terrain;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public TerrainType getTerrain() {
        return terrain;
    }
}
