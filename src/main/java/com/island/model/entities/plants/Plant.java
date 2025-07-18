package com.island.model.entities.plants;

import com.island.model.entities.Entity;
import com.island.model.entities.animals.herbivores.Herbivore;

public class Plant extends Entity {
    static {
        maxPerLocation = 200;
    }

    protected Plant() {
        super(1.0);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isEdibleBy(Entity other) {
        return other instanceof Herbivore;
    }

    @Override
    public int getEdibilityProbability() {
        return 100;
    }
}
