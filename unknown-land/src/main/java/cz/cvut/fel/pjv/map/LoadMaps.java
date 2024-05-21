package cz.cvut.fel.pjv.map;

import java.util.ArrayList;

import cz.cvut.fel.pjv.levels.Levels;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.TILE_SIZE;
import static cz.cvut.fel.pjv.utils.LoadFiles.*;
import static cz.cvut.fel.pjv.utils.Constants.MapConstants.*;

import javafx.scene.image.Image;

/**
 * Load maps and their collision from txt files.
 * Load it only once to save memory.
 * This class is used only to store maps and their collision.
 * 
 * @author Son Ngoc Tran
 */
public class LoadMaps {

    private ArrayList<ArrayList<Integer>> level1txt, level2txt, dungeon1txt;
    private Image[][] level1, level2, dungeon1;

    /**
     * Constructor of the LoadMaps class.
     * Load maps PNG and their collision from txt files.
     */
    public LoadMaps() {
        level1txt = LoadTxt(LVL1_COLLISION);
        level2txt = LoadTxt(LVL2_COLLISION);
        dungeon1txt = LoadTxt(LVL1_DUNGEON_COLLISION);
        level1 = LoadSubImages(LVL1_MAP, TILE_SIZE);
        level2 = LoadSubImages(LVL2_MAP, TILE_SIZE);
        dungeon1 = LoadSubImages(LVL1_DUNGEON, TILE_SIZE);
    }

    /**
     * Get images of the current level.
     * 
     * @return 2D array of images
     */
    public Image[][] getLVLImage() {
        switch (Levels.currentLevel) {
            case LEVEL1:
                return level1;
            case LEVEL2:
                return level2;
            default:
                return null;
        }
    }

    /**
     * Get collision of the current level.
     * 
     * @return 2D array of integers
     */
    public ArrayList<ArrayList<Integer>> getLVLCollision() {
        switch (Levels.currentLevel) {
            case LEVEL1:
                return level1txt;
            case LEVEL2:
                return level2txt;
            default:
                return null;
        }
    }

    /**
     * Get images of the current dungeon.
     * 
     * @return 2D array of images
     */
    public Image[][] getDungeonImage() {
        switch (Levels.currentDungeon) {
            case DUNGEON1:
                return dungeon1;
            default:
                return null;
        }
    }

    /**
     * Get collision of the current dungeon.
     * 
     * @return 2D array of integers
     */
    public ArrayList<ArrayList<Integer>> getDungeonCollision() {
        switch (Levels.currentDungeon) {
            case DUNGEON1:
                return dungeon1txt;
            default:
                return null;
        }
    }

}
