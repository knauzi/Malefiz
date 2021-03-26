package model.agents;

import model.game.Board;
import model.game.Game;
import model.game.GameLogic;
import model.game.Move;
import model.utils.Color;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;

import java.util.List;

public class TDLAgent extends ArtificialAgent{

    private final NeuralNetwork evaluationNetwork;

    public TDLAgent(Color color, Board board, NeuralNetwork evaluationNetwork) {
        super(color, board);
        this.evaluationNetwork = evaluationNetwork;
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
            INDArray p = evaluationNetwork.predict(evaluationNetwork.createStateVector(game.getBoard()));
            double utility = calcUtility(p);
            if (utility > maxUtility) {
                maxUtility = utility;
                nextMove = move;
            }
            GameLogic.undoMoveOnGame(game, move);
        }
        return nextMove;
    }

    private double calcUtility(INDArray p) {
        double agentWinProp = p.get(NDArrayIndex.indices(getColor().ordinal())).getDouble();
        //double sumOfRest = p.sumNumber().doubleValue() - agentWinProp;
        //return agentWinProp - sumOfRest;
        return agentWinProp;
    }
}
