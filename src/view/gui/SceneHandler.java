package view.gui;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SceneHandler
{
    /* stage containing all the scenes */
    private final Stage stage;

    /* all scenes of the Malefiz application */
    private Scene startingScene;
    private Scene gameScene;

    /* for saving selections from starting scene */
    private static final int NUM_PLAYERS = 4;
    private final TextField[] names = new TextField[NUM_PLAYERS];
    private final ChoiceBox[] types = new ChoiceBox[NUM_PLAYERS];

    public SceneHandler(Stage stage)
    {
        this.stage = stage;
        stage.setResizable(false);

        buildStartingScene();
        buildGameScene();
    }

    public void buildStartingScene()
    {
        final HBox[] hbs = new HBox[NUM_PLAYERS];
        final String[] allTypes = new String[]{ "-", "Mensch", "KI" };

        // column labels
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.BASELINE_RIGHT);
        Label typeCol = new Label("Typ");
        Label nameCol = new Label("Name\t\t\t\t\t    ");
        topRow.setSpacing(107);
        topRow.getChildren().addAll(typeCol, nameCol);

        // forms for all player (specify type and name)
        for (int i = 0; i < NUM_PLAYERS; i++)
        {
            final Label label = new Label("Spieler " + (i + 1) + ": ");
            final ChoiceBox cb = types[i] = new ChoiceBox();
            cb.getItems().addAll(allTypes);
            cb.getSelectionModel().selectFirst();
            TextField name = names[i] =  new TextField();
            HBox hb = hbs[i] = new HBox();
            hb.setSpacing(10);
            hb.setAlignment(Pos.CENTER);
            hb.getChildren().addAll(label, cb, name);
        }

        // start button
        final Button startButton = new Button("Starte Spiel");
        startButton.setOnAction(this::startGame);

        // overall layout of the scene
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        layout.getChildren().add(topRow);
        layout.getChildren().addAll(hbs);
        layout.getChildren().add(startButton);

        startingScene = new Scene(layout, 400, 250);
    }

    public void buildGameScene()
    {
        /* main container */

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_LEFT);

        /* game board */

        Pane gameBoard = new Pane();
        gameBoard.setMinSize(600, 600);
        gameBoard.setStyle("-fx-padding: 2;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 4;" +
                "-fx-border-color: black;");

        Image imgBackground = new Image("file:resources/images/board.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(
                imgBackground,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background background = new Background(backgroundImage);
        gameBoard.setBackground(background);

        /* side bar */

        VBox sideBar = new VBox();
        sideBar.setMinSize(150, 600);
        sideBar.setAlignment(Pos.CENTER);
        sideBar.setStyle("-fx-padding: 2;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 4 4 4 0;" +
                "-fx-border-color: black;" +
                "-fx-background-color: #A9A9A9;");

        Label label = new Label("Side bar");
        sideBar.getChildren().addAll(label);

        hb.getChildren().addAll(gameBoard, sideBar);
        gameScene = new Scene(hb, 750, 600);
    }

    private void startGame(Event e)
    {
        System.out.println("Spiel wird gestartet...");

        // retrieve and save selection from forms

        // change to game scene
        stage.setScene(gameScene);
        stage.centerOnScreen();
    }

    public Scene getStartingScene()
    {
        return startingScene;
    }

    public Scene getGameScene()
    {
        return gameScene;
    }
}
