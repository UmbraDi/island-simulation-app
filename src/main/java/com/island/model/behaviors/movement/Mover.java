package com.island.model.behaviors.movement;

import com.island.model.entities.animals.Animal;
import com.island.model.locations.Location;
import java.util.concurrent.ThreadLocalRandom;

public class Mover {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public void move(Animal animal, Location currentLoc) {
        int maxStep = animal.getMaxSpeed();

    }
}
