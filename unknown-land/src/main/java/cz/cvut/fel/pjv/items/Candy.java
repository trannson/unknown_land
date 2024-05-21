package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.CANDY_IMG;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.FOOD;
import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.ITEM_PIX_SIZE;

/**
 * Represents a candy item.
 * Extends Food class.
 * @see Food
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class Candy extends Food {

    public Candy(float tileX, float tileY) {
        super(FOOD, 10, tileX, tileY, CANDY_IMG, ITEM_PIX_SIZE, ITEM_PIX_SIZE, 1, 2);
    }

    @Override
    public Food clone(int x, int y) {
        return new Candy(x, y);
    }
    
    
}
