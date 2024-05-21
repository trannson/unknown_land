package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.ITEM_PIX_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.MAPITEM_IMG;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.MAP_ITEM;

import cz.cvut.fel.pjv.entities.Player;
import cz.cvut.fel.pjv.features.Inventory;

/**
 * Represents a map item.
 * Extends Item class.
 * Item type is unique. It's usable.
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class MapItem extends Item {

    public MapItem(float tileX, float tileY) {
        super(MAP_ITEM, 0, tileX, tileY, MAPITEM_IMG, ITEM_PIX_SIZE, ITEM_PIX_SIZE, 100);

    }
    /**
     * If used for the first time, it will reveal the location of a new planet on
     * the navigation map. It will also display a message on the screen. - method in Inventory class
     * The map is then replaced by an invisible item.
     * 
     * @param player
     * @param inventory
     * @param activeSlotIdx
     */
    @Override
    public void use(Player player, Inventory inventory, int activeSlotIdx) {
        Item temp = inventory.getItem(activeSlotIdx);
        inventory.setItem(activeSlotIdx, new InvisItem(temp.getX(), temp.getY()));
        inventory.setMapUsed(true);
        log.info("Map used + NEW LOCATION REVELEAD on MAP!");
        

    }


}
