package cz.cvut.fel.pjv.gamesave;

import java.io.Serializable;
import java.util.ArrayList;
import cz.cvut.fel.pjv.gamestates.PlayingStates;
import cz.cvut.fel.pjv.levels.Levels;
import cz.cvut.fel.pjv.utils.Vector2D;

/**
 * Storage for all game data to be saved and loaded.
 * Implements Serializable interface to be able to save and load this object.
 * It's for GameSave class.
 * Some array list fields are used to store data about items, enemies, shop
 * items, etc.
 * We're only storing their name and position in the game world because
 * otherwise
 * it wouldn't be serializable.
 * @see GameSave
 * 
 * @author Son Ngoc Tran
 */
public class GameData implements Serializable {
    
    // Player data
    protected float playerX, playerY;
    protected int health;
    protected int coinsAmount;
    // Game data
    protected PlayingStates currentPlayingState;
    protected Levels currentLevel;
    protected int level;

    // General game data
    protected boolean dungeonEntered;
    protected int questNumber;

    // Time data
    protected int minutes, hours;
    protected String dayCycle;

    // Inventory data
    protected ArrayList<String> inventoryItemNames;
    protected ArrayList<Integer> foodQuantities;
    protected ArrayList<Integer> invisPlaceholders;
    protected boolean mapUsed;

    // Items data
    protected ArrayList<String> planet1ItemNameList;
    protected ArrayList<Vector2D> planet1ItemPositions;
    protected ArrayList<String> planet2ItemNameList;
    protected ArrayList<Vector2D> planet2ItemPositions;
    protected ArrayList<String> dungeon1ItemNameList;
    protected ArrayList<Vector2D> dungeon1ItemPositions;

    // Shop data
    protected ArrayList<String> shopNameList;
    protected ArrayList<Integer> shopItemQuantities;
    protected boolean firstShopSell;
    protected boolean firstShopBuy;

    // Space ship data
    protected Vector2D spaceShipPosition;

    // Enemy data
    protected ArrayList<Integer> enemyTypeList;
    protected ArrayList<Vector2D> enemyPositions;
    protected ArrayList<Integer> enemyHealthList;
    protected int enemyCount;

    // Necromancer data
    protected Vector2D necromancerPosition;
    protected int necromancerHealth;

}
