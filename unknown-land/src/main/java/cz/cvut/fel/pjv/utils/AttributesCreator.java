package cz.cvut.fel.pjv.utils;

import static cz.cvut.fel.pjv.utils.Constants.Attributes.*;
import static cz.cvut.fel.pjv.utils.Constants.Enemy.*;

import cz.cvut.fel.pjv.entities.EnemyAttributes;

/**
 * Each enemy has different health, damage and HITBOXES!
 * HITBOXES are used for collision detection.
 * SolidBox is used for collision with walls.
 * DmgBox is used for colllision with player. - player can deal 
 * damage to DmgBox.
 * WeaponBox is used for collision with player. - enemy can deal
 * damage to WeaponBox.
 * Offset is used to set HITBOXES in the right position.
 * 
 * @see EnemyAttributes
 * 
 * @author Son Ngoc Tran
 */
public class AttributesCreator {
    
    private Vector2D skelSolidOffset, skelDmgBoxOffset, skelWeaponOffset;
    private Vector2D zomSolidOffset, zomDmgBoxOffset, zomWeaponOffset;
    private Vector2D eyeSolidOffset, eyeDmgOffset, eyeWeaponOffset;
    private Vector2D necSolidOffset, necDmgOffset, necWeaponOffset;

    /**
     * Constructor for the AttributesCreator class.
     * Initializes HITBOXES offsets for each enemy.
     */
    public AttributesCreator() {
        setHitboxOffset();
    }

    /**
     * Sets HITBOXES offsets for each enemy.
     * This is done manually, because each enemy has different
     * size.
     */
    private void setHitboxOffset() {
        skelSolidOffset = new Vector2D(70, 100);
        skelDmgBoxOffset = new Vector2D(0, 45);
        skelWeaponOffset = new Vector2D(60, 60);

        zomSolidOffset = new Vector2D(25, 60);
        zomDmgBoxOffset = new Vector2D(-5, 35);
        zomWeaponOffset = new Vector2D(10, 45);

        eyeSolidOffset = new Vector2D(10, 0);
        eyeDmgOffset = new Vector2D(1, 1);
        eyeWeaponOffset = new Vector2D(10, 10);

        necSolidOffset = new Vector2D(175, 180);
        necDmgOffset = new Vector2D(30, 90);
        necWeaponOffset = new Vector2D(100, 100);
    }

    /**
     * Returns attributes for each enemy.
     * @param enemyType type of enemy
     * @return attributes for the type of enemy
     */
    public EnemyAttributes getAttributes(int enemyType) {
        switch (enemyType) {
            case SKELETON:
                return new EnemyAttributes(SKEL_HEALTH, SKEL_DAMAGE, SKELETON, skelSolidOffset, skelDmgBoxOffset, skelWeaponOffset, SKEL_PIX_SIZE, SKEL_SPEED);
            case ZOMBIE:
                return new EnemyAttributes(ZOM_HEALTH, ZOM_DAMAGE, ZOMBIE, zomSolidOffset, zomDmgBoxOffset, zomWeaponOffset, ZOM_PIX_SIZE, ZOM_SPEED);
            case EYEBALL:
                return new EnemyAttributes(EYE_HEALTH, EYE_DAMAGE, EYEBALL, eyeSolidOffset, eyeDmgOffset, eyeWeaponOffset, EYE_PIX_SIZE, EYE_SPEED);
            case NECROMANCER:
                return  new EnemyAttributes(NECRO_HEALTH, NECRO_DAMAGE, NECROMANCER, necSolidOffset, necDmgOffset, necWeaponOffset, NECRO_PIX_SIZE, NECRO_SPEED);
            default:
                return null;
        }
    }
}
