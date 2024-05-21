package cz.cvut.fel.pjv.gamestates;

import cz.cvut.fel.pjv.view.GameView;

/**
 * Class to display Win text and button.
 * Extends DeathState class.
 * @see DeathState
 * 
 * @author Son Ngoc Tran
 */
public class WinState extends DeathState {
    
    public WinState(GameView gameView, String message) {
        super(gameView, message);
    }
}

