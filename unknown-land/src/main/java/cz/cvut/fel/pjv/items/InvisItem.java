package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.INVIS_IMG;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.INVIS_ITEM;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.ITEM_PIX_SIZE;

/**
 * Represents an invisibility item.
 * Extends Item class.
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class InvisItem extends Item{

    public InvisItem(float tileX, float tileY) {
        super(INVIS_ITEM, 0, tileX, tileY, INVIS_IMG, ITEM_PIX_SIZE, ITEM_PIX_SIZE, 0);
    }
    
}
