package model.agents;

import model.game.Board;
import model.game.Game;
import model.game.Move;
import model.utils.Color;

public class TDLAgent extends ArtificialAgent{

    public TDLAgent(Color color, Board board) {
        super(color, board);
    }

    @Override
    public Move getNextMove(Game game, int diceResult) {
        return null;
    }
}
