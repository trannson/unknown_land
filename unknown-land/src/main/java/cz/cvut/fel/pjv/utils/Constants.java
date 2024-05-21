package cz.cvut.fel.pjv.utils;

/**
 * Class for the Constants.
 * It is used to store the constants used in the game.
 * 
 * @author Son Ngoc Tran
 */
public class Constants {

    public static class Enemy {
        public static final int SKELETON = 0;
        public static final int ZOMBIE = 1;
        public static final int EYEBALL = 2;
        public static final int NECROMANCER = 3;

        public static final String SKELETON_SPRITE = "skeleton.png";

        public static final int ENEMY_WIDTH = 88;
        public static final int ENEMY_HEIGHT = 88;

        public static final int ZOM_RUNNING = 2;
        public static final int ZOM_ATTACK = 1;
        public static final int ZOM_HIT = 5;

        public static final int SKEL_RUNNING = 2;
        public static final int SKEL_ATTACK = 0;
        public static final int SKEL_HIT = 4;

        public static final int EYE_RUNNING = 1;
        public static final int EYE_ATTACK = 2;
        public static final int EYE_HIT = 3;

        public static final int NECROMANCER_RUNNING = 0;
        public static final int NECROMANCER_ATTACK = 1;
        public static final int NECROMANCER_HIT = 2;
        public static final int NECROMANCER_SPELL = 4;
        public static final int NECRO_SPELL_APPEAR = 0;
        public static final int NECRO_SPELL_DISAPPEAR = 1;

        /**
         * Get the amount of animations for the enemy.
         * 
         * @param enemy_action action of the enemy
         * @param enemy_type   type of the enemy
         * @return
         */
        public static int GetAniAmount(int enemy_action, int enemy_type) {
            switch (enemy_type) {
                case SKELETON:
                    if (enemy_action == SKEL_RUNNING) {
                        return 12;
                    } else if (enemy_action == SKEL_ATTACK) {
                        return 13;
                    } else if (enemy_action == SKEL_HIT) {
                        return 3;
                    }

                case ZOMBIE:
                    if (enemy_action == ZOM_RUNNING) {
                        return 8;
                    } else if (enemy_action == ZOM_ATTACK) {
                        return 7;
                    } else if (enemy_action == ZOM_HIT) {
                        return 3;
                    }

                case EYEBALL:
                    if (enemy_action == EYE_RUNNING) {
                        return 4;
                    } else if (enemy_action == EYE_ATTACK) {
                        return 4;
                    } else if (enemy_action == EYE_HIT) {
                        return 4;
                    }
                case NECROMANCER:
                    if (enemy_action == NECROMANCER_RUNNING) {
                        return 5;
                    } else if (enemy_action == NECROMANCER_ATTACK) {
                        return 5;
                    } else if (enemy_action == NECROMANCER_HIT) {
                        return 4;
                    } else if (enemy_action == NECROMANCER_SPELL) {
                        return 8;
                    } else if (enemy_action == NECRO_SPELL_APPEAR) {
                        return 5;
                    } else if (enemy_action == NECRO_SPELL_DISAPPEAR) {
                        return 3;
                    }
                default:
                    return 1;
            }
        }
    }

    public static class ItemConstants {
        public static final int UNUASBLE_ITEM = 0;
        public static final int FOOD = 1;
        public static final int INVIS_ITEM = 5;
        public static final int SWORD = 6;
        public static final int MAP_ITEM = 8;

        public static final int ITEM_PIX_SIZE = 32;
        public static final int SWORD_PIX_SIZE = 64;
        public static final int MAX_STACK = 5;

        public static final String BALL_IMG = "items/ball.png";
        public static final String BLUESWORD_IMG = "items/bluesword.png";
        public static final String BREAD_IMG = "items/bread.png";
        public static final String BURGER_IMG = "items/burger.png";
        public static final String CANDY_IMG = "items/candy.png";
        public static final String COOKIE_IMG = "items/cookie.png";
        public static final String FLASHLIGHT_IMG = "items/flashlight.png";
        public static final String GREENSWORD_IMG = "items/greensword.png";
        public static final String INVIS_IMG = "items/nothing.png";
        public static final String PAN_IMG = "items/pan.png";
        public static final String RADIO_IMG = "items/radio.png";
        public static final String REDSWORD_IMG = "items/redsword.png";
        public static final String TEDDYBEAR_IMG = "items/teddy_bear.png";
        public static final String MAPITEM_IMG = "items/map.png";
    }

    public static class Attributes {
        public static final int SKEL_PIX_SIZE = 132;
        public static final int SKEL_HEALTH = 100;
        public static final int SKEL_DAMAGE = 25;
        public static final float SKEL_SPEED = 1.5f;

        public static final int ZOM_PIX_SIZE = 64;
        public static final int ZOM_HEALTH = 70;
        public static final int ZOM_DAMAGE = 20;
        public static final float ZOM_SPEED = 2.0f;

        public static final int EYE_PIX_SIZE = 32;
        public static final int EYE_HEALTH = 30;
        public static final int EYE_DAMAGE = 10;
        public static final float EYE_SPEED = 3f;

        public static final int NECRO_PIX_SIZE = 200;
        public static final int NECRO_HEALTH = 300;
        public static final int NECRO_DAMAGE = 30;
        public static final float NECRO_SPEED = 2.0f;
    }

    public static class MenuConstants {
        public static final String MENU = "menu/menugif.gif";
        public static final String ICON = "menu/ul_icon.png";
        public static final String PLAYER_IMAGE1 = "menu/first_player.png";
        public static final String PLAYER_IMAGE2 = "menu/second_player.png";
    }

    public static class GameConstants {

        public static final int FPS_SET = 80;
        public static final float ENTITY_SCALE = 1.5f;
        public static final int MAP_SCALE = 1;
        public static final int TILE_SIZE = 48;
        public static final int TILES_IN_WIDTH = 50;
        public static final int TILES_IN_HEIGHT = 50;
        public static final int WINDOW_WIDTH = 800;
        public static final int WINDOW_HEIGHT = 600;
        public static final int PLANET1_WIDTH = 35 * TILE_SIZE;
        public static final int PLANET1_HEIGHT = 40 * TILE_SIZE;
        public static final int PLANET2_WIDTH = 50 * TILE_SIZE;
        public static final int PLANET2_HEIGHT = 50 * TILE_SIZE;
        public static final int DUNGEON1_WIDTH = 50 * TILE_SIZE;
        public static final int DUNGEON1_HEIGHT = 50 * TILE_SIZE;
        public static final int MAP_WIDTH = 50 * TILE_SIZE * MAP_SCALE;
        public static final int MAP_HEIGHT = 50 * TILE_SIZE * MAP_SCALE;
        public static final int WORLD_WIDTH = TILE_SIZE * TILES_IN_WIDTH;
        public static final int WORLD_HEIGHT = TILE_SIZE * TILES_IN_HEIGHT;
        public static final int PLAYER_START_X = 14 * MAP_SCALE * TILE_SIZE;
        public static final int PLAYER_START_Y = 29 * MAP_SCALE * TILE_SIZE;
    }

    public static class HitboxConstants {
        public static final int PLAYER_HITBOX_WIDTH = 30;
        public static final int PLAYER_HITBOX_HEIGHT = 35;
        public static final int PLAYER_HITBOX_OFFSET_X = 30;
        public static final int PLAYER_HITBOX_OFFSET_Y = 48;
    }

    public static class MapConstants {
        public static final String LVL1_MAP = "map/level1.png";
        public static final String LVL1_COLLISION = "map/level1collision.txt";
        public static final String LVL1_DUNGEON = "map/dungeon1.png";
        public static final String LVL1_DUNGEON_COLLISION = "map/dungeon1collision.txt";
        public static final String LVL2_MAP = "map/level2.png";
        public static final String LVL2_COLLISION = "map/level2collision.txt";
    }

    public static class PlayerConstants {

        public static final String NORMAL_PLAYER = "playerImages/player.png";
        public static final String GREEN_SWORD_PLAYER = "playerImages/greenplayer.png";
        public static final String BLUE_SWORD_PLAYER = "playerImages/blueplayer.png";
        public static final String RED_SWORD_PLAYER = "playerImages/redplayer.png";
        public static final String HIT_PLAYER = "playerImages/playerHit.png";

        public static final int PLAYER_MAX_HEALTH = 120;

        public static final int PLAYER_WIDTH = 88;
        public static final int PLAYER_HEIGHT = 88;

        public static final int RUNNINGU = 5;
        public static final int RUNNINGD = 4;
        public static final int RUNNINGL = 6;
        public static final int RUNNINGR = 7;

        public static final int IDLEU = 1;
        public static final int IDLED = 0;
        public static final int IDLEL = 2;
        public static final int IDLER = 3;

        public static final int ATTACKD = 8;

        public static final int HIT_UP = 0;
        public static final int HIT_DOWN = 3;
        public static final int HIT_LEFT = 1;
        public static final int HIT_RIGHT = 2;

        public static final int ATTACK_UP = 1;
        public static final int ATTACK_DOWN = 3;
        public static final int ATTACK_LEFT = 0;
        public static final int ATTACK_RIGHT = 2;

        /**
         * Get the amount of running or idle animations for the player.
         * 
         * @param player_action
         * @return
         */
        public static int GetAniAmount(int player_action) {
            switch (player_action) {
                case RUNNINGU:
                    return 2;
                case RUNNINGD:
                    return 3;
                case RUNNINGL:
                    return 6;
                case RUNNINGR:
                    return 6;
                case IDLEU:
                    return 1;
                case IDLED:
                    return 1;
                case IDLEL:
                    return 1;
                case IDLER:
                    return 1;
                case ATTACKD:
                    return 10;
                default:
                    return 1;

            }
        }

        /**
         * Get the amount of attack animations for the player.
         * 
         * @param player_action
         * @return
         */
        public static int GetAttackAmount(int player_action) {
            switch (player_action) {
                case 0:
                    return 5;
                case 1:
                    return 4;
                case 2:
                    return 5;
                case 3:
                    return 7;
                default:
                    return 0;
            }
        }

    }

}
