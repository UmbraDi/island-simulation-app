package com.island.model.entities.animals;

import com.island.model.entities.Entity;

public abstract class Animal extends Entity {
    protected static int maxMovementSpeed;
    protected static double starvationRate; //Скорость набора голода
    protected final double satietyLimit; //Лимит сытости
    protected final double hungerThreshold; //Уровень сытости при котором нужно поесть
    protected final Gender gender;

    public Animal(double weight, double satietyLimit, double hungerThreshold) {
        super(weight);
        this.satietyLimit = satietyLimit;
        this.hungerThreshold = hungerThreshold;
        this.gender = Gender.getRandomGender();
    }

    public abstract void eat();

    public abstract void move();

    public abstract void reproduce();

    //Возможность размножения
    public boolean canReproduceWith(Animal other) {
        return this.gender != other.gender && this.getClass() == other.getClass();
    }


}
