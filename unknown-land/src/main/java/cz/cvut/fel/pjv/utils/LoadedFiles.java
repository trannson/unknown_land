package cz.cvut.fel.pjv.utils;

import javafx.scene.image.Image;

import static cz.cvut.fel.pjv.utils.LoadFiles.LoadSubImages;
import static cz.cvut.fel.pjv.utils.Constants.PlayerConstants.*;

/**
 * Class for the LoadedFiles.
 * It is used to store the loaded images.
 * @see LoadFiles
 * 
 * @author Son Ngoc Tran
 */
public class LoadedFiles {

    private Image[][] normalPlayer, greenPlayer, bluePlayer, redPlayer, hitPlayer;

    public LoadedFiles() {
        normalPlayer = LoadSubImages(NORMAL_PLAYER, 64);
        greenPlayer = LoadSubImages(GREEN_SWORD_PLAYER, 64);
        bluePlayer = LoadSubImages(BLUE_SWORD_PLAYER, 64);
        redPlayer = LoadSubImages(RED_SWORD_PLAYER, 64);
        hitPlayer = LoadSubImages(HIT_PLAYER, 64);
    }

    /**
     * Get the player image depending on the index.
     * 
     * @param index
     * @return player image
     */
    public Image[][] getPlayerImage(int index) {
        switch (index) {
            case 0:
                return normalPlayer;
            case 1:
                return greenPlayer;
            case 2:
                return bluePlayer;
            case 3:
                return redPlayer;
            default:
                return null;
        }
    }

    public Image[][] getHitPlayer() {
        return hitPlayer;
    }

}
