package com.island.model.entities.animals.herbivores;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.Herbivore;

public class Duck extends Herbivore {

    public Duck() {
        super(AnimalConfig.DUCK);
    }

    @Override
    protected Animal createOffspring() {
        return new Duck();
    }
}
