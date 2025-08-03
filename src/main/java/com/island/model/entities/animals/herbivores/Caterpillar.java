package com.island.model.entities.animals.herbivores;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.Herbivore;

public class Caterpillar extends Herbivore {

    public Caterpillar() {
        super(AnimalConfig.CATERPILLAR);
    }

    @Override
    protected Animal createOffspring() {
        return new Caterpillar();
    }
}
