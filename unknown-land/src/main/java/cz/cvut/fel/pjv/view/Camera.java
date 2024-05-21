package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.entities.Player;
import javafx.scene.canvas.GraphicsContext;

import static cz.cvut.fel.pjv.utils.Constants.PlayerConstants.PLAYER_WIDTH;
import static cz.cvut.fel.pjv.utils.Constants.PlayerConstants.PLAYER_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.PLANET1_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.PLANET1_WIDTH;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_HEIGHT;

/**
 * Class for the Camera.
 * Camera follows the player and moves the canvas.
 * Camera moves canvas in the opposite direction of the player's movement
 * making the player always in the middle of the screen.
 * 
 * @author Son Ngoc Tran
 */
public class Camera {

    private Player player;
    private double cameraX, cameraY;
    private int mapWidth, mapHeight;

    /**
     * Constructor for the Camera.
     * 
     * @param player to use for coordinates
     */
    public Camera(Player player) {
        this.player = player;
        mapWidth = PLANET1_WIDTH;
        mapHeight = PLANET1_HEIGHT;
    }

    /**
     * Move the camera coordinates.
     * Camera follows the player and set him in the middle of the screen.
     */
    public void moveCameraCoord() {
        cameraX = player.getX() - WINDOW_WIDTH / 2 + PLAYER_WIDTH / 2;
        cameraY = player.getY() - WINDOW_HEIGHT / 2 + PLAYER_HEIGHT / 2;

        checkOutOfBounds();
    }

    /**
     * gc.translate shifts the whole canvas.
     * When player moves, the camera moves in the opposite direction(-cameraX,
     * -cameraY)
     * (canvas is shifted in the opposite direction),
     * so the player stays always in the middle of the screen.
     * 
     * @param gc
     */
    public void render(GraphicsContext gc) {
        gc.translate(-cameraX, -cameraY);
    }

    public void update() {
        moveCameraCoord();
    }

    /**
     * Check if the camera is out of bounds.
     * If it is, set it to the edge of the map, don't move it out of the map.
     */
    public void checkOutOfBounds() {
        if (cameraX < 0)
            cameraX = 0;
        if (cameraX + WINDOW_WIDTH >= mapWidth)
            cameraX = mapWidth - WINDOW_WIDTH;
        if (cameraY < 0)
            cameraY = 0;
        if (cameraY + WINDOW_HEIGHT >= mapHeight)
            cameraY = mapHeight - WINDOW_HEIGHT;

    }

    public void resetBorders(int width, int height) {
        mapWidth = width;
        mapHeight = height;
    }

    public double getX() {
        return cameraX;
    }

    public double getY() {
        return cameraY;
    }
}
