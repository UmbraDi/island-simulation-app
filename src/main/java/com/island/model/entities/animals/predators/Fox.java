package com.island.model.entities.animals.predators;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.Predator;


public class Fox extends Predator {
    {
        diet = AnimalConfig.EatingProfile.FOX_DIET;
    }

    public Fox() {
        super(AnimalConfig.FOX);
    }

    @Override
    protected Animal createOffspring() {
        return new Fox();
    }
}
