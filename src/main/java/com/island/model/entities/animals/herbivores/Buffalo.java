package com.island.model.entities.animals.herbivores;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.Herbivore;

public class Buffalo extends Herbivore {

    public Buffalo() {
        super(AnimalConfig.BUFFALO);
    }

    @Override
    protected Animal createOffspring() {
        return new Buffalo();
    }
}
