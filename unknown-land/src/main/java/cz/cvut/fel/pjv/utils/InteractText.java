package cz.cvut.fel.pjv.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Class for the InteractText.
 * It is used as hint for the player to interact with the object.
 * 
 * @author Son Ngoc Tran
 */
public abstract class InteractText {

    /**
     * Draw the interaction text.
     * It's a hint for the player to interact with the object.
     * 
     * @param key keycode to interact
     * @param x   x coordinate
     * @param y   y coordinate
     * @param gc
     */
    public static void drawClueText(String key, float x, float y, GraphicsContext gc) {
        String text = "Press '" + key + "' to interact";
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial Black", 20));
        gc.strokeText(text, x - 50, y - 10);
        gc.fillText(text, x - 50, y - 10);
    }
}
