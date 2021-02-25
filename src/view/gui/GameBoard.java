package view.gui;

import model.game.Game;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class GameBoard extends Pane
{
    public GameBoard(Game game)
    {
        super();
        redraw(game);
    }

    private void initGameBoard()
    {
        setMinSize(600, 600);
        setStyle("-fx-padding: 2;" +
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
        setBackground(background);
    }

    private void drawComponents(Game game)
    {
        // TODO
    }

    public void redraw(Game game)
    {
        getChildren().clear();
        initGameBoard();
        drawComponents(game);
    }
}
