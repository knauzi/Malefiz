package tdl_stuff.tdl.FourPlayer;

import model.agents.ArtificialAgent;
import model.game.Board;
import model.game.Game;
import model.game.GameLogic;
import model.game.Move;
import model.utils.Color;
import tdl_stuff.net.NeuralNetwork;

import java.util.List;

public class TDLAgent4P extends ArtificialAgent {

    private final NeuralNetwork neuralNetwork;

    public TDLAgent4P(Color color, Board board, NeuralNetwork neuralNetwork) {
        super(color, board);
        this.neuralNetwork = neuralNetwork;
    }

    @Override
    public Move getNextMove(Game game, int diceResult) {
        List<Move> possibleMoves = GameLogic.getPossibleMovesOfAgent(game.getBoard(), this, diceResult);
        return getMaxUtilityMove(game, possibleMoves);
    }

    private Move getMaxUtilityMove(Game game, List<Move> possibleMoves) {
        double maxUtility = Integer.MIN_VALUE;
        Move nextMove = null;
        for (Move move : possibleMoves) {
            GameLogic.makeMoveOnGame(game, move);
            double utility = Utils4P.calcUtility(neuralNetwork.getValue(Utils4P.createStateVector(game)), this);
            if (utility > maxUtility) {
                maxUtility = utility;
                nextMove = move;
            }
            GameLogic.undoMoveOnGame(game, move);
        }
        return nextMove;
    }

}
