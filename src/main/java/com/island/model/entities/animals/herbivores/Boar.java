package com.island.model.entities.animals.herbivores;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.Herbivore;

public class Boar extends Herbivore {

    public Boar() {
        super(AnimalConfig.BOAR);
    }

    @Override
    protected Animal createOffspring() {
        return new Boar();
    }
}
