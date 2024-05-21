package cz.cvut.fel.pjv.entities;

import java.util.Random;

import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.utils.Collision;
import cz.cvut.fel.pjv.utils.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import static cz.cvut.fel.pjv.utils.Constants.Enemy.*;

/**
 * Abstract class Enemy. Not meant to be instantiated.
 * Represents an enemy entity in the game.
 * It's extending the Entity class.
 * It's extended by the child classes: Skeleton, Zombie, EyeBall, Necromancer.
 * This class keep track of the enemy's health, damage, position, direction,
 * speed, and animations.
 * It also manages the enemy's movement - random or towards the player.
 * @see Entity
 * 
 * @author Son Ngoc Tran
 */
public abstract class Enemy extends Entity {

    private int aniTick, aniSpeed, aniIndex, enemyDirection, updateCounter, updateThreshold;
    private Random random;
    private Collision collision;
    private Player player;
    private float diffX, diffY, fastest_path, visibleRadius;
    private HitDetector hitDetector;
    private int prevHealth, aniAttackCounter;
    private boolean hit;
    private Vector2D solidOffset;

    // Variables used by the child classes or hitDetector
    protected int enemy_type, health, attackDelayCounter, getHitCounter;
    protected Vector2D dmgBoxOffset, weaponOffset;
    protected int pixSize, aniAttackThreshold, enemyAction;
    protected Image sprites[][];
    protected Vector2D positionOffset;
    protected boolean moving, attack, playerVisible;
    protected float enemySpeed, tempX, tempY;

    /**
     * Constructs a new Enemy entity.
     * 
     * @param playState       Current game state
     * @param x               X coordinate where the enemy will be placed
     * @param y               Y coordinate where the enemy will be placed
     * @param enemyAttributes Attributes of the enemy - health, damage, speed, etc.
     */
    public Enemy(PlayState playState, float x, float y, EnemyAttributes enemyAttributes) {
        super(x, y, enemyAttributes.getHealth(), enemyAttributes.getDamage());
        hitDetector = new HitDetector(playState);
        collision = playState.getCollision();
        player = playState.getPlayer();
        random = new Random();

        extractAttributes(enemyAttributes);

        initVariables();
    }

    /**
     * Renders the enemy entity at solid hitbox coordinate to fit into the hitbox
     * rectangle.
     * 
     * @param gc            GraphicsContext object used for rendering
     * @param invertOffsetX Offset used for rendering the enemy in the correct
     *                      position
     *                      due to the sprite not being centered
     */
    public void render(GraphicsContext gc, int invertOffsetX) {
        if (aniIndex < sprites[enemyAction].length) {
            gc.drawImage(sprites[enemyAction][aniIndex],
                    hitbox.getX() - solidOffset.getX() + invertOffsetX, hitbox.getY() - solidOffset.getY(),
                    pixSize * 1.5, pixSize * 1.5);
        }

    }

    /**
     * Updates the enemy entity.
     * Checks if the enemy is hit, sets the direction, updates the position,
     * checks the attack delay, updates the tick, and sets the animation.
     * Also checks for player collision and hit.
     * It manages enemy tick + enemy index - the variables for render.
     */
    public void update() {
        checkHit();
        setDirection();
        updateMovingPosition();
        checkAttackDelay();
        updateTick();
        setAni();

        hitDetector.checkPlayerCollision(this);
        hitDetector.checkGetHit(this);

    }

    private void extractAttributes(EnemyAttributes enemyAttributes) {
        solidOffset = enemyAttributes.getSolidBoxOffset();
        dmgBoxOffset = enemyAttributes.getDmgBoxOffset();
        weaponOffset = enemyAttributes.getWeaponOffset();
        pixSize = enemyAttributes.getPixSize();
        enemySpeed = enemyAttributes.getSpeed();
        enemy_type = enemyAttributes.getEnemyType();
        health = enemyAttributes.getHealth();
    }

    private void initVariables() {
        prevHealth = health;
        positionOffset = new Vector2D(0, 0);
        aniTick = 0;
        aniIndex = 0;
        aniSpeed = 7;
        attackDelayCounter = 0;
        getHitCounter = 0;
        moving = true;
        attack = false;
        hit = false;
        visibleRadius = 300;
        updateCounter = 0;
        updateThreshold = 100;
        aniAttackThreshold = 30;
        aniAttackCounter = aniAttackThreshold;
        tempX = x;
        tempY = y;
    }

    /**
     * Updates the enemy's tick - the speed between each animation frame.
     * If the enemy is attacking, it checks the middle of the attack animation
     * to check for player hit - it usually is the main attack frame.
     * If enemy is being hit or attacking, it checks for the end of the animation
     * to prevent empty frames in sprite animation.
     */
    private void updateTick() {
        aniTick++;
        attackDelayCounter++;
        getHitCounter++;

        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            checkAttackSprite();
            checkAnimationSprite();
        }
    }

    /**
     * Checking the middle of the attack animation - the main attack frame.
     * That's the frame that player can be hit.
     */
    private void checkAttackSprite() {
        // if the enemy is attacking, check the middle of the sprite frame
        // stop moving and hitting to prevent multiple animations
        if (attack && aniIndex + 1 >= GetAniAmount(enemyAction, enemy_type) / 2
                && aniIndex < GetAniAmount(enemyAction, enemy_type) - 2) {
            hitDetector.checkHitPlayer(this);
            hit = false;
            moving = false;
        }
    }

    /**
     * Checks if the enemy animation has reached the end.
     * If yes, we reset the animation and set the attack boolean to false
     * if the enemy was attacking. Hit boolean is also set to false.
     */
    private void checkAnimationSprite() {
        if (aniIndex >= GetAniAmount(enemyAction, enemy_type)) {
            aniIndex = 0;
            if (attack) {
                aniAttackCounter = 0;
                attack = false;
            }
            hit = false;

            // if the necromancer is done with casting a spell, reset the animation
            if (enemyAction == NECROMANCER_SPELL) {
                enemyAction = NECROMANCER_RUNNING;
            }
        }
    }

    /**
     * Updates the enemy's position.
     * If the enemy is moving, it updates the position based on the position
     * offset(direction).
     * If the enemy is not moving, it resets the position to the previous one to
     * prevent being
     * moved into the collision tile.
     * It also updates the hitbox, damage hitbox, and weapon hitbox position.
     * Everything has to
     * be updated to the new position otherwise it would stay at the same play all
     * the time.
     */
    private void updateMovingPosition() {
        if (moving) {
            x += positionOffset.getX() * enemySpeed;
            y += positionOffset.getY() * enemySpeed;

            if (!collision.isMoveValid(x, y, hitbox.getWidth(), hitbox.getHeight())) {
                x -= positionOffset.getX() * enemySpeed;
                y -= positionOffset.getY() * enemySpeed;
            }

            tempX = x;
            tempY = y;
        }
        // ensure player can't get enemy stuck in the collision tile
        else {
            x = tempX;
            y = tempY;
        }
        hitbox.setX(x);
        hitbox.setY(y);
        dmgHitbox.setX(x - dmgBoxOffset.getX());
        dmgHitbox.setY(y - dmgBoxOffset.getY());
        weaponHitbox.setX(x - weaponOffset.getX());
        weaponHitbox.setY(y - weaponOffset.getY());

    }

    /**
     * Sets the direction of the enemy.
     * If the player is in the visible radius, the enemy will move towards the
     * player.
     * Otherwise, the enemy will move randomly every update threshold.
     */
    private void setDirection() {
        updateCounter++;
        playerVisible = isPlayerVisible();

        if (playerVisible) {
            // Normalize the vector to get the direction, to get the direction + value of
            // it: <-1,1>
            float directionX = diffX / fastest_path;
            float directionY = diffY / fastest_path;

            // Set the position offset
            positionOffset.setVector(directionX, directionY);
        } else {
            if (updateCounter >= updateThreshold) {
                moving = true;
                enemyDirection = random.nextInt(50);
                if (enemyDirection < 10) {
                    positionOffset.setVector(1, 0);
                } else if (enemyDirection < 20) {
                    positionOffset.setVector(-1, 0);
                } else if (enemyDirection < 30) {
                    positionOffset.setVector(0, 1);
                } else if (enemyDirection < 40) {
                    positionOffset.setVector(0, -1);
                } else {
                    moving = false;
                }

                updateCounter = 0; // Reset the counter
            }

        }
    }

    /**
     * Sets the enemy action based on the its action.
     * If the enemy action has changed, it resets the animation.
     */
    private void setAni() {
        int tempAni = enemyAction;

        enemyAction = chooseEnemyAction(); // depending on the key pressed, the player action is set

        if (tempAni != enemyAction) { // if the player action has changed
            resetAni(); // reset the animation so it starts from the beginning
        }

    }

    /**
     * Chooses the enemy action based on the enemy type and its state.
     * 
     * @return The enemy action based on the enemy type and its state.
     */
    private int chooseEnemyAction() {
        if (enemy_type == NECROMANCER) {
            if (enemyAction == NECROMANCER_SPELL) {
                hit = false;
                moving = false;
                attack = false;
                return NECROMANCER_SPELL;
            }
        }

        if (hit) {
            switch (enemy_type) {
                case SKELETON:
                    return SKEL_HIT;
                case ZOMBIE:
                    return ZOM_HIT;
                case EYEBALL:
                    return EYE_HIT;
                case NECROMANCER:
                    return NECROMANCER_HIT;
            }
        }

        else if (attack) {
            switch (enemy_type) {
                case SKELETON:
                    return SKEL_ATTACK;
                case ZOMBIE:
                    return ZOM_ATTACK;
                case EYEBALL:
                    return EYE_ATTACK;
                case NECROMANCER:
                    return NECROMANCER_ATTACK;
            }
        } else if (moving) {
            switch (enemy_type) {
                case SKELETON:
                    return SKEL_RUNNING;
                case ZOMBIE:
                    return ZOM_RUNNING;
                case EYEBALL:
                    return EYE_RUNNING;
                case NECROMANCER:
                    return NECROMANCER_RUNNING;
            }
        }

        return 0;
    }

    /**
     * Calculates the distance between the player and the enemy.
     * It also checks if the player is in the visible radius of the enemy.
     * 
     * @return True if the player is in the visible radius of the enemy, false
     *         otherwise.
     */
    private boolean isPlayerVisible() {
        diffX = player.getX() - this.x; // difference between player and enemy x
        diffY = player.getY() - this.y; // difference between player and enemy y
        fastest_path = (float) Math.sqrt(diffX * diffX + diffY * diffY); // Pythagorean theorem

        return fastest_path <= visibleRadius;
    }

    /*
     * Checks if the enemy is hit.
     * If yes sets the hit boolean to true and updates the previous health.
     * This is used to trigger the hit animation.
     */
    private void checkHit() {
        if (prevHealth != health) {
            hit = true;
            prevHealth = health;
        }
    }

    /**
     * Delay between each enemy attack.
     */
    private void checkAttackDelay() {
        aniAttackCounter++;
        if (aniAttackCounter < aniAttackThreshold) {
            attack = false;
        }
    }

    protected void resetAni() {
        aniTick = 0;
        aniIndex = 0;
    }

    protected void setSprite(Image[][] sprites) {
        this.sprites = sprites;
    }


    protected void decreasePlayerHealth(Player player) {
        player.health -= damage;
    }

    public int getHealth() {
        return health;
    }

    public Vector2D getPosOffset() {
        return positionOffset;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getType() {
        return enemy_type;
    }

    protected void setEnemyPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
