package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.FLASHLIGHT_IMG;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.ITEM_PIX_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.UNUASBLE_ITEM;

/**
 * Represents a flashlight item.
 * Extends Item class.
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class FlashLight extends Item {

    public FlashLight(float tileX, float tileY) {
        super(UNUASBLE_ITEM, 0, tileX, tileY, FLASHLIGHT_IMG, ITEM_PIX_SIZE, ITEM_PIX_SIZE, 40);
    }


    
}
