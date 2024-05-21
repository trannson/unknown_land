package cz.cvut.fel.pjv.gamestates;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;

import cz.cvut.fel.pjv.view.GameView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static cz.cvut.fel.pjv.utils.LoadFiles.LoadImage;
import static cz.cvut.fel.pjv.utils.Constants.MenuConstants.MENU;
import static cz.cvut.fel.pjv.utils.LoadFiles.LoadFont;
import static cz.cvut.fel.pjv.Main.stopGameLoop;
import static cz.cvut.fel.pjv.gamestates.MenuState.*;

/**
 * Class for the PauseState.
 * It is displayed when the player pauses the game with ESCAPE key.
 * It has buttons for returning to the game, going to the settings, or returning
 * to the menu.
 * It uses static methods from the MenuState class.
 * 
 * @author Son Ngoc Tran
 */
public class PauseState {

    private Scene scene;
    private Button backbutton, settingsbutton, menuSavebutton;
    private Font font;

    /**
     * Constructor for the PauseState class.
     * 
     * @param gameView used for setting the scene
     */
    public PauseState(GameView gameView) {
        Stage stage = gameView.getStage();
        Image image = LoadImage(MENU);
        VBox vbox = new VBox(20);
        StackPane stackPane = new StackPane();
        BorderPane borderPane = new BorderPane();

        scene = new Scene(stackPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        stackPane.getChildren().add(borderPane);

        initButtons();
        setUpVBOX(vbox, stackPane);
        setBackground(stackPane, image);
        setImageOnBackground(borderPane);
        setUpInputs(stage, gameView);
    }

    private void initButtons() {
        font = LoadFont("transamericabold.ttf", 70);
        backbutton = new Button("BACK");
        beautifyButtons(backbutton, font);

        settingsbutton = new Button("SETTINGS");
        beautifyButtons(settingsbutton, font);

        menuSavebutton = new Button("MENU & SAVE");
        beautifyButtons(menuSavebutton, font);

    }

    /**
     * Set up the VBOX.
     * Add buttons to the VBOX and set the alignment.
     * Then add the VBOX to the StackPane.
     * 
     * @param vbox      where the buttons are added
     * @param stackPane where the VBOX is added
     */
    private void setUpVBOX(VBox vbox, StackPane stackPane) {
        vbox.getChildren().addAll(backbutton, settingsbutton, menuSavebutton);
        vbox.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(vbox);
    }

    /**
     * Set up the inputs for the buttons.
     * If ESCAPE key is pressed, the scene changes and
     * the current state is set to PLAY. Same for the BACK button.
     * If SETTINGS button is pressed, the scene changes to the settings state.
     * And we also need to set the previous state to PLAY otherwise
     * it would collide with MENU state.
     * If MENU button is pressed, the scene changes to the menu state.
     * Game loop is stopped when switching to the menu state.
     */
    private void setUpInputs(Stage stage, GameView gameView) {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ESCAPE:
                    States.currentState = States.PLAY;
                    break;
                default:
                    break;
            }
        });

        backbutton.setOnAction(e -> {
            States.currentState = States.PLAY;
        });

        settingsbutton.setOnAction(e -> {
            States.currentState = States.SETTINGS;
            States.previousState = States.PLAY;
            stage.setScene(gameView.getSettingsState().getScene());
        });


        menuSavebutton.setOnAction(e -> {
            States.currentState = States.MENU;
            gameView.getGameSave().saveGame();
            stopGameLoop();
            stage.setScene(gameView.getMenuState().getScene());
        });
    }

    public Scene getScene() {
        return scene;
    }

}