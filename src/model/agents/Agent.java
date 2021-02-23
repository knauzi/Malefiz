package model.agents;

import model.game.Board;
import model.game.Figure;
import model.utils.Color;

public abstract class Agent
{
    private final Color color;

    private final Figure[] figures;

    public Agent(Color color, Board board)
    {
        this.color = color;
        // init figures (position in base)
        figures = new Figure[5];
        figures[0] = new Figure(color, board.getTileById(Board.BASE_POINTERS[color.ordinal()]));
        figures[1] = new Figure(color, board.getTileById(Board.BASE_POINTERS[color.ordinal()] + 1));
        figures[2] = new Figure(color, board.getTileById(Board.BASE_POINTERS[color.ordinal()] + 2));
        figures[3] = new Figure(color, board.getTileById(Board.BASE_POINTERS[color.ordinal()] + 3));
        figures[4] = new Figure(color, board.getTileById(Board.BASE_POINTERS[color.ordinal()] + 4));
    }

    public Color getColor()
    {
        return color;
    }

    public Figure[] getFigures()
    {
        return figures;
    }

    public Figure getFigure(int i)
    {
        return figures[i];
    }
}
