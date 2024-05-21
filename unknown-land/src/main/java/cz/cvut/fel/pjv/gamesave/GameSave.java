package cz.cvut.fel.pjv.gamesave;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cz.cvut.fel.pjv.Main.startGameLoop;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.gamestates.PlayingStates;
import cz.cvut.fel.pjv.gamestates.States;
import cz.cvut.fel.pjv.levels.Levels;
import cz.cvut.fel.pjv.view.GameView;

/**
 * Saves and loads game data.
 * Brain of the game save system.
 * Everything to be saved is stored in GameData object.
 * When we want to load from save file, we extract data from GameData object.
 * We save data as save.ser file.
 * @see GameData
 * 
 * @author Son Ngoc Tran
 */
public class GameSave {

    private GameData gameData;
    private PlayState playState;
    private GameView gameView;
    private Logger log;
    private ItemSave itemSave;
    private InventorySave inventorySave;
    private MapSave mapSave;
    private ShopSave shopSave;
    private EnemySave enemySave;

    /**
     * GameSave constructor.
     * 
     * @param gameView  used to start game loop
     * @param playState used to get game data
     */
    public GameSave(GameView gameView, PlayState playState) {
        this.gameView = gameView;
        this.playState = playState;
        gameData = new GameData();
        log = Logger.getLogger(GameSave.class.getName());
        log.setLevel(Level.INFO);

        itemSave = new ItemSave(playState, log);
        inventorySave = new InventorySave(playState, log);
        mapSave = new MapSave(playState, log);
        shopSave = new ShopSave(playState, log);
        enemySave = new EnemySave(playState, log);
    }

    /**
     * If there isn't an error with locating/creating a save.ser file,
     * we initialize game data. That means that we're setting data from
     * playState into the GameData object which implements Serializable.
     * After all data are stored in GameData, we write it in save.ser
     * using objectOutput.writeObject
     */
    public void saveGame() {
        try (FileOutputStream fileOutput = new FileOutputStream("save.ser");
                ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput)) {
            initGameData();

            objectOutput.writeObject(gameData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * If there isn't an error locating file, we reset playState so
     * we can load data from save.ser file and it wouldn't be mixed with
     * the current game data. Then we extract data from GameData object.
     * That means we're setting data from GameData into playState.
     * After we extract all data, we start game loop. If file is
     * not found we're logging warning.
     */
    public void loadGame() {
        try (FileInputStream fileIn = new FileInputStream("save.ser");
                ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            gameData = (GameData) objectIn.readObject();
            playState.reset();

            extractGameData();

            startGameLoop(gameView);

        } catch (Exception e) {
            log.warning("No save file found");

        }
    }

    public void deleteSave() {
        try {
            Path savePath = Paths.get("save.ser");
            Files.deleteIfExists(savePath);
            log.info("Save file deleted");
        } catch (Exception e) {
            log.warning("ERROR: Save file not deleted");
            e.printStackTrace();
        }
    }

    /**
     * Initializes game data.
     * Put all data needed from playState into GameData object.
     */
    private void initGameData() {
        gameData.playerX = playState.getPlayer().getX();
        gameData.playerY = playState.getPlayer().getY();
        gameData.health = playState.getPlayer().getHealth();
        gameData.coinsAmount = playState.getCoinManager().getCoins();

        gameData.currentPlayingState = PlayingStates.currentState;
        gameData.currentLevel = Levels.currentLevel;
        gameData.level = playState.getLevel();

        gameData.minutes = playState.getGameTime().getMinutes();
        gameData.hours = playState.getGameTime().getHours();
        gameData.dayCycle = playState.getGameTime().getDayCycle();

        inventorySave.saveInventoryItems(gameData);
        inventorySave.saveMapUsed(gameData);
        itemSave.saveGroundItems(gameData);

        shopSave.saveShopData(gameData);

        gameData.dungeonEntered = playState.getItemManager().getDungeonEntered();
        gameData.questNumber = playState.getQuests().getQuestNumber();

        gameData.spaceShipPosition = playState.getSpaceShip().getPosition();
        enemySave.saveEnemyData(gameData);
        enemySave.saveNecromancerData(gameData);
    }

    /**
     * Extract data from GameData object.
     * Load all data stored in GameData to play state.
     */
    private void extractGameData() {
        playState.getPlayer().setTeleported(true);
        playState.getPlayer().resetPosition(gameData.playerX, gameData.playerY);
        playState.getPlayer().setHealth(gameData.health);
        playState.getCoinManager().setCoins(gameData.coinsAmount);
        States.currentState = States.PLAY;
        PlayingStates.currentState = gameData.currentPlayingState;
        Levels.currentLevel = gameData.currentLevel;
        playState.setLevel(gameData.level);

        mapSave.loadMap();
        playState.getGameTime().setTime(gameData.hours, gameData.minutes, gameData.dayCycle);
        inventorySave.loadInventoryItems(gameData);
        inventorySave.loadMapUsed(gameData);
        itemSave.loadGroundItems(gameData);

        shopSave.loadShopData(gameData);

        playState.getItemManager().setDungeonEntered(gameData.dungeonEntered);
        playState.getQuests().setQuestNumber(gameData.questNumber);

        playState.getSpaceShip().setShipPos((int) gameData.spaceShipPosition.getX(),
                (int) gameData.spaceShipPosition.getY());
        enemySave.loadEnemyData(gameData);
        enemySave.loadNecromancerData(gameData);
    }

}
