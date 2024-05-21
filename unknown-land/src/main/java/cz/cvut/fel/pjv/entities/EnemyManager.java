package cz.cvut.fel.pjv.entities;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.TILE_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.Enemy.*;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.TILES_IN_WIDTH;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.time.GameTime;
import cz.cvut.fel.pjv.utils.AttributesCreator;
import cz.cvut.fel.pjv.utils.Collision;
import cz.cvut.fel.pjv.utils.Vector2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * Manages the enemies in the game.
 * Spawns enemies based on the time of day and the current hour.
 * Updates the enemies - their movement, health, and death.
 * Renders the enemies - animations.
 * @see Enemy
 * 
 * @author Son Ngoc Tran
 */
public class EnemyManager {

    private PlayState playState;
    private GameTime gameTime;
    private int enemyCount, enemyAmountMax, spawnHour, maxSpawnHour, minSpawnHour;
    private int skelRate, zomRate, eyeRate;
    private Random random;
    private ArrayList<Enemy> enemyList, enemyToRemove;
    private Set<Vector2D> usedCoords;
    private Collision collision;
    private String dayCycle;
    private Necromancer necromancer;
    private AttributesCreator attributesCreator;
    private Logger log;

    /**
     * Constructor for the EnemyManager class.
     * 
     * @param playState current state of the game, used to access collision
     * @param gameTime  current time of the game, used to determine when to spawn
     *                  enemies
     *                  and increases spawn rate based on the time of day.
     */
    public EnemyManager(PlayState playState, GameTime gameTime) {
        this.playState = playState;
        this.gameTime = gameTime;

        attributesCreator = new AttributesCreator();

        random = new Random();
        enemyList = new ArrayList<>();
        enemyToRemove = new ArrayList<>();
        usedCoords = new HashSet<>();
        collision = playState.getCollision();
        setSpawningInterval();
        setSpawnRates();

        necromancer = new Necromancer(playState, 20 * TILE_SIZE, 26 * TILE_SIZE,
        attributesCreator.getAttributes(NECROMANCER));
        log = Logger.getLogger(EnemyManager.class.getName());
        log.setLevel(Level.INFO);

        initVariables();
    }

    private void initVariables() {
        enemyCount = 0;
        enemyAmountMax = 8;
        minSpawnHour = 1;
        maxSpawnHour = 3;
        dayCycle = "AM";

        spawnHour = gameTime.getHours() + random.nextInt(maxSpawnHour - minSpawnHour + 1) + minSpawnHour;
        checkSpawnHour();
        log.info("First enemy spawn at: " + spawnHour + " " + dayCycle);
    }

    /**
     * Renders the necromancer separately.
     * 
     * @param gc used to draw on the canvas
     */
    public void renderNecromancer(GraphicsContext gc) {
        if (!necromancer.checkDeath()) {
            necromancer.renderNecro(gc);
        }
    }

    /**
     * Updates the necromancer separately.
     */
    public void updateNecromancer() {
        if (!necromancer.checkDeath()) {
            necromancer.updateNecro();
        }
    }

    /**
     * Updates the enemies.
     * Spawns enemies based on the time of day and the current hour.
     * Updates the enemies - their movement, health, and death.
     * If an enemy dies, it is put into the enemyToRemove list.
     * After all the iterations, the enemies in the enemyToRemove list
     * are removed from the enemyList to prevent ERROR.
     */
    public void update() {
        setSpawningInterval();
        checkTimeToSpawn();
        for (Enemy enemy : enemyList) {
            enemy.update();
            checkDeath(enemy);
        }
        enemyList.removeAll(enemyToRemove);
    }

    /**
     * Renders all the enemies in the enemyList.
     * EnemyList is an ArrayList of all the enemies in the dungeon.
     * 
     * @param gc
     */
    public void render(GraphicsContext gc) {
        for (Enemy enemy : enemyList) {
            enemy.render(gc, 0);
        }
    }

    private void setSpawnRates() {
        skelRate = 20;
        zomRate = 38;
        eyeRate = 42;
    }

    /**
     * Sets the interval for spawning enemies based on the time of day.
     * If it is 6 AM - 6 PM, the interval is set to 2-6 hours. - it is day.
     * If it is 6 PM - 6 AM, the interval is set to 1-2 hours. - it is night.
     * During the night the spawn rate is higher.
     */
    private void setSpawningInterval() {
        int hours = gameTime.getHours();
        String dayCycle = gameTime.getDayCycle();
        if (hours <= 6 && dayCycle.equals("PM") || hours >= 6 && dayCycle.equals("AM")) {
            minSpawnHour = 2;
            maxSpawnHour = 6;
        } else {
            minSpawnHour = 1;
            maxSpawnHour = 2;
        }
    }

    /**
     * Checks if it is time to spawn an enemy and
     * if the maximum amount of enemies has not been reached.
     * If the spawn hour is reached by current time, an enemy is spawned.
     */
    private void checkTimeToSpawn() {
        int currentHour = gameTime.getHours();
        if (enemyCount < enemyAmountMax) {
            if (currentHour >= spawnHour && dayCycle.equals(gameTime.getDayCycle()) ||
                currentHour < spawnHour && !dayCycle.equals(gameTime.getDayCycle())) {
                spawnEnemy();
                spawnHour = currentHour + random.nextInt(maxSpawnHour - minSpawnHour + 1) + minSpawnHour;
                checkSpawnHour();
                enemyCount++;
                log.info("Next enemy spawn at: " + spawnHour + " " + dayCycle);
            }
        }
    }

    private void checkSpawnHour() {
        if (spawnHour >= 12) {
            spawnHour = 1;
            if (dayCycle.equals("AM")) {
                dayCycle = "PM";
            } else {
                dayCycle = "AM";
            }
        }
    }

    /**
     * Spawns an enemy at a random location in the dungeon.
     * Coordinates are generated randomly and put into a Vector2D Set.
     * If the coordinates are already used, the iteration is skipped.
     */
    private void spawnEnemy() {
        int randomNum = random.nextInt(100);

        while (true) {
            int x = random.nextInt(TILES_IN_WIDTH);
            int y = random.nextInt(TILES_IN_WIDTH);
            Vector2D coords = new Vector2D(x, y);
            if (usedCoords.contains(coords)) {
                continue; // skip iteration if the coords are already used
            }
            usedCoords.add(coords);
            x *= TILE_SIZE;
            y *= TILE_SIZE;
            // random number determines which enemy will be spawned
            if (randomNum <= skelRate) {
                if (canBeSpawned(x, y, 32, 32)) {
                    enemyList.add(new Skeleton(playState, x, y, attributesCreator.getAttributes(SKELETON)));
                    break;
                }
            } else if (randomNum <= skelRate + zomRate) {
                if (canBeSpawned(x, y, 32, 32)) {
                    enemyList.add(new Zombie(playState, x, y, attributesCreator.getAttributes(ZOMBIE)));
                    break;
                }
            } else if (randomNum <= skelRate + zomRate + eyeRate) {
                if (canBeSpawned(x, y, 32, 32)) {
                    enemyList.add(new EyeBall(playState, x, y, attributesCreator.getAttributes(EYEBALL)));
                    break;
                }
            }

        }
        log.info("Enemy spawned at: " + spawnHour + " " + dayCycle);
    }

    /**
     * Checks if an enemy can be spawned at the given coordinates.
     * 
     * @param x      x-coordinate
     * @param y      y-coordinate
     * @param width  width of the enemy
     * @param height height of the enemy
     * @return true if the enemy can be spawned, false otherwise
     */
    private boolean canBeSpawned(int x, int y, int width, int height) {
        return collision.isSpawnValid(x, y, width, height);
    }

    /**
     * Checks if an enemy has died.
     * If yes - the enemy is put into the enemyToRemove list
     * and will be removed from the enemyList.
     * Enemy count is decreased by 1 so that we could
     * spawn another enemy.
     * Player gets coins for killing an enemy.
     * 
     * @param enemy check death of this enemy
     */
    private void checkDeath(Enemy enemy) {
        if (enemy.getHealth() <= 0) {
            enemyToRemove.add(enemy);
            enemyCount--;
            playState.getCoinManager().addCoinsForKill();
        }
    }

    public int getNecromancerHealth() {
        return necromancer.getHealth();
    }

    public ArrayList<Enemy> getEnemyList() {
        return enemyList;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        if (enemyCount > 0 && enemyCount <= enemyAmountMax) {
            this.enemyCount = enemyCount;
        }
    }

    public Necromancer getNecromancer() {
        return necromancer;
    }

    public AttributesCreator getAttributesCreator() {
        return attributesCreator;
    }
}
