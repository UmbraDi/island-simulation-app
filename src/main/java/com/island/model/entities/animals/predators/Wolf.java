package com.island.model.entities.animals.predators;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Predator;


public class Wolf extends Predator {
    {
        diet = AnimalConfig.EatingProfile.WOLF_DIET;
    }

    public Wolf() {
        super(AnimalConfig.WOLF);
    }


}
