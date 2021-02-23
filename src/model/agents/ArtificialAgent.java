package model.agents;

import model.game.Board;
import model.game.Game;
import model.game.Move;
import model.utils.Color;

public abstract class ArtificialAgent extends Agent
{
    public ArtificialAgent(Color color, Board board)
    {
        super(color, board);
    }

    public abstract Move getNextMove(Game game, int diceResult);
}
