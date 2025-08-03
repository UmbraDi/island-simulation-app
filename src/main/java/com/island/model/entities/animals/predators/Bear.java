package com.island.model.entities.animals.predators;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Predator;


public class Bear extends Predator {
    {
        diet = AnimalConfig.EatingProfile.BEAR_DIET;
    }

    public Bear() {
        super(AnimalConfig.BEAR);
    }


}
