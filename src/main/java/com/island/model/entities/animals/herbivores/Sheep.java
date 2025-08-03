package com.island.model.entities.animals.herbivores;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.Herbivore;

public class Sheep extends Herbivore {

    public Sheep() {
        super(AnimalConfig.SHEEP);
    }

    @Override
    protected Animal createOffspring() {
        return new Sheep();
    }
}
