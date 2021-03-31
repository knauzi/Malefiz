package tdl_stuff.tdl;

import model.agents.Agent;
import model.game.Board;
import model.game.Figure;
import model.game.Game;
import model.game.Tile;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import tdl_stuff.net.NeuralNetwork;

public class Utils {

    public static double[] createStateVector(Game game) {
        double[] stateVec = new double[566];
        stateVec[game.getActiveAgent()] = 1.0;
        for (Agent agent : game.getAgents()) {
            if (agent != null) {
                for (Figure figure : agent.getFigures()) {
                    int figPos = figure.getPosition().getId();
                    int agentColor = agent.getColor().ordinal();
                    if (figPos < 112) {
                        stateVec[4 + (agentColor * 117) + figPos] = 1.0;
                    } else {
                        stateVec[4 + (agentColor * 117) + 112 + (figPos - Board.BASE_POINTERS[agentColor])] = 1.0;
                    }
                }
            }
        }
        Board board = game.getBoard();
        for (int i = 472; i < 566; i++) {
            if (board.getTileById(i - 472 + 17).getState() == Tile.State.BLOCKED) {
                stateVec[i] = 1.0;
            }
        }
        return stateVec;
    }

    public static double calcUtility(double[] output, Agent agent) {
        norm(output);
        double agentWinProp = output[agent.getColor().ordinal()];
        return 2.0 * agentWinProp - 1.0;
        // return agentWinProp;
    }

    private static double sum(double...values) {
        double result = 0;
        for (double value:values)
            result += value;
        return result;
    }

    private static void norm(double[] vector) {
        double sum = sum(vector);
        for(int i = 0; i < vector.length; i++) {
            vector[i] /= sum;
        }
    }

}
