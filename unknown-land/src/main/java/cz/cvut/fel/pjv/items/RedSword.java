package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.REDSWORD_IMG;

/**
 * Represents a red sword item.
 * Extends Sword class.
 * @see Sword
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public class RedSword extends Sword{
    
    public RedSword(float tileX, float tileY) {
        super(tileX, tileY, REDSWORD_IMG, 120);
    }

    @Override
    public int getColor() {
        return 3;
    }
}
