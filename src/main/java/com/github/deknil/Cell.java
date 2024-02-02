package com.github.deknil;

import java.util.ArrayList;

/**
 * Map cell class
 */
public class Cell {
    public ArrayList<Particle> particles; // Child particles
    public int x;
    public int y;

    /**
     * Cell constructor
     * @param x row index
     * @param y column index
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        particles = new ArrayList<>();
    }
}
