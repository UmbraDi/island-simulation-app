package com.island.model.entities.animals.herbivores;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.CaterpillarEater;
import com.island.model.entities.animals.Herbivore;

public class Boar extends Herbivore implements CaterpillarEater {

    public Boar() {
        super(AnimalConfig.BOAR);
    }

    @Override
    protected Animal createOffspring() {

        return new Boar();
    }

    @Override
    public int getCaterpillarEatingChance() {
        return AnimalConfig.EatingProfile.BOAR_CATERPILLAR;
    }
}
