package cz.cvut.fel.pjv.items;

import static cz.cvut.fel.pjv.utils.Constants.ItemConstants.SWORD;

/**
 * Abstract class Sword. Can't be instantiated.
 * Represents a sword item.
 * Extends Item class.
 * @see Item
 * 
 * @author Son Ngoc Tran
 */
public abstract class Sword extends Item  {
    
    public Sword(float tileX, float tileY, String fileName, int price) {
        super(SWORD, 0, tileX, tileY, fileName, 40, 40, price);
    }

    /**
     * Get color of sword
     * Every sword has different int color
     * @return
     */
    public abstract int getColor();
}
