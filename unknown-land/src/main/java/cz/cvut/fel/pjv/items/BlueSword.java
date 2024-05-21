package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.BLUESWORD_IMG;

/**
 * Represents a blue sword item.
 * Extends Sword class.
 * @see Sword
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class BlueSword extends Sword {
    
    public BlueSword(float tileX, float tileY) {
        super(tileX, tileY, BLUESWORD_IMG, 80);
    }

    @Override
    public int getColor() {
        return 2;
    }
}
