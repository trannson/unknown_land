package cz.cvut.fel.pjv.items;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Abstract class representing food items.
 * Food items can be consumed by the player to restore health.
 * Food items are stackable.
 * Food items are displayed in the inventory.
 * Food items are displayed in the shop.
 * Food items are displayed in the world.
 * 
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public abstract class Food extends Item {

    private int quantity;
    private int slotOffX, slotOffY;
    protected int item_type;

    /**
     * Constructor of the Food class.
     * 
     * @param item_type which type of item it is
     * @param healing   how much health it restores
     * @param tileX     x coordinate
     * @param tileY     y coordinate
     * @param imageName name of the image file
     * @param width     width of the image
     * @param height    height of the image
     * @param quantity  how many items are in the stack
     * @param price     how much the item costs
     */
    public Food(int item_type, int healing, float tileX, float tileY, String imageName, int width, int height,
            int quantity, int price) {
        super(item_type, healing, tileX, tileY, imageName, width, height, price);
        this.quantity = quantity;
        this.item_type = item_type;
        slotOffX = 35;
        slotOffY = 45;
    }

    /**
     * Clone method for the Food class.
     * We need this method to be able to drop the item from the inventory.
     * 
     * @param x x coordinate where the cloned item will be placed
     * @param y y coordinate where the cloned item will be placed
     * @return new instance of the Food class
     */
    public abstract Food clone(int x, int y);

    /**
     * Draw the quantity of the item in the inventory.
     * 
     * @param gc
     * @param tileX x coordinate where the number will be displayed
     * @param tileY y coordinate where the number will be displayed
     */
    public void drawQuantity(GraphicsContext gc, float tileX, float tileY) {
        gc.setFont(new Font("Verdana", 10));
        gc.setFill(Color.WHITE);
        gc.fillText("x" + quantity, tileX + slotOffX, tileY + slotOffY);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int increaseQuantity() {
        return ++quantity;
    }

    public int decreaseQuantity() {
        return --quantity;
    }

    public int getQuantity() {
        return quantity;
    }

}
