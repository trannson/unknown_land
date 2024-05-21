package cz.cvut.fel.pjv.gamestates;

/**
 * Enum for game states.
 * We keep track of the current and previous state.
 * 
 * @author Son Ngoc Tran
 */
public enum States {

    MENU,
    PLAY,
    PAUSE,
    DEATH,
    WIN,
    SETTINGS;

    public static States currentState = MENU;
    public static States previousState = MENU;
}
