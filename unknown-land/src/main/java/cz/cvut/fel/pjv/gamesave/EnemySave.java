package cz.cvut.fel.pjv.gamesave;

import java.util.ArrayList;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.entities.*;
import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.utils.AttributesCreator;
import cz.cvut.fel.pjv.utils.Vector2D;

import static cz.cvut.fel.pjv.utils.Constants.Enemy.*;

/**
 * Saves and loads enemy data.
 * It's for GameSave class.
 * @see GameSave
 * 
 * @author Son Ngoc Tran
 */
public class EnemySave {

    private PlayState playState;
    private Logger log;

    /**
     * EnemySave constructor.
     * 
     * @param playState used to get enemy data
     * @param log       used to log errors
     */
    public EnemySave(PlayState playState, Logger log) {
        this.playState = playState;
        this.log = log;
    }

    /**
     * Saves enemy data.
     * We're saving enemy type, position, health and count of all enemies in
     * dungeon1 area.
     * We're checking if there are any enemies to save. Otherwise we would get null
     * pointer exception.
     * 
     * @param gameData stores all data we want to save
     */
    protected void saveEnemyData(GameData gameData) {
        gameData.enemyTypeList = new ArrayList<>();
        gameData.enemyPositions = new ArrayList<>();
        gameData.enemyHealthList = new ArrayList<>();
        gameData.enemyCount = playState.getEnemyManager().getEnemyCount();

        if (playState.getEnemyManager().getEnemyList() != null) {
            for (Enemy enemy : playState.getEnemyManager().getEnemyList()) {
                gameData.enemyTypeList.add(enemy.getType());
                gameData.enemyPositions.add(new Vector2D(enemy.getX(), enemy.getY()));
                gameData.enemyHealthList.add(enemy.getHealth());
            }
        } else {
            log.info("No enemies to save.");
        }
    }

    /**
     * Loading enemy data from GameData object.
     * We're creating new enemies based on their type and setting their position and
     * health.
     * We're also setting enemy count so we know how many enemies are in dungeon1
     * area
     * and also the enemy count is capped at 8.
     * 
     * @param gameData
     */
    protected void loadEnemyData(GameData gameData) {
        AttributesCreator attributesCreator = playState.getEnemyManager().getAttributesCreator();
        playState.getEnemyManager().getEnemyList().clear();

        if (gameData.enemyTypeList != null) {
            for (int i = 0; i < gameData.enemyTypeList.size(); i++) {
                float x = (float) gameData.enemyPositions.get(i).getX();
                float y = (float) gameData.enemyPositions.get(i).getY();
                switch (gameData.enemyTypeList.get(i)) {
                    case SKELETON:
                        playState.getEnemyManager().getEnemyList()
                                .add(new Skeleton(playState, x, y, attributesCreator.getAttributes(SKELETON)));
                        break;
                    case ZOMBIE:
                        playState.getEnemyManager().getEnemyList()
                                .add(new Zombie(playState, x, y, attributesCreator.getAttributes(ZOMBIE)));
                        break;
                    case EYEBALL:
                        playState.getEnemyManager().getEnemyList()
                                .add(new EyeBall(playState, x, y, attributesCreator.getAttributes(EYEBALL)));
                        break;
                }

                playState.getEnemyManager().getEnemyList().get(i).setHealth(gameData.enemyHealthList.get(i));
            }
        }
        playState.getEnemyManager().setEnemyCount(gameData.enemyCount);
    }

    /**
     * Saves necromancer data.
     * We're saving necromancer position and health.
     * If there is no necromancer to save we're logging warning.
     * 
     * @param gameData
     */
    protected void saveNecromancerData(GameData gameData) {
        if (playState.getEnemyManager().getNecromancer() != null) {
            gameData.necromancerPosition = new Vector2D(playState.getEnemyManager().getNecromancer().getX(),
                    playState.getEnemyManager().getNecromancer().getY());
            gameData.necromancerHealth = playState.getEnemyManager().getNecromancer().getHealth();
        } else {
            log.warning("No necromancer to save.");
        }
    }

    /**
     * Loading necromancer data.
     * Necromancer is created automatically when everything initializes.
     * We're just setting his position and health.
     * 
     * @param gameData
     */
    protected void loadNecromancerData(GameData gameData) {
        if (gameData.necromancerPosition != null) {
            float x = (float) gameData.necromancerPosition.getX();
            float y = (float) gameData.necromancerPosition.getY();
            playState.getEnemyManager().getNecromancer().setEnemyPos(x, y);
            playState.getEnemyManager().getNecromancer().setHealth(gameData.necromancerHealth);
        } else {
            log.warning("No necromancer to load.");
        }
    }

}
