package cz.cvut.fel.pjv.entities;

import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.utils.LoadFiles;

/**
 * Class representing the EyeBall enemy.
 * EyeBall is a subclass of the Enemy class.
 * It's using Enemy class methods.
 * @see Enemy
 * 
 * @author Son Ngoc Tran
 */
public class EyeBall extends Enemy {

    /**
     * Constructor of the EyeBall class.
     * 
     * @param playState     current state of the game
     * @param x             x-coordinate at which the EyeBall is created
     * @param y             y-coordinate at which the EyeBall is created
     * @param eyeAttributes attributes of the EyeBall
     */
    public EyeBall(PlayState playState, float x, float y, EnemyAttributes eyeAttributes) {
        super(playState, x, y, eyeAttributes);
        sprites = LoadFiles.LoadSubImages("entity/Eyeball.png", 32);

        initHitbox(x, y, 30, 30);
        initDmgHitbox(x, y, 38, 40);
        initWeaponHitbox(x, y, 60, 60);
    }
}
