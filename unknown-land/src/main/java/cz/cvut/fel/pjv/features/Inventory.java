package cz.cvut.fel.pjv.features;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.TILE_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.entities.Player;
import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.items.Food;
import cz.cvut.fel.pjv.items.Item;
import cz.cvut.fel.pjv.items.Sword;
import cz.cvut.fel.pjv.utils.Vector2D;
import cz.cvut.fel.pjv.view.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static cz.cvut.fel.pjv.utils.LoadFiles.LoadFont;
import static cz.cvut.fel.pjv.utils.LoadFiles.LoadImage;

/**
 * Class for managing player's inventory.
 * It has methods for adding, removing and swapping items.
 * It deeply connects with InventoryManager class.
 * 
 * @author Son Ngoc Tran
 */
public class Inventory {

    private ArrayList<Item> inventory;
    private ArrayList<Integer> invisPlaceholders;
    private Camera camera;
    private int width, height;
    private double placementX, placementY;
    private int slotSize;
    private boolean mapUsed;
    private int mapTimer, mapThreshold, openedOffsetY, invMaxSize;
    private Font font;
    private Image mapBig;
    private boolean mark, activeSlot;
    private int markIdx, itemSize, itemDrawOffset;
    private Player player;
    private SpaceShip spaceShip;
    private PlayState playState;
    private Logger log;
    private Vector2D textPosOffset;
    public boolean inventoryOpen; // used in Merchant class

    /**
     * Constructor for Inventory.
     * We are initializing the inventory and placeholders.
     * Placeholder are used to replace item with invisible item
     * when the item is removed from the inventory.
     * 
     * @param playState used for the access to the camera and player
     * @param spaceShip used to set the map as revealed when used
     */
    public Inventory(PlayState playState, SpaceShip spaceShip) {
        this.playState = playState;
        this.camera = playState.getCamera();
        this.player = playState.getPlayer();
        this.spaceShip = spaceShip;

        font = LoadFont("transamericabold.ttf", 70);
        mapBig = LoadImage("items/map.png");
        inventory = new ArrayList<>();
        invisPlaceholders = new ArrayList<>();

        initVariables();

        log = Logger.getLogger(Inventory.class.getName());
        log.setLevel(Level.INFO);
    }

    private void initVariables() {
        width = 400;
        height = 50;
        slotSize = 50;
        mapUsed = false;
        mapTimer = 0;
        mapThreshold = 200;
        invMaxSize = 8;
        inventoryOpen = false;
        openedOffsetY = 0;
        activeSlot = true;
        itemSize = slotSize - 12;
        itemDrawOffset = 6;
        textPosOffset = new Vector2D(40, 30);
    }

    /**
     * Adding item to the inventory.
     * If there is a placeholder, we are replacing it with the item.
     * Otherwise, we are adding the item to the inventory if
     * the inventory is not full.
     * 
     * @param item item we want to add
     */
    public void addItem(Item item) {
        if (!invisPlaceholders.isEmpty()) {
            invisPlaceholders.sort(null);
            int index = invisPlaceholders.get(0); // get the first placeholder
            // if the index is valid and the inventory is not full
            if (index != -1 && index < inventory.size()) {
                inventory.set(index, item);
                invisPlaceholders.remove(0);
                log.info("Placeholder replaced with item");
            } else {
                log.warning("Error: Placeholder not found");
            }
        } else {
            if (inventory.size() < invMaxSize) {
                inventory.add(item);
                log.info("Item added to inventory");
            }
        }
    }

    /**
     * Removing item from the inventory if the inventory is not empty.
     * If the item is a placeholder, we are not removing it.
     * 
     * @param index index of the item we want to remove
     */
    public void removeItem(int index) {
        if (inventory.size() > 0) {
            if (inventory.get(index).getItemType() != INVIS_ITEM) {
                inventory.remove(index);
            }
        }
    }

    /**
     * Mainly used for replacing the item with the placeholder.
     * We're basically replacing the item on the index with
     * different item.
     * 
     * @param index index of the item we want to replace
     * @param item  item we want to replace the item on the index with
     */
    public void setItem(int index, Item item) {
        inventory.set(index, item);
        if (item.getItemType() == INVIS_ITEM) {
            invisPlaceholders.add(index);
            log.info("Placeholder added at: " + index);
        }
    }

    /**
     * Rendering the inventory.
     * We are drawing the inventory slots, items, active slot and map notification.
     * 
     * @param gc
     * @param index index of the active slot
     */
    public void render(GraphicsContext gc, int index) {
        checkInventoryOpen(gc); // if it's opened, draw the inventory
        drawInvRectangle(gc, openedOffsetY);
        drawSlots(gc);
        drawActiveSlot(gc, index);
        drawItems(gc);
        checkMapUsed(gc); // if the map is used, draw the notification
        checkSwapping(gc); // if the item is being marked(for swapping), draw the mark
    }

    /**
     * Drawing the items in the inventory.
     * If the item is a food, we are also drawing the quantity of the food.
     * 
     * @param gc
     */
    private void drawItems(GraphicsContext gc) {
        for (int i = 0; i < inventory.size(); i++) {
            Item currentItem = inventory.get(i);

            int x = (int) placementX + i * slotSize;
            int y = (int) placementY;
            gc.drawImage(currentItem.getImage(), x + itemDrawOffset, y + itemDrawOffset, itemSize, itemSize);

            if (currentItem.getItemType() == FOOD) {
                Food food = (Food) currentItem;
                food.drawQuantity(gc, x, y);
            }
        }
    }

    /**
     * Drawing the active slot red.
     * If the item in the active slot is a sword, we are setting the sword color.
     * That means that we are changing sprites in the player class.
     * 
     * @param gc
     * @param index index of the active slot
     */
    private void drawActiveSlot(GraphicsContext gc, int index) {
        // if the inventory is not opened, we don't need to draw the active slot
        if (!activeSlot) {
            return;
        }

        gc.setStroke(Color.RED);
        gc.setLineWidth(4); // Width of the slot borders

        // drawing the active slot with color red
        int x = (int) placementX + index * slotSize;
        int y = (int) placementY;
        gc.strokeRect(x, y, slotSize, slotSize);

        // if the item in the active slot is a sword, we are setting the sword color
        if (inventory.size() > index && inventory.get(index).getItemType() == SWORD) {
            Sword sword = (Sword) inventory.get(index);
            player.setSwordColor(sword.getColor());
        } else {
            player.setSwordColor(0);
        }
    }

    /**
     * Drawing the BIG rectangle for slots.
     * If TAB is pressed meaning the inventory is opened, we are moving the
     * inventory up.
     * 
     * @param gc
     * @param openedOffsetY offset for the inventory when opened
     */
    private void drawInvRectangle(GraphicsContext gc, int openedOffsetY) {
        placementX = camera.getX() + WINDOW_WIDTH / 2 - width / 2;
        placementY = camera.getY() + WINDOW_HEIGHT - height - 10 + openedOffsetY;

        gc.setFill(Color.rgb(182, 182, 182, 0.7));
        gc.fillRect(placementX, placementY, width, height);
    }

    /**
     * Drawing the slots in the inventory.
     * 
     * @param gc
     */
    private void drawSlots(GraphicsContext gc) {
        int slotsPerRow = width / slotSize;

        gc.setStroke(Color.rgb(45, 46, 51, 0.8)); // Color of the slot borders
        gc.setLineWidth(4); // Width of the slot borders

        for (int i = 0; i < slotsPerRow; i++) {
            int x = (int) placementX + i * slotSize;
            int y = (int) placementY;
            gc.strokeRect(x, y, slotSize, slotSize);
        }
    }

    /**
     * If map is used, we are drawing the notification.
     * We are drawing the map and the text: NEW LOCATION REVEALED!!!
     * 
     * @param gc
     */
    private void drawMapNotification(GraphicsContext gc) {
        gc.setFont(font);
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10);

        String text1 = "NEW LOCATION";
        String text2 = "REVEALED!!!";

        double text1X = camera.getX() + 100;
        double text1Y = camera.getY() + WINDOW_HEIGHT / 2 - 50;

        double text2X = text1X + 80;
        double text2Y = text1Y + 50;

        gc.strokeText(text1, text1X, text1Y);
        gc.strokeText(text2, text2X, text2Y);

        gc.fillText(text1, text1X, text1Y);
        gc.fillText(text2, text2X, text2Y);
        gc.drawImage(mapBig, camera.getX() + 300, camera.getY() + WINDOW_HEIGHT / 2 + 25, 200, 200);
    }

    /**
     * Checking if the inventory is opened.
     * If it is, we are darkening the background and drawing the inventory title.
     * 
     * @param gc
     */
    private void checkInventoryOpen(GraphicsContext gc) {
        if (inventoryOpen) {
            // darkening the background
            gc.setFill(new Color(0, 0, 0, 0.5));
            gc.fillRect(camera.getX() - TILE_SIZE, camera.getY() - TILE_SIZE, WINDOW_WIDTH + 2 * TILE_SIZE,
                    WINDOW_HEIGHT + 2 * TILE_SIZE);

            // drawing the inventory title + offsetting the inventory
            String textInventory = "INVENTORY";
            openedOffsetY = -150;
            gc.setFont(font);
            gc.setFill(Color.WHITE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(10);
            gc.strokeText(textInventory, placementX - textPosOffset.getX(), placementY - textPosOffset.getY());
            gc.fillText(textInventory, placementX - textPosOffset.getX(), placementY - textPosOffset.getY());
        } else {
            openedOffsetY = 0;
        }
    }

    /**
     * Checking if the map is used.
     * If it is, we are drawing the notification for a certain amount of time.
     * 
     * @param gc
     */
    private void checkMapUsed(GraphicsContext gc) {
        if (mapUsed && mapTimer < mapThreshold) {
            drawMapNotification(gc);
            mapTimer++;
        }
    }

    /**
     * Checking if the item is being marked for swapping.
     * If it is, we are drawing the mark.
     * 
     * @param gc
     */
    private void checkSwapping(GraphicsContext gc) {
        if (mark) {
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(4);
            gc.strokeRect(placementX + markIdx * slotSize, placementY, slotSize, slotSize);
        }
    }

    /**
     * Swapping items in the inventory.
     * If the marked item is invisible and the active slot is invisible, no need to
     * remove or add the placeholder.
     * If the marked item is invisible, we are removing the placeholder at the
     * marked position and adding the item to the marked position.
     * If the active slot is invisible, we are removing the placeholder at the
     * active slot and adding the item to the active slot.
     * It's used by InventoryManager class.
     * 
     * @param temp          one of the items we want to swap
     * @param tempIdx       index of the temp item we want to swap
     * @param activeSlotIdx index of the second item we want to swap
     */
    protected void swapItems(Item temp, int tempIdx, int activeSlotIdx) {
        // if the marked item is invisible and the active slot is invisible, no need
        // remove or add the placeholder
        if (temp.getItemType() == INVIS_ITEM && inventory.get(activeSlotIdx).getItemType() == INVIS_ITEM) {
            log.info("Swapping placeholders doesn't do anything.");
        }

        else if (temp.getItemType() == INVIS_ITEM) { // if marked item is invisible -> placeholder
            // remove the placeholder at marked pos + add item to marked pos and add
            // placeholder at the active slot
            invisPlaceholders.remove(invisPlaceholders.indexOf(tempIdx)); // indexOf returns index of the element(idx)
            invisPlaceholders.add(activeSlotIdx); // add the placeholder at the active slot
            log.info("Swapping placeholder with item.");
        } else if (inventory.get(activeSlotIdx).getItemType() == INVIS_ITEM) { // if active slot is invisible -> placeholder
            // remove the placeholder at active slot + add item to active slot and add
            // placeholder at the marked pos
            invisPlaceholders.remove(invisPlaceholders.indexOf(activeSlotIdx));
            invisPlaceholders.add(tempIdx);
            log.info("Swapping item with placeholder.");
        }

        inventory.set(tempIdx, inventory.get(activeSlotIdx));
        inventory.set(activeSlotIdx, temp);

        mark = false;
    }

    /**
     * Setting the item as marked for swapping.
     * Used by InventoryManager class.
     * 
     * @param activeSlotIdx
     * @return first item we want to swap
     */
    protected Item setMark(int activeSlotIdx) {
        mark = true;
        markIdx = activeSlotIdx;
        Item item = inventory.get(activeSlotIdx);
        return item;
    }

    public void setActiveSlot(boolean active) {
        activeSlot = active;
    }

    protected void setOpen(boolean inventoryOpen) {
        this.inventoryOpen = inventoryOpen;
    }

    /**
     * Setting the map as used.
     * If the map is used, we are revealing the map and increasing the quest number.
     * We are displaying the notification for a certain amount of time.
     * 
     * @param used
     */
    public void setMapUsed(boolean used) {
        if (used) {
            mapUsed = used;
            mapTimer = 0; // reset the timer
            spaceShip.setMapRevealed(true);
            this.playState.getQuests().increaseQuestNumber();
        }
    }

    public ArrayList<Integer> getPlaceHolders() {
        return invisPlaceholders;
    }

    public int getSize() {
        return inventory.size();
    }

    public Item getItem(int index) {
        return inventory.get(index);
    }

    public ArrayList<Item> getInvArray() {
        return inventory;
    }

    public int getMaxSize() {
        return invMaxSize;
    }

    public void loadInventory(ArrayList<Item> inv) {
        this.inventory = inv;
    }

    public void setPlaceholders(ArrayList<Integer> placeholders) {
        this.invisPlaceholders = placeholders;
    }

    public boolean getMapUsed() {
        return mapUsed;
    }
    
    public void setMapTimerToMax() {
        mapTimer = mapThreshold;
    }
}
