package com.island.model.entities.animals.herbivores;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.Herbivore;

public class Goat extends Herbivore {

    public Goat() {
        super(AnimalConfig.GOAT);
    }

    @Override
    protected Animal createOffspring() {
        return new Goat();
    }
}
