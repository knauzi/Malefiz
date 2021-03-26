package view.gui;

import controller.gui.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.game.Game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameView implements PropertyChangeListener, View
{
    private Scene scene;
    private final Stage stage;

    /* model */
    private Game game;

    private Controller controller;

    /* gui representation of the game board */
    private GameBoard gameBoard;

    private Label diceResultLabel;

    public GameView(Game game, Controller controller)
    {
        this.game = game;
        this.controller = controller;
        this.stage = controller.getStage();
        buildGameScene(game);
    }

    public void buildGameScene(Game game)
    {
        /* game board */

        gameBoard = new GameBoard(game, controller);
        gameBoard.addEventHandler(MouseEvent.MOUSE_CLICKED, controller);

//        gameBoard.setOnMouseMoved(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                String msg = "(x: "       + event.getX()      + ", y: "       + event.getY()       + ")\n" +
//                             "(sceneX: "  + event.getSceneX() + ", sceneY: "  + event.getSceneY()  + ")\n" +
//                             "(screenX: " + event.getScreenX()+ ", screenY: " + event.getScreenY() + ")\n";
//
//                label.setText(msg);
//            }
//        });
//
//        Image redFig = new Image("file:resources/images/token_red.png", 20, 25, false, false);
//        ImageView redFigView = new ImageView(redFig);
//        redFigView.setLayoutX(33 - redFig.getWidth() / 2);
//        redFigView.setLayoutY(66 - redFig.getHeight() / 2);
//        gameBoard.getChildren().add(redFigView);

        /* side bar */

        VBox sideBar = new VBox();
        sideBar.setMinSize(150, 600);
        sideBar.setSpacing(10);
        sideBar.setAlignment(Pos.CENTER);
        sideBar.setStyle("-fx-padding: 2;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 4 4 4 0;" +
                "-fx-border-color: black;" +
                "-fx-background-color: #A9A9A9;");

        Button rollButton = new Button();
        rollButton.setGraphic(new ImageView(new Image("file:resources/images/dice.jpg", 50, 50, false, false)));
        //rollButton.setPrefSize(50, 50);
        rollButton.setId("roll");
        rollButton.addEventHandler(ActionEvent.ACTION, controller);
        sideBar.getChildren().add(rollButton);

        diceResultLabel = new Label("");
        diceResultLabel.setStyle("-fx-padding: 2;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 1;" +
                "-fx-border-color: black;" +
                "-fx-font-weight: bold");
        diceResultLabel.setFont(new Font("Arial", 24));
        diceResultLabel.setMinSize(30, 30);
        diceResultLabel.setAlignment(Pos.CENTER);
        sideBar.getChildren().add(diceResultLabel);


        /* main container */

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.getChildren().addAll(gameBoard, sideBar);

        scene = new Scene(hb, 750, 600);
    }

    @Override
    public void update()
    {
        Platform.runLater(() -> {
            gameBoard.redraw(game, controller);
        });

        // TODO side bar
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if(event.getPropertyName().equals("game"))
        {
            Platform.runLater(() -> {
                this.game = (Game) event.getNewValue();
                gameBoard.redraw(game, controller);
                setDiceResult(game.getDiceResult());
                if(game.isOver())
                {
                    game.setPhase(Game.Phase.GAME_OVER);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Spieler " + game.getActiveAgent() + " hat gewonnen!\n\nSpiel neustarten?");
                    alert.showAndWait()
                            .filter(response -> response == ButtonType.OK)
                            .ifPresent(response -> game.resetGame());
                }
            });
        }
    }

    public Stage getStage()
    {
        return stage;
    }

    public Scene getScene()
    {
        return scene;
    }

    public GameBoard getGameBoard()
    {
        return gameBoard;
    }

    public void setDiceResult(int diceResult)
    {
        this.diceResultLabel.setText(String.valueOf(diceResult));
    }
}
