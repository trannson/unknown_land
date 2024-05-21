package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.ITEM_PIX_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.BREAD_IMG;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.FOOD;

/**
 * Represents a bread item.
 * Extends Food class.
 * @see Food
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class Bread extends Food{
    
    public Bread(float tileX, float tileY) {
        super(FOOD,20, tileX, tileY, BREAD_IMG, ITEM_PIX_SIZE, ITEM_PIX_SIZE, 1, 8);
    }

    @Override
    public Food clone(int x, int y) {
        return new Bread(x, y);
    }
    
}
