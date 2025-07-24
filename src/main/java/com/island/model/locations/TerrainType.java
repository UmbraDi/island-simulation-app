package com.island.model.locations;

public enum TerrainType {
    FIELD(0.4, 2),
    FOREST(0.7, 3),
    RIVER(0.1, 1);

    private final double growthProbability;
    private final int maxGrowthAmount;

    TerrainType(double growthProbability, int maxGrowthAmount) {
        this.growthProbability = growthProbability;
        this.maxGrowthAmount = maxGrowthAmount;
    }

    public double getGrowthProbability() {
        return growthProbability;
    }

    public int getMaxGrowthAmount() {
        return maxGrowthAmount;
    }
}
