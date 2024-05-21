package cz.cvut.fel.pjv.levels;

/**
 * Class for managing levels and dungeons.
 * @see Levels
 * 
 * @author Son Ngoc Tran
 */
public class LevelManager {

    /**
     * Method for checking the level.
     * 
     * @param level
     */
    public static void checkLevel(int level) {
        switch (level) {
            case 1:
                Levels.currentLevel = Levels.LEVEL1;
                break;
            case 2:
                Levels.currentLevel = Levels.LEVEL2;
                break;
            default:
                break;
        }
    }

    /**
     * Method for checking the dungeon.
     * 
     * @param level
     */
    public static void checkDungeon(int level) {
        switch (level) {
            case 1:
                Levels.currentDungeon = Levels.DUNGEON1;
                break;
            default:
                break;
        }
    }

}
