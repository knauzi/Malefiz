package view.gui;

import controller.gui.Controller;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.agents.Agent;
import model.game.Figure;
import model.game.Game;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import model.game.Tile;
import view.gui.utils.Highlight;
import view.gui.utils.Point;
import view.gui.utils.PositionMap;

import java.util.ArrayList;

public class GameBoard extends Pane
{
    private final PositionMap positionMap;

    public static final int FIGURE_SIZE = 16;
    public static final int HIGHLIGHT_SIZE = 14;

    private ArrayList<Tile> highlightTiles;

    public GameBoard(Game game, Controller controller)
    {
        super();
        positionMap = new PositionMap(game.getBoard());
        redraw(game, controller);
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

    private void drawComponents(Game game, Controller controller)
    {
        // figures
        for (Agent agent : game.getAgents())
        {
            if(agent != null)
            {
                for (Figure figure : agent.getFigures())
                {
                    Point position = positionMap.getPoint(figure.getPosition().getId());
                    FigureGUI figureGUI = new FigureGUI(figure, position.getX() - FIGURE_SIZE / 2,
                                                   position.getY() - FIGURE_SIZE / 2, FIGURE_SIZE, FIGURE_SIZE);
                    figureGUI.setStrokeWidth(2);

                    switch (figure.getColor())
                    {
                        case RED    -> figureGUI.setFill(Color.RED);
                        case GREEN  -> figureGUI.setFill(Color.GREEN);
                        case YELLOW -> figureGUI.setFill(Color.YELLOW);
                        case BLUE   -> figureGUI.setFill(Color.BLUE);
                        default     -> figureGUI.setFill(Color.WHITE);
                    }

                    if (agent.getColor().ordinal() == game.getActiveAgent())
                    {
                        figureGUI.setStroke(Color.ORANGE);
                    }
                    else
                    {
                        figureGUI.setStroke(Color.WHITE);
                    }

                    figureGUI.addEventHandler(MouseEvent.MOUSE_CLICKED, controller);
                    getChildren().add(figureGUI);
                }
            }
        }

        // blocks
        // TODO

        // highlighted tiles
        if (highlightTiles != null)
        {
            for (Tile tile : highlightTiles)
            {
                Point position = positionMap.getPoint(tile.getId());

                Highlight highlight = new Highlight(tile, position.getX(), position.getY(), HIGHLIGHT_SIZE);
                highlight.setFill(Color.web("#ffe338", 0.4));
                highlight.setStroke(Color.web("#ffe338", 1.0));
                highlight.setStrokeWidth(3);

                highlight.addEventHandler(MouseEvent.MOUSE_CLICKED, controller);
                getChildren().add(highlight);
            }
        }

    }

    public void setHighlightTiles(ArrayList<Tile> highlightTiles)
    {
        this.highlightTiles = highlightTiles;
    }

    public void redraw(Game game, Controller controller)
    {
        getChildren().clear();
        initGameBoard();
        drawComponents(game, controller);
    }
}
