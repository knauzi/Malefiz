package view.gui;

import controller.gui.Controller;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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

    public GameView(Game game, Controller controller)
    {
        this.game = game;
        this.stage = controller.getStage();
        buildGameScene(game, controller);
    }

    public void buildGameScene(Game game, Controller controller)
    {
        /* game board */

        gameBoard = new GameBoard(game);

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

        /* main container */

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.getChildren().addAll(gameBoard, sideBar);

        scene = new Scene(hb, 750, 600);
    }

    @Override
    public void update()
    {
        gameBoard.redraw(game);
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
}
