package com.island.model.locations;

import com.island.model.entities.*;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.plants.Plant;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Location {
    private final int x, y;
    private final TerrainType terrain;
    private final ConcurrentHashMap<Class<? extends Animal>, Set<Animal>> animals = new ConcurrentHashMap<>();
    private final BlockingQueue<Plant> plants = new LinkedBlockingQueue<>(200);

    public Location(int x, int y, TerrainType terrain) {
        this.x = x;
        this.y = y;
        this.terrain = terrain;
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

    public Plant consumePlant() {
        return plants.poll();
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
}
