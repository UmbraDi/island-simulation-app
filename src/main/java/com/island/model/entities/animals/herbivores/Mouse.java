package com.island.model.entities.animals.herbivores;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.CaterpillarEater;
import com.island.model.entities.animals.Herbivore;

public class Mouse extends Herbivore implements CaterpillarEater {

    public Mouse() {
        super(AnimalConfig.MOUSE);
    }

    @Override
    protected Animal createOffspring() {
        return new Mouse();
    }

    @Override
    public int getCaterpillarEatingChance() {
        return AnimalConfig.EatingProfile.MOUSE_CATERPILLAR;
    }
}
