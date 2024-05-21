package cz.cvut.fel.pjv.entities;

import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.utils.LoadFiles;
import cz.cvut.fel.pjv.utils.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static cz.cvut.fel.pjv.utils.Constants.Enemy.NECROMANCER;
import static cz.cvut.fel.pjv.utils.Constants.Enemy.NECROMANCER_SPELL;
import static cz.cvut.fel.pjv.utils.Constants.Enemy.NECRO_SPELL_APPEAR;
import static cz.cvut.fel.pjv.utils.Constants.Enemy.NECRO_SPELL_DISAPPEAR;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;
import static cz.cvut.fel.pjv.utils.LoadFiles.LoadFont;
import static cz.cvut.fel.pjv.utils.Constants.Enemy.GetAniAmount;

import java.util.Random;

/**
 * Necromancer is a type of enemy that can cast a spell.
 * The spell is a hitbox that deals damage to the player if he stays in it for too long.
 * The necromancer has a health bar displayed above him.
 * Also the necromancer has 2 types of sprites - left and right to 
 * make the necromancer centered when changing direction.
 * @see Enemy 
 * 
 * @author Son Ngoc Tran
 */
public class Necromancer extends Enemy {

    private int rightOffset, aniSpellIndex, aniSpellTick, aniSpellSpeed;
    private int spellDMGthreshold, spellDMGcounter, spellDMG;
    private int spellTimer, spellThreshold, prevEnemyAction;
    private boolean spellAppeared, spellAlreadyDrawn, savePos;
    private double playerX, playerY;
    private Image[][] spritesRight, spritesLeft, spellSprite;
    private Random random;

    private Vector2D necWeaponOffset;
    private Rectangle spellHitbox;
    private PlayState playState;
    private Rectangle healthBar, healthBarOutline;
    private Player player;
    private Font font;

    /**
     * Create a necromancer.
     * @param playState current game state, used to get the player and camera position
     * @param x X coordinate where the necromancer will be placed
     * @param y Y coordinate where the necromancer will be placed
     * @param necAttributes attributes of the necromancer - health, speed, damage, etc.
     */
    public Necromancer(PlayState playState, float x, float y, EnemyAttributes necAttributes) {
        super(playState, x, y, necAttributes);
        this.playState = playState;
        this.necWeaponOffset = necAttributes.getWeaponOffset();
        this.player = playState.getPlayer();

        sprites = LoadFiles.LoadSubImages("entity/necromancer.png", 128);
        spritesRight = LoadFiles.LoadSubImages("entity/necromancer_right.png", 128);
        spritesLeft = LoadFiles.LoadSubImages("entity/necromancer.png", 128);
        spellSprite = LoadFiles.LoadSubImages("entity/necrospell.png", 128);

        font = LoadFont("transamericabold.ttf", 70);
        healthBar = new Rectangle(super.health, 40, Color.PURPLE);
        healthBarOutline = new Rectangle(super.health, 40, Color.BLACK);
        random = new Random();

        initHitboxes();
        initVariables();
    }

    private void initVariables() {
        rightOffset = 100;
        spellTimer = 0;
        spellThreshold = 400;
        aniSpellIndex = 0;
        aniSpellTick = 0;
        aniSpellSpeed = 7;
        prevEnemyAction = 0;
        spellDMG = 20;
        spellDMGthreshold = 35;
        spellDMGcounter = 0;

        spellAppeared = false;
        spellAlreadyDrawn = false;
        savePos = false;

        // setting the attack threshold to 120 so the necromancer wouldn't attack too often
        // otherwise he would be too strong
        super.aniAttackThreshold = 120;
    }

    private void initHitboxes() {
        super.initHitbox(x, y, 45, 50);
        super.initDmgHitbox(x, y, 110, 140);
        super.initWeaponHitbox(x, y, 130, 150);
        spellHitbox = new Rectangle(x, y, 40, 70);
    }

    /**
     * Drawing spell animation if available.
     * Drawing health bar when necromancer is alive.
     * Drawing necromancer sprite based on its direction
     * and setting the offset to center the sprite.
     * 
     * @param gc used to draw on the canvas
     */
    public void renderNecro(GraphicsContext gc) {
        drawSpell(gc);
        displayHealthBar(gc);
        displayName(gc);

        // we need offset cause the sprite is not centered
        if (sprites == spritesLeft) {
            super.render(gc, 0);
        } else if (sprites == spritesRight) {
            super.render(gc, rightOffset);
        }

    }

    /**
     * Updating necromancer's health bar.
     * Updating necromancer's weapon hitbox.
     * Checking necromancer's direction.
     * Checking if spell can be cast.
     * Drawing spell after Necromancer finishes casting.
     * Checking spell collision. - if player is hit by the spell
     */
    public void updateNecro() {
        super.update();
        updateHealthBar();
        updateWeaponHitbox();
        checkNecroDir();
        checkCastSpell();
        castSpell();
        checkSpellCollision();
    }

    /**
     * Set the necromancer's sprite based on the necromancer's direction.
     */
    private void checkNecroDir() {
        Vector2D necroPos = super.getPosOffset();
        if (necroPos.getX() < 0) {
            sprites = spritesLeft;
        } else if (necroPos.getX() > 0) {
            sprites = spritesRight;
        }
    }

    /**
     * Checking if the necromancer can cast a spell. - spell threshold is reached.
     * If the spell can be cast, we reset the animation and set the spell action.
     * We set moving and attacking to false, so the necromancer animation is
     * not interrupted by other actions.
     * Also we randomize the spell threshold for the next casting.
     */
    private void checkCastSpell() {
        spellTimer++;
        if (spellTimer >= spellThreshold) {
            resetAni();
            spellTimer = 0;
            super.enemyAction = NECROMANCER_SPELL;
            prevEnemyAction = super.enemyAction;

            spellAlreadyDrawn = false;
            super.attack = false;
            super.moving = false;

            spellThreshold = random.nextInt(400) + 300;
        }
    }

    /**
     * Changing weapon hitbox position based on the necromancer's direction.
     */
    private void updateWeaponHitbox() {
        if (sprites == spritesRight) {
            super.weaponHitbox.setX(x + necWeaponOffset.getX() - 80);
            super.weaponHitbox.setY(y - necWeaponOffset.getY());
        } else if (sprites == spritesLeft) {
            super.weaponHitbox.setX(x - necWeaponOffset.getX());
            super.weaponHitbox.setY(y - necWeaponOffset.getY());
        }
    }

    /**
     * Checks if the necromancer is done casting the spell.
     * If yes - we start the spell animation.
     * Spell appears and set spellAppeared to true.
     * After the spell disappears, we set spellAppeared to false to
     * draw the spell disappearing.
     * At the end we set the spellAlreadyDrawn to true so we wouldn't draw
     * it multiple times. We also reset the spell position and its
     * booleans - savePos, spellAppeared.
     * We reset spellDMGcounter to 0 because we increase it when the player
     * is in the spell hitbox.
     */
    private void castSpell() {
        // after necromancer casting ends, we begin the spell animation
        if (prevEnemyAction == NECROMANCER_SPELL && super.enemyAction != NECROMANCER_SPELL) {
            aniSpellTick++;
            if (aniSpellTick >= aniSpellSpeed && !spellAlreadyDrawn) {
                aniSpellTick = 0;
                aniSpellIndex++;

                savePlayerPos(); // saving the player position for the spell animation
                // drawing the spell appearing
                if (aniSpellIndex >= GetAniAmount(NECRO_SPELL_APPEAR, NECROMANCER) && !spellAppeared) {
                    aniSpellIndex = 0;
                    spellAppeared = true;
                }
                // drawing the spell disappearing
                if (aniSpellIndex >= GetAniAmount(NECRO_SPELL_DISAPPEAR, NECROMANCER) && spellAppeared) {
                    aniSpellIndex = 0;
                    spellAppeared = false;
                    spellAlreadyDrawn = true; // after the spell has disappeared, we set the boolean to true so we
                                              // wouldn't draw it multiple times
                    savePos = false;
                    spellHitbox.setX(0);
                    spellHitbox.setY(0);
                    spellDMGcounter = 0;
                }

            }
        } else {
            /*
             * if the necromancer is not casting a spell, we reset the spell animation +
             * position
             */
            aniSpellIndex = 0;
            aniSpellTick = 0;
            playerX = 0;
            playerY = 0;
            spellHitbox.setX(0);
            spellHitbox.setY(0);

        }
    }

    /**
     * Drawing the spell animation if the necromancer is done casting a spell.
     * If spell hasn't appeared yet, we draw the spell appearing.
     * If the spell has appeared, we draw the spell disappearing.
     */
    private void drawSpell(GraphicsContext gc) {
        if (prevEnemyAction == NECROMANCER_SPELL && super.enemyAction != NECROMANCER_SPELL && !spellAlreadyDrawn) {
            // checking boolean spellAppeared to determine which spell animation to draw
            if (!spellAppeared) { // draw the spell appearing
                gc.drawImage(spellSprite[NECRO_SPELL_APPEAR][aniSpellIndex], playerX, playerY, pixSize, pixSize);
            } else { // draw the spell disappearing
                gc.drawImage(spellSprite[NECRO_SPELL_DISAPPEAR][aniSpellIndex], playerX, playerY, pixSize, pixSize);
            }
        }
    }

    /**
     * We set the spell hitbox position to the player's position.
     * We do this only once, so the spell hitbox wouldn't follow the player
     * making it possible to dodge.
     */
    private void savePlayerPos() {
        if (!savePos) {
            spellHitbox.setX(player.getX() - 5);
            spellHitbox.setY(player.getY() - 40);
            playerX = player.getX() - 85;
            playerY = player.getY() - 150;

            savePos = true;
        }
    }

    /**
     * Checking if the player is in the spell hitbox.
     * If yes - we increase the spellDMGcounter.
     * If the counter reaches the threshold, we decrease the player's health.
     * In other words if player stays long enough in the spell hitbox, he gets hit.
     */
    private void checkSpellCollision() {
        if (player.dmgHitbox.getBoundsInParent().intersects(spellHitbox.getBoundsInParent())) {
            spellDMGcounter++;
            if (spellDMGcounter >= spellDMGthreshold) {
                player.health -= spellDMG;
                spellDMGcounter = 0;
            }
        }
    }

    /**
     * Displaying in the top center of the screen.
     * Drawing the health bar and its outline.
     * 
     * @param gc used to draw on the canvas
     */
    private void displayHealthBar(GraphicsContext gc) {
        gc.setFill(healthBar.getFill());
        gc.fillRect(healthBar.getX(), healthBar.getY(), healthBar.getWidth(), healthBar.getHeight());
        gc.setStroke(healthBarOutline.getFill());
        gc.setLineWidth(3);
        gc.strokeRect(healthBarOutline.getX(), healthBarOutline.getY(), healthBarOutline.getWidth(),
                healthBarOutline.getHeight());
    }

    /**
     * Displaying the necromancer's name in the top center of the screen.
     * It's displayed above the health bar.
     * @param gc
     */
    private void displayName(GraphicsContext gc) {
        double x = this.playState.getCamera().getX() + WINDOW_WIDTH / 2 - 175;
        double y = this.playState.getCamera().getY() + 50;
        String text = "Necromancer";
        gc.setFont(Font.font(font.getFamily(), FontWeight.BOLD, 40));

        // Draw the outline
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(6);
        gc.strokeText(text, x, y);
    
        // Draw the main text
        gc.setFill(Color.GAINSBORO);
        gc.fillText(text, x, y);
    }

    /**
     * Updating the health bar position based on the camera position.
     */
    private void updateHealthBar() {
        double x = this.playState.getCamera().getX() + WINDOW_WIDTH / 2 - 150;
        double y = this.playState.getCamera().getY() + 60;

        healthBar.setWidth(super.health);
        healthBar.setX(x);
        healthBar.setY(y);
        healthBarOutline.setX(x);
        healthBarOutline.setY(y);

    }

    protected boolean checkDeath() {
        return super.getHealth() <= 0;
    }

    public void setEnemyPos(float x, float y) {
        super.setEnemyPos(x, y);
    }
}
