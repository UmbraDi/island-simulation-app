package com.island.model.entities.animals.herbivores;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.Herbivore;

public class Rabbit extends Herbivore {

    public Rabbit() {
        super(AnimalConfig.RABBIT);
    }

    @Override
    protected Animal createOffspring() {
        return new Rabbit();
    }
}
