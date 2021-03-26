package model.agents;

import model.game.*;
import model.utils.Color;
import org.nd4j.linalg.activations.impl.ActivationSigmoid;
import org.nd4j.linalg.activations.impl.ActivationSoftmax;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Neural Network with one hidden layer
 */
public class NeuralNetwork {

    /** hyper parameters */
    private final double lambda = 0.7; // td lambda parameter
    private final double alpha = 0.1;  // learning rate alpha

    /** parameters of network */
    private INDArray w2;
    private INDArray w3;
    private INDArray b2;
    private INDArray b3;

    /** dimensions of layers */
    private final int inputSize = 132;
    private final int hiddenSize = 20;
    private final int outputSize = 4;

    /** activation functions */
    private final ActivationSigmoid sigmoid = new ActivationSigmoid();
    private final ActivationSoftmax softmax = new ActivationSoftmax();

    /** fresh init */
    public NeuralNetwork() {
        w2 = Nd4j.randn(hiddenSize, inputSize).mul(0.5);
        w3 = Nd4j.randn(outputSize, hiddenSize).mul(0.5);
        b2 = Nd4j.zeros(hiddenSize);
        b3 = Nd4j.zeros(outputSize);
    }

    /** load parameters from files */
    public NeuralNetwork(File w2File, File w3File, File b2File, File b3File) {
        double[][] javaW2 = w2.toDoubleMatrix();
        double[][] javaW3 = w3.toDoubleMatrix();
        double[] javab2 = b2.toDoubleVector();
        double[] javab3 = b2.toDoubleVector();
    }

    public INDArray predict(INDArray gameState) {
        INDArray a2 = sigmoid.getActivation(w2.mmul(gameState).add(b2), false);
        return softmax.getActivation(w3.mmul(a2).add(b3), false);
    }

    public INDArray createStateVector(Board board) {
        float[] stateVec = new float[inputSize];
        for (int i = 0; i < inputSize; i++) {
            stateVec[i] = board.getTileById(i).getState().ordinal();
        }
        return Nd4j.create(stateVec);
    }

    public void train(int numIterations) {

        Instant startTime, endTime;
        long timeElapsed;

        // bunch of variables needed for saving intermediate results
        INDArray z2, a2, z3, a3, error2, error3, dw2, dw3, db2, db3;
        double delta;

        for(int i = 0; i < numIterations; i++) {
            System.out.println("Iteration: " + (i+1));
            startTime = Instant.now();

            INDArray ew2 = Nd4j.zeros(hiddenSize, inputSize);
            INDArray ew3 = Nd4j.zeros(outputSize, hiddenSize);
            INDArray eb2 = Nd4j.zeros(hiddenSize);
            INDArray eb3 = Nd4j.zeros(outputSize);

            // play with 2 tdl agents and 2 simple ai agents
            Game game = new Game();
            Agent[] agents = new Agent[4];
            int[] indices = new int[] {0, 1, 2, 3};
            shuffleArray(indices);
            agents[indices[0]] = new TDLAgent(Color.getColorById(indices[0]), game.getBoard(), this);
            agents[indices[1]] = new TDLAgent(Color.getColorById(indices[1]), game.getBoard(), this);
            agents[indices[2]] = new SimpleAI(Color.getColorById(indices[2]), game.getBoard());
            agents[indices[3]] = new SimpleAI(Color.getColorById(indices[3]), game.getBoard());
            game.setAgents(agents);
            game.initActiveAgent();

            INDArray currState = createStateVector(game.getBoard());

            int counter = 1;
            while (!game.isOver()) {
//                if(counter++ % 100 == 0){
//                    System.out.println(game.getBoard());
//                }

                game.rollDice();
                int diceResult = game.getDiceResult();
                Move nextMove = ((ArtificialAgent) game.getAgent(game.getActiveAgent())).getNextMove(game, diceResult);
                if (nextMove == null) {
                    game.advanceToNextAgentLearning();
                    continue;
                }
                GameLogic.makeMoveOnGame(game, nextMove);
                INDArray nextState = createStateVector(game.getBoard());

                // prediction of the two consecutive states
                INDArray currStatePred = predict(currState);
                INDArray nextStatePred = predict(nextState);

                // forward pass and save intermediate results
                z2 = w2.mmul(currState).add(b2);
                a2 = sigmoid.getActivation(z2, true);
                z3 = w3.mmul(a2).add(b3);
                a3 = softmax.getActivation(z3, true);

                // backpropagation error
                if(game.isOver()) {
                    INDArray z = Nd4j.zeros(outputSize);
                    z.put(game.getActiveAgent(), Nd4j.scalar(1));
                    error3 = a3.sub(z);
                    delta = z.sub(currStatePred).sumNumber().doubleValue();
                } else {
                    error3 = a3.sub(nextStatePred);
                    delta = (nextStatePred.sub(currStatePred)).sumNumber().doubleValue();
                }
                error2 = a2.mul(Nd4j.ones(a2.shape()).sub(a2));

                // derivatives
                dw2 = error2.mmul(currState);
                dw3 = error3.mmul(a2);
                db2 = error2;
                db3 = error3;

                // eligibility traces
                ew2 = ew2.mul(lambda).add(dw2);
                ew3 = ew3.mul(lambda).add(dw3);
                eb2 = eb2.mul(lambda).add(db2);
                eb3 = eb3.mul(lambda).add(db3);

                // parameter update
                w2 = w2.add(ew2.mul(delta * alpha));
                w3 = w3.add(ew3.mul(delta * alpha));
                b2 = b2.add(eb2.mul(delta * alpha));
                b3 = b3.add(eb3.mul(delta * alpha));

                currState = nextState;
                game.advanceToNextAgentLearning();
            }

            endTime = Instant.now();
            timeElapsed = Duration.between(startTime, endTime).toSeconds();
            System.out.println("Game over after " + timeElapsed + " s");
        }
    }

    // Implementing Fisher–Yates shuffle
    static void shuffleArray(int[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
