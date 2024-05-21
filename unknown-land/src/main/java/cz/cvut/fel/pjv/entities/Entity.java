package cz.cvut.fel.pjv.entities;

import java.util.logging.Logger;

import cz.cvut.fel.pjv.utils.Collision;
import javafx.scene.shape.Rectangle;

/**
 * Abstract class for all entities in the game.
 * Not meant to be instantiated.
 * Contains methods for creating hitboxes and drawing them.
 * Keep track of the entity's health and damage and coordinates.
 * It's a parent class for Player and Enemy.
 * 
 * @author Son Ngoc Tran
 */
public abstract class Entity {

    protected boolean moving;
    protected Rectangle entityRect, hitbox, dmgHitbox;
    protected Rectangle weaponHitbox;
    protected Collision collision;
    protected float x, y;
    protected int health, damage;
    protected static final Logger log = Logger.getLogger(Entity.class.getName());

    /**
     * Constructor for the Entity class.
     * 
     * @param x      x coordinate of the entity
     * @param y      y coordinate of the entity
     * @param health health of the entity
     * @param damage damage of the entity
     */
    public Entity(float x, float y, int health, int damage) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;

    }

    // Creating a hitbox for the entity
    public void initHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle(x, y, width, height);
    }

    public void initDmgHitbox(float x, float y, float width, float height) {
        dmgHitbox = new Rectangle(x, y, width, height);
    }

    public void initWeaponHitbox(float x, float y, float width, float height) {
        weaponHitbox = new Rectangle(x, y, width, height);
    }

}
