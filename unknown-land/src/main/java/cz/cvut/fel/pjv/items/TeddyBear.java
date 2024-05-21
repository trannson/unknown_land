package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.ITEM_PIX_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.TEDDYBEAR_IMG;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.UNUASBLE_ITEM;

/**
 * Represents a teddy bear item.
 * Extends Item class.
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class TeddyBear extends Item{

    public TeddyBear(float tileX, float tileY) {
        super(UNUASBLE_ITEM, 0, tileX, tileY, TEDDYBEAR_IMG, ITEM_PIX_SIZE, ITEM_PIX_SIZE, 25);
    }
}
