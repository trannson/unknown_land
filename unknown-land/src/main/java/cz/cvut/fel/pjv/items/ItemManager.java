package cz.cvut.fel.pjv.items;

import cz.cvut.fel.pjv.entities.Player;
import cz.cvut.fel.pjv.features.Inventory;
import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.gamestates.PlayingStates;
import cz.cvut.fel.pjv.utils.Collision;
import cz.cvut.fel.pjv.utils.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.TILES_IN_WIDTH;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.TILE_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.ITEM_PIX_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.SWORD_PIX_SIZE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for managing items.
 * Items can be spawned in the world.
 * This class spawns items in the world and
 * keep track of them. That means that they render
 * items depending on the current state of the game.
 * 
 * @see Item
 * @see Inventory
 * 
 * @author Son Ngoc Tran
 */
public class ItemManager {
    private int itemsAmount, minAmount, maxAmount;
    private int delay, pickUpTimer;
    private Player player;
    private Rectangle playerHitbox;
    private Random random;
    private ArrayList<Item> itemList, planet1ItemList, itemsOnGround, planet2ItemList;
    private Set<Vector2D> usedCoords;
    private int candyRate, breadRate, ballRate, burgerRate,
            cookieRate, panRate, teddybearRate, radioRate, mapRate, flashlightRate,
            greenSwRate, blueSwRate, redSwRate;
    private Collision collision;
    private boolean dungeonEntered;
    private Inventory inventory;
    private PlayState playState;
    private Logger log;

    /**
     * Constructor for the ItemManager class.
     * Initializes the ItemManager class and its variables.
     * Create 2 lists for items in the dungeon and on the planet.
     * Create a set for used coordinates.
     * 
     * @param playState used for getting the player
     * @param inventory used for adding items to the inventory
     */
    public ItemManager(PlayState playState, Inventory inventory) {
        this.playState = playState;
        this.inventory = inventory;
        usedCoords = new HashSet<>();
        player = playState.getPlayer();
        random = new Random();
        collision = playState.getCollision();

        dungeonEntered = false;

        itemList = new ArrayList<>();
        planet1ItemList = new ArrayList<>();
        planet2ItemList = new ArrayList<>();

        minAmount = 20;
        maxAmount = 30;

        delay = 60;
        pickUpTimer = delay;

        log = Logger.getLogger(ItemManager.class.getName());
        log.setLevel(Level.INFO);

    }

    /**
     * Update method for the ItemManager class.
     * Renders items on the ground depending on the current state of the game.
     * 
     * @param gc
     */
    public void render(GraphicsContext gc) {
        pickUpTimer++;
        switchItemList();

        for (Item item : itemsOnGround) {
            item.render(gc);
        }

    }

    /**
     * Setting spawn rates for all items.
     */
    private void setSpawnRates() {
        candyRate = 12;
        breadRate = 10;
        ballRate = 12;
        burgerRate = 5;
        cookieRate = 10;
        panRate = 10;
        teddybearRate = 10;
        radioRate = 11;
        mapRate = 3;
        flashlightRate = 7;
        greenSwRate = 5;
        blueSwRate = 3;
        redSwRate = 2;

    }

    /**
     * Spawn items if dungeon is entered for the first time.
     * Increase the quest number.
     * Items amount is random between minAmount and maxAmount.
     */
    public void setSpawningItems() {
        if (!dungeonEntered) { // for the first time, spawn items only once;
            itemsAmount = random.nextInt(maxAmount - minAmount + 1) + minAmount; // + 1 to include maxAmount
            spawnItem();

            dungeonEntered = true;
            playState.getQuests().increaseQuestNumber();
        }

    }

    /**
     * Spawn items in the world.
     * Items are spawned randomly in the world.
     * Used coordinates are stored in a set.
     * That means that items are not spawned on the same tile.
     * Items are spawned in the dungeon.
     * Items are spawned depending on the spawn rates.
     * Items are spawned in the world if the spawn is valid.
     * Items are added to the list of items.
     * Items are spawned until the itemsAmount is reached.
     */
    private void spawnItem() {
        setSpawnRates();
        int itemCounter = 0;
        while (itemCounter < itemsAmount) {
            int x = random.nextInt(TILES_IN_WIDTH);
            int y = random.nextInt(TILES_IN_WIDTH);
            Vector2D coords = new Vector2D(x, y);
            if (usedCoords.contains(coords)) {
                continue; // skip iteration if the coords are already used
            }
            usedCoords.add(coords);
            x *= TILE_SIZE;
            y *= TILE_SIZE;

            int randomNum = random.nextInt(100);
            if (randomNum <= candyRate) {
                if (canBeSpawned(x, y, ITEM_PIX_SIZE, ITEM_PIX_SIZE)) {
                    itemList.add(new Candy(x, y));
                    itemCounter++;
                }
            } else if (randomNum <= breadRate + candyRate) {
                if (canBeSpawned(x, y, ITEM_PIX_SIZE, ITEM_PIX_SIZE)) {
                    itemList.add(new Bread(x, y));
                    itemCounter++;
                }
            } else if (randomNum <= ballRate + breadRate + candyRate) {
                if (canBeSpawned(x, y, ITEM_PIX_SIZE, ITEM_PIX_SIZE)) {
                    itemList.add(new Ball(x, y));
                    itemCounter++;
                }
            } else if (randomNum <= burgerRate + ballRate + breadRate + candyRate) {
                if (canBeSpawned(x, y, ITEM_PIX_SIZE, ITEM_PIX_SIZE)) {
                    itemList.add(new Burger(x, y));
                    itemCounter++;
                }
            } else if (randomNum <= cookieRate + burgerRate + ballRate + breadRate + candyRate) {
                if (canBeSpawned(x, y, ITEM_PIX_SIZE, ITEM_PIX_SIZE)) {
                    itemList.add(new Cookie(x, y));
                    itemCounter++;
                }
            } else if (randomNum <= panRate + cookieRate + burgerRate + ballRate + breadRate + candyRate) {
                if (canBeSpawned(x, y, ITEM_PIX_SIZE, ITEM_PIX_SIZE)) {
                    itemList.add(new Pan(x, y));
                    itemCounter++;
                }
            } else if (randomNum <= teddybearRate + panRate + cookieRate + burgerRate + ballRate + breadRate
                    + candyRate) {
                if (canBeSpawned(x, y, ITEM_PIX_SIZE, ITEM_PIX_SIZE)) {
                    itemList.add(new TeddyBear(x, y));
                    itemCounter++;
                }
            } else if (randomNum <= radioRate + teddybearRate + panRate + cookieRate + burgerRate + ballRate + breadRate
                    + candyRate) {
                if (canBeSpawned(x, y, ITEM_PIX_SIZE, ITEM_PIX_SIZE)) {
                    itemList.add(new Radio(x, y));
                    itemCounter++;
                }

            } else if (randomNum <= mapRate + radioRate + teddybearRate + panRate + cookieRate + burgerRate + ballRate
                    + breadRate + candyRate) {
                if (canBeSpawned(x, y, ITEM_PIX_SIZE, ITEM_PIX_SIZE)) {
                    itemList.add(new MapItem(x, y));
                    itemCounter++;
                }
            } else if (randomNum <= flashlightRate + mapRate + radioRate + teddybearRate + panRate + cookieRate
                    + burgerRate + ballRate + breadRate + candyRate) {
                if (canBeSpawned(x, y, ITEM_PIX_SIZE, ITEM_PIX_SIZE)) {
                    itemList.add(new FlashLight(x, y));
                    itemCounter++;
                }
            } else if (randomNum <= greenSwRate + flashlightRate + mapRate + radioRate + teddybearRate + panRate
                    + cookieRate + burgerRate + ballRate + breadRate + candyRate) {
                if (canBeSpawned(x, y, SWORD_PIX_SIZE, SWORD_PIX_SIZE)) {
                    itemList.add(new GreenSword(x, y));
                    itemCounter++;
                }
            } else if (randomNum <= blueSwRate + greenSwRate + flashlightRate + mapRate + radioRate + teddybearRate
                    + panRate + cookieRate + burgerRate + ballRate + breadRate + candyRate) {
                if (canBeSpawned(x, y, SWORD_PIX_SIZE, SWORD_PIX_SIZE)) {
                    itemList.add(new BlueSword(x, y));
                    itemCounter++;
                }
            } else if (randomNum <= redSwRate + blueSwRate + greenSwRate + flashlightRate + mapRate + radioRate
                    + teddybearRate + panRate + cookieRate + burgerRate + ballRate + breadRate + candyRate) {
                if (canBeSpawned(x, y, SWORD_PIX_SIZE, SWORD_PIX_SIZE)) {
                    itemList.add(new RedSword(x, y));
                    itemCounter++;
                }
            }
        }
        log.info("Items spawned successfully");
    }

    /**
     * Switch arrayList of items depending on the current state of the game.
     */
    private void switchItemList() {
        switch (PlayingStates.currentState) {
            case DUNGEON1:
                itemsOnGround = itemList;
                break;
            case PLANET1:
                itemsOnGround = planet1ItemList;
                break;
            case PLANET2:
                itemsOnGround = planet2ItemList;
                break;
            default:
                log.warning("Unknown state");
                break;
        }
    }

    /**
     * Check if the item can be spawned depending on the coordinates and size.
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    private boolean canBeSpawned(int x, int y, int width, int height) {
        return collision.isSpawnValid(x, y, width, height);
    }

    /**
     * If the player intersects with the item, player is able to pick up the item.
     * 
     * @return true if the player collides with the item, false otherwise
     */
    public boolean checkItemCollision() {
        playerHitbox = player.getHitbox();
        switchItemList();
        for (Item item : itemsOnGround) {
            if (item.getItemRect().getBoundsInParent().intersects(playerHitbox.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Pick up the item if the player collides with the item.
     * If the item is food, check if the item is stackable.
     * If the item is stackable, increase the quantity of the item.
     * If the item is not stackable, add the item to the inventory.
     */
    public void pickUpItem() {
        switchItemList();

        if (pickUpTimer <= delay) {
            return;
        }
        playerHitbox = player.getHitbox();
        for (Item item : itemsOnGround) {
            if (item.getItemRect().getBoundsInParent().intersects(playerHitbox.getBoundsInParent())) {
                if (!isFoodAddable(item)) {
                    return;
                }
                isItemAddable(item);
                break; // stop searching for item after one is picked up
            }
        }
    }

    /**
     * Check if the food item is stackable.
     * 
     * @param item to be checked
     * @return false if stack is full, if it's not full quantity is increased,
     *         otherwise return true
     */
    private boolean isFoodAddable(Item item) {
        boolean ret = true;
        if (item.getItemType() == 1) { // if the item is food = it's stackable
            for (Item invItem : inventory.getInvArray()) {
                if (invItem.getName() == item.getName()) {
                    Food food = (Food) invItem;
                    if (food.getQuantity() >= 5) {
                        log.info("Item is already in inventory and stack is full.");
                        ret = false;
                        break;
                    }
                    item.pickUP();
                    food.increaseQuantity();
                    itemsOnGround.remove(item);
                    log.info("Item picked up and added to inventory to stack.");
                    ret = false;
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * Checks if the item can be added to the inventory.
     * It's used in pickUpItem method. If the item isn't food,
     * this method is called. If the inventory is full, the item isn't added.
     * 
     * @param item
     */
    private void isItemAddable(Item item) {
        if (inventory.getSize() < 8 || inventory.getPlaceHolders().size() > 0) {
            inventory.addItem(item);
            item.pickUP();
            itemsOnGround.remove(item);
            pickUpTimer = 0;
            log.info("Item picked up and added to inventory.");
        } else {
            log.info("Inventory is full.");
        }
    }

    /**
     * Add item to the list of items on the ground.
     * Change the list of items depending on the current state of the game.
     * 
     * @param item
     */
    public void addItem(Item item) {
        switchItemList();
        itemsOnGround.add(item);
        item.drop();

    }

    public ArrayList<Item> getDungeonItemList() {
        return itemList;
    }

    public ArrayList<Item> getPlanet1ItemList() {
        return planet1ItemList;
    }

    public ArrayList<Item> getPlanet2ItemList() {
        return planet2ItemList;
    }

    public void setDungeonEntered(boolean dungeonEntered) {
        this.dungeonEntered = dungeonEntered;
    }

    public boolean getDungeonEntered() {
        return dungeonEntered;
    }

}
