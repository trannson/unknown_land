package cz.cvut.fel.pjv.features;

import static cz.cvut.fel.pjv.utils.LoadFiles.LoadImage;

import java.util.Random;

import cz.cvut.fel.pjv.view.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Class for managing PLAYER'S coins.
 * It has methods for adding, removing and getting coins.
 * It also has a method for rendering the coins.
 * 
 * @author Son Ngoc Tran
 */
public class CoinManager {
    
    private Image coinIMG;
    private int playerCoinAmount;
    private Random random;
    private Camera camera;

    /**
     * Constructor for CoinManager.
     * @param camera camera used for rendering in the right place
     */
    public CoinManager(Camera camera) {
        this.camera = camera;
        coinIMG = LoadImage("items/coin.png");
        playerCoinAmount = 0;
        random = new Random();
    }

    /**
     * Drawing the coin image and the amount of coins.
     * X and Y coordinates are set by camera.
     * @param gc used for drawing on canvas
     */
    public void render(GraphicsContext gc) {
        double x = camera.getX() + 30;
        double y = camera.getY() + 60;

        // drawing coin image
        gc.drawImage(coinIMG, x, y, 32, 32);

        // drawing text
        gc.setFill(Color.WHITE); // set font color (white
        gc.setFont(Font.font("Verdana", FontWeight.BOLD , 20));
        gc.fillText("x" + playerCoinAmount, x + 40, y + 25);
    }

    public void addCoins(int amount) {
        if (amount > 0 && amount < 130) { // max 130 coins for 1 item
            playerCoinAmount += amount;
        }
    }

    public void removeCoins(int amount) {
        if (amount > 0 && amount < 130) {
            playerCoinAmount -= amount;
        }
    }

    public void addCoinsForKill() {
        playerCoinAmount += random.nextInt(20) + 5; // random amount of coins between 5 and 25
    }

    public int getCoins()  {
        return playerCoinAmount;
    }

    public void setCoins(int amount) {
        if (amount >= 0) {
            playerCoinAmount = amount;
        }
    }

}
