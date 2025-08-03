package com.island.model.entities.animals.predators;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.Predator;


public class Eagle extends Predator {
    {
        diet = AnimalConfig.EatingProfile.EAGLE_DIET;
    }

    public Eagle() {
        super(AnimalConfig.EAGLE);
    }

    @Override
    protected Animal createOffspring() {
        return new Eagle();
    }
}
