package cz.cvut.fel.pjv.entities;

import cz.cvut.fel.pjv.utils.LoadFiles;

import cz.cvut.fel.pjv.gamestates.PlayState;

/**
 * Class for creating Skeleton enemy.
 * Skeleton is a type of enemy.
 * It has its own attributes and sprites.
 * @see Enemy
 * 
 * @author Son Ngoc Tran
 */
public class Skeleton extends Enemy {

    /**
     * We are creating a Skeleton enemy.
     * 
     * @param playState      current game state, the Enemy class needs it
     * @param x              x-coordinate of the Skeleton where it will be created
     * @param y              y-coordinate of the Skeleton it will be created
     * @param skelAttributes attributes of the Skeleton
     */
    public Skeleton(PlayState playState, float x, float y, EnemyAttributes skelAttributes) {
        super(playState, x, y, skelAttributes);
        sprites = LoadFiles.LoadSubImages("entity/skeleton.png", 132);
        initHitbox(x, y, 45, 50);
        initDmgHitbox(x, y, 45, 90);
        initWeaponHitbox(x, y, 180, 80);
    }
}
