package model.agents;

import com.google.gson.Gson;
import model.game.*;
import model.utils.Color;
import org.nd4j.linalg.activations.impl.ActivationSigmoid;
import org.nd4j.linalg.activations.impl.ActivationSoftmax;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Neural Network with one hidden layer
 */
public class NeuralNetwork2 extends BaseNetwork{

    /** hyper parameters */
    private final double lambda = 0.7; // td lambda parameter
    private final double alpha = 0.2;  // learning rate alpha

    /** parameters of network */
    public INDArray w2;
    private INDArray w3;
    private INDArray b2;
    private INDArray b3;

    /** dimensions of layers */
    private final int inputSize = 566;
    private final int hiddenSize = 80;
    private final int outputSize = 1;

    /** activation functions */
    private final ActivationSigmoid sigmoid = new ActivationSigmoid();

    /** fresh init */
    public NeuralNetwork2() {
        w2 = Nd4j.randn(hiddenSize, inputSize).mul(0.5);
        w3 = Nd4j.randn(outputSize, hiddenSize).mul(0.5);
        b2 = Nd4j.zeros(hiddenSize);
        b3 = Nd4j.zeros(outputSize);
    }

    /** load parameters from files */
    public NeuralNetwork2(String filePath) {
        Gson gson = new Gson();
        Map<String, String> jsonData;
        try {
            jsonData = gson.fromJson(new FileReader(filePath), HashMap.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        float[][] javaW2 = gson.fromJson(jsonData.get("w2"), float[][].class);
        float[][] javaW3 = gson.fromJson(jsonData.get("w3"), float[][].class);
        float[] javab2 = gson.fromJson(jsonData.get("b2"), float[].class);
        float[] javab3 = gson.fromJson(jsonData.get("b3"), float[].class);

        w2 = Nd4j.create(javaW2);
        w3 = Nd4j.create(javaW3);
        b2 = Nd4j.create(javab2);
        b3 = Nd4j.create(javab3);
    }

    public void save(String filePath) throws IOException {
        float[][] javaW2 = w2.toFloatMatrix();
        float[][] javaW3 = w3.toFloatMatrix();
        float[] javab2 = b2.toFloatVector();
        float[] javab3 = b3.toFloatVector();

        Gson gson = new Gson();
        String jsonW2 = gson.toJson(javaW2);
        String jsonW3 = gson.toJson(javaW3);
        String jsonb2 = gson.toJson(javab2);
        String jsonb3 = gson.toJson(javab3);

        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("w2", jsonW2);
        jsonData.put("w3", jsonW3);
        jsonData.put("b2", jsonb2);
        jsonData.put("b3", jsonb3);

        String data = gson.toJson(jsonData);
        BufferedWriter writer = new BufferedWriter(new FileWriter("nn.json"));
        writer.write(data);
        writer.close();
    }

    public INDArray predict(INDArray gameState) {
        INDArray a2 = sigmoid.getActivation(w2.mmul(gameState).add(b2), false);
        return sigmoid.getActivation(w3.mmul(a2).add(b3), false);
    }

//    public INDArray createStateVector(Game game) {
//        float[] stateVec = new float[inputSize];
//        for (int i = 0; i < inputSize; i++) {
//            stateVec[i] = game.getTileById(i).getState().ordinal();
//        }
//        return Nd4j.create(stateVec);
//    }

    public INDArray createStateVector(Game game) {
        float[] stateVec = new float[566];
        stateVec[game.getActiveAgent()] = 1.0f;
        for(Agent agent : game.getAgents()) {
            if (agent != null){
                for(Figure figure : agent.getFigures()) {
                    int figPos = figure.getPosition().getId();
                    int agentColor = agent.getColor().ordinal();
                    if(figPos < 112) {
                        stateVec[4 + (agentColor * 117) + figPos] = 1.0f;
                    } else {
                        stateVec[4 + (agentColor * 117) + 112 + (figPos - Board.BASE_POINTERS[agentColor])] = 1.0f;
                    }
                }
            }
        }
        Board board = game.getBoard();
        for (int i = 472; i < 566; i++) {
            if(board.getTileById(i-472+17).getState() == Tile.State.BLOCKED) {
                stateVec[i] = 1.0f;
            }
        }
        return Nd4j.create(stateVec);
    }

    @Override
    public double calcUtility(Game game, Agent agent) {
        return predict(createStateVector(game)).getDouble();
    }

    public void train(int numIterations) {

//        Instant startTime, endTime;
//        long timeElapsed;

        // bunch of variables needed for saving intermediate results
        INDArray z2, a2, z3, a3, error2, error3, dw2, dw3, db2, db3;
        double delta;

        for(int i = 0; i < numIterations; i++) {
//            System.out.println("Iteration: " + (i+1));
//            startTime = Instant.now();

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
            agents[indices[2]] = null; // new SimpleAI(Color.getColorById(indices[2]), game.getBoard());
            agents[indices[3]] = null; // new SimpleAI(Color.getColorById(indices[3]), game.getBoard());
            game.setAgents(agents);
            game.initActiveAgent();

            INDArray currState = createStateVector(game);

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
                INDArray nextState = createStateVector(game);

                // prediction of the next state (prediction of current state is a3)
//                INDArray currStatePred = predict(currState);
                INDArray nextStatePred = predict(nextState);

                // forward pass and save intermediate results
                z2 = w2.mmul(currState).add(b2);
                a2 = sigmoid.getActivation(z2, true);
                z3 = w3.mmul(a2).add(b3);
                a3 = sigmoid.getActivation(z3, true);

                // backpropagation error
                if(game.isOver()) {
                    error3 = a3.sub(1.0f);
                    delta = (Nd4j.scalar(1.0).sub(a3).sumNumber()).doubleValue();
                } else {
                    error3 = a3.sub(nextStatePred);
                    delta = (nextStatePred.sub(a3)).sumNumber().doubleValue();
                }
                error2 = (a2.mul(Nd4j.ones(a2.shape()).sub(a2))).mul((w3.transpose()).mmul(error3));

                System.out.println(Arrays.toString(w3.shape()));
                System.out.println(Arrays.toString(w3.transpose().shape()));
                System.out.println(Arrays.toString(a2.shape()));
                System.out.println(Arrays.toString(error3.shape()));
                System.out.println(Arrays.toString(error2.shape()));
                System.out.println(Arrays.toString((w3.transpose().mmul(error3)).shape()));
                System.out.println(Arrays.toString((a2.mul(Nd4j.ones(a2.shape()).sub(a2))).shape()));

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

//            endTime = Instant.now();
//            timeElapsed = Duration.between(startTime, endTime).toSeconds();
//            System.out.println("Game over after " + timeElapsed + " s");
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
