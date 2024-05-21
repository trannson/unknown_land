package cz.cvut.fel.pjv.utils;

import java.util.ArrayList;
import cz.cvut.fel.pjv.gamestates.PlayState;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.TILE_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.MapConstants.LVL1_COLLISION;

/**
 * Class for the Collision.
 * It is used to check the collision of the entities with the map.
 * It is also used for item spawning.
 * 
 * @author Son Ngoc Tran
 */
public class Collision {

    private ArrayList<ArrayList<Integer>> collisionMap;
    private PlayState playState;

    /**
     * Initialize the Collision class.
     * Load the collision map from the file.
     * 
     * @param playState to enter/exit the dungeon
     */
    public Collision(PlayState playState) {
        this.playState = playState;
        this.collisionMap = LoadFiles.LoadTxt(LVL1_COLLISION);
    }

    /**
     * Check if the entity is on the solid tile.
     * If tile is 2, the player will enter the dungeon.
     * If tile is 3, the player will exit the dungeon.
     * 
     * @param x x coordinate of the entity/item
     * @param y y coordinate of the entity/item
     * @return true if the entity is on the solid tile, false otherwise
     */
    public boolean isSolid(double x, double y) {
        int tileX = (int) Math.floor(x / TILE_SIZE);
        int tileY = (int) Math.floor(y / TILE_SIZE);

        if (collisionMap.get(tileY).get(tileX) == 2) {
            // ensure that the player will port to the dungeon when PLAYER steps on the tile
            if (tileX == playState.getPlayer().getX() / TILE_SIZE
                    && tileY == playState.getPlayer().getY() / TILE_SIZE) {
                playState.enterDungeon();
            }
        }

        if (collisionMap.get(tileY).get(tileX) == 3) {
            if (tileX == playState.getPlayer().getX() / TILE_SIZE
                    && tileY == playState.getPlayer().getY() / TILE_SIZE) {
                playState.exitDungeon();
            }
        }

        return collisionMap.get(tileY).get(tileX) == 1;
    }

    /**
     * Check if the whole entity is on the solid tile.
     * Check if all 4 corners of the entity are on the solid tile.
     * 
     * @param x      x coordinate of the entity/item
     * @param y      y coordinate of the entity/item
     * @param width  width of the entity/item
     * @param height height of the entity/item
     * @return true if the entity is on the solid tile, false otherwise
     */
    public boolean isMoveValid(float x, float y, double width, double height) {
        return !isSolid(x, y) && !isSolid(x + width, y) && !isSolid(x, y + height) && !isSolid(x + width, y + height);
    }

    /**
     * Check if the entity/item can be spawned on the tile.
     * 
     * @param x      x coordinate of the entity/item
     * @param y      y coordinate of the entity/item
     * @param width  width of the entity/item
     * @param height height of the entity/item
     * @return true if the entity/item can be spawned on the tile, false otherwise
     */
    public boolean isSpawnValid(float x, float y, double width, double height) {
        return isSpawnAble(x, y) && isSpawnAble(x + width, y) && isSpawnAble(x, y + height)
                && isSpawnAble(x + width, y + height);
    }

    /**
     * If the tile is 8, the entity/item can be spawned on the tile.
     * 
     * @param x x coordinate of the entity/item
     * @param y y coordinate of the entity/item
     * @return true if the entity/item can be spawned on the tile, false otherwise
     */
    private boolean isSpawnAble(double x, double y) {
        int tileX = (int) Math.floor(x / TILE_SIZE);
        int tileY = (int) Math.floor(y / TILE_SIZE);

        return collisionMap.get(tileY).get(tileX) == 8;
    }

    public void setCollisionMap(ArrayList<ArrayList<Integer>> collisionMap) {
        this.collisionMap = collisionMap;
    }

    public int getWidth() {
        return collisionMap.get(0).size() * TILE_SIZE;
    }

    public int getHeight() {
        return collisionMap.size() * TILE_SIZE;
    }

}
