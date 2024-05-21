package cz.cvut.fel.pjv.entities;

import static cz.cvut.fel.pjv.utils.Constants.Enemy.NECROMANCER_SPELL;

import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.utils.Vector2D;

/**
 * Class representing the hit detection in the game.
 * HitDetector is used to detect hits between player and enemies.
 * 
 * @author Son Ngoc Tran
 */
public class HitDetector {

    private PlayState playState;
    private Player player;
    private int collisionCounter, collisionThreshold;
    private float getUnstuck;
    private int attackDelayThreshold, getHitThreshold;
    private Logger log;

    /**
     * Constructor for the HitDetector class.
     * Initialize all variables.
     * 
     * @param playState current state of the game
     */
    public HitDetector(PlayState playState) {
        this.playState = playState;
        collisionThreshold = 100;
        collisionCounter = collisionThreshold;

        initVariables();

        log = Logger.getLogger(HitDetector.class.getName());
        log.setLevel(Level.INFO);
    }

    private void initVariables() {
        // attackDelayCounter is incremented in the Enemy class
        // getHitCounter is incremented in the Enemy class
        attackDelayThreshold = 70;
        getHitThreshold = 35;
        getUnstuck = 1.5f;
    }

    /**
     * Check if the player was hit by the enemy.
     * If the player was hit, decrease the player's health.
     * Player is hit only if the delay between attacks is
     * greater than the threshold meaning the player can't be hit every frame
     * of the enemy attack animation.
     * 
     * @param enemy enemy that is attacking the player
     */
    protected void checkHitPlayer(Enemy enemy) {
        enemy.attackDelayCounter++;
        player = playState.getPlayer();
        if ((enemy.weaponHitbox.getBoundsInParent().intersects(player.dmgHitbox.getBoundsInParent()))
                && enemy.enemyAction != NECROMANCER_SPELL) {
            if (enemy.attackDelayCounter >= attackDelayThreshold) {
                enemy.decreasePlayerHealth(player);
                enemy.attackDelayCounter = 0;
                log.info("Player was hit, health: " + player.health);
            }
        }

    }

    /**
     * Check if the player collided with the enemy.
     * If the player dmg hitbox collides with the enemy weapon hitbox, the enemy
     * attacks and stops moving.
     * If the player solid hitbox collided with the enemy solid hitbox, the player
     * is pushed back and
     * the enemy is pushed back as well to prevent them from overlapping.
     * If the collision counter is greater than the threshold, the enemy can move
     * again.
     * This is to prevent the enemy moving the player when they collide.
     * 
     * @param enemy
     */
    protected void checkPlayerCollision(Enemy enemy) {
        collisionCounter++;
        player = playState.getPlayer();
        Vector2D playerPosOffset = playState.getPlayer().getPositionOffset();
        float playerNewX = (float) (player.getX() - player.getSpeed() * playerPosOffset.getX());
        float playerNewY = (float) (player.getY() - player.getSpeed() * playerPosOffset.getY());

        if (enemy.weaponHitbox.getBoundsInParent().intersects(player.dmgHitbox.getBoundsInParent())) {
            enemy.attack = true;
            enemy.moving = false;
        }

        if (enemy.hitbox.getBoundsInParent().intersects(player.hitbox.getBoundsInParent())) {
            enemy.x -= enemy.positionOffset.getX() * enemy.enemySpeed * getUnstuck; // * getUnstuck to get the enemy
                                                                                    // unstuck in case of collision
            enemy.y -= enemy.positionOffset.getY() * enemy.enemySpeed * getUnstuck;
            player.resetPosition(playerNewX, playerNewY);
        }

        if (collisionCounter >= collisionThreshold) {
            enemy.moving = true;
            collisionCounter = 0;
        }

    }

    /**
     * Check if the enemy was hit by the player.
     * If the enemy was hit, decrease the enemy's health.
     * Enemy is hit only if the delay between attacks is
     * greater than the threshold meaning the enemy can't be hit every frame.
     * 
     * @param enemy
     */
    protected void checkGetHit(Enemy enemy) {
        enemy.getHitCounter++;

        player = playState.getPlayer();
        if (enemy.dmgHitbox.getBoundsInParent().intersects(player.weaponHitbox.getBoundsInParent())) {
            if (enemy.getHitCounter >= getHitThreshold && player.attack) {
                enemy.health -= player.damage;
                enemy.getHitCounter = 0;
                log.info("Enemy was hit, health: " + enemy.health);
            }
        }
    }

}
