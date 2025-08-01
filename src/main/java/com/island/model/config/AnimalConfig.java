package com.island.model.config;

import com.island.model.entities.animals.Animal;
import com.island.model.entities.animals.herbivores.*;
import com.island.model.entities.animals.predators.*;

import java.util.function.Supplier;

public enum AnimalConfig {

    //Хищники
    WOLF(50, 30, 3, 8, 25, 50, 50, Wolf::new),
    BOA(15, 30, 1, 3, 25, 50, 50, Boa::new),
    BEAR(500, 5, 2, 80, 25, 50, 50, Bear::new),
    EAGLE(6, 20, 3, 1, 25, 50, 50, Eagle::new),
    FOX(8, 30, 2, 2, 25, 50, 50, Fox::new),

    //Травоядные
    BOAR(400, 50, 2, 50, 25, 50, 50, Boar::new),
    BUFFALO(700, 10, 3, 100, 25, 50, 50, Buffalo::new),
    CATERPILLAR(0.01, 1000, 0, 0, 25, 50, 50, Caterpillar::new),
    DEER(300, 20, 4, 50, 25, 50, 50, Deer::new),
    DUCK(1, 200, 4, 0.15, 25, 50, 50, Duck::new),
    GOAT(60, 140, 3, 10, 25, 50, 50, Goat::new),
    HORSE(400, 20, 4, 60, 25, 50, 50, Horse::new),
    MOUSE(0.05, 500, 1, 0.01, 25, 50, 50, Mouse::new),
    RABBIT(2, 150, 2, 0.45, 25, 50, 50, Rabbit::new),
    SHEEP(70, 140, 3, 15, 25, 50, 50, Sheep::new);

    public final double weight;

    public final int maxPerLocation;

    public final int maxSpeed;

    //Уровень сытости
    public final double satietyLimit;

    //Скорость набора голода
    public final int starvationRate;

    //Уровень сытости при котором нужно поесть
    public final int hungerThreshold;

    public final double reproductionChance;

    public final Supplier<Animal> factory;

    public final Gender gender;

    public enum Gender {
        BABY,
        MALE,
        FEMALE;

        public static Gender getRandomGender() {
            return Math.random() > 0.5 ? MALE : FEMALE;
        }
    }

    AnimalConfig(double weight, int maxPerLocation, int maxSpeed, double satietyLimit,
                 int starvationRate, int hungerThreshold, double reproductionChance,
                 Supplier<Animal> factory) {
        this.weight = weight;
        this.maxPerLocation = maxPerLocation;
        this.maxSpeed = maxSpeed;
        this.satietyLimit = satietyLimit;
        this.starvationRate = starvationRate;
        this.hungerThreshold = hungerThreshold;
        this.reproductionChance = reproductionChance;
        this.factory = factory;
        this.gender = Gender.getRandomGender();
    }

    public Animal createAnimal() {
        return factory.get();
    }

    public int getStartingValue() {
        return switch (this) {
            case WOLF -> StartingValue.WOLF;
            case BOA -> StartingValue.BOA;
            case FOX -> StartingValue.FOX;
            case BEAR -> StartingValue.BEAR;
            case EAGLE -> StartingValue.EAGLE;
            case HORSE -> StartingValue.HORSE;
            case DEER -> StartingValue.DEER;
            case RABBIT -> StartingValue.RABBIT;
            case MOUSE -> StartingValue.MOUSE;
            case GOAT -> StartingValue.GOAT;
            case SHEEP -> StartingValue.SHEEP;
            case BOAR -> StartingValue.BOAR;
            case BUFFALO -> StartingValue.BUFFALO;
            case DUCK -> StartingValue.DUCK;
            case CATERPILLAR -> StartingValue.CATERPILLAR;
        };
    }

    public static final class StartingValue {
        public static final int WOLF = 30;
        public static final int BOA = 20;
        public static final int FOX = 30;
        public static final int BEAR = 5;
        public static final int EAGLE = 15;
        public static final int HORSE = 20;
        public static final int DEER = 20;
        public static final int RABBIT = 100;
        public static final int MOUSE = 300;
        public static final int GOAT = 100;
        public static final int SHEEP = 100;
        public static final int BOAR = 40;
        public static final int BUFFALO = 10;
        public static final int DUCK = 150;
        public static final int CATERPILLAR = 500;
    }

    public static final class EatingChance {
        // ========= ВОЛК =========
        public static final int WOLF_RABBIT = 60;
        public static final int WOLF_MOUSE = 80;
        public static final int WOLF_GOAT = 60;
        public static final int WOLF_SHEEP = 70;
        public static final int WOLF_HORSE = 10;
        public static final int WOLF_DEER = 15;
        public static final int WOLF_BOAR = 15;
        public static final int WOLF_BUFFALO = 10;
        public static final int WOLF_DUCK = 40;

        // ========= УДАВ =========
        public static final int BOA_FOX = 15;
        public static final int BOA_RABBIT = 20;
        public static final int BOA_MOUSE = 40;
        public static final int BOA_DUCK = 10;

        // ========= ЛИСА =========
        public static final int FOX_RABBIT = 70;
        public static final int FOX_MOUSE = 90;
        public static final int FOX_DUCK = 60;
        public static final int FOX_CATERPILLAR = 40;

        // ========= МЕДВЕДЬ =========
        public static final int BEAR_BOA = 80;
        public static final int BEAR_HORSE = 40;
        public static final int BEAR_DEER = 80;
        public static final int BEAR_RABBIT = 80;
        public static final int BEAR_MOUSE = 90;
        public static final int BEAR_GOAT = 70;
        public static final int BEAR_SHEEP = 70;
        public static final int BEAR_BOAR = 50;
        public static final int BEAR_DUCK = 10;

        // ========= ОРЕЛ =========
        public static final int EAGLE_FOX = 10;
        public static final int EAGLE_RABBIT = 90;
        public static final int EAGLE_MOUSE = 90;
        public static final int EAGLE_DUCK = 80;

        // ========= ОСОБЫЕ СЛУЧАИ =========
        public static final int MOUSE_CATERPILLAR = 90;  // Мышь ест гусеницу
        public static final int BOAR_CATERPILLAR = 90;  // Кабан ест гусеницу
        public static final int DUCK_CATERPILLAR = 90;   // Утка ест гусеницу
    }
}
