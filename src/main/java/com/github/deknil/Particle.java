package com.github.deknil;

import com.github.deknil.enums.ParticleType;

/**
 * Main class of particles
 */
public class Particle {
    public final ParticleType type;
    public int currentCellIndexX = 0;
    public int currentCellIndexY = 0;
    public float x;
    public float y;
    public float dx = 0;
    public float dy = 0;

   /**
     * Particle constructor
     * @param type particle type
     * @param x horizontal position
     * @param y vertical position
     */
    public Particle(ParticleType type, int x, int y) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
