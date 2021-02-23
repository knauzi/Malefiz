package model.game;

import model.utils.Color;

public class Figure
{
    /* color of the figure */
    private final Color color;

    /* tile where the figure is currently located */
    private Tile position;

    public Figure(Color color, Tile position)
    {
        this.color = color;
        this.position = position;
    }

    public Color getColor()
    {
        return color;
    }

    public Tile getPosition()
    {
        return position;
    }

    public void setPosition(Tile position)
    {
        this.position = position;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Figure other = (Figure) o;
        return  this.color == other.getColor() &&
                this.position == other.getPosition();
    }

}
