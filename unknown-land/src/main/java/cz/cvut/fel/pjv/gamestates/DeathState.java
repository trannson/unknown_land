package cz.cvut.fel.pjv.gamestates;

import cz.cvut.fel.pjv.view.GameView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static cz.cvut.fel.pjv.utils.LoadFiles.LoadImage;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;
import static cz.cvut.fel.pjv.utils.Constants.MenuConstants.MENU;
import static cz.cvut.fel.pjv.utils.LoadFiles.LoadFont;
import static cz.cvut.fel.pjv.gamestates.MenuState.*;

/**
 * Class for the DeathState.
 * It is displayed when the player dies.
 * It has buttons for returning to the menu or exiting the game.
 * It uses static methods from the MenuState class.
 * 
 * @author Son Ngoc Tran
 */
public class DeathState {

    private Scene scene;
    private Button menubutton, exitbutton;
    private Font font;
    private Stage primarystage;
    private String message;

    /**
     * Constructor for the DeathState class.
     * 
     * @param gameView used for setting the scene
     * @param message  message displayed when the player dies
     */
    public DeathState(GameView gameView, String message) {
        this.message = message;
        primarystage = gameView.getStage();
        Image image = LoadImage(MENU);
        VBox vbox = new VBox(20);
        StackPane stackPane = new StackPane();
        BorderPane borderPane = new BorderPane();
        scene = new Scene(stackPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        stackPane.getChildren().add(borderPane);

        initButtons();
        setUpText(vbox, font);
        setUpVBOX(vbox, stackPane);
        setBackground(stackPane, image);
        setImageOnBackground(borderPane);
        setUpInputs(primarystage, gameView);
    }

    private void initButtons() {
        font = LoadFont("transamericabold.ttf", 70);
        menubutton = new Button("MENU");
        beautifyButtons(menubutton, font);
        exitbutton = new Button("EXIT");
        beautifyButtons(exitbutton, font);
    }

    /**
     * Add buttons to the VBOX and set the alignment.
     * Then add the VBOX to the StackPane.
     * 
     * @param vbox
     * @param stackPane
     */
    private void setUpVBOX(VBox vbox, StackPane stackPane) {
        vbox.getChildren().addAll(menubutton, exitbutton);
        vbox.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(vbox);
    }

    /**
     * Set up the inputs for the buttons.
     * Switching scene and state based on the button clicked.
     * Game loop is stopped when the menu button is clicked.
     * @param stage
     * @param gameView
     */
    private void setUpInputs(Stage stage, GameView gameView) {
        menubutton.setOnAction(e -> {
            States.currentState = States.MENU;
            stage.setScene(gameView.getMenuState().getScene());
        });

        exitbutton.setOnAction(e -> {
            gameView.close();
        });
    }

    /**
     * Set up the title text.
     * DropShadow is used for the text effect.
     * 
     * @param vbox
     * @param font
     */
    private void setUpText(VBox vbox, Font font) {
        Text text = new Text(message);
        text.setFont(Font.font(font.getFamily(), 70));
        text.setFill(Color.web("#c49ed9"));
        text.setStrokeWidth(5);
        text.setStroke(Color.BLACK);
        text.setTranslateY(-50);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(15.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.WHITESMOKE);
        text.setEffect(dropShadow);
        vbox.getChildren().add(text);
    }

    public Scene getScene() {
        return scene;
    }

}
