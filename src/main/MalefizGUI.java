package main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import model.game.Board;
import view.gui.SceneHandler;

public class MalefizGUI extends Application {

    private Parent createContent()
    {
        return new StackPane(new Text("Hello World"));
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        SceneHandler sceneHandler = new SceneHandler(stage);
        Scene startingScene = sceneHandler.getStartingScene();
        stage.setScene(startingScene);
        stage.setTitle("Malefiz");
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);

        Board board = new Board();

        /*
        System.out.println(board);
        System.out.println();
        */

        /*
        Figure figure = new Figure(Color.RED, 44);
        for (int i = 1; i < 7; i++)
        {
            Tile[] targetTiles = figure.getPossibleTargetTiles(board, i);

            System.out.println("Target tiles for dice result " + i);
            for (Tile tile : targetTiles)
            {
                System.out.println(tile);
            }
            System.out.println("#################################");
        }
        */
    }
}
