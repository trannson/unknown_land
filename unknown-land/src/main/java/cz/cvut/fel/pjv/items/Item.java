package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.FOOD;
import static cz.cvut.fel.pjv.utils.Constants.PlayerConstants.PLAYER_MAX_HEALTH;
import static cz.cvut.fel.pjv.utils.LoadFiles.LoadImage;

import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.entities.Player;
import cz.cvut.fel.pjv.features.Inventory;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Abstract class representing items.
 * Items can be picked up by the player.
 * Items can be used by the player.
 * Items can be dropped by the player.
 * Items can be displayed in the inventory.
 * Items can be displayed in the shop.
 * Items can be displayed in the world.
 * 
 * @author Son Ngoc Tran
 */
public abstract class Item {

    private float tileX, tileY;
    private Image image;
    private int width, height;
    private Rectangle itemRect;
    private boolean isPickedUP;
    private int healing, price, itemType;
    private String imageName;
    protected Logger log; // used by mapItem

    /**
     * Constructor of the Item class.
     * 
     * @param item_type which type of item it is
     * @param healing   how much health it restores
     * @param tileX     x coordinate where the item will be placed
     * @param tileY     y coordinate where the item will be placed
     * @param imageName name of the image file
     * @param width     the width of the image
     * @param height    the height of the image
     * @param price     how much the item costs
     */
    public Item(int item_type, int healing, float tileX, float tileY,
            String imageName, int width, int height, int price) {
        this.itemType = item_type;
        this.healing = healing;
        this.tileX = tileX;
        this.tileY = tileY;
        this.image = LoadImage(imageName);
        this.width = width;
        this.height = height;
        this.itemRect = new Rectangle(tileX, tileY, width, height);
        this.itemType = item_type;
        this.imageName = imageName;
        this.price = price;

        isPickedUP = false;
        log = Logger.getLogger(Item.class.getName());
        log.setLevel(Level.INFO);

    }

    /**
     * If the item is not picked up, render it on the ground.
     * 
     * @param gc
     */
    public void render(GraphicsContext gc) {
        if (!isPickedUP) {
            gc.drawImage(image, tileX, tileY, width, height);
        }
    }

    /**
     * If inventory is opened, render the item description
     * in the inventory depending on the active slot.
     * On top of that food items display the healing amount.
     * 
     * @param gc
     * @param x  x coordinate where the description will be displayed
     * @param y  y coordinate where the description will be displayed
     */
    public void renderDescription(GraphicsContext gc, double x, double y) {
        drawBox(gc, x, y);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
        if (itemType == 1) {
            gc.setFill(Color.PINK);
            gc.fillText("Heals for: " + healing, x, y + 2);
        }
        gc.setFill(Color.YELLOW);
        gc.fillText("BUY/SELL price: " + price, x, y + 22);

    }

    /**
     * Draw a box for the item description.
     * 
     * @param gc
     * @param x  x coordinate where the box will be displayed
     * @param y  y coordinate where the box will be displayed
     */
    private void drawBox(GraphicsContext gc, double x, double y) {
        double placementX = x - 10;
        double placementY = y - 20;
        gc.setFill(Color.rgb(182, 182, 182, 0.7));
        gc.fillRect(placementX, placementY, 220, 50);
        gc.setStroke(Color.rgb(45, 46, 51, 0.8));
        gc.strokeRect(placementX, placementY, 220, 50);
    }

    /**
     * If the item is food, use it to heal the player.
     * If the player is at full health, don't use it.
     * If the player is not at full health, heal the player.
     * If the player is not at full health, but the healing amount is greater
     * than the missing health, heal the player to full health.
     * If the food item quantity is greater than 1, decrease the quantity.
     * If the food item quantity is 1, replace the item with an invisible item.
     * 
     * @param player
     * @param inventory
     * @param activeSlotIdx
     */
    public void use(Player player, Inventory inventory, int activeSlotIdx) {
        if (itemType == FOOD) {
            if (player.getHealth() == PLAYER_MAX_HEALTH) {
                log.info("Player is already at full health");
            } else if (player.getHealth() + healing < PLAYER_MAX_HEALTH) {
                Food temp = (Food) inventory.getItem(activeSlotIdx);
                player.setHealth(player.getHealth() + healing);
                if (temp.getQuantity() > 1) {
                    temp.decreaseQuantity();
                } else {
                    inventory.setItem(activeSlotIdx, new InvisItem(temp.getX(), temp.getY()));
                }
                log.info("Item healed player for " + healing);
            } else {
                Food temp = (Food) inventory.getItem(activeSlotIdx);
                player.setHealth(PLAYER_MAX_HEALTH);
                if (temp.getQuantity() > 1) {
                    temp.decreaseQuantity();
                } else {
                    inventory.setItem(activeSlotIdx, new InvisItem(temp.getX(), temp.getY()));
                }
                log.info("Item healed player for" + (PLAYER_MAX_HEALTH - player.getHealth()));
            }
        }
    }
    /**
     * Pick up the item if it is not picked up yet.
     */
    public void pickUP() {
        if (!isPickedUP) {
            isPickedUP = true;
            log.info("Item picked up");
        }

    }

    /**
     * If the item was picked up, drop it.
     */
    public void drop() {
        if (isPickedUP) {
            isPickedUP = false;
            log.info("Item dropped");
        }

    }

    public void setPlace(float x, float y) {
        this.tileX = x;
        this.tileY = y;
        this.itemRect.setX(x);
        this.itemRect.setY(y);
    }

    public int getPrice() {
        return price;
    }

    public float getX() {
        return tileX;
    }

    public float getY() {
        return tileY;
    }

    public Image getImage() {
        return image;
    }

    public Rectangle getItemRect() {
        return itemRect;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getItemType() {
        return itemType;
    }

    public String getName() {
        return imageName;
    }
}
