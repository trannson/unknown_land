package cz.cvut.fel.pjv.gamesave;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.*;

import java.util.ArrayList;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.features.Shop;
import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.items.*;

/**
 * Class to load and save shop data.
 * It's used in GameSave class.
 * We're saving shop items and their quantities.
 * We're also saving if player has bought or sold
 * something for the first time. This is to avoid
 * quest increasing when it shouldn't.
 * @see GameSave
 * 
 * @author Son Ngoc Tran
 */
public class ShopSave {

    private PlayState playState;
    private Logger log;

    /**
     * Constructor to save shop data.
     * 
     * @param playState to set/get data
     * @param log
     */
    public ShopSave(PlayState playState, Logger log) {
        this.playState = playState;
        this.log = log;
    }

    /**
     * We're saving shop items and their quantities.
     * We're putting data in gameData storage.
     * We also save if player has every sold or bought
     * anything so we wouldn't increase quest number
     * again.
     * We only need to save item names and quantities
     * because we can create items from their names and
     * set their quantities. If item is food, we set its
     * quantity. On the other hand, if it's not
     * food we put quantity 0 to it. Otherwise we would
     * have problem with accesing quantity array list.
     * 
     * @param gameData
     */
    protected void saveShopData(GameData gameData) {
        gameData.shopNameList = new ArrayList<>();
        gameData.shopItemQuantities = new ArrayList<>();

        gameData.firstShopSell = playState.getShop().isFirstSell();
        gameData.firstShopBuy = playState.getShop().isFirstBuy();

        Shop shop = playState.getShop();
        ArrayList<Item> shopList = shop.getShopItems();

        if (shopList == null) {
            return;
        }
        for (Item item : shopList) {
            gameData.shopNameList.add(item.getName());
            if (item.getItemType() == FOOD) {
                Food food = (Food) item;
                gameData.shopItemQuantities.add(food.getQuantity());
            } else {
                gameData.shopItemQuantities.add(0);
            }
        }
    }

    /**
     * Loading item names and quantities from gameData storage.
     * We need to clear array list first, otherwise we would
     * have old items from the previous load.
     * We set firstBuy and firstSell booleans.
     * If the item type matches with our item types, we
     * create a new item and add it to the shop.
     * If the item is food, we set quantity to it.
     * 
     * @param gameData
     */
    public void loadShopData(GameData gameData) {
        playState.getShop().getShopItems().clear();

        playState.getShop().setFirstSell(gameData.firstShopSell);
        playState.getShop().setFirstBuy(gameData.firstShopBuy);

        if (gameData.shopNameList == null) {
            return;
        }

        for (String itemName : gameData.shopNameList) {
            switch (itemName) {
                case BALL_IMG:
                    playState.getShop().getShopItems().add(new Ball(0, 0));
                    break;
                case BLUESWORD_IMG:
                    playState.getShop().getShopItems().add(new BlueSword(0, 0));
                    break;
                case BREAD_IMG:
                    playState.getShop().getShopItems().add(new Bread(0, 0));
                    break;
                case BURGER_IMG:
                    playState.getShop().getShopItems().add(new Burger(0, 0));
                    break;
                case CANDY_IMG:
                    playState.getShop().getShopItems().add(new Candy(0, 0));
                    break;
                case COOKIE_IMG:
                    playState.getShop().getShopItems().add(new Cookie(0, 0));
                    break;
                case FLASHLIGHT_IMG:
                    playState.getShop().getShopItems().add(new FlashLight(0, 0));
                    break;
                case GREENSWORD_IMG:
                    playState.getShop().getShopItems().add(new GreenSword(0, 0));
                    break;
                case INVIS_IMG:
                    playState.getShop().getShopItems().add(new InvisItem(0, 0));
                    break;
                case MAPITEM_IMG:
                    playState.getShop().getShopItems().add(new MapItem(0, 0));
                    break;
                case PAN_IMG:
                    playState.getShop().getShopItems().add(new Pan(0, 0));
                    break;
                case RADIO_IMG:
                    playState.getShop().getShopItems().add(new Radio(0, 0));
                    break;
                case REDSWORD_IMG:
                    playState.getShop().getShopItems().add(new RedSword(0, 0));
                    break;
                case TEDDYBEAR_IMG:
                    playState.getShop().getShopItems().add(new TeddyBear(0, 0));
                    break;
                default:
                    log.warning("Unknown item name");
                    break;
            }
        }
        if (gameData.shopItemQuantities != null) {
            for (int i = 0; i < gameData.shopItemQuantities.size(); i++) {
                Item item = playState.getShop().getShopItems().get(i);
                if (item.getItemType() == FOOD) {
                    Food food = (Food) item;
                    food.setQuantity(gameData.shopItemQuantities.get(i));
                }
            }
        }
    }

}
