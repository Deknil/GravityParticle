package com.github.deknil;

import com.github.deknil.enums.ParticleType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Playing field class
 */
public class Canvas extends JPanel {
    private final int cellMaxIndexX = Config.WINDOW_WIDTH / Config.CELL_SIZE;
    private final int cellMaxIndexY = Config.WINDOW_HEIGHT / Config.CELL_SIZE;
    private final Cell[][] cells = new Cell[cellMaxIndexX][cellMaxIndexY];
    private final BufferedImage image = new BufferedImage(cellMaxIndexX * Config.CELL_SIZE, cellMaxIndexY * Config.CELL_SIZE, BufferedImage.TYPE_INT_RGB);

    /**
     * Game map constructor
     */
    public Canvas() {
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        createScene();
    }

    /**
     * Creating a game scene, generating particles, etc.
     */
    private void createScene() {
        for (int x = 0; x < cellMaxIndexX; x++) {
            for (int y = 0; y < cellMaxIndexY; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }

        Random random = new Random();
        for (int i = 0; i < Config.PARTICLE_START_COUNT; i++) {
            add(ParticleType.getRandomParticleType(), random.nextInt(Config.WINDOW_WIDTH), random.nextInt(Config.WINDOW_HEIGHT));
        }
    }

    /**
     * Adding a particle to a game map
     * @param type particle type
     * @param x horizontal position
     * @param y vertical position
     */
    private void add(ParticleType type, int x, int y) {
        Particle particle = new Particle(type, x, y);
        particle.dx = new Random().nextInt(2);
        particle.dy = new Random().nextInt(2);

        int cellIndexX = (int) Math.clamp(particle.x / Config.CELL_SIZE, 0, cellMaxIndexX-1);
        int cellIndexY = (int) Math.clamp(particle.y / Config.CELL_SIZE, 0, cellMaxIndexY-1);
        Cell cell = cells[cellIndexX][cellIndexY];
        particle.currentCellIndexX = cellIndexX;
        particle.currentCellIndexY = cellIndexY;

        cell.particles.add(particle);
    }

    /**
     * Updating a game map
     */
    public void update() {
        updateLogic();
        repaint();
    }

    /**
     * Obtaining a game map cell by coordinates
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return Cell of the playing field
     */
    private Cell getCellByPosition(int x, int y) {
        return cells[Math.clamp(x / Config.CELL_SIZE, 0, cellMaxIndexX-1)][Math.clamp(y / Config.CELL_SIZE, 0, cellMaxIndexY-1)];
    }

    /**
     * Game logic update
     */
    private void updateLogic() {
        for (int x = 0; x < cellMaxIndexX; x++) {
            for (int y = 0; y < cellMaxIndexY; y++) {
                Cell cell = cells[x][y];

                // Handling particles in a game cage
                for (int particleIndex = 0; particleIndex < cell.particles.size(); particleIndex++) {
                    Particle particle = cell.particles.get(particleIndex);

                    float forceX = 0;
                    float forceY = 0;

                    // Processing neighboring particles for a given
                    for (int particleNeighborIndex = 0; particleNeighborIndex < cell.particles.size(); particleNeighborIndex++) {
                        if (particleIndex == particleNeighborIndex) continue;

                        Particle neighborParticle = cell.particles.get(particleNeighborIndex);

                        float distanceX = neighborParticle.x - particle.x;
                        float distanceY = neighborParticle.y - particle.y;
                        float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                        if (distance <= Config.PARTICLE_DISTANCE_IGNORE) continue;

                        float forceMagnitude = Config.PARTICLE_GRAVITY_CONSTANT / (distance * distance);
                        forceMagnitude = Math.min(forceMagnitude, Config.PARTICLE_MAX_FORCE);

                        float normalizedDistanceX = distanceX / distance;
                        float normalizedDistanceY = distanceY / distance;

                        if (isShouldRepel(particle.type, neighborParticle.type)) {
                            forceX -= forceMagnitude * normalizedDistanceX;
                            forceY -= forceMagnitude * normalizedDistanceY;
                        } else {
                            forceX += forceMagnitude * normalizedDistanceX;
                            forceY += forceMagnitude * normalizedDistanceY;
                        }
                    }

                    particle.dx += forceX;
                    particle.dy += forceY;

                    updateParticleMovement(cell, particle);
                }
            }
        }
    }

    /**
     * Should particles repel each other
     * @param type1 type of the first particle
     * @param type2 type of the second particle
     * @return true - they should, false - they shouldn't
     */
    private boolean isShouldRepel(ParticleType type1, ParticleType type2) {
        if ((type1 == ParticleType.BLUE && type2 == ParticleType.GREEN) || (type1 == ParticleType.GREEN && type2 == ParticleType.BLUE)) return true;
        return false;
    }

    /**
     * Particle movement update
     * @param cell the current cell of the particle
     * @param particle the particle to process
     */
    private void updateParticleMovement(Cell cell, Particle particle) {
        particle.x += particle.dx;
        particle.y += particle.dy;

        particle.dx *= 0.99f;
        particle.dy *= 0.99f;

        Cell newCell = getCellByPosition((int) particle.x, (int) particle.y);

        // Handling collisions with screen boundaries
        if (particle.x > Config.WINDOW_WIDTH || particle.x < 0) particle.dx *= -1;
        if (particle.y > Config.WINDOW_HEIGHT - Config.PARTICLE_SIZE * 2 || particle.y < 0) particle.dy *= -1;

        if (cell == newCell) return;

        cell.particles.remove(particle);
        newCell.particles.add(particle);
    }

    /**
     * Drawing a game map
     */
    private void render() {
        Graphics2D g = image.createGraphics();
        g.setColor(Config.BACKGROUND_COLOR);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        renderGrid(g);

        renderParticles(g);
    }

    /**
     * Particle rendering
     */
    private void renderParticles(Graphics2D g) {
        for (int x = 0; x < cellMaxIndexX; x++) {
            for (int y = 0; y < cellMaxIndexY; y++) {
                Cell cell = cells[x][y];
                for (int particleIndex = 0; particleIndex < cell.particles.size(); particleIndex++) {
                    Particle particle = cell.particles.get(particleIndex);

                    int cellXStart = x * Config.CELL_SIZE;
                    int cellYStart = y * Config.CELL_SIZE;
                    int cellXEnd = cellXStart + Config.CELL_SIZE;
                    int cellYEnd = cellYStart + Config.CELL_SIZE;

                    if (particle.x >= cellXStart && particle.x <= cellXEnd && particle.y >= cellYStart && particle.y <= cellYEnd) {
                        g.setColor(particle.type.color);
                        g.fillOval((int) particle.x - Config.PARTICLE_SIZE, (int) particle.y - Config.PARTICLE_SIZE, Config.PARTICLE_SIZE * 2, Config.PARTICLE_SIZE * 2);
                    }
                }
            }
        }
    }

    /**
     * Drawing a game map
     */
    private void renderGrid(Graphics2D g) {
        g.setColor(Config.GRID_COLOR);

        for (int y = 0; y <= Config.WINDOW_HEIGHT; y += Config.CELL_SIZE) {
            g.drawLine(0, y, Config.WINDOW_WIDTH, y);
        }

        for (int x = 0; x <= Config.WINDOW_WIDTH; x += Config.CELL_SIZE) {
            g.drawLine(x, 0, x, Config.WINDOW_HEIGHT);
        }
    }

    /**
     * Window drawing logic
     */
    @Override
    public void paint(Graphics g) {
        render();
        ((Graphics2D)g).drawImage(image, null, 0, 0);
    }
}
