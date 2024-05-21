package cz.cvut.fel.pjv.levels;

/**
 * Enum for levels and dungeons.
 * @see LevelManager
 * 
 * @author Son Ngoc Tran
 */
public enum Levels {

    LEVEL1,
    DUNGEON1,
    LEVEL2;

    public static Levels currentLevel = LEVEL1;
    public static Levels currentDungeon = DUNGEON1;
}
