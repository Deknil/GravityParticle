package com.github.deknil.enums;

import java.awt.*;
import java.util.Random;

/**
 * List of available particle types
 */
public enum ParticleType {
    GREEN(new Color(128, 255, 128)),
    RED(new Color(255, 128, 128)),
    BLUE(new Color(128, 128, 255));

    public final Color color; // Color type

    /**
     * Type constructor
     * @param color the color for a specific type
     */
    ParticleType(Color color) {
        this.color = color;
    }

    /**
     * Getting a random type
     * @return random particle type
     */
    public static ParticleType getRandomParticleType() {
        ParticleType[] values = ParticleType.values();
        int index = new Random().nextInt(values.length);
        return values[index];
    }
}
