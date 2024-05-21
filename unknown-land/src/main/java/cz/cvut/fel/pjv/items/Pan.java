package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.ITEM_PIX_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.PAN_IMG;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.UNUASBLE_ITEM;

/**
 * Represents a pan item.
 * Extends Item class.
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class Pan extends Item{

    public Pan(float tileX, float tileY) {
        super(UNUASBLE_ITEM,0 , tileX, tileY, PAN_IMG, 40, ITEM_PIX_SIZE, 25);
    }

}
