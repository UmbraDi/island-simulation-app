package com.island;

import com.island.model.Island;
import com.island.model.config.SimulationConfig;
import com.island.model.entities.animals.Animal;
import com.island.model.services.Simulation;

public class Main {
    public static void main(String[] args) {
        Island island = new Island();
        Simulation simulation = new Simulation(island);
        Animal.setSimulation(simulation);
        simulation.startSimulation();
        try {
            Thread.sleep(SimulationConfig.SIMULATION_DURATION_MINUTES * 60 * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Симуляция была прервана досрочно!");
        }
        simulation.stopSimulation();
        System.out.println("\n=== Финальное состояние острова ===");
    }
}
