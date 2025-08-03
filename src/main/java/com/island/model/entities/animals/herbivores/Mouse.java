package com.island.model.entities.animals.herbivores;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.Herbivore;

public class Mouse extends Herbivore {

    public Mouse() {
        super(AnimalConfig.MOUSE);
    }

    @Override
    protected Animal createOffspring() {
        return new Mouse();
    }
}
