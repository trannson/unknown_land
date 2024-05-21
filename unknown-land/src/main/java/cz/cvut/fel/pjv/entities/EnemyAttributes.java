package cz.cvut.fel.pjv.entities;

import cz.cvut.fel.pjv.utils.Vector2D;

/**
 * Class for EnemyAttributes
 * This class is used to store the attributes of the enemies.
 * It was created mainly to make the code more readable.
 * @see Enemy
 * 
 * @author Son Ngoc Tran
 */
public class EnemyAttributes {
    
    private int health, damage, enemy_type, pixSize;
    private float speed;
    private Vector2D solidBoxOffset, dmgBoxOffset, weaponOffset;

    /**
     * Constructor for EnemyAttributes
     * @param health enemy health
     * @param damage enemy damage
     * @param enemy_type enemy type
     * @param solidBoxOffset offset for solid hitbox
     * @param dmgBoxOffset offset for damage hitbox
     * @param weaponOffset offset for weapon hitbox
     * @param pixSize pixel size of the enemy
     * @param speed enemy speed
     */
    public EnemyAttributes(int health, int damage, int enemy_type, Vector2D solidBoxOffset, 
    Vector2D dmgBoxOffset, Vector2D weaponOffset, int pixSize, float speed) {
        this.health = health;
        this.damage = damage;
        this.enemy_type = enemy_type;
        this.solidBoxOffset = solidBoxOffset;
        this.dmgBoxOffset = dmgBoxOffset;
        this.weaponOffset = weaponOffset;
        this.pixSize = pixSize;
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public int getEnemyType() {
        return enemy_type;
    }

    public Vector2D getSolidBoxOffset() {
        return solidBoxOffset;
    }

    public Vector2D getDmgBoxOffset() {
        return dmgBoxOffset;
    }

    public Vector2D getWeaponOffset() {
        return weaponOffset;
    }

    public int getPixSize() {
        return pixSize;
    }

    public float getSpeed() {
        return speed;
    }
}
