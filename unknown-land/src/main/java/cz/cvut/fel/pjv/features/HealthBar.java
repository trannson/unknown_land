package cz.cvut.fel.pjv.features;

import cz.cvut.fel.pjv.entities.Player;
import cz.cvut.fel.pjv.utils.LoadFiles;
import cz.cvut.fel.pjv.view.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class for creating player's health bar.
 * Health bar is a rectangle that represents player's health.
 * This class manages the health bar's position and size.
 * 
 * @author Son Ngoc Tran
 */
public class HealthBar {

    private Rectangle healthBar, healthBarBorder;
    private double cameraX, cameraY;
    private int health;
    private Image heart;
    private Camera camera;
    private Player player;
    private int offsetX, offsetY;

    /**
     * Constructor for HealthBar.
     * 
     * @param camera camera used for rendering in the right place
     * @param player player's health is managed by this class
     */
    public HealthBar(Camera camera, Player player) {
        this.camera = camera;
        this.player = player;
        this.cameraX = camera.getX();
        this.cameraY = camera.getY();
        this.health = player.getHealth();
        offsetX = 70;
        offsetY = 30;
        heart = LoadFiles.LoadImage("heart.png");
        initHealthBar();
    }

    /**
     * Initialize health bar.
     * Offset is used for setting the position of the health bar
     * on the screen.
     */
    private void initHealthBar() {
        healthBar = new Rectangle(health, 15, Color.RED);
        healthBar.setX(cameraX + offsetX);
        healthBar.setY(cameraY + offsetY);

        healthBarBorder = new Rectangle(health, 15, Color.BLACK);
        healthBar.setX(cameraX + offsetX + 1);
        healthBar.setY(cameraY + offsetY + 1);
    }

    /**
     * Updating the health bar width + position.
     */
    public void update() {
        updateHealthBar();
        updateHealthBarPos();
    }

    /**
     * Drawing the health bar.
     * We are drawing the heart image and the health bar.
     * 
     * @param gc
     */
    public void render(GraphicsContext gc) {
        gc.drawImage(heart, healthBar.getX() - 45, healthBar.getY() - 10, 40, 40);
        gc.setFill(healthBar.getFill());
        gc.fillRect(healthBar.getX(), healthBar.getY(), healthBar.getWidth(), healthBar.getHeight());

        gc.setStroke(healthBarBorder.getFill());
        gc.setLineWidth(2);
        gc.strokeRect(healthBarBorder.getX(), healthBarBorder.getY(), healthBarBorder.getWidth(),
                healthBarBorder.getHeight());
    }

    /**
     * Updating the health bar width.
     */
    private void updateHealthBar() {
        health = player.getHealth();
        healthBar.setWidth(health);
    }

    /**
     * Updating the health bar position and its borders.
     */
    private void updateHealthBarPos() {
        cameraX = camera.getX();
        cameraY = camera.getY();
        healthBar.setX(cameraX + offsetX);
        healthBar.setY(cameraY + offsetY);
        healthBarBorder.setX(cameraX + offsetX + 1);
        healthBarBorder.setY(cameraY + offsetY + 1);
    }

}
