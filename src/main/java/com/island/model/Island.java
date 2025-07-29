package com.island.model;

import com.island.model.locations.Location;

public class Island {
    private final int width;
    private final int height;
    private final Location[][] locations;

    public Island() {
        this.width = 100;
        this.height = 100;
        this.locations = new Location[width][height];

    }

    public void createLocations() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                locations[x][y] = new Location(x, y);
            }
        }
    }


}
