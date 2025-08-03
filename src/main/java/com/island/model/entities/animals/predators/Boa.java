package com.island.model.entities.animals.predators;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.Predator;


public class Boa extends Predator {
    {
        diet = AnimalConfig.EatingProfile.BOA_DIET;
    }

    public Boa() {
        super(AnimalConfig.BOA);
    }

    @Override
    protected Animal createOffspring() {
        return new Boa();
    }
}
