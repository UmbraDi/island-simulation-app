package com.island.model.entities.animals;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.herbivores.Caterpillar;
import com.island.model.locations.Location;
import com.island.util.StatisticsManager;

import java.util.ArrayList;
import java.util.List;


public abstract class Herbivore extends Animal {

    public Herbivore(AnimalConfig config) {
        super(config);
    }

    public double calculateMoveProbability() {
        boolean hasPredator = this.location.hasAnimalType(Predator.class);
        boolean hasFood = this.location.hasPlant();
        if (hasPredator) {
            return 0.9;
        } else if (hasFood) {
            return 0.25;
        } else {
            return 0.7;
        }
    }

    @Override
    public void eat() {
        if (this instanceof CaterpillarEater) {
            tryEatCaterpillar(location);
            return;
        }
        eatPlants(location);
    }

    protected boolean eatPlants(Location location) {
        double foodNeeded = config.satietyLimit - currentSatiety;
        double eaten = location.eatPlants(foodNeeded);
        //System.out.println("Покушал травы: " + eaten);
        currentSatiety += eaten;
        return eaten > 0;
    }

    protected boolean tryEatCaterpillar(Location location) {
        if (!(this instanceof CaterpillarEater eater)) return false;
        int chance = eater.getCaterpillarEatingChance();
        List<Animal> caterpillars = new ArrayList<>(location.getAnimals().get(Caterpillar.class));
        if (caterpillars.isEmpty()) return false;
        if (getProbability(chance)) {
            Animal caterpillar = caterpillars.remove(random.nextInt(caterpillars.size()));
            double nutrition = Math.min(caterpillar.config.weight, config.satietyLimit - currentSatiety);
            currentSatiety += nutrition;
            simulation.statsManager.recordDeath(caterpillar, StatisticsManager.DeathCause.EATEN);
            caterpillar.die();
            return true;
        }
        return false;
    }
}
