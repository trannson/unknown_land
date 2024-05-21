package cz.cvut.fel.pjv.map;

import cz.cvut.fel.pjv.utils.LoadFiles;
import cz.cvut.fel.pjv.view.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.TILE_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;
import static cz.cvut.fel.pjv.utils.Constants.MapConstants.LVL1_MAP;

/**
 * Class for the Map.
 * It is used to render the map.
 * 
 * @author Son Ngoc Tran
 */
public class Map {

    private Image map[][];
    private Camera camera;

    /**
     * Constructor for the Map class.
     * 
     * @param camera to get the position of the camera
     */
    public Map(Camera camera) {
        this.camera = camera;
        map = LoadFiles.LoadSubImages(LVL1_MAP, TILE_SIZE);

    }

    /**
     * Render the map.
     * Render only the part of the map that is visible on the screen
     * (the part that is inside the window).
     * 
     * @param gc
     */
    public void render(GraphicsContext gc) {
        double x, y;
        for (int j = 0; j < map.length; j++) {
            for (int i = 0; i < map[j].length; i++) {
                x = i * TILE_SIZE;
                y = j * TILE_SIZE;

                /*
                 * render only part of the map that is visible on the screen,
                 * + tileSize so the last row and column are rendered too
                 * + checking if the map is being rendered outside the window
                 */
                if (x + TILE_SIZE >= camera.getX() && y + TILE_SIZE >= camera.getY()
                        && x <= camera.getX() + WINDOW_WIDTH && y <= camera.getY() + WINDOW_HEIGHT) {
                    gc.drawImage(map[j][i], x, y, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    public void setMap(Image[][] map) {
        this.map = map;
    }

}
