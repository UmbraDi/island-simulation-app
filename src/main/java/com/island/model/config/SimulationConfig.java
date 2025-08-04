package com.island.model.config;

public class SimulationConfig {

    private SimulationConfig() {
    }

    public static final int ISLAND_WIDTH = 10;

    public static final int ISLAND_HEIGHT = 10;

    public static final int DAY_DURATION_MS = 100;

    public static final int STATISTICS_PRINT_INTERVAL_DAYS = 30;

    public static final int ISLAND_PRINT_INTERVAL_DAYS = 30;

    public static final int SIMULATION_DURATION_MINUTES = 10;

    public static final double INITIAL_SATIETY_PERCENT = 0.7;

    public static final double DAILY_SATIETY_LOSS = 0.3;

    public static final double SATIETY_LOSS_PER_ACTION = 0.3;
}
