package com.github.deknil;

import javax.swing.*;

/**
 * This class represents a custom window for the Game of Life application.
 * It extends JFrame and implements the Runnable interface.
 */
public class Window extends JFrame implements Runnable {
    private final Canvas canvas;

    /**
     * Constructor for the Window class.
     * Initializes the window with the necessary properties.
     */
    public Window() {
        setTitle("The game of life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new Canvas();
        add(canvas);
        pack();

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    /**
     * This method is the implementation of the Runnable interface's run method.
     * It runs the canvas's update method to refresh the display.
     */
    @Override
    public void run() {
        canvas.update();
    }
}
