package cz.cvut.fel.pjv.levels;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.PLAYER_START_X;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.PLAYER_START_Y;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.TILE_SIZE;

/**
 * Enum for player coordinates.
 * It's used to keep track of player's position when entering and exiting
 * dungeons or planets.
 * 
 * @author Son Ngoc Tran
 */
public enum PlayerCoords {

    DUNGEON1(24 * TILE_SIZE, 38 * TILE_SIZE, 9 * TILE_SIZE, 15 * TILE_SIZE),
    PLANET1(PLAYER_START_X, PLAYER_START_Y, 0, 0),
    PLANET2(24 * TILE_SIZE, 36 * TILE_SIZE, 0, 0);

    private final int xEnter;
    private final int yEnter;
    private final int xExit;
    private final int yExit;

    PlayerCoords(int xEnter, int yEnter, int xExit, int yExit) {
        this.xEnter = xEnter;
        this.yEnter = yEnter;
        this.xExit = xExit;
        this.yExit = yExit;

    }

    public int getxEnter() {
        return xEnter;
    }

    public int getyEnter() {
        return yEnter;
    }

    public int getxExit() {
        return xExit;
    }

    public int getyExit() {
        return yExit;
    }

    /**
     * Get dungeon coordinates based on level.
     * 
     * @param level
     * @return enum with coordinates
     */
    public static PlayerCoords getCoords(int level) {
        switch (level) {
            case 1:
                return DUNGEON1;
            default:
                return null;
        }
    }

    /**
     * Get planet coordinates based on level.
     * 
     * @param level
     * @return enum with coordinates
     */
    public static PlayerCoords getPlanetCoords(int level) {
        switch (level) {
            case 1:
                return PLANET1;
            case 2:
                return PLANET2;
            default:
                return null;
        }
    }

}
