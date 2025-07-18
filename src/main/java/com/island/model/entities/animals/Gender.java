package com.island.model.entities.animals;

public enum Gender {
    BABY,
    MALE,
    FEMALE;

    public static Gender getRandomGender() {
        return Math.random() > 0.5 ? MALE : FEMALE;
    }
}
