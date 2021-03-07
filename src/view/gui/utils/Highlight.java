package view.gui.utils;

import javafx.scene.shape.Circle;
import model.game.Tile;

public class Highlight extends Circle
{
    private final Tile tile;

    public Highlight(Tile tile, double x, double y, double radius)
    {
        super(x, y, radius);
        this.tile = tile;
    }

    public Tile getTile()
    {
        return tile;
    }
}
