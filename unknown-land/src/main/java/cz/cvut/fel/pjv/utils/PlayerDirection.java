package cz.cvut.fel.pjv.utils;

import static cz.cvut.fel.pjv.utils.Constants.PlayerConstants.*;

/**
 * Enum for sprite amount in each direction.
 * Used in the Player class.
 * @see Constants.PlayerConstants
 * 
 * @author Son Ngoc Tran
 */
public enum PlayerDirection {
    UP(RUNNINGU, IDLEU),
    DOWN(RUNNINGD, IDLED),
    LEFT(RUNNINGL, IDLEL),
    RIGHT(RUNNINGR, IDLER);

    private int running;
    private int idle;

    private PlayerDirection(int running, int idle) {
        this.running = running;
        this.idle = idle;
    }

    public int getRunning() {
        return running;
    }

    public int getIdle() {
        return idle;
    }
}