package cz.cvut.fel.pjv.time;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.MAP_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.MAP_WIDTH;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;

import cz.cvut.fel.pjv.entities.Player;
import cz.cvut.fel.pjv.gamestates.PlayingStates;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class for the MapBrightness.
 * It is used to render the darkness on the map.
 * The darkness is rendered around the player.
 * The darkness is rendered only on the map.
 * The darkness is rendered only in the PLANET1 state and
 * it depends on the time of the day.
 * The darkness is always in the DUNGEON1 state.
 * @see GameTime
 * 
 * @author Son Ngoc Tran
 */
public class MapBrightness {

    private Rectangle darkZoneTOP, darkZoneBOTTOM, darkZoneLEFT, darkZoneRIGHT, darkZone;
    private double x, y, opacity, opacityEverywhere, opacityOutside, opacityEverywhereTemp;
    private Player player;
    private GameTime gameTime;
    private int prevHour;
    private boolean saveHour;

    /**
     * Constructor for the MapBrightness class.
     * Create 4 big black rectangles for the darkness around the player.
     * 
     * @param player   to get the position of the player
     * @param gameTime to get the time of the day
     */
    public MapBrightness(Player player, GameTime gameTime) {
        this.player = player;
        this.gameTime = gameTime;
        x = player.getX();
        y = player.getY();
        opacity = 0.5;
        opacityEverywhere = 0.0;
        opacityOutside = 0.0;
        opacityEverywhereTemp = 0.0;
        prevHour = 0;
        saveHour = false;

        darkZoneTOP = new Rectangle();
        darkZoneBOTTOM = new Rectangle();
        darkZoneLEFT = new Rectangle();
        darkZoneRIGHT = new Rectangle();
        darkZone = new Rectangle();

    }

    /**
     * Render the darkness on the map.
     * Opacity of the darkness depends on the time of the day.
     * 
     * @param gc
     */
    public void render(GraphicsContext gc) {
        gc.setFill(Color.rgb(0, 0, 0, opacity));
        gc.fillRect(darkZoneTOP.getX(), darkZoneTOP.getY(), darkZoneTOP.getWidth(), darkZoneTOP.getHeight());
        gc.fillRect(darkZoneBOTTOM.getX(), darkZoneBOTTOM.getY(), darkZoneBOTTOM.getWidth(),
                darkZoneBOTTOM.getHeight());
        gc.fillRect(darkZoneLEFT.getX(), darkZoneLEFT.getY(), darkZoneLEFT.getWidth(), darkZoneLEFT.getHeight());
        gc.fillRect(darkZoneRIGHT.getX(), darkZoneRIGHT.getY(), darkZoneRIGHT.getWidth(), darkZoneRIGHT.getHeight());

        gc.setFill(Color.rgb(0, 0, 0, opacityEverywhere));
        gc.fillRect(darkZone.getX(), darkZone.getY(), darkZone.getWidth(), darkZone.getHeight());

    }

    /**
     * Update the position of the darkness around the player.
     * If the player is in the PLANET1 state, the darkness is rendered around the
     * player and
     * the opacity of the darkness depends on the time of the day.
     * If the player is in the DUNGEON1 state, the darkness is perma rendered around
     * the player.
     * We use opacityOutside and opacityEverywhereTemp to keep track of the opacity
     * outside the dungeon. Otherwise the opacity outside wouldn't change when
     * we are inside the dungeon.
     */
    public void update() {
        if (PlayingStates.currentState == PlayingStates.PLANET1) {
            updatePos();
            updateOpacity();
            opacity = opacityOutside;
            opacityEverywhere = opacityEverywhereTemp;
        } else if (PlayingStates.currentState == PlayingStates.DUNGEON1) {
            updatePos();
            updateOpacity();
            opacity = 0.90;
            opacityEverywhere = 0.0;
        }

    }

    /**
     * Update the position of the darkness around the player.
     * The darkness is rendered around the player.
     */
    private void updatePos() {
        x = player.getX();
        y = player.getY();
        darkZoneTOP.setX(x - WINDOW_WIDTH / 2 - 300);
        darkZoneTOP.setY(y - WINDOW_HEIGHT / 2 - 400);
        darkZoneTOP.setWidth(MAP_WIDTH);
        darkZoneTOP.setHeight(WINDOW_HEIGHT / 2 + 300);

        darkZoneBOTTOM.setX(x - WINDOW_WIDTH / 2 - 300);
        darkZoneBOTTOM.setY(y + WINDOW_HEIGHT / 2 - 200);
        darkZoneBOTTOM.setWidth(MAP_WIDTH);
        darkZoneBOTTOM.setHeight(WINDOW_HEIGHT);

        darkZoneLEFT.setX(x - WINDOW_WIDTH / 2 - 300);
        darkZoneLEFT.setY(y - WINDOW_HEIGHT / 2 + WINDOW_HEIGHT / 2 - 100);
        darkZoneLEFT.setWidth(WINDOW_WIDTH / 2 + 200);
        darkZoneLEFT.setHeight(WINDOW_HEIGHT / 2 - 100);

        darkZoneRIGHT.setX(x + WINDOW_WIDTH / 2 - 270);
        darkZoneRIGHT.setY(y - WINDOW_HEIGHT / 2 + WINDOW_HEIGHT / 2 - 100);
        darkZoneRIGHT.setWidth(WINDOW_WIDTH);
        darkZoneRIGHT.setHeight(WINDOW_HEIGHT / 2 - 100);

        darkZone.setX(x - WINDOW_WIDTH / 2 - 300);
        darkZone.setY(y - WINDOW_HEIGHT / 2 - 400);
        darkZone.setWidth(MAP_WIDTH);
        darkZone.setHeight(MAP_HEIGHT);
    }

    /**
     * Update the opacity of the darkness.
     * The opacity of the darkness depends on the time of the day.
     * This darkness is rendered only in the PLANET1 state.
     * If the time is between 2AM and 7AM, the opacity of the darkness is decreasing
     * every hour.
     * If the time is between 8AM and 12AM, the opacity of the darkness is 0.
     * If the time is between 2PM and 5PM, the opacity of the darkness is 0.
     * If the time is between 6PM and 12PM, the opacity of the darkness is
     * increasing every hour.
     * We use opacityOutside to keep track of the opacity of the darkness outside
     * because when we are inside the dungeon we set opacity to 0.90.
     */
    private void updateOpacity() {
        int hours = gameTime.getHours();
        if (gameTime.getDayCycle().equals("AM")) {
            if (hours >= 2 && hours <= 7) {
                saveHours();
                if (prevHour != hours) {
                    opacityOutside -= 0.1;
                    opacityEverywhereTemp -= 0.05;
                    if (opacityOutside < 0.0) {
                        opacityOutside = 0.0; // Ensure opacity never goes below 0.0
                    }
                    if (opacityEverywhereTemp < 0.0) {
                        opacityEverywhereTemp = 0.0; // Ensure opacityEverywhere never goes below 0.0
                    }
                    saveHour = false;
                }
            }
            if (hours >= 8 && hours <= 12) {
                opacityOutside = 0.0;
            }
        } else {
            if (hours >= 2 && hours <= 5) {
                opacityOutside = 0.0;
            }
            if (hours >= 6 && hours <= 12) {
                saveHours();
                if (prevHour != hours) {
                    opacityOutside += 0.1;
                    opacityEverywhereTemp += 0.05;
                    if (opacityOutside > 1) {
                        opacityOutside = 1.0; // Ensure opacity never goes above 1.0
                    }
                    if (opacityEverywhereTemp > 0.5) {
                        opacityEverywhereTemp = 0.5; // Ensure opacityEverywhere never goes above 0.5
                    }
                    saveHour = false;
                }
            }
        }
    }

    /**
     * Save the current hour.
     * This is used to be compared with the next hour so
     * we can increase or decrease the opacity of the darkness.
     */
    private void saveHours() {
        if (!saveHour) {
            prevHour = gameTime.getHours();
            saveHour = true;
        }
    }

}
