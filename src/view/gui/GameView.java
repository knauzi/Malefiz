package view.gui;

import controller.gui.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.game.Game;

public class GameView implements View
{
    private Scene scene;
    private final Stage stage;

    /* model */
    private Game game;

    /* gui representation of the game board */
    private GameBoard gameBoard;

    private Label diceResultLabel;

    public GameView(Game game, Controller controller)
    {
        this.game = game;
        this.stage = controller.getStage();
        buildGameScene(game, controller);
    }

    public void buildGameScene(Game game, Controller controller)
    {
        Label label = new Label("Side bar");

        /* game board */

        gameBoard = new GameBoard(game, controller);
        gameBoard.addEventHandler(MouseEvent.MOUSE_CLICKED, controller);
        gameBoard.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String msg = "(x: "       + event.getX()      + ", y: "       + event.getY()       + ")\n" +
                             "(sceneX: "  + event.getSceneX() + ", sceneY: "  + event.getSceneY()  + ")\n" +
                             "(screenX: " + event.getScreenX()+ ", screenY: " + event.getScreenY() + ")\n";

                label.setText(msg);
            }
        });

        Image redFig = new Image("file:resources/images/token_red.png", 20, 25, false, false);
        ImageView redFigView = new ImageView(redFig);
        redFigView.setLayoutX(33 - redFig.getWidth() / 2);
        redFigView.setLayoutY(66 - redFig.getHeight() / 2);
        gameBoard.getChildren().add(redFigView);

        Circle circle = new Circle(100, 66, 14);
        // circle.setFill(Color.TRANSPARENT);
        circle.setFill(Color.web("#ffe338", 0.4));
        circle.setStroke(Color.web("#ffe338", 1.0));
        circle.setStrokeWidth(3);
        gameBoard.getChildren().add(circle);

        Circle block = new Circle(100, 66, 12);
        // circle.setFill(Color.TRANSPARENT);
        block.setFill(Color.GRAY);
        gameBoard.getChildren().add(block);

        Rectangle rect = new Rectangle(133.5 - 8, 66 - 8, 16, 16);
        rect.setFill(Color.RED);
        rect.setStroke(Color.WHITE);
        rect.setStrokeWidth(2);
        gameBoard.getChildren().add(rect);

        /* side bar */

        VBox sideBar = new VBox();
        sideBar.setMinSize(300, 600);
        sideBar.setAlignment(Pos.CENTER);
        sideBar.setStyle("-fx-padding: 2;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 4 4 4 0;" +
                "-fx-border-color: black;" +
                "-fx-background-color: #A9A9A9;");

        Button rollButton = new Button("Würfeln");
        rollButton.addEventHandler(ActionEvent.ACTION, controller);
        sideBar.getChildren().add(rollButton);

        diceResultLabel = new Label("");
        sideBar.getChildren().add(diceResultLabel);

        sideBar.getChildren().addAll(label);

        /* main container */

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.getChildren().addAll(gameBoard, sideBar);

        scene = new Scene(hb, 900, 600);
    }

    @Override
    public void update(Controller controller)
    {
        gameBoard.redraw(game, controller);
        // TODO side bar
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
