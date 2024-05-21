package cz.cvut.fel.pjv.entities;

import cz.cvut.fel.pjv.utils.PlayerDirection;
import cz.cvut.fel.pjv.utils.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import static cz.cvut.fel.pjv.utils.Constants.PlayerConstants.*;
import static cz.cvut.fel.pjv.utils.Constants.HitboxConstants.*;

import cz.cvut.fel.pjv.gamestates.PlayState;

/**
 * Class representing the player entity.
 * Player is a subclass of the Entity class.
 * It's using Entity class methods.
 * Player is moving and attacking.
 * Player can move in four directions and attack in four directions.
 * Player's weapon hitboxes change based on the direction of the attack.
 * Player's sprite changes based on the color of the sword.
 * @see Entity
 * 
 * @author Son Ngoc Tran
 */
public class Player extends Entity {

    private PlayerDirection playerDirection;
    private Vector2D positionOffset;
    private PlayState playState;
    private Image sprites[][];
    private float playerSpeed, tempX, tempY;
    private int playerAction, dmgBoxOffsetY;
    private int aniTimer, spriteIndex, aniSpeed, attackSpeed, movingSpeed;
    private int swordColor;
    private int currentHealth, prevHealth;
    private int attackTimer, attackCooldown;
    private boolean hit, teleported;
    protected boolean attack; // used by the HitDetector class

    /**
     * Constructor for the Player class.
     * 
     * @param x         initial x-coordinate
     * @param y         initial y-coordinate
     * @param playState used to access collision
     */
    public Player(float x, float y, PlayState playState) {
        super(x, y, PLAYER_MAX_HEALTH, 0);
        this.playState = playState;
        collision = playState.getCollision();

        sprites = playState.getLoadedFiles().getPlayerImage(0);

        initVariables();

        initHitbox(x, y, PLAYER_HITBOX_WIDTH, PLAYER_HITBOX_HEIGHT);
        initDmgHitbox(x, y, 40, 60);
        initWeaponHitbox(x, y, 50, 50);

    }

    private void initVariables() {
        playerSpeed = 4f;
        aniTimer = 0;
        spriteIndex = 0;
        aniSpeed = 0;
        swordColor = 0;
        movingSpeed = 8;
        attackSpeed = 4;
        prevHealth = 120;
        tempX = x;
        tempY = y;
        attackTimer = 0;
        attackCooldown = 40;
        dmgBoxOffsetY = 30;

        playerDirection = PlayerDirection.DOWN;
        positionOffset = new Vector2D(0, 0);

        moving = false;
        attack = false;
        hit = false;
        teleported = false;
    }

    /**
     * Render the player.
     * Player sprite is drawn into the hitboxes rectangle.
     * 
     * @param gc
     */
    public void render(GraphicsContext gc) {
        if (spriteIndex < sprites[playerAction].length) {
            gc.drawImage(sprites[playerAction][spriteIndex],
                    hitbox.getX() - PLAYER_HITBOX_OFFSET_X, hitbox.getY() - PLAYER_HITBOX_OFFSET_Y, PLAYER_WIDTH,
                    PLAYER_HEIGHT);
        }
    }

    /**
     * Update the player.
     * Update the player's position, hitboxes, animation, health, and direction.
     */
    public void update() {
        updateMovingPosition();
        updateWeaponHitbox();
        updateTick();
        setAni();

        checkHealth();

    }

    /**
     * Update the player's animation.
     * Animation speed is based on the player's action.
     * Attack speed is faster than moving speed.
     * If player is doing an action, other actions are set
     * to false to prevent multiple actions at once.
     */
    private void updateTick() {
        aniSpeed = attack ? attackSpeed : movingSpeed;
        aniTimer++;
        attackTimer++;
        if (aniTimer >= aniSpeed) {
            aniTimer = 0;
            spriteIndex++;
            // HITTING
            if (hit) {
                // avoid out of bounds exception
                if (spriteIndex >= 1) {
                    spriteIndex = 0;
                    hit = false;
                }
                attack = false;
                moving = false;
            }
            // ATTACKING
            else if (attack) {
                if (spriteIndex >= GetAttackAmount(playerAction)) {
                    spriteIndex = 0;
                    attack = false;
                }
                hit = false;
                moving = false;
            }
            // MOVING
            else {
                if (spriteIndex >= GetAniAmount(playerAction)) {
                    spriteIndex = 0;
                }
                attack = false;
                hit = false;
            }
        }
    }

    /**
     * Update the player's position - movement.
     * If the player is moving, update the position based on the direction.
     * If the player is teleported, don't set the position to the previous one.
     * We are using tempX and tempY to store the previous position to prevent
     * the player from being moved by other entities.
     */
    private void updateMovingPosition() {
        if (moving || teleported) {
            x += positionOffset.getX() * playerSpeed;
            y += positionOffset.getY() * playerSpeed;

            // if collision is detected, move the player back
            if (!collision.isMoveValid(x, y, hitbox.getWidth(), hitbox.getHeight())) {
                x -= positionOffset.getX() * playerSpeed;
                y -= positionOffset.getY() * playerSpeed;
            }

            tempX = x;
            tempY = y;
            if (teleported) {
                teleported = false;
            }
        } else {
            if (!teleported) {
                x = tempX;
                y = tempY;
            }
        }
        hitbox.setX(x);
        hitbox.setY(y);
        dmgHitbox.setX(x);
        dmgHitbox.setY(y - dmgBoxOffsetY); // offset to fit on the player sprite
    }

    /**
     * Set the player's animation based on the player's action.
     * If the previous action is different from the current action, reset the
     * animation.
     */
    private void setAni() {
        int tempAni = playerAction;

        playerAction = choosePlayerAction(); // depending on the key pressed, the player action is set

        if (tempAni != playerAction) { // if the player action has changed
            resetAni(); // reset the animation so it starts from the beginning
        }

    }

    private void resetAni() {
        aniTimer = 0;
        spriteIndex = 0;
    }

    /**
     * Choose the player's action based on the key pressed or
     * if the player is hit.
     * We need to change the sprites based on the player's action
     * because not all of the sprites are in the same order.
     * 
     * @return the player's action
     */
    private int choosePlayerAction() {
        if (hit) {
            attack = false;
            moving = false;
            // swap the sprites to the hit sprites
            sprites = playState.getLoadedFiles().getHitPlayer();
            switch (playerDirection) {
                case UP:
                    return HIT_UP;
                case DOWN:
                    return HIT_DOWN;
                case LEFT:
                    return HIT_LEFT;
                case RIGHT:
                    return HIT_RIGHT;
            }
        } else if (attack) {
            hit = false;
            moving = false;
            // swap the sprites to the attack sprites depending on the sword color
            sprites = playState.getLoadedFiles().getPlayerImage(swordColor);
            switch (playerDirection) {
                case UP:
                    return ATTACK_UP;
                case DOWN:
                    return ATTACK_DOWN;
                case LEFT:
                    return ATTACK_LEFT;
                case RIGHT:
                    return ATTACK_RIGHT;
            }
        } else {
            hit = false;
            attack = false;
            // swap the sprites to the player moving sprites
            sprites = playState.getLoadedFiles().getPlayerImage(0);
            if (moving) {
                return playerDirection.getRunning(); // get the amount of sprites for the animation
            }
        }
        // if the player doesn't have any action, return the idle sprite
        return playerDirection.getIdle();
    }

    /**
     * Update the weapon hitbox based on the player's direction.
     * The hitbox is updated only when the player is attacking.
     * The hitbox is set to the player's position and size based on the direction.
     * We need to add offset to the weapon hitbox cause we want to
     * hit the enemy in 4 directions.
     */
    private void updateWeaponHitbox() {
        if (attack) {
            switch (playerDirection) {
                case UP:
                    weaponHitbox.setWidth(80);
                    weaponHitbox.setHeight(50);
                    weaponHitbox.setX(x - 30);
                    weaponHitbox.setY(y - 40);
                    break;
                case DOWN:
                    weaponHitbox.setWidth(60);
                    weaponHitbox.setHeight(40);
                    weaponHitbox.setX(x - 15);
                    weaponHitbox.setY(y + 10);
                    break;
                case LEFT:
                    weaponHitbox.setWidth(70);
                    weaponHitbox.setHeight(50);
                    weaponHitbox.setX(x - 30);
                    weaponHitbox.setY(y - 30);
                    break;
                case RIGHT:
                    weaponHitbox.setWidth(70);
                    weaponHitbox.setHeight(50);
                    weaponHitbox.setX(x - 10);
                    weaponHitbox.setY(y - 30);
                    break;
            }
        }
    }

    /**
     * Check the player's health.
     * If the player's health is lower than the previous health, the player was hit.
     * That means we need to change animation to the hit animation.
     */
    private void checkHealth() {
        currentHealth = getHealth();
        if (currentHealth < prevHealth) {
            hit = true;
            prevHealth = currentHealth;
        }
    }

    /**
     * Set the player's direction based on the key pressed.
     * It's used in playState to set the direction of the player
     * based on the key pressed.
     * 
     * @param direction the direction of key pressed
     */
    public void setDirection(PlayerDirection direction) {
        // if the player is attacking, don't move
        if (attack) {
            positionOffset.setVector(0, 0);
            return;
        }

        this.playerDirection = direction;
        moving = true;
        switch (direction) {
            case UP:
                positionOffset.setVector(0, -1);
                break;
            case DOWN:
                positionOffset.setVector(0, 1);
                break;
            case LEFT:
                positionOffset.setVector(-1, 0);
                break;
            case RIGHT:
                positionOffset.setVector(1, 0);
                break;
        }

    }

    public void setHealth(int health) {
        if (health <= PLAYER_MAX_HEALTH && health >= 0) {
            this.health = health;
        }
    }

    public void resetPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setAttack(boolean attack) {
        if (swordColor != 0 && attackTimer >= attackCooldown) {
            this.attack = attack;
            attackTimer = 0;
        }
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public Vector2D getPositionOffset() {
        return positionOffset;
    }

    public float getSpeed() {
        return playerSpeed;
    }

    public int getHealth() {
        return health;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public Rectangle getWeaponHitbox() {
        return weaponHitbox;
    }

    public Rectangle getDmgHitbox() {
        return dmgHitbox;
    }

    public void setSwordColor(int color) {
        swordColor = color;
        switch (color) {
            case 1:
                damage = 15;
                break;
            case 2:
                damage = 25;
                break;
            case 3:
                damage = 40;
                break;
            default:
                damage = 0;
        }
    }

    public void setTeleported(boolean teleported) {
        this.teleported = teleported;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public int getDamage() {
        return damage;
    }

}
