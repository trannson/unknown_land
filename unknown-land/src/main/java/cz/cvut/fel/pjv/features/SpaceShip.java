package cz.cvut.fel.pjv.features;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.PLAYER_START_X;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.PLAYER_START_Y;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;
import static cz.cvut.fel.pjv.utils.LoadFiles.LoadImage;

import java.util.HashSet;

import cz.cvut.fel.pjv.entities.Player;
import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.gamestates.PlayingStates;
import cz.cvut.fel.pjv.utils.Vector2D;
import cz.cvut.fel.pjv.view.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Class for managing the spaceship and 
 * the navigation between planets.
 * It has methods for updating and rendering the spaceship.
 * It also has methods for checking keys and drawing the navigation map.
 * It has a method for setting the current planet.
 * 
 * @author Son Ngoc Tran
 */
public class SpaceShip {
    
    private Image shipIMG, navMap, planetIMG;
    private int x, y, level;
    private Rectangle interactHitbox;
    private Player player;
    private Camera camera;
    private boolean Fpressed, mapRevelead, ENTERpressed;
    private int markIdx, maxMarkIdx;
    private Color colorForChosenPlanet;
    private PlayState playState;
    private final int shipWidth;
    private final int shipHeight;

    /**
     * SpaceShip constructor.
     * @param playState used for setting the level
     * @param player used for checking the collision
     * @param camera used for rendering in the right place
     */
    public SpaceShip(PlayState playState, Player player, Camera camera) {
        this.playState = playState;
        this.player = player;
        this.camera = camera;
        shipIMG = LoadImage("planets/spaceship.png");
        navMap = LoadImage("planets/navigationMap.png");
        planetIMG = LoadImage("planets/lavaplanet.png");
        shipWidth = 164;
        shipHeight = 164;
        initVariables();
        initHitboxes();
    }

    private void initVariables() {
        x = PLAYER_START_X - shipWidth / 2;
        y = PLAYER_START_Y;
        Fpressed = false;
        mapRevelead = false;
        ENTERpressed = false;
        markIdx = 0;
        maxMarkIdx = 0;
        colorForChosenPlanet = Color.BLUE;
        level = 1;
    }

    public void update(HashSet<KeyCode> pressedKeys) {
        checkKeys(pressedKeys);
    }

    /**
     * Drawing the spaceship and the hint for navigation.
     * It also draws the mark on the navigation map to 
     * show the chosen planet.
     * @param gc
     */
    public void render(GraphicsContext gc) {
        gc.drawImage(shipIMG, x, y, shipWidth, shipHeight); // draw ship
        drawHint(gc);
        drawNavigation(gc);
        drawMark(gc);
    }

    /**
     * Set the position of the spaceship.
     * @param x new x-coordinate
     * @param y new y-coordinate
     */
    public void setShipPos(int x, int y) {
        this.x = x;
        this.y = y;
        interactHitbox.setX(x);
        interactHitbox.setY(y);
    }

    private void initHitboxes() {
        interactHitbox = new Rectangle(x - 30, y, shipWidth + 60, shipHeight + 60);
    }

    /**
     * Drawing the hint for navigation.
     * If the player is close to the spaceship, the hint is displayed.
     * If the key shown in the hint is pressed, the navigation is opened.
     * If the navigation is opened, the hint is changed.
     * @param gc
     */
    private void drawHint(GraphicsContext gc) {
        double hintX = camera.getX() + WINDOW_WIDTH - 300;
        double hintY = camera.getY() + WINDOW_HEIGHT - 90;
        if (interactHitbox.getBoundsInParent().intersects(player.getHitbox().getBoundsInParent())) {
            if (!Fpressed) {
                gc.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
                gc.setFill(Color.WHITE);
                gc.fillText("Press 'F' to open navigation", hintX, hintY);
            }
            if (Fpressed) {
                gc.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
                gc.setFill(Color.WHITE);
                gc.fillText("Press 'F' to close navigation", hintX + 30, hintY + 80);
                gc.fillText("Press RIGHT or LEFT arrows to switch planets", hintX - 470, hintY + 80);
            }
        }
    }

    /**
     * Check if the player is close to the spaceship.
     * Set F key to open/close navigation.
     * Set arrow keys to switch between planets.
     * Set ENTER key to travel to the chosen planet.
     * We also increase the quest number + level when the player travels to Oranis.
     * We decrease the quest number + level when the player travels to the Lava planet.
     * @param pressedKeys set of pressed keys
     */
    private void checkKeys(HashSet<KeyCode> pressedKeys) {
        if (interactHitbox.getBoundsInParent().intersects(player.getHitbox().getBoundsInParent())) {
            if (pressedKeys.contains(KeyCode.F)) {
                Fpressed = !Fpressed;
                pressedKeys.remove(KeyCode.F);
            }
            if (pressedKeys.contains(KeyCode.RIGHT)) {
                if (markIdx < maxMarkIdx) {
                    markIdx++;
                }
            }
            if (pressedKeys.contains(KeyCode.LEFT)) {
                if (markIdx > 0) {
                    markIdx--;
                }
            }
            if (pressedKeys.contains(KeyCode.ENTER) && colorForChosenPlanet == Color.RED && Fpressed) {
                ENTERpressed = !ENTERpressed;
                pressedKeys.remove(KeyCode.ENTER);
                if (ENTERpressed && PlayingStates.currentState == PlayingStates.PLANET1) {
                    level = 2;
                    playState.setLevel(level);
                    Fpressed = false;
                    playState.getQuests().increaseQuestNumber();
                }
                else if (ENTERpressed && PlayingStates.currentState == PlayingStates.PLANET2) {
                    level = 1;
                    playState.setLevel(level);
                    Fpressed = false;
                    playState.getQuests().decreaseQuestNumber();
                }
            }

        }
        else if (!interactHitbox.getBoundsInParent().intersects(player.getHitbox().getBoundsInParent())) {
            Fpressed = false;
        }
    }
    
    /**
     * Drawing the navigation map.
     * If map was used, the planet is marked on the map.
     * @param gc
     */
    private void drawNavigation(GraphicsContext gc) {
        if (Fpressed && interactHitbox.getBoundsInParent().intersects(player.getHitbox().getBoundsInParent())) {
            gc.drawImage(navMap, camera.getX() + 150, camera.getY() + 25);
            if (mapRevelead) {
                gc.drawImage(planetIMG, camera.getX() + WINDOW_WIDTH / 2 + 33, camera.getY() + 153);
            }
        }
    }

    /**
     * Drawing the mark on the navigation map.
     * If the player is close to the spaceship and 
     * navigation is opened, the mark is displayed.
     * The mark shows the chosen planet.
     * We also reset the markIdx when navigation is closed.
     * The planet name changes when planet changes.
     * @param gc
     */
    private void drawMark(GraphicsContext gc) {
        if (Fpressed) {
            switch (markIdx) {
                case 1:
                    if (PlayingStates.currentState == PlayingStates.PLANET1) {
                        colorForChosenPlanet = Color.RED;
                        drawPlanetName(gc, "Chosen planet: Lava planet");
                        gc.setFill(Color.RED);
                        gc.fillText("Press ENTER to visit.", camera.getX() + WINDOW_WIDTH - 270, camera.getY() + WINDOW_HEIGHT - 30);
                    }
                    else {
                        colorForChosenPlanet = Color.BLUE;
                        drawPlanetName(gc, "Current planet: Lava planet");
                    }

                    gc.setFill(colorForChosenPlanet);
                    gc.fillOval(camera.getX() + WINDOW_WIDTH / 2 + 33, camera.getY() + 153, 10, 10);
                    break;
                case 0:
                    if (PlayingStates.currentState == PlayingStates.PLANET2) {
                        colorForChosenPlanet = Color.RED;
                        drawPlanetName(gc, "Chosen planet: Oranis");
                        gc.setFill(Color.RED);
                        gc.fillText("Press ENTER to visit.", camera.getX() + WINDOW_WIDTH - 270, camera.getY() + WINDOW_HEIGHT - 30);
                    }
                    else {
                        colorForChosenPlanet = Color.BLUE;
                        drawPlanetName(gc, "Current planet: Oranis");
                    }
                    gc.setFill(colorForChosenPlanet);
                    gc.fillOval(camera.getX() + WINDOW_WIDTH / 2 - 150, camera.getY() + 190, 10, 10);

                    break;
            }
        }
        else { // reset markIdx when navigation is closed
            markIdx = PlayingStates.currentState == PlayingStates.PLANET1 ? 0 : 1;
        }
    }

    /**
     * Drawing the planet name under the navigation map.
     * @param gc
     * @param mapName
     */
    private void drawPlanetName(GraphicsContext gc, String mapName) {
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        gc.setFill(Color.rgb(140, 212, 220));
        gc.fillText(mapName, camera.getX() + WINDOW_WIDTH / 2 - 120, camera.getY() + WINDOW_HEIGHT - 50);        
    }

    public void setMapRevealed(boolean mapRevealed) {
        this.mapRevelead = mapRevealed;
        if (mapRevealed) {
            maxMarkIdx = 1;
        }
    }

    public boolean isMapOpened() {
        return Fpressed;
    }

    public boolean switchToPlanet() {
        return ENTERpressed;
    }
    
    public void setENTERpressed(boolean ENTERpressed) {
        this.ENTERpressed = ENTERpressed;
    }

    public Vector2D getPosition() {
        return new Vector2D(x, y);
    }


   
}
