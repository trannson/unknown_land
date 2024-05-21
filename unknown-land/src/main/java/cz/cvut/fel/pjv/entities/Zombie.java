package cz.cvut.fel.pjv.entities;

import cz.cvut.fel.pjv.gamestates.PlayState;

import static cz.cvut.fel.pjv.utils.LoadFiles.LoadSubImages;

/**
 * Class for creating Zombie enemy.
 * Zombie is a type of enemy.
 * It has its own attributes and sprites.
 * @see Enemy
 * 
 * @author Son Ngoc Tran
 */
public class Zombie extends Enemy {

    /**
     * We are creating a Zombie enemy.
     * 
     * @param playState     current game state, the Enemy class needs it
     * @param x             x-coordinate of the Zombie where it will be created
     * @param y             y-coordinate of the Zombie where it will be created
     * @param zomAttributes attributes of the Zombie
     */
    public Zombie(PlayState playState, float x, float y, EnemyAttributes zomAttributes) {
        super(playState, x, y, zomAttributes);
        sprites = LoadSubImages("entity/zombie.png", 64);
        initHitbox(x, y, 40, 40);
        initDmgHitbox(x, y, 35, 70);
        initWeaponHitbox(x, y, 70, 65);
    }

}
