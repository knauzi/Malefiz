package model.agents;

import model.agents.ArtificialAgent;
import model.game.Board;
import model.game.Game;
import model.game.GameLogic;
import model.game.Move;
import model.utils.Color;

import java.util.List;

public class HeuristicAgent extends ArtificialAgent {

    private final int[] parameters;

    public HeuristicAgent(Color color, Board board, int[] parameters) {
        super(color, board);
        this.parameters = parameters.clone();
    }

    @Override
    public Move getNextMove(Game game, int diceResult) {
        List<Move> possibleMoves = GameLogic.getPossibleMovesOfAgent(game.getBoard(), this, diceResult);
        return getMaxUtilityMove(game, possibleMoves);
    }

    private Move getMaxUtilityMove(Game game, List<Move> possibleMoves) {
        return null;
    }
}
