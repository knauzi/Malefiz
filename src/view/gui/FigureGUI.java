package view.gui;

import javafx.scene.shape.Rectangle;
import model.game.Figure;

public class FigureGUI extends Rectangle
{
    private final Figure figure;

    public FigureGUI(Figure figure, double x, double y, double width, double height)
    {
        super(x, y, width, height);
        this.figure = figure;
    }

    public Figure getFigure()
    {
        return figure;
    }
}
