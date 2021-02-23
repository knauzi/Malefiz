package model.game;

import model.utils.Color;

public class Move
{
    private final Figure figure;            // figure to move
    private final Tile oldPositionOfFigure; // location of figure before move gets applied to game
    private final Tile targetOfFigure;      // location of figure after move applied to game
    private final int diceResult;           // result of rolling the dice
    private Tile targetOfBlock;             // if there was a block at the target tile then this is its new location
    private Color beatenColor;              // if there was another agents figure at the target tile then record its color
                                            // to send it back to its base when applying move to game

    public Move(Figure figure, Tile targetOfFigure, int diceResult)
    {
        this.figure = figure;
        this.oldPositionOfFigure = figure.getPosition();
        this.targetOfFigure = targetOfFigure;
        this.diceResult = diceResult;
    }

    public Move(Figure figure, Tile targetOfFigure, int diceResult, Tile targetOfBlock)
    {
        this.figure = figure;
        this.oldPositionOfFigure = figure.getPosition();
        this.targetOfFigure = targetOfFigure;
        this.diceResult = diceResult;
        this.targetOfBlock = targetOfBlock;
    }

    public Move(Figure figure, Tile targetOfFigure, int diceResult, Color beatenColor)
    {
        this.figure = figure;
        this.oldPositionOfFigure = figure.getPosition();
        this.targetOfFigure = targetOfFigure;
        this.diceResult = diceResult;
        this.beatenColor = beatenColor;
    }

    public Color getBeatenColor()
    {
        return beatenColor;
    }

    public Figure getFigure()
    {
        return figure;
    }

    public Tile getTargetOfFigure()
    {
        return targetOfFigure;
    }

    public Tile getTargetOfBlock()
    {
        return targetOfBlock;
    }

    public int getDiceResult()
    {
        return diceResult;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Move other = (Move) o;
        return  this.figure == other.getFigure() &&
                this.diceResult == other.getDiceResult() &&
                this.targetOfFigure == other.targetOfFigure &&
                this.beatenColor == other.getBeatenColor() &&
                this.targetOfBlock == other.getTargetOfBlock();
    }
}
