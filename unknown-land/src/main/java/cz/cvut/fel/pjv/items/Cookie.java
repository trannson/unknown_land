package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.COOKIE_IMG;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.FOOD;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.ITEM_PIX_SIZE;

/**
 * Represents a cookie item.
 * Extends Food class.
 * @see Food
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class Cookie extends Food {

    public Cookie(float tileX, float tileY) {
        super(FOOD, 15, tileX, tileY, COOKIE_IMG, ITEM_PIX_SIZE, ITEM_PIX_SIZE, 1, 5);
    }

    @Override
    public Food clone(int x, int y) {
        return new Cookie(x, y);
    }
}
