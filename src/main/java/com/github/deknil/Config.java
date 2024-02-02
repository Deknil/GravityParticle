package com.github.deknil;

import java.awt.*;

/**
 * Constants for setting up the game
 */
public class Config {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final int CELL_SIZE = 64;
    public static final int PARTICLE_SIZE = 4;
    public static final int PARTICLE_DISTANCE_IGNORE = 10;
    public static final int PARTICLE_MAX_FORCE = 3;
    public static final int PARTICLE_START_COUNT = 5000;
    public static final int PARTICLE_GRAVITY_CONSTANT = 10;

    public static long TIME_SPEED = 6;
    public static Color BACKGROUND_COLOR = new Color(10, 10, 10, 255);
    public static Color GRID_COLOR = new Color(255, 255, 255, 5);
}
