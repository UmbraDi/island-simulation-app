package com.island.model.locations;

import com.island.model.entities.Entity;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.plants.Plant;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Location {
    private final int x, y;
    private final TerrainType terrain;
    private final ConcurrentHashMap<Class<? extends Animal>, Set<Animal>> animals = new ConcurrentHashMap<>();
    private final BlockingQueue<Plant> plants = new LinkedBlockingQueue<>(200);

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
        this.terrain = (ThreadLocalRandom.current().nextDouble() > 0.7) ? TerrainType.FOREST : TerrainType.FIELD;
        initializePlants();
    }

    public TerrainType getTerrain() {
        return terrain;
    }

    private void initializePlants() {
        for (int i = 0; i < plants.size() / 2; i++) {
            plants.add(new Plant());
        }
    }

    public void growPlants() {
        if (plants.size() >= 200) return;
        if (ThreadLocalRandom.current().nextDouble() < terrain.getGrowthProbability()) {
            int countPlants = ThreadLocalRandom.current().nextInt(1, terrain.getMaxGrowthAmount() + 1);
            for (int i = 0; i < countPlants && plants.size() < 200; i++) {
                plants.offer(new Plant());
            }
        }
    }

    public double eatPlants(double amount) {
        if (amount <= 0 || plants.isEmpty()) return 0;
        double totalEaten = 0;
        while (totalEaten < amount && !plants.isEmpty()) {
            Plant plant = plants.poll();
            totalEaten += plant.weight;
        }

        return totalEaten;
    }

    public BlockingQueue<Plant> getPlants() {
        return plants;
    }

    public boolean addAnimal(Animal animal) {
       return animals.computeIfAbsent(animal.getClass(),
               _ -> ConcurrentHashMap.newKeySet()).add(animal);
    }

    public boolean removeAnimal(Animal animal) {
        Set<Animal> set = animals.get(animal.getClass());
        return set != null && set.remove(animal);
    }

    public ConcurrentHashMap<Class<? extends Animal>, Set<Animal>> getAnimals() {
        return animals;
    }

    public boolean hasAnimalType(Class<? extends Animal> animalType) {
        return animals.entrySet().stream().anyMatch(animalType::isInstance);
    }

    public boolean hasPlant() {
        return !plants.isEmpty();
    }


}
