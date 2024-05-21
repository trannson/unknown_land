package cz.cvut.fel.pjv.features;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.entities.Merchant;
import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.items.*;
import cz.cvut.fel.pjv.view.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;
import static cz.cvut.fel.pjv.utils.LoadFiles.LoadFont;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.*;

/**
 * Shop class is used to display shop on the screen.
 * It is displayed with player's inventory.
 * Player can buy and sell items in the shop.
 * Shop is displayed when player interacts with the merchant.
 * 
 * @author Son Ngoc Tran
 */
public class Shop {

    private int slotSize, shopSize, maxShopSize, shopWidth, shopHeight, offset,
            itemSize, itemDrawOffset;
    private double placementX, placementY;
    private Camera camera;
    private Merchant merchant;
    private InventoryManager inventoryManager;
    private Inventory inventory;
    private boolean activeSlot;
    private Font font;
    private CoinManager coinManager;
    private Random random;
    private boolean firstBuy, firstSell;
    private PlayState playState;
    private ArrayList<Item> shopItems;
    private static final Logger log = Logger.getLogger(Shop.class.getName());

    /**
     * Shop that is displayed when player interacts with the merchant.
     * 
     * @param playState        used for camera.
     * @param merchant         used for for the shop display boolean.
     * @param inventoryManager used for inventory.
     * @param coinManager      used for player's coins.
     */
    public Shop(PlayState playState, Merchant merchant, InventoryManager inventoryManager, CoinManager coinManager) {
        this.playState = playState;
        camera = playState.getCamera();
        this.merchant = merchant;
        this.inventoryManager = inventoryManager;
        this.coinManager = coinManager;
        random = new Random();
        inventory = merchant.getInventory();
        shopItems = new ArrayList<>();
        font = LoadFont("transamericabold.ttf", 70);

        initVariables();
        addItems();
    }

    private void initVariables() {
        shopSize = 0;
        maxShopSize = 8;
        slotSize = 50;
        shopWidth = 400;
        shopHeight = 50;
        offset = 150;
        activeSlot = false;
        firstBuy = firstSell = false;
        itemSize = slotSize - 12;
        itemDrawOffset = 6;
    }

    /**
     * Add items to the shop.
     * Shop has 3 fixed items and the rest is random.
     * Random number is generated and based on the number,
     * different items are added to the shop.
     * If the shop is full, no more items are added.
     * If the item is food, quantity is set to random number between 1 and 5.
     */
    private void addItems() {
        shopItems.add(new RedSword(0, 0));
        shopItems.add(new BlueSword(0, 0));
        shopItems.add(new MapItem(0, 0));
        shopSize = 3;

        for (int i = 0; i < maxShopSize - shopSize; i++) {
            int randNum = random.nextInt(100);
            if (randNum < 20) {
                Food burger = new Burger(0, 0);
                burger.setQuantity(random.nextInt(5) + 1);
                shopItems.add(burger);
            } else if (randNum < 30) {
                shopItems.add(new GreenSword(0, 0));
            } else if (randNum < 40) {
                shopItems.add(new FlashLight(0, 0));
            } else if (randNum < 50) {
                shopItems.add(new Ball(0, 0));
            } else if (randNum < 65) {
                Food bread = new Bread(0, 0);
                bread.setQuantity(random.nextInt(5) + 1);
                shopItems.add(bread);
            } else if (randNum < 75) {
                shopItems.add(new Radio(0, 0));
            } else if (randNum < 85) {
                shopItems.add(new Pan(0, 0));
            } else {
                shopItems.add(new TeddyBear(0, 0));
            }
        }
        shopSize = maxShopSize;
    }

    /**
     * Update shop and inventory.
     * Check if the active slot is in the shop or inventory.
     * Check if the player is buying or selling an item.
     * 
     * @param pressedKeys set of keys pressed
     */
    public void update(HashSet<KeyCode> pressedKeys) {
        checkActiveSlot(pressedKeys);
        checkBuyingItem(pressedKeys);
        checkSellingItem(pressedKeys);
    }

    /**
     * Render shop on the screen if the inventory is open
     * and the player is close to the merchant.
     * Draw shop logo, slots, items, active slot and hint.
     * 
     * @param gc
     */
    public void render(GraphicsContext gc) {
        if (!merchant.getShopDisplay()) {
            activeSlot = false;
            inventory.setActiveSlot(true);
            inventoryManager.setItemsArray(inventory.getInvArray());
            return;
        }
        drawShopLogo(gc);
        drawShopRectangle(gc);
        drawItems(gc);
        drawSlots(gc);
        drawActiveSlot(gc);
        displayHint(gc);
    }

    /**
     * Draw items in the shop.
     * Draw food quantity if the item is food.
     * 
     * @param gc
     */
    private void drawItems(GraphicsContext gc) {
        placementX = camera.getX() + WINDOW_WIDTH / 2 - shopWidth / 2;
        placementY = camera.getY() + shopHeight + offset;
        for (int i = 0; i < shopItems.size(); i++) {
            Item currentItem = shopItems.get(i);
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
     * Draw big rectangle for the shop slots.
     * 
     * @param gc
     */
    private void drawShopRectangle(GraphicsContext gc) {
        placementX = camera.getX() + WINDOW_WIDTH / 2 - shopWidth / 2;
        placementY = camera.getY() + shopHeight + offset;
        gc.setFill(Color.rgb(182, 182, 182, 0.7));
        gc.fillRect(placementX, placementY, shopWidth, shopHeight);
    }

    /**
     * Draw slots for the shop.
     * Shop has only one row of slots.
     * 
     * @param gc
     */
    private void drawSlots(GraphicsContext gc) {
        int slotsPerRow = shopWidth / slotSize;
        gc.setStroke(Color.rgb(45, 46, 51, 0.8));
        gc.setLineWidth(4);
        for (int i = 0; i < slotsPerRow; i++) {
            int x = (int) placementX + i * slotSize;
            int y = (int) placementY;
            gc.strokeRect(x, y, slotSize, slotSize);
        }
    }

    /**
     * Check if the active slot is in the shop or inventory.
     * activeSlot is true if the player is in the shop.
     * activeSlot is false if the player is in the inventory.
     * 
     * @param pressedKeys
     */
    private void checkActiveSlot(HashSet<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.UP) && merchant.getShopDisplay()) {
            inventory.setActiveSlot(false);
            activeSlot = true;
            inventoryManager.setItemsArray(shopItems);
            pressedKeys.remove(KeyCode.UP);
        }
        if (pressedKeys.contains(KeyCode.DOWN) && merchant.getShopDisplay()) {
            inventory.setActiveSlot(true);
            activeSlot = false;
            inventoryManager.setItemsArray(inventory.getInvArray());
            pressedKeys.remove(KeyCode.DOWN);
        }
    }

    /**
     * Check if the player is buying an item.
     * If the item isn't food, check if the player has enough coins and
     * if the inventory isn't full. If the conditions are met, the player can buy
     * the item.
     * If the item is food, check if the player has enough coins and
     * if the inventory isn't full. It also check whether the player
     * has the same food item in the inventory. If he does, only increase the
     * quantity.
     * We have to clone the food item and add it to the inventory if the player
     * doesn't have it yet.
     * We also check if the inventory has a full stack of the food item. If he does,
     * we can't add more.
     * 
     * @param pressedKeys
     */
    private void checkBuyingItem(HashSet<KeyCode> pressedKeys) {
        boolean InvFoodisShopFood = false; // for checking if food in inventory is the same as in shop
        if (pressedKeys.contains(KeyCode.B) && activeSlot && inventoryManager.getActiveSlotIdx() <= shopItems.size() - 1
                && merchant.getShopDisplay()) {
            Item item = shopItems.get(inventoryManager.getActiveSlotIdx());
            if (item.getItemType() == INVIS_ITEM) {
                return;
            }
            // if player doesn't have enough coins to buy the item
            if (coinManager.getCoins() < item.getPrice()) {
                log.info("Not enough coins to buy item");
                pressedKeys.remove(KeyCode.B);
                return;
            }
            if (item.getItemType() == FOOD) { // FOOD ONLY
                Food tempShop = (Food) item;
                // if inventory has some amount of items
                for (int i = 0; i < inventory.getSize(); i++) {
                    if (inventory.getItem(i).getItemType() == FOOD) {
                        Food tempInv = (Food) inventory.getItem(i);
                        // if shop has an item that the player's inventory already has, just increase
                        // quantity in inventory
                        if (inventory.getItem(i).getName().equals(tempShop.getName())) {
                            InvFoodisShopFood = true;
                            if (tempInv.getQuantity() < MAX_STACK) {
                                tempInv.increaseQuantity(); // increase quantity of the item in inventory
                                break;
                            } else {
                                log.info("Inventory has a full stack");
                                return;
                            }
                        }
                    }
                }
                // if inventory is empty or doesn't have that food item yet or has that food
                // item already and InvFoodisShopFood is set to true so we don't clone and add
                // it again to inventory
                checkBuyingFood(tempShop, InvFoodisShopFood, pressedKeys);
            } else if (!isInvFull()) { // OTHER ITEMS
                shopItems.set(inventoryManager.getActiveSlotIdx(),
                        new InvisItem(item.getX(), item.getY()));
                inventory.addItem(item);
                pressedKeys.remove(KeyCode.B);
                coinManager.removeCoins(item.getPrice());
            }
            activateFirstBuy();
        }
    }

    /**
     * Increase the quest number if the player buys an item for the first time.
     * It happens only once.
     */
    private void activateFirstBuy() {
        if (!firstBuy) {
            playState.getQuests().increaseQuestNumber();
            firstBuy = true;
        }
    }

    /**
     * Check if the player is buying food. If the player doesn't have that food item
     * in the inventory,
     * clone it and add it to the inventory.
     * If the player has the food item in the inventory, just decrease the quantity
     * of the food item in the shop.
     * 
     * @param tempShop          temporary food item in the shop
     * @param InvFoodisShopFood boolean if the food item is the same in the
     *                          inventory and shop
     * @param pressedKeys       set of keys pressed
     */
    private void checkBuyingFood(Food tempShop, boolean InvFoodisShopFood, HashSet<KeyCode> pressedKeys) {
        // we decrease shop food item quantity if the quantity is more than 1 and if the
        // food item is the same as in inventory
        if (tempShop.getQuantity() > 1) {
            tempShop.decreaseQuantity();
            coinManager.removeCoins(tempShop.getPrice());
            // if the quantity is 1, we set the slot to invisible item - placeholder
        } else {
            shopItems.set(inventoryManager.getActiveSlotIdx(),
                    new InvisItem(tempShop.getX(), tempShop.getY()));
        }
        // if inventory isn't full + doesn't have that food item yet, we need to clone
        // it and add it to inventory to have 1 quantity
        if (!isInvFull() && !InvFoodisShopFood) {
            inventory.addItem(tempShop.clone(1, 1));
        }
        pressedKeys.remove(KeyCode.B);
    }

    /**
     * Check if the player is selling an item.
     * If the item is food and the quantity is more than 1, decrease the quantity.
     * If the item is food and the quantity is 1, set the slot to invisible item -
     * placeholder.
     * If the item isn't food, set the slot to invisible item - placeholder.
     * Add coins to the player's inventory.
     * Increase the quest number if the player sells an item for the first time.
     * 
     * @param pressedKeys
     */
    private void checkSellingItem(HashSet<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.B) && !activeSlot && inventory.getSize() > 0
                && inventoryManager.getActiveSlotIdx() <= inventory.getSize() - 1 && merchant.getShopDisplay()) {
            Item item = inventory.getItem(inventoryManager.getActiveSlotIdx());
            if (item.getItemType() != INVIS_ITEM) {
                if (item.getItemType() == FOOD && ((Food) item).getQuantity() > 1) {
                    ((Food) item).decreaseQuantity();
                    coinManager.addCoins(item.getPrice());
                } else {
                    inventory.setItem(inventoryManager.getActiveSlotIdx(),
                            new InvisItem(item.getX(), item.getY()));
                    coinManager.addCoins(item.getPrice());
                }
                activateFirstSell();
                pressedKeys.remove(KeyCode.B);
            }
        }
    }

    /**
     * Increase the quest number if the player sells an item for the first time.
     * It happens only once.
     */
    private void activateFirstSell() {
        if (!firstSell) {
            playState.getQuests().increaseQuestNumber();
            firstSell = true;
        }
    }

    private boolean isInvFull() {
        if (inventory.getSize() >= inventory.getMaxSize() && inventory.getPlaceHolders().size() < 1) {
            log.info("Inventory is full");
            return true;
        }
        return false;
    }

    /**
     * Draw the active slot in the shop.
     * It is a red rectangle around the active slot.
     * If the player is in the shop, the active slot is in the shop.
     * If the player is in the inventory, the active slot is in the inventory
     * so we don't draw the active slot in the shop.
     * 
     * @param gc
     */
    private void drawActiveSlot(GraphicsContext gc) {
        int index = inventoryManager.getActiveSlotIdx();
        if (!activeSlot) {
            return;
        }
        gc.setStroke(Color.RED);
        gc.setLineWidth(4); // Width of the slot borders

        int x = (int) placementX + index * slotSize;
        int y = (int) placementY;
        gc.strokeRect(x, y, slotSize, slotSize);
    }

    private void drawShopLogo(GraphicsContext gc) {
        String text = "SHOP";
        gc.setFont(font);
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10);
        gc.strokeText(text, placementX + 100, placementY - 30);
        gc.fillText(text, placementX + 100, placementY - 30);
    }

    /**
     * Display hint on the screen.
     * Hint changes based on the active slot.
     * If the player is in the shop, display BUY.
     * If the player is in the inventory, display SELL.
     * 
     * @param gc
     */
    private void displayHint(GraphicsContext gc) {
        double x = camera.getX() + WINDOW_WIDTH - 180;
        double y = camera.getY() + WINDOW_HEIGHT - 35;

        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        gc.setFill(Color.WHITE);
        // check for slots in shop, display BUY
        if (activeSlot && inventoryManager.getActiveSlotIdx() <= shopItems.size() - 1) {
            gc.fillText("Press 'B' to BUY", x, y);
        }
        // check for slots in inventory, display SELL
        else if (!activeSlot && inventoryManager.getActiveSlotIdx() <= inventory.getSize() - 1) {
            gc.fillText("Press 'B' to SELL", x, y);
        }
    }

    public boolean isFirstSell() {
        return firstSell;
    }

    public boolean isFirstBuy() {
        return firstBuy;
    }

    public void setFirstSell(boolean firstSell) {
        this.firstSell = firstSell;
    }

    public void setFirstBuy(boolean firstBuy) {
        this.firstBuy = firstBuy;
    }

    public ArrayList<Item> getShopItems() {
        return shopItems;
    }

    public void setShopItems(ArrayList<Item> shopItems) {
        this.shopItems = shopItems;
    }
    
}
