package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.BALL_IMG;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.ITEM_PIX_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.UNUASBLE_ITEM;

/**
 * Represents a ball item.
 * Extends Item class.
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class Ball extends Item {

    public Ball(float tileX, float tileY) {
        super(UNUASBLE_ITEM, 0, tileX, tileY, BALL_IMG, ITEM_PIX_SIZE, ITEM_PIX_SIZE, 15);

    }
}
