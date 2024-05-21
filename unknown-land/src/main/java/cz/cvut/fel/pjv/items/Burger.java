package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.BURGER_IMG;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.FOOD;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.ITEM_PIX_SIZE;

/**
 * Represents a burger item.
 * Extends Food class.
 * @see Food
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class Burger extends Food {
    
    public Burger(float tileX, float tileY) {
        super(FOOD, 30, tileX, tileY, BURGER_IMG, ITEM_PIX_SIZE, ITEM_PIX_SIZE, 1, 15);
    }

    @Override
    public Food clone(int x, int y) {
        return new Burger(x, y);
    }

    

}
