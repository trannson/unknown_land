package cz.cvut.fel.pjv.gamestates;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static cz.cvut.fel.pjv.Main.startGameLoop;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;
import static cz.cvut.fel.pjv.utils.Constants.MenuConstants.*;
import static cz.cvut.fel.pjv.utils.LoadFiles.LoadImage;

import cz.cvut.fel.pjv.view.GameView;

import static cz.cvut.fel.pjv.utils.LoadFiles.LoadFont;

/**
 * Class for the MenuState.
 * It is displayed when the game starts or if the player clicks on MENU button.
 * It has buttons for starting the game, going to the settings, or exiting the
 * game.
 * 
 * @author Son Ngoc Tran
 */
public class MenuState {

    private Button startbutton, settingsButton, exitbutton, loadButton;
    private Scene scene;

    /**
     * Constructor for the MenuState class.
     * 
     * @param gameView used for setting the scene
     */
    public MenuState(GameView gameView) {
        Image image = LoadImage(MENU);
        Font font = LoadFont("transamericabold.ttf", 70);

        VBox vbox = new VBox(20);
        StackPane stackPane = new StackPane();
        BorderPane borderPane = new BorderPane();

        scene = new Scene(stackPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        stackPane.getChildren().add(borderPane);

        initButtons();
        beautifyButtons(startbutton, font);
        beautifyButtons(loadButton, font);
        beautifyButtons(settingsButton, font);
        beautifyButtons(exitbutton, font);
        setUpText(vbox, font);
        setUpVBOX(vbox, stackPane);
        setBackground(stackPane, image);
        setImageOnBackground(borderPane);

        setUpInputs(gameView);
    }

    /**
     * Set up the inputs for the buttons.
     * Switches the state of the game based on the button clicked.
     * 
     * @param gameView
     */
    private void setUpInputs(GameView gameView) {
        startbutton.setOnAction(e -> {
            States.currentState = States.PLAY;
            gameView.getPlayState().reset();
            startGameLoop(gameView);
        });

        loadButton.setOnAction(e -> {
            gameView.getGameSave().loadGame();
        });

        settingsButton.setOnAction(e -> {
            States.currentState = States.SETTINGS;
            States.previousState = States.MENU;
            gameView.getStage().setScene(gameView.getSettingsState().getScene());
        });

        exitbutton.setOnAction(e -> {
            gameView.close();
        });
    }

    protected void initButtons() {
        startbutton = new Button("NEW GAME");
        loadButton = new Button("LOAD GAME");
        settingsButton = new Button("SETTINGS");
        exitbutton = new Button("EXIT");
    }

    /**
     * Beautify the buttons.
     * Making them bigger, changing the color on mouse hover.
     * 
     * @param button button to beautify
     * @param font   font to set on the button
     */
    protected static void beautifyButtons(Button button, Font font) {
        button.setFont(Font.font(font.getFamily(), 35));
        button.setTextFill(Color.web("#343036"));
        button.setStyle("-fx-background-color: #e4dce8");
        button.setPrefWidth(350);
        button.setPrefHeight(50);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #c49ed9"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #e4dce8"));
    }

    /**
     * Set the background of the StackPane.
     * 
     * @param stackPane where to set the background
     * @param image     image to set as background
     */
    protected static void setBackground(StackPane stackPane, Image image) {
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        stackPane.setBackground(background);
    }

    /**
     * Add buttons to the VBOX and set the alignment.
     * Then add the VBOX to the StackPane.
     * 
     * @param vbox
     * @param stackPane
     */
    protected void setUpVBOX(VBox vbox, StackPane stackPane) {
        vbox.getChildren().addAll(startbutton,loadButton, settingsButton, exitbutton);
        vbox.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(vbox);
    }

    /**
     * Set up the title text.
     * DropShadow is used for the text effect.
     * 
     * @param vbox where to add the text
     * @param font font to set on the text
     */
    private void setUpText(VBox vbox, Font font) {
        Text text = new Text("Unknown Land");
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

    /**
     * Set the image of the players on the background.
     * Two players are displayed on the background.
     * One on the left side and one on the right side.
     * 
     * @param borderPane
     */
    protected static void setImageOnBackground(BorderPane borderPane) {
        int offsetDOWN = WINDOW_WIDTH / 2 - 350;
        int offsetLEFT = WINDOW_HEIGHT / 2 - 250;
        int offsetRIGHT = WINDOW_HEIGHT / 2 - 270;
        Image playerOne = LoadImage(PLAYER_IMAGE1);
        Image playerTwo = LoadImage(PLAYER_IMAGE2);

        ImageView imageViewOne = new ImageView(playerOne);
        imageViewOne.setFitHeight(300);
        imageViewOne.setPreserveRatio(true);
        imageViewOne.setSmooth(true);

        ImageView imageViewTwo = new ImageView(playerTwo);
        imageViewTwo.setFitHeight(300);
        imageViewTwo.setPreserveRatio(true);
        imageViewTwo.setSmooth(true);

        // Add the ImageView to the BorderPane
        borderPane.setLeft(imageViewOne);
        borderPane.setRight(imageViewTwo);
        BorderPane.setAlignment(imageViewOne, Pos.CENTER_LEFT);
        BorderPane.setAlignment(imageViewTwo, Pos.CENTER_RIGHT);
        BorderPane.setMargin(imageViewOne, new Insets(offsetDOWN, 0, 0, offsetRIGHT)); // offset
        BorderPane.setMargin(imageViewTwo, new Insets(offsetDOWN, offsetLEFT, 0, 0));

    }

    public Scene getScene() {
        return scene;
    }

}
