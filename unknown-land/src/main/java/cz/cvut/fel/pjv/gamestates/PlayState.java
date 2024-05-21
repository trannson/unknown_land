package cz.cvut.fel.pjv.gamestates;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.PLANET1_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.PLANET1_WIDTH;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.PLAYER_START_X;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.PLAYER_START_Y;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.TILE_SIZE;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cz.cvut.fel.pjv.levels.LevelManager.checkLevel;
import static cz.cvut.fel.pjv.levels.LevelManager.checkDungeon;

import cz.cvut.fel.pjv.entities.EnemyManager;
import cz.cvut.fel.pjv.entities.Merchant;
import cz.cvut.fel.pjv.entities.Player;
import cz.cvut.fel.pjv.features.CoinManager;
import cz.cvut.fel.pjv.features.HealthBar;
import cz.cvut.fel.pjv.features.Inventory;
import cz.cvut.fel.pjv.features.InventoryManager;
import cz.cvut.fel.pjv.features.Quests;
import cz.cvut.fel.pjv.features.Shop;
import cz.cvut.fel.pjv.features.SpaceShip;
import cz.cvut.fel.pjv.items.ItemManager;
import cz.cvut.fel.pjv.levels.Levels;
import cz.cvut.fel.pjv.levels.PlayerCoords;
import cz.cvut.fel.pjv.map.LoadMaps;
import cz.cvut.fel.pjv.map.Map;
import cz.cvut.fel.pjv.time.GameTime;
import cz.cvut.fel.pjv.time.MapBrightness;
import cz.cvut.fel.pjv.utils.Collision;
import cz.cvut.fel.pjv.utils.LoadedFiles;
import cz.cvut.fel.pjv.utils.PlayerDirection;
import cz.cvut.fel.pjv.view.Camera;
import cz.cvut.fel.pjv.view.GameView;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

/**
 * Class for the PlayState.
 * It is the main state of the game.
 * It is responsible for updating and rendering the game - play state.
 * It also handles the keyboard and mouse input.
 * It also handles the game states.
 * It's the brain of the game.
 * We merge all the classes here.
 * 
 * @author Son Ngoc Tran
 */
public class PlayState {

    private Camera camera;
    private Map map;
    private Player player;
    private GameView gameView;
    private Collision collision;
    private int level;
    private PlayerCoords playerCoords;
    private HealthBar healthBar;
    private ItemManager itemManager;
    private LoadMaps loadMaps;
    private Inventory inventory;
    private InventoryManager inventoryManager;
    private Merchant merchant;
    private LoadedFiles loadedFiles;
    private GameTime gameTime;
    private EnemyManager enemyManager;
    private Shop shop;
    private CoinManager coinManager;
    private SpaceShip spaceShip;
    private MapBrightness mapBrightness;
    private Quests quests;
    private Logger log;
    private HashSet<KeyCode> pressedKeys;

    /**
     * Constructor for the PlayState class.
     * 
     * @param gameView used for setting the scene
     */
    public PlayState(GameView gameView) {
        this.gameView = gameView;
        gameTime = new GameTime();
        loadMaps = new LoadMaps();
        pressedKeys = new HashSet<>();
        loadedFiles = new LoadedFiles();

        initClasses();
        keyboardHandle();
        mouseHandle();

        log = Logger.getLogger(PlayState.class.getName());
        log.setLevel(Level.INFO);
    }

    private void initClasses() {
        level = 1;
        gameTime.setTime(8, 0, "AM");

        PlayingStates.currentState = PlayingStates.PLANET1;
        Levels.currentLevel = Levels.LEVEL1;

        collision = new Collision(this);
        player = new Player(PLAYER_START_X, PLAYER_START_Y, this);
        camera = new Camera(player);
        map = new Map(camera);
        enemyManager = new EnemyManager(this, gameTime);
        healthBar = new HealthBar(camera, player);
        coinManager = new CoinManager(camera);
        spaceShip = new SpaceShip(this, player, camera);
        inventory = new Inventory(this, spaceShip);
        merchant = new Merchant(inventory, player, 11 * TILE_SIZE, 650);
        itemManager = new ItemManager(this, inventory);
        inventoryManager = new InventoryManager(this, inventory, itemManager, spaceShip);
        shop = new Shop(this, merchant, inventoryManager, coinManager);
        mapBrightness = new MapBrightness(player, gameTime);
        quests = new Quests(camera);
    }

    /**
     * Updating everything in the game.
     */
    public void update() {
        checkGameState();
        camera.update();
        updatePlayerKeys();
        mapBrightness.update();
        updateStates(pressedKeys);
        healthBar.update();
        inventoryManager.update(pressedKeys);
        shop.update(pressedKeys);
    }

    /**
     * Rendering everything in the game.
     * We use gc.save() and gc.restore() to save and restore the original state of
     * the GraphicsContext
     * because we are modifying it in the camera.render() method. We are moving the
     * canvas with the camera.
     * 
     * @param gc
     */
    public void render(GraphicsContext gc) {
        gc.save(); // saves current state, important for camera movement - gc.translate(-cameraX,
                   // -cameraY)

        camera.render(gc); // moving the camera
        map.render(gc); // drawing the whole map
        itemManager.render(gc); // drawing the items
        renderStates(gc); // drawing depending on the state
        healthBar.render(gc); // drawing the health bar
        coinManager.render(gc); // drawing the coins
        gameTime.render(gc, camera); // drawing the time
        quests.render(gc); // drawing the quests

        gc.restore(); // restores the state saved by gc.save() but the settings in xxxx.render() are
                      // still modified

    }

    /**
     * Rendering depending on the state.
     * We avoid rendering everything at once because
     * it would be too much for the game loop and it would
     * cause troubles with overlapping.
     * 
     * @param gc
     */
    private void renderStates(GraphicsContext gc) {
        switch (PlayingStates.currentState) {
            case PLANET1:
                merchant.render(gc); // drawing the merchant
                player.render(gc); // drawing the player
                mapBrightness.render(gc); // drawing the map brightness
                spaceShip.render(gc); // drawing the spaceship
                inventoryManager.render(gc); // drawing the inventory
                shop.render(gc);
                break;
            case DUNGEON1:
                player.render(gc); // drawing the player
                enemyManager.render(gc); // drawing the enemies
                mapBrightness.render(gc); // drawing the map brightness
                inventoryManager.render(gc); // drawing the inventory
                break;
            case PLANET2:
                player.render(gc); // drawing the player
                enemyManager.renderNecromancer(gc);
                spaceShip.render(gc); // drawing the spaceship
                inventoryManager.render(gc); // drawing the inventory
                break;
            default:
                log.warning("Unknown state");
                break;
        }
    }

    /**
     * Updating the states depending on the current state.
     * Avoid updating everything at once because it would be too much for the game
     * loop.
     * We also keep track of when we want to switch to another level.
     * 
     * @param pressedKeys set of pressed keys
     */
    private void updateStates(HashSet<KeyCode> pressedKeys) {
        if (spaceShip.switchToPlanet()) {
            nextLevel();
            spaceShip.setENTERpressed(false);
            log.info("Switching to planet");
        }
        switch (PlayingStates.currentState) {
            case PLANET1:
                merchant.update();
                spaceShip.update(pressedKeys);
                player.update();
                break;
            case DUNGEON1:
                player.update();
                enemyManager.update();
                break;
            case PLANET2:
                player.update();
                enemyManager.updateNecromancer();
                spaceShip.update(pressedKeys);
                break;
            default:
                break;
        }
    }

    /**
     * Handling the keyboard input.
     * Add the pressed key to the hash set of pressed keys.
     */
    private void keyboardHandle() {
        Scene scene = gameView.getScene();

        scene.setOnKeyPressed(e -> {
            pressedKeys.add(e.getCode());
        });

        scene.setOnKeyReleased(e -> {
            pressedKeys.remove(e.getCode());
        });

    }

    /**
     * Updating the player direction depending on the pressed keys.
     * Also handle the ESCAPE key for the pause state.
     */
    private void updatePlayerKeys() {
        if (pressedKeys.contains(KeyCode.W)) {
            player.setDirection(PlayerDirection.UP);
        } else if (pressedKeys.contains(KeyCode.S)) {
            player.setDirection(PlayerDirection.DOWN);
        } else if (pressedKeys.contains(KeyCode.A)) {
            player.setDirection(PlayerDirection.LEFT);
        } else if (pressedKeys.contains(KeyCode.D)) {
            player.setDirection(PlayerDirection.RIGHT);
        } else {
            player.setMoving(false);
        }
        if (pressedKeys.contains(KeyCode.ESCAPE)) {
            States.currentState = States.PAUSE;
            // manually remove ESCAPE key from the set cause switching scenes would not
            // register it
            pressedKeys.remove(KeyCode.ESCAPE);
        }
    }

    /**
     * Handling the mouse input.
     * If the left mouse button is clicked, the player attacks.
     */
    public void mouseHandle() {
        Scene scene = gameView.getScene();
        scene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                player.setAttack(true);
            }
        });
    }

    /**
     * Switching to the next level.
     * We set the current state to the next level.
     * We need to reset the map, collision map, player position, camera borders.
     * We also set the current planet for the spaceship and update spaceship
     * position.
     */
    private void nextLevel() {
        if (level == 1) {
            PlayingStates.currentState = PlayingStates.PLANET1;
        } else if (level == 2) {
            PlayingStates.currentState = PlayingStates.PLANET2;
        }
        playerCoords = PlayerCoords.getPlanetCoords(level);
        checkLevel(level);
        map.setMap(loadMaps.getLVLImage());
        collision.setCollisionMap(loadMaps.getLVLCollision());
        player.setTeleported(true);
        player.resetPosition(playerCoords.getxEnter(), playerCoords.getyEnter());
        spaceShip.setShipPos(playerCoords.getxEnter() - 200, playerCoords.getyEnter() - 100);
        camera.resetBorders(collision.getWidth(), collision.getHeight());
    }

    /**
     * Entering the dungeon.
     * If we enter the dungeon, we set the current state to the dungeon.
     * We need to reset the map, collision map, player position, camera borders.
     * We set spawning items in the dungeon. It spawns only once, it's solved
     * in the ItemManager class.
     */
    public void enterDungeon() {
        PlayingStates.currentState = PlayingStates.DUNGEON1;
        playerCoords = PlayerCoords.getCoords(level);
        checkDungeon(level);
        map.setMap(loadMaps.getDungeonImage());
        collision.setCollisionMap(loadMaps.getDungeonCollision());
        player.resetPosition(playerCoords.getxEnter(), playerCoords.getyEnter());
        camera.resetBorders(collision.getWidth(), collision.getHeight());
        itemManager.setSpawningItems();
    }

    /**
     * Exiting the dungeon.
     * If we exit the dungeon, we set the current state to the planet.
     * We need to reset the map, collision map, player position, camera borders.
     */
    public void exitDungeon() {
        PlayingStates.currentState = PlayingStates.PLANET1;

        playerCoords = PlayerCoords.getCoords(level);
        checkLevel(level);
        map.setMap(loadMaps.getLVLImage());
        collision.setCollisionMap(loadMaps.getLVLCollision());
        player.resetPosition(playerCoords.getxExit(), playerCoords.getyExit());
        camera.resetBorders(PLANET1_WIDTH, PLANET1_HEIGHT);
    }

    /**
     * Checking 2 game states - win and death.
     * If the player's health is less than 0, the current state is set to death.
     * If the necromancer's health is less than 0, the current state is set to win.
     * Scene is switched using enum States.
     * If we win, we delete our save cause we don't need it anymore.
     * GameLoop is stopped in gameView when states are set to win or death.
     */
    private void checkGameState() {
        if (player.getHealth() <= 0) {
            States.currentState = States.DEATH;
        }
        if (enemyManager.getNecromancerHealth() <= 0) {
            States.currentState = States.WIN;
            gameView.getGameSave().deleteSave();
        }
    }

    /**
     * Resetting the game.
     * We reset all the classes by initializing them again.
     * We set the current state to the first planet.
     */
    public void reset() {
        initClasses();
        PlayingStates.currentState = PlayingStates.PLANET1;
    }

    public Player getPlayer() {
        return player;
    }

    public Collision getCollision() {
        return collision;
    }

    public Map getMap() {
        return map;
    }

    public LoadedFiles getLoadedFiles() {
        return loadedFiles;
    }

    public GameTime getGameTime() {
        return gameTime;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Camera getCamera() {
        return camera;
    }

    public CoinManager getCoinManager() {
        return coinManager;
    }

    public Quests getQuests() {
        return quests;
    }

    public int getLevel() {
        return level;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public LoadMaps getLoadMaps() {
        return loadMaps;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public Shop getShop() {
        return shop;
    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

}
