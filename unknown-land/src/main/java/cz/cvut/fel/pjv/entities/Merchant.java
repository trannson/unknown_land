package cz.cvut.fel.pjv.entities;

import static cz.cvut.fel.pjv.utils.LoadFiles.LoadImage;
import static cz.cvut.fel.pjv.utils.InteractText.drawClueText;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.ENTITY_SCALE;

import cz.cvut.fel.pjv.features.Inventory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Class representing the Merchant entity.
 * Merchant is a subclass of the Entity class.
 * It's using Entity class methods.
 * Merchant is not moving and has a shop.
 * He stays in one place and sells items to the player.
 * Player can also sell items to the Merchant.
 * 
 * @see Entity
 * 
 * @author Son Ngoc Tran
 */
public class Merchant extends Entity {

    private Image sprite;
    private Player player;
    private boolean clue;
    private boolean shopDisplay;
    private Inventory inventory;
    private int merchantWidth, merchantHeight;

    /**
     * Constructor for the Merchant class.
     * 
     * @param inventory inventory of the player
     * @param player    player of the game
     * @param x         x-coordinate at which the Merchant is created
     * @param y         y-coordinate at which the Merchant is created
     */
    public Merchant(Inventory inventory, Player player, float x, float y) {
        super(x, y, 100, 0);
        this.player = player;
        this.inventory = inventory;
        sprite = LoadImage("entity/merchant.png");
        initHitbox(x, y, 120, 120);
        clue = false;
        shopDisplay = false;
        merchantWidth = 40;
        merchantHeight = 50;
    }

    /**
     * Update the Merchant.
     * Check if the player is close to the Merchant.
     * Check if the player has the inventory open.
     */
    public void update() {
        checkPlayerCollision();
        canDisplayShop();
    }

    /**
     * Render the Merchant.
     * Draw the Merchant sprite.
     * Draw the clue text if the player is close to the Merchant.
     * 
     * @param gc to draw the Merchant on the canvas
     */
    public void render(GraphicsContext gc) {
        gc.drawImage(sprite, x, y, merchantWidth * ENTITY_SCALE, merchantHeight * ENTITY_SCALE);
        /* draw the clue text if the player is close to the Merchant and the shop is not
        displayed */
        if (clue && !shopDisplay) {
            drawClueText("TAB", x, y - 10, gc);
        }
    }

    /**
     * Check if the player is close to the Merchant.
     * If the player is close to the Merchant, set clue to true
     * to display the clue text.
     */
    private void checkPlayerCollision() {
        if (hitbox.getBoundsInParent().intersects(player.getHitbox().getBoundsInParent())) {
            clue = true;
        } else {
            clue = false;
            shopDisplay = false;
        }
    }

    /**
     * Check if the player can display the shop.
     * If the player is close to the Merchant and the inventory is open,
     * the shop can be displayed.
     */
    private void canDisplayShop() {
        if (clue && inventory.inventoryOpen) {
            shopDisplay = true;
        } else if (!inventory.inventoryOpen) {
            shopDisplay = false;
        }

    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean getShopDisplay() {
        return shopDisplay;
    }

}
