package cz.cvut.fel.pjv.gamesave;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.*;

import java.util.ArrayList;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.gamestates.PlayingStates;
import javafx.scene.image.Image;

/**
 * Loads Map data.
 * We don't really need to store them first because
 * we store PlayingStates enum.
 * We can extract PNG map and collision txt
 * map from that.
 * @see GameSave
 * 
 * @author Son Ngoc Tran
 */
public class MapSave {

    private PlayState playState;
    private Logger log;

    /**
     * Constructor for saving map.
     * 
     * @param playState used to set data
     * @param log
     */
    public MapSave(PlayState playState, Logger log) {
        this.playState = playState;
        this.log = log;
    }

    /**
     * We're setting data into the whole project through play state.
     * This function is called after PlayingStates.currentState is
     * set from gameData object. We're only checking where we are
     * and with that we can adjust PNG Image Map and Txt Collision.
     */
    protected void loadMap() {
        Image[][] planetMapPNG = playState.getLoadMaps().getLVLImage();
        Image[][] dungeonMapPNG = playState.getLoadMaps().getDungeonImage();
        ArrayList<ArrayList<Integer>> planetMapCollision = playState.getLoadMaps().getLVLCollision();
        ArrayList<ArrayList<Integer>> dungeonCollision = playState.getLoadMaps().getDungeonCollision();
        switch (PlayingStates.currentState) {
            case PLANET1:
                playState.getMap().setMap(planetMapPNG);
                playState.getCollision().setCollisionMap(planetMapCollision);
                playState.getCamera().resetBorders(PLANET1_WIDTH, PLANET1_HEIGHT);
                break;
            case DUNGEON1:
                playState.getMap().setMap(dungeonMapPNG);
                playState.getCollision().setCollisionMap(dungeonCollision);
                playState.getCamera().resetBorders(DUNGEON1_WIDTH, DUNGEON1_HEIGHT);
                break;
            case PLANET2:
                playState.getMap().setMap(planetMapPNG);
                playState.getCollision().setCollisionMap(planetMapCollision);
                playState.getCamera().resetBorders(PLANET2_WIDTH, PLANET2_HEIGHT);
                break;
            default:
                log.warning("Unknown playing state");
                break;
        }
    }

}
