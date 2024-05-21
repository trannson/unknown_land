package cz.cvut.fel.pjv.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import cz.cvut.fel.pjv.gamesave.GameSave;
import cz.cvut.fel.pjv.gamestates.DeathState;
import cz.cvut.fel.pjv.gamestates.MenuState;
import cz.cvut.fel.pjv.gamestates.PauseState;
import cz.cvut.fel.pjv.gamestates.PlayState;
import cz.cvut.fel.pjv.gamestates.SettingsState;
import cz.cvut.fel.pjv.gamestates.States;
import cz.cvut.fel.pjv.gamestates.WinState;
import cz.cvut.fel.pjv.levels.LevelManager;

import static cz.cvut.fel.pjv.Main.stopGameLoop;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.*;
import static cz.cvut.fel.pjv.utils.Constants.MenuConstants.ICON;
import static cz.cvut.fel.pjv.utils.LoadFiles.LoadImage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for the GameView.
 * This class detemines the game scene and states.
 * It is used to switch between the game states.
 * It is used to render the game.
 * It is used to close the game.
 * 
 * @see MenuState
 * @see PlayState
 * @see PauseState
 * @see DeathState
 * @see WinState
 * @see SettingsState
 * @see GameSave
 * 
 * @author Son Ngoc Tran
 */
public class GameView {

    private Pane pane;
    private Scene scene;
    private Canvas canvas;
    private Stage stage;
    private PlayState playState;
    private PauseState pauseState;
    private MenuState menuState;
    private LevelManager levelManager;
    private DeathState deathState;
    private WinState winState;
    private SettingsState settingsState;
    private GameSave gameSave;
    private Logger log;

    /**
     * Constructor for the GameView.
     * Initialize the game window.
     * Initialize all the game states.
     * 
     * @param stage
     */
    public GameView(Stage stage) {
        this.stage = stage;
        levelManager = new LevelManager();
        canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        pane = new Pane(canvas);
        scene = new Scene(pane);

        menuState = new MenuState(this);
        playState = new PlayState(this);
        pauseState = new PauseState(this);
        deathState = new DeathState(this, "YOU DIED!");
        winState = new WinState(this, "YOU WON!!!");
        settingsState = new SettingsState(this);
        gameSave = new GameSave(this, playState);

        log = Logger.getLogger(GameView.class.getName());
        log.setLevel(Level.INFO);
        log.info("Everything is set up and initialized.");
    }

    /**
     * Initialize the game window.
     * Used in the Main class.
     */
    public void init() {
        stage.setScene(menuState.getScene()); // switch to the game scene
        stage.centerOnScreen();
        stage.setTitle("Unknown Land");
        stage.setResizable(false);
        stage.getIcons().add(LoadImage(ICON));

        stage.show();

        log.info("Game window is set up and displayed.");
    }

    /**
     * Render the game.
     * Switch between the game states.
     * Update the game states.
     * If the scene is not switched to the current state, switch it.
     * Game loop starts when Play state is active.
     */
    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        switch (States.currentState) {
            case PLAY:
                if (stage.getScene() != scene) { // if for some reason scene is not switched to play
                    stage.setScene(scene);
                }
                playState.render(gc);
                playState.update();
                break;
            case MENU: // if for some reason scene is not switched to menu
                if (stage.getScene() != getMenuState().getScene()) {
                    stage.setScene(getMenuState().getScene());
                }
                break;
            case PAUSE: // if for some reason scene is not switched to pause
                if (stage.getScene() != getPauseState().getScene()) {
                    stage.setScene(getPauseState().getScene());
                }
                break;
            case DEATH:
                if (stage.getScene() != getDeathState().getScene()) {
                    stage.setScene(getDeathState().getScene());
                }
                stopGameLoop();
                break;
            case WIN:
                if (stage.getScene() != getWinState().getScene()) {
                    stage.setScene(getWinState().getScene());
                }
                stopGameLoop();
                break;
            case SETTINGS:
                if (stage.getScene() != getSettingsState().getScene()) {
                    stage.setScene(getSettingsState().getScene());
                }
                break;
            default:
                log.warning("Unknown state");
                break;
        }
    }
    public void close() {
        stage.close();
    }

    public Scene getScene() {
        return scene;
    }

    public PlayState getPlayState() {
        return playState;
    }

    public PauseState getPauseState() {
        return pauseState;
    }

    public MenuState getMenuState() {
        return menuState;
    }

    public Stage getStage() {
        return stage;
    }

    public DeathState getDeathState() {
        return deathState;
    }

    public WinState getWinState() {
        return winState;
    }

    public SettingsState getSettingsState() {
        return settingsState;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public GameSave getGameSave() {
        return gameSave;
    }

}
