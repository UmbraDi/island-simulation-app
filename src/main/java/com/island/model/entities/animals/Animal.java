package com.island.model.entities.animals;

import com.island.model.Island;
import com.island.model.config.AnimalConfig;
import com.island.model.config.SimulationConfig;
import com.island.model.entities.Entity;
import com.island.model.locations.Location;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public abstract class Animal extends Entity {
    protected Location currentLocation;
    protected static final ThreadLocalRandom random = ThreadLocalRandom.current();
    public final AnimalConfig config;
    protected double currentSatiety;
    protected Map<Class<? extends Animal>, Integer> diet;


    public Animal(AnimalConfig config) {
        this.config = config;
    }

    public int getMaxSpeed() {
        return config.maxSpeed;
    }

    protected void decreaseSatiety() {
        currentSatiety -= SimulationConfig.SATIETY_LOSS_PER_ACTION;
    }

    protected static boolean getProbability(double percent) {
        return random.nextDouble(100) < percent;
    }

    public abstract void eat();

    protected boolean move() {
        decreaseSatiety();
        Island island = Island.getIsland();
        List<Location> neighbors = island.getNeighbors(currentLocation);
        if (neighbors.isEmpty()) return false;
        Location target = neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
        return performMove(target);
    };

    private boolean performMove(Location target) {
        currentLocation.removeAnimal(this);
        target.addAnimal(this);
        currentLocation = target;
        return true;
    }

    private void movement(int steps) {
        if (steps <= 0) return;
        if (random.nextDouble() < calculateMoveProbability()) {
            move();
            movement(steps - 1);
        }

    }

    public void movement() {
        int steps = this.config.maxSpeed;
        movement(steps);
    }

    protected abstract double calculateMoveProbability();

    public double getCurrentSatiety() {
        return currentSatiety;
    }

    public void reproduce() {
        if (getPartner() != null && getProbability(config.reproductionChance)) {
            Animal offspring = createOffspring();
            currentLocation.addAnimal(offspring);
            decreaseSatiety();
            decreaseSatiety();
        }
    };

    protected abstract Animal createOffspring();

    protected Animal getPartner() {
        Set<Animal> possiblePartners = currentLocation.getAnimals().get(this.getClass()).stream()
                .filter(animal -> config.gender != animal.config.gender)
                .collect(Collectors.toSet());
        synchronized (possiblePartners) {
            if (possiblePartners.isEmpty()) return null;
            int randomIndex = random.nextInt(possiblePartners.size());
            Iterator<Animal> iterator = possiblePartners.iterator();
            for (int i = 0; i < randomIndex; i++) {
                iterator.next();
            }
            return iterator.next();
        }


    }

    public void die() {
        if (isAlive) {
            isAlive = false;
            if (currentLocation != null) {
                currentLocation.removeAnimal(this);
            }
        }
    }



}
