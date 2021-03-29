package model.agents;

import model.game.Game;
import org.nd4j.linalg.api.ndarray.INDArray;

public abstract class BaseNetwork {

    public abstract INDArray predict(INDArray gameState);

    public abstract INDArray createStateVector(Game game);

    public abstract double calcUtility(Game game, Agent agent);
}
