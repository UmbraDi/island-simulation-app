package com.island.model.entities.animals;

import com.island.model.config.AnimalConfig;
import com.island.model.config.SimulationConfig;
import com.island.model.entities.Entity;
import com.island.model.locations.Location;
import com.island.model.services.Simulation;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public abstract class Animal extends Entity {
    //public Island island;
    protected Location location;
    protected static final ThreadLocalRandom random = ThreadLocalRandom.current();
    public final AnimalConfig config;
    protected double currentSatiety;
    protected Map<Class<? extends Animal>, Integer> diet;
    protected static Simulation simulation;


    public void setLocation(Location location) {
        this.location = location;
    }

    public Animal(AnimalConfig config) {
        this.config = config;
        this.currentSatiety = config.satietyLimit;
    }



    public static void setSimulation(Simulation simulation) {
        Animal.simulation = simulation;
    }

    public int getMaxSpeed() {
        return config.maxSpeed;
    }

    public void decreaseSatiety() {
        currentSatiety -= SimulationConfig.SATIETY_LOSS_PER_ACTION;
    }

    protected static boolean getProbability(double percent) {
        return random.nextDouble(100) < percent;
    }

    public abstract void eat();

    public Location getLocation() {
        return location;
    }

    public boolean move() {
        List<Location> neighbors = simulation.getIsland().getNeighbors(location);
        if (neighbors.isEmpty()) return false;
        Location target = neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
        return performMove(target);
    };

    private boolean performMove(Location target) {
        location.removeAnimal(this);
        target.addAnimal(this);
        location = target;
        return true;
    }

    private void movement(int steps) {
        if (steps <= 0) return;
        if (random.nextDouble(1) < calculateMoveProbability()) {
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
            System.out.println("Создание потомка");
            location.addAnimal(offspring);
            simulation.recordBirth(offspring);
            decreaseSatiety();
            decreaseSatiety();
        }
    };

    protected abstract Animal createOffspring();

    protected Animal getPartner() {
        Set<Animal> possiblePartners = location.getAnimals().get(this.getClass()).stream()
                .filter(animal -> config.gender != animal.config.gender)
                .collect(Collectors.toSet());
            if (possiblePartners.isEmpty()) return null;
            int randomIndex = random.nextInt(possiblePartners.size());
            Iterator<Animal> iterator = possiblePartners.iterator();
            for (int i = 0; i < randomIndex; i++) {
                iterator.next();
            }
            return iterator.next();
    }

    public void die() {
        if (isAlive) {
            isAlive = false;
            if (location != null) {
                location.removeAnimal(this);
            }
            simulation.recordDeath(this);
        }
    }



}
