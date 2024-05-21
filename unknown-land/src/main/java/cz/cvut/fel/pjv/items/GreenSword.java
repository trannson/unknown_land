package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.GREENSWORD_IMG;

/**
 * Represents a green sword item.
 * Extends Sword class.
 * @see Sword
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class GreenSword extends Sword { 
    
    public GreenSword(float tileX, float tileY) {
        super(tileX, tileY, GREENSWORD_IMG, 40);
    }

    @Override
    public int getColor() {
        return 1;
    }
}
