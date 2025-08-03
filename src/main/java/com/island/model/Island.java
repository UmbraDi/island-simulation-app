package com.island.model;

import com.island.model.config.AnimalConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.entities.plants.Plant;
import com.island.model.locations.Location;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Island {
    private final int width;
    private final int height;
    private final Location[][] locations;
    private static Island island;

    public Island() {
        this.width = 100;
        this.height = 100;
        this.locations = new Location[width][height];
        createLocations();
        spawnAnimals();
    }

    public int getIslandSize() {
        return width * height;
    }

    public static synchronized Island getIsland() {
        if (island == null) {
            island = new Island();
        }
        return island;
    }

    private void createLocations() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                locations[x][y] = new Location(x, y);
            }
        }
    }

    public Location[][] getLocations() {
        return locations;
    }

    private void spawnAnimals() {
        for (AnimalConfig config: AnimalConfig.values()) {
            for (int i = 0; i < config.getStartingValue(); i++) {
                Location location = getRandomLocation();
                location.addAnimal(config.createAnimal());
            }
        }
    }

    public Location getRandomLocation() {
        int x = ThreadLocalRandom.current().nextInt(width);
        int y = ThreadLocalRandom.current().nextInt(height);
        return locations[x][y];
    }

    public Location getLocation(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return locations[x][y];
        }
        return null;
    }

    public List<Location> getNeighbors(Location location) {
        return Stream.of(
            getLocation(location.getX(), location.getY() - 1),
                getLocation(location.getX() + 1, location.getY()),
                getLocation(location.getX(), location.getY() + 1),
                getLocation(location.getX() - 1, location.getY())
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<Animal> getAllAnimals() {
        return Arrays.stream(locations)
                .flatMap(Arrays::stream) // Преобразуем в поток локаций
                .flatMap(location -> location.getAnimals().values().stream())
                .flatMap(Set::stream) // Двойной flatMap для ConcurrentHashMap<Class, Set>
                .collect(Collectors.toList());
    }

    public List<Plant> getAllPlants() {
        return Arrays.stream(locations)
                .flatMap(Arrays::stream)
                .flatMap(location -> location.getPlants().stream())
                .collect(Collectors.toList());
    }


}
