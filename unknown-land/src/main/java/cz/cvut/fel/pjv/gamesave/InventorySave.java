package cz.cvut.fel.pjv.gamesave;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.*;

import java.util.ArrayList;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.items.*;

/**
 * Saves and loads inventory data.
 * Everything including placeholders and items is stored in GameData object.
 * We're putting data into GameData object when saving and extracting data from
 * it when loading.
 * @see GameSave
 * 
 * @author Son Ngoc Tran
 */
public class InventorySave {

    private PlayState playState;
    private Logger log;

    /**
     * InventorySave constructor.
     * 
     * @param playState used to get inventory data
     * @param log       used to log warnings
     */
    public InventorySave(PlayState playState, Logger log) {
        this.playState = playState;
        this.log = log;
    }

    /**
     * We're saving names of items in inventory.
     * That's all we need to save because we can create items from their names.
     * We cannot save items themselves because they're not serializable.
     * We also save item quantity but only food has quantity more than 0.
     * 
     * @param gameData used to store data
     */
    protected void saveInventoryItems(GameData gameData) {
        gameData.invisPlaceholders = playState.getInventory().getPlaceHolders();
        gameData.inventoryItemNames = new ArrayList<>();
        gameData.foodQuantities = new ArrayList<>();

        for (int i = 0; i < playState.getInventory().getSize(); i++) {
            gameData.inventoryItemNames.add(playState.getInventory().getItem(i).getName());
            if (playState.getInventory().getItem(i).getItemType() == FOOD) {
                Food food = (Food) playState.getInventory().getItem(i);
                gameData.foodQuantities.add(food.getQuantity());
            } else {
                gameData.foodQuantities.add(0);
            }
        }
    }

    /**
     * If there are names of items stored in GameData inventory,
     * we iterate through all of them and create + add item that
     * matches with itemName.
     * If there are names in game data storage, there has
     * to be quantities. That's the reason we iterate over
     * quantities array list and if the item is food we set
     * the quantity of it. We also update placeholder array
     * list that store indexes. Otherwise we would have
     * invisible items in inventory that cannot be replaced.
     * We use playstate to set everything in it's right place.
     * 
     * @param gameData storage from which we extract data
     */
    protected void loadInventoryItems(GameData gameData) {
        playState.getInventory().getInvArray().clear();

        if (gameData.inventoryItemNames == null) {
            return;
        }
        for (String itemName : gameData.inventoryItemNames) {
            switch (itemName) {
                case BALL_IMG:
                    playState.getInventory().addItem(new Ball(0, 0));
                    break;
                case BLUESWORD_IMG:
                    playState.getInventory().addItem(new BlueSword(0, 0));
                    break;
                case BREAD_IMG:
                    playState.getInventory().addItem(new Bread(0, 0));
                    break;
                case BURGER_IMG:
                    playState.getInventory().addItem(new Burger(0, 0));
                    break;
                case CANDY_IMG:
                    playState.getInventory().addItem(new Candy(0, 0));
                    break;
                case COOKIE_IMG:
                    playState.getInventory().addItem(new Cookie(0, 0));
                    break;
                case FLASHLIGHT_IMG:
                    playState.getInventory().addItem(new FlashLight(0, 0));
                    break;
                case GREENSWORD_IMG:
                    playState.getInventory().addItem(new GreenSword(0, 0));
                    break;
                case INVIS_IMG:
                    playState.getInventory().addItem(new InvisItem(0, 0));
                    break;
                case MAPITEM_IMG:
                    playState.getInventory().addItem(new MapItem(0, 0));
                    break;
                case PAN_IMG:
                    playState.getInventory().addItem(new Pan(0, 0));
                    break;
                case RADIO_IMG:
                    playState.getInventory().addItem(new Radio(0, 0));
                    break;
                case REDSWORD_IMG:
                    playState.getInventory().addItem(new RedSword(0, 0));
                    break;
                case TEDDYBEAR_IMG:
                    playState.getInventory().addItem(new TeddyBear(0, 0));
                    break;
                default:
                    log.warning("Unknown item name");
                    break;
            }
        }
        if (gameData.foodQuantities != null) {
            for (int i = 0; i < gameData.foodQuantities.size(); i++) {
                if (playState.getInventory().getItem(i).getItemType() == FOOD) {
                    Food food = (Food) playState.getInventory().getItem(i);
                    food.setQuantity(gameData.foodQuantities.get(i));
                }
            }
        }
        if (gameData.invisPlaceholders != null) {
            playState.getInventory().setPlaceholders(gameData.invisPlaceholders);
        }
    }

    /**
     * We're saving if the map is used.
     * Otherwise the map used text will be displayed again.
     */
    protected void saveMapUsed(GameData gameData) {
        gameData.mapUsed = playState.getInventory().getMapUsed();
    }

    /**
     * Loading boolean mapUsed to prevent map used
     * text from being displayed again.
     * 
     * @param gameData
     */
    protected void loadMapUsed(GameData gameData) {
        playState.getInventory().setMapUsed(gameData.mapUsed);
        if (gameData.mapUsed) {
            playState.getInventory().setMapTimerToMax();
        }
    }

}