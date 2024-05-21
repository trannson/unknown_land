package cz.cvut.fel.pjv.gamesave;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.*;

import java.util.ArrayList;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.items.*;
import cz.cvut.fel.pjv.utils.Vector2D;

/**
 * Saves and loads items that are on the ground.
 * We're saving and loading 3 arraylist because we
 * have 3 places where items can be dropped on the ground.
 * @see GameSave
 * 
 * @author Son Ngoc Tran
 */
public class ItemSave {

    private PlayState playState;
    private Logger log;

    /**
     * ItemSave constructor.
     * 
     * @param playState used to get item data or set item data
     * @param log
     */
    public ItemSave(PlayState playState, Logger log) {
        this.playState = playState;
        this.log = log;
    }

    /**
     * We're saving names of items on the ground.
     * We also save their positions.
     * We only need this because we can create items from their names and position.
     * We're setting 3 different arraylists for 3 different places where items can
     * be dropped.
     * 
     * @param gameData used to store data
     */
    protected void saveGroundItems(GameData gameData) {
        gameData.planet1ItemNameList = new ArrayList<>();
        gameData.planet2ItemNameList = new ArrayList<>();
        gameData.dungeon1ItemNameList = new ArrayList<>();

        gameData.planet1ItemPositions = new ArrayList<>();
        gameData.planet2ItemPositions = new ArrayList<>();
        gameData.dungeon1ItemPositions = new ArrayList<>();

        ArrayList<Item> dungeonList = playState.getItemManager().getDungeonItemList();
        ArrayList<Item> planet1List = playState.getItemManager().getPlanet1ItemList();
        ArrayList<Item> planet2List = playState.getItemManager().getPlanet2ItemList();

        if (dungeonList != null) {
            for (Item item : dungeonList) {
                gameData.dungeon1ItemNameList.add(item.getName());
                gameData.dungeon1ItemPositions.add(new Vector2D(item.getX(), item.getY()));
            }
        }

        if (planet1List != null) {
            for (Item item : planet1List) {
                gameData.planet1ItemNameList.add(item.getName());
                gameData.planet1ItemPositions.add(new Vector2D(item.getX(), item.getY()));
            }
        }

        if (planet2List != null) {
            for (Item item : planet2List) {
                gameData.planet2ItemNameList.add(item.getName());
                gameData.planet2ItemPositions.add(new Vector2D(item.getX(), item.getY()));
            }
        }
    }

    /**
     * If there are names of items stored in GameData ground items,
     * we iterate through all of them and create + add item that
     * matches with itemType.
     * 
     * @param gameData
     */
    protected void loadGroundItems(GameData gameData) {
        ArrayList<String> dungeonList = gameData.dungeon1ItemNameList;
        ArrayList<String> planet1List = gameData.planet1ItemNameList;
        ArrayList<String> planet2List = gameData.planet2ItemNameList;

        ArrayList<Item> targetDungeonItems = playState.getItemManager().getDungeonItemList();
        ArrayList<Item> targetPlanet1Items = playState.getItemManager().getPlanet1ItemList();
        ArrayList<Item> targetplanet2Items = playState.getItemManager().getPlanet2ItemList();

        ArrayList<Vector2D> dungeonPositions = gameData.dungeon1ItemPositions;
        ArrayList<Vector2D> planet1Positions = gameData.planet1ItemPositions;
        ArrayList<Vector2D> planet2Positions = gameData.planet2ItemPositions;

        convertStringToItem(dungeonList, targetDungeonItems, dungeonPositions);
        convertStringToItem(planet1List, targetPlanet1Items, planet1Positions);
        convertStringToItem(planet2List, targetplanet2Items, planet2Positions);
    }

    /**
     * We're converting string names of items to actual items.
     * We check if the name matches with any item and create it.
     * Then we extract its position from positions arraylist and
     * set it to the item so it's on the right place.
     * 
     * @param itemList   game data storage
     * @param targetList where we're adding items
     * @param positions  game data storage for item positions
     */
    private void convertStringToItem(ArrayList<String> itemList, ArrayList<Item> targetList,
            ArrayList<Vector2D> positions) {
        targetList.clear();

        if (itemList == null) {
            return;
        }

        for (int i = 0; i < itemList.size(); i++) {
            float x = (float) positions.get(i).getX();
            float y = (float) positions.get(i).getY();
            switch (itemList.get(i)) {
                case BALL_IMG:
                    targetList.add(new Ball(x, y));
                    break;
                case BLUESWORD_IMG:
                    targetList.add(new BlueSword(x, y));
                    break;
                case BREAD_IMG:
                    targetList.add(new Bread(x, y));
                    break;
                case BURGER_IMG:
                    targetList.add(new Burger(x, y));
                    break;
                case CANDY_IMG:
                    targetList.add(new Candy(x, y));
                    break;
                case COOKIE_IMG:
                    targetList.add(new Cookie(x, y));
                    break;
                case FLASHLIGHT_IMG:
                    targetList.add(new FlashLight(x, y));
                    break;
                case GREENSWORD_IMG:
                    targetList.add(new GreenSword(x, y));
                    break;
                case INVIS_IMG:
                    targetList.add(new InvisItem(x, y));
                    break;
                case MAPITEM_IMG:
                    targetList.add(new MapItem(x, y));
                    break;
                case PAN_IMG:
                    targetList.add(new Pan(x, y));
                    break;
                case RADIO_IMG:
                    targetList.add(new Radio(x, y));
                    break;
                case REDSWORD_IMG:
                    targetList.add(new RedSword(x, y));
                    break;
                case TEDDYBEAR_IMG:
                    targetList.add(new TeddyBear(x, y));
                    break;
                default:
                    log.warning("Unknown item name while loading items");
                    break;
            }
        }
    }

}
