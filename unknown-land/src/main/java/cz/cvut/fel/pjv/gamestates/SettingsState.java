package cz.cvut.fel.pjv.gamestates;

import static cz.cvut.fel.pjv.gamestates.MenuState.beautifyButtons;
import static cz.cvut.fel.pjv.gamestates.MenuState.setBackground;
import static cz.cvut.fel.pjv.gamestates.MenuState.setImageOnBackground;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;
import static cz.cvut.fel.pjv.utils.Constants.MenuConstants.MENU;
import static cz.cvut.fel.pjv.utils.LoadFiles.LoadImage;
import static cz.cvut.fel.pjv.utils.LoadFiles.LoadFont;

import cz.cvut.fel.pjv.view.GameView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The SettingsState class represents the game state for the settings menu.
 * It allows the player to view control settings.
 * 
 * @author Son Ngoc Tran
 */
public class SettingsState {

    private Stage primarystage;
    private Scene scene;
    private Font font;
    private Button backButton;
    private int fontSize, titleSize;

    /**
     * Constructor for the SettingsState class.
     * 
     * @param gameView to set the scene
     */
    public SettingsState(GameView gameView) {
        primarystage = gameView.getStage();
        Image image = LoadImage(MENU);
        VBox vbox = new VBox(20);
        VBox vboxText = new VBox(10);
        StackPane stackPane = new StackPane();
        BorderPane borderPane = new BorderPane();

        fontSize = 15;
        titleSize = 30;

        scene = new Scene(stackPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        stackPane.getChildren().add(borderPane);

        initButtons();
        addTextToVbox(vboxText);
        setUpVBOX(vbox, vboxText, stackPane);
        setBackground(stackPane, image);
        setImageOnBackground(borderPane);
        setUpInputs(primarystage, gameView);
    }

    /**
     * Adds text to the text VBox that is on
     * top of the basic vBox.
     * 
     * @param vboxText
     */
    private void addTextToVbox(VBox vboxText) {
        setUpText(vboxText, font, titleSize, "Keyboard Controls");

        setUpText(vboxText, font, fontSize, "W : Move Up");
        setUpText(vboxText, font, fontSize, "A : Move Left");
        setUpText(vboxText, font, fontSize, "S : Move Down");
        setUpText(vboxText, font, fontSize, "D : Move Right");

        setUpText(vboxText, font, fontSize, "TAB : Inventory/Shop");
        setUpText(vboxText, font, fontSize, "F : Interact with objects");
        setUpText(vboxText, font, fontSize, "E : Use Item");
        setUpText(vboxText, font, fontSize, "Q : Drop Item");
        setUpText(vboxText, font, fontSize, "B : Buy/Sell Item");
        setUpText(vboxText, font, fontSize, "DIGIT1 - DIGIT8 : Move Inventory Slot");
        setUpText(vboxText, font, fontSize, "LEFT/RIGHT : Move Inventory Slot");

        setUpText(vboxText, font, fontSize, "ENTER : Swap Items");
        setUpText(vboxText, font, fontSize, "ESC : Pause Game");

        setUpText(vboxText, font, titleSize, "Mouse Controls");
        setUpText(vboxText, font, fontSize, "MOUSE1 : Attack");

    }

    private void initButtons() {
        font = LoadFont("transamericabold.ttf", 70);
        backButton = new Button("Back");
        beautifyButtons(backButton, font);

    }

    /**
     * Set up the VBOX.
     * Add buttons to the VBOX and set the alignment.
     * Then add the VBOX + VBOXTEXT to the StackPane.
     * 
     * @param vbox
     * @param vboxText
     * @param stackPane
     */
    private void setUpVBOX(VBox vbox, VBox vboxText, StackPane stackPane) {
        vbox.getChildren().addAll(backButton);
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        stackPane.getChildren().add(vboxText);
        stackPane.getChildren().add(vbox);
    }

    /**
     * Set up the inputs for the buttons.
     * Switching scene and state based on the button clicked.
     * 
     * @param stage
     * @param gameView
     */
    private void setUpInputs(Stage stage, GameView gameView) {
        backButton.setOnAction(e -> {
            if (States.previousState == States.PLAY) {
                States.currentState = States.PAUSE;
                stage.setScene(gameView.getPauseState().getScene());
            } else {
                States.currentState = States.MENU;
                stage.setScene(gameView.getMenuState().getScene());
            }
        });
    }

    /**
     * Set up the text and add it to the text VBox.
     * 
     * @param vboxText
     * @param font
     * @param fontSize
     * @param message  what the text should say
     */
    private void setUpText(VBox vboxText, Font font, int fontSize, String message) {
        Text text = new Text(message);
        text.setFont(Font.font(font.getFamily(), fontSize));
        text.setFill(Color.web("#c49ed9"));
        text.setStrokeWidth(1);
        text.setStroke(Color.BLACK);

        vboxText.setAlignment(Pos.TOP_CENTER);
        vboxText.getChildren().add(text);
    }

    public Scene getScene() {
        return scene;
    }
}
