package cz.cvut.fel.pjv.features;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.items.Food;
import cz.cvut.fel.pjv.items.InvisItem;
import cz.cvut.fel.pjv.items.Item;
import cz.cvut.fel.pjv.items.ItemManager;
import cz.cvut.fel.pjv.utils.Vector2D;
import cz.cvut.fel.pjv.view.Camera;
import cz.cvut.fel.pjv.entities.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

/**
 * Class for managing player's inventory.
 * It's mainly used for key handling and rendering.
 * @see Inventory
 * 
 * @author Son Ngoc Tran
 */
public class InventoryManager {

    private ArrayList<Item> items;
    private Inventory inventory;
    private ItemManager itemManager;
    private Player player;
    private int activeSlotIdx;
    private boolean inventoryOpen;
    private boolean markFirst, firstTimePick;
    private Item temp;
    private int tempIdx;
    private Camera camera;
    private SpaceShip spaceShip;
    private PlayState playState;
    private Vector2D descriptionOffset;
    private Logger log;

    /**
     * Constructor for InventoryManager.
     * 
     * @param playState   used for getting camera, player and quests
     * @param inventory   player's inventory
     * @param itemManager manager for items
     * @param spaceShip   used for rendering - cause we don't want render inventory
     *                    when map is opened
     */
    public InventoryManager(PlayState playState, Inventory inventory, ItemManager itemManager, SpaceShip spaceShip) {
        this.playState = playState;
        this.inventory = inventory;
        this.itemManager = itemManager;
        this.spaceShip = spaceShip;
        camera = playState.getCamera();
        items = inventory.getInvArray();
        player = playState.getPlayer();
        activeSlotIdx = 0;
        inventoryOpen = false;
        markFirst = false;
        firstTimePick = false;
        descriptionOffset = new Vector2D(15, WINDOW_HEIGHT - 50);

        log = Logger.getLogger(InventoryManager.class.getName());
        log.setLevel(Level.INFO);
    }

    /**
     * Check for pressed keys and update inventory.
     * It's only active when map is not opened.
     * 
     * @param pressedKeys - set of pressed keys
     */
    public void update(HashSet<KeyCode> pressedKeys) {
        if (!spaceShip.isMapOpened()) {
            updateActiveSlot(pressedKeys);
            checkKeyCodes(pressedKeys);
            updateActiveWithArrows(pressedKeys);
        }
    }

    /**
     * Render inventory.
     * It's only active when map is not opened.
     * 
     * @param gc
     */
    public void render(GraphicsContext gc) {
        if (!spaceShip.isMapOpened()) {
            inventory.render(gc, activeSlotIdx);
            drawItemDescription(gc, items);
        }
    }

    /**
     * Check for pressed keys and update inventory depending on the key.
     * 
     * @param pressedKeys set of pressed keys
     */
    public void checkKeyCodes(HashSet<KeyCode> pressedKeys) {
        checkFKey(pressedKeys);
        checkQKey(pressedKeys);
        checkEKey(pressedKeys);
        checkTABKey(pressedKeys);
        checkSwapItems(pressedKeys);
    }

    /**
     * Check if player is on the item and press F to pick it up.
     * If it's the first time player picks up the item, increase quest number by 1.
     * 
     * @param pressedKeys set of pressed keys
     */
    private void checkFKey(HashSet<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.F) && itemManager.checkItemCollision()) {
            itemManager.pickUpItem();
            if (!firstTimePick) {
                this.playState.getQuests().increaseQuestNumber();
                firstTimePick = true;
                log.info("First item picked up, quest number increased by 1.");
            }
        }
    }

    /**
     * Check if player wants to drop item from inventory.
     * If markFirst is true meaning that the first item is marked for swapping,
     * prohibit dropping item.
     * Otherwise, drop the item on the player's position.
     * If the item is food and quantity is more than 1, decrease quantity by 1 and
     * drop the new food item.
     * If the quantity is 1, drop the item and put placeholder in the inventory at
     * that slot.
     * 
     * @param pressedKeys set of pressed keys
     */
    private void checkQKey(HashSet<KeyCode> pressedKeys) {
        // prohibit dropping item if the first item is marked(for swapping)
        if (markFirst) {
            return;
        }
        if (pressedKeys.contains(KeyCode.Q) && inventory.getSize() > 0 && activeSlotIdx < inventory.getSize()) {
            Item item = inventory.getItem(activeSlotIdx);
            if (item.getItemType() != INVIS_ITEM) { // can't drop invis item
                if (item.getItemType() == FOOD && ((Food) item).getQuantity() > 1) { // if food, check quantity and drop
                                                                                     // only one
                    Food food = (Food) item;
                    Food newFood = food.clone(player.getX(), player.getY());
                    food.decreaseQuantity();
                    newFood.setPlace(player.getX(), player.getY()); 
                    itemManager.addItem(newFood);
                    log.info("Food dropped, quantity decreased by 1.");
                } else {
                    inventory.setItem(activeSlotIdx,
                            new InvisItem(item.getX(), item.getY()));
                    item.setPlace(player.getX(), player.getY());
                    itemManager.addItem(item);
                    log.info("Item dropped.");
                }
                pressedKeys.remove(KeyCode.Q);
            }
        }
    }

    /**
     * Check if player wants to use item from inventory.
     * If the item is not usable, do nothing.
     * If the item is usable(FOOD), use it.
     * 
     * @param pressedKeys set of pressed keys
     */
    private void checkEKey(HashSet<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.E) && inventory.getSize() > 0 && activeSlotIdx < inventory.getSize()) {
            Item item = inventory.getItem(activeSlotIdx);
            if (item.getItemType() != UNUASBLE_ITEM) {
                item.use(player, inventory, activeSlotIdx);
            }
            pressedKeys.remove(KeyCode.E);
        }
    }

    /**
     * Check if player wants to open/close inventory.
     * Using toggle mechanism.
     * 
     * @param pressedKeys
     */
    private void checkTABKey(HashSet<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.TAB)) {
            inventoryOpen = !inventoryOpen;
            inventory.setOpen(inventoryOpen);
            pressedKeys.remove(KeyCode.TAB);
            log.info("Inventory open: " + inventoryOpen);
        }
    }

    /**
     * Update active slot depending on the pressed digit.
     * 
     * @param pressedKeys set of pressed keys
     */
    private void updateActiveSlot(HashSet<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.DIGIT1)) {
            activeSlotIdx = 0;
        } else if (pressedKeys.contains(KeyCode.DIGIT2)) {
            activeSlotIdx = 1;
        } else if (pressedKeys.contains(KeyCode.DIGIT3)) {
            activeSlotIdx = 2;
        } else if (pressedKeys.contains(KeyCode.DIGIT4)) {
            activeSlotIdx = 3;
        } else if (pressedKeys.contains(KeyCode.DIGIT5)) {
            activeSlotIdx = 4;
        } else if (pressedKeys.contains(KeyCode.DIGIT6)) {
            activeSlotIdx = 5;
        } else if (pressedKeys.contains(KeyCode.DIGIT7)) {
            activeSlotIdx = 6;
        } else if (pressedKeys.contains(KeyCode.DIGIT8)) {
            activeSlotIdx = 7;
        }
    }

    /**
     * Update active slot with arrows.
     * Moving the active slot to the left or right.
     * 
     * @param pressedKeys
     */
    private void updateActiveWithArrows(HashSet<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.LEFT)) {
            if (activeSlotIdx > 0) {
                activeSlotIdx--;
            }
            pressedKeys.remove(KeyCode.LEFT);
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            if (activeSlotIdx < inventory.getMaxSize() - 1) {
                activeSlotIdx++;
            }
            pressedKeys.remove(KeyCode.RIGHT);
        }
    }

    /**
     * Check if player wants to swap items in inventory.
     * If the size of inventory is more than 1 and the active slot is not bigger
     * than the inventory size,
     * mark the item. After the item is marked, mark another one. That swaps the
     * items.
     * Using toggle mechanism.
     * 
     * @param pressedKeys set of pressed keys
     */
    private void checkSwapItems(HashSet<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.ENTER) && inventory.getSize() > 1 && activeSlotIdx < inventory.getSize()) {
            markFirst = !markFirst;
            // if the first item is marked, save it and its index
            if (markFirst) {
                temp = inventory.setMark(activeSlotIdx);
                tempIdx = activeSlotIdx;

            }
            // if the first item is not marked, swap the items
            else {
                inventory.swapItems(temp, tempIdx, activeSlotIdx);
            }
            pressedKeys.remove(KeyCode.ENTER);
        }
    }

    /**
     * Draw item description.
     * If the active slot is in the shop inventory, render the shop item.
     * If the active slot is in the player's inventory, render the player's item.
     * 
     * @param gc
     * @param box player's or shop's inventory
     */
    private void drawItemDescription(GraphicsContext gc, ArrayList<Item> box) {
        if (box.size() > 0 && activeSlotIdx < box.size() && inventoryOpen) {
            Item item = box.get(activeSlotIdx);
            if (item.getItemType() != INVIS_ITEM) {
                item.renderDescription(gc, camera.getX() + descriptionOffset.getX(),
                        camera.getY() + descriptionOffset.getY());
            }
        }
    }

    public int getActiveSlotIdx() {
        return activeSlotIdx;
    }

    // used by Shop class
    public void setItemsArray(ArrayList<Item> items) {
        this.items = items;
    }
}
