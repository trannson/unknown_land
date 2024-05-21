package cz.cvut.fel.pjv.gamestates;

/**
 * Enum for different playing states.
 * We keep track of the current state of the game in here.
 * 
 * @author Son Ngoc Tran
 */
public enum PlayingStates {
    
    PLANET1,
    DUNGEON1,
    PLANET2;

    public static PlayingStates currentState = PLANET1;
}
