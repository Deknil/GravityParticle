package com.github.deknil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main application entry point class
 */
public class Main {
    /**
     * The main method of the application.
     * It creates a Window object and starts it in a new thread.
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Create a new instance of the Window class
        Window window = new Window();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(window, 5, Config.TIME_SPEED, TimeUnit.MILLISECONDS);
    }
}