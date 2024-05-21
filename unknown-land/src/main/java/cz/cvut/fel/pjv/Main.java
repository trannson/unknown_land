package cz.cvut.fel.pjv;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.view.GameView;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.FPS_SET;

/**
 * Main class for the game.
 * It is used to start the game.
 * It is used to stop the game.
 * It is used to start the game loop.
 * It is used to stop the game loop.
 * Everything starts from here.
 * 
 * @author Son Ngoc Tran
 */
public class Main extends Application {

    private static AnimationTimer gameLoop;
    private GameView gameView;
    private static final Logger log = Logger.getLogger(Main.class.getName());

    /**
     * Starting the application.
     * Initialize the game view.
     */
    @SuppressWarnings("exports")
    @Override
    public void start(Stage primaryStage) {
        gameView = new GameView(primaryStage);
        gameView.init();
        log.setLevel(Level.INFO);
    }

    /**
     * Stopping the game loop and game time thread.
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        gameView.getPlayState().getGameTime().stopTime();
        if (gameLoop != null)
            gameLoop.stop();
        log.info("Game loop stopped and stopping game.");
    }

    /**
     * Starting the game loop.
     * Render is called 60 times per second capped by JavaFX's AnimationTimer.
     * Using AnimationTimer for the game loop thread.
     * 
     * @param gameView
     */
    @SuppressWarnings("exports")
    public static void startGameLoop(GameView gameView) {
        gameLoop = new AnimationTimer() {
            int fps = 0;
            long lastUpdate = 0;
            long lastFpsPrint = 0;

            @Override
            public void handle(long now) { // called every frame
                if (now - lastUpdate >= 1_000_000_000 / FPS_SET) { // 1 second in nanoseconds
                    gameView.render();
                    fps++; // max fps is capped at 60 by JavaFX's AnimationTimer, even though the gameLoop
                           // runs at 80 fps
                    lastUpdate = now;
                }
                if (now - lastFpsPrint >= 1000000000) {
                    // log.info("FPS: " + fps);
                    fps = 0;
                    lastFpsPrint = now;
                }
            }
        };
        gameLoop.start();
        log.info("Game loop started.");
    }

    /**
     * Stopping the game loop.
     * This is used when the player dies or wins or
     * if player goes to the menu.
     */
    public static void stopGameLoop() {
        gameLoop.stop();
        log.info("Game loop stopped.");
    }

}