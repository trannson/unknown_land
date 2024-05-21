package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.ITEM_PIX_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.RADIO_IMG;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.UNUASBLE_ITEM;

/**
 * Represents a radio item.
 * Extends Item class.
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class Radio extends Item{
    public Radio(float tileX, float tileY) {
        super(UNUASBLE_ITEM, 0, tileX, tileY, RADIO_IMG, ITEM_PIX_SIZE, ITEM_PIX_SIZE, 45);
    }

}
