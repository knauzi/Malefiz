package tdl_stuff.tdl;

import model.agents.Agent;
import model.agents.ArtificialAgent;
import model.agents.RandomAI;
import model.agents.SimpleAI;
import model.game.Game;
import model.game.GameLogic;
import model.game.Move;
import model.utils.Color;
import tdl_stuff.net.HiddenUnit;
import tdl_stuff.net.NeuralNetwork;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Learning {

    private final NeuralNetwork nn;
    private final double LAMBDA;
    private final double ALPHA;
    private final double BETA;
    private final double[][] Ew;
    private final double[][][] Ev;

    public Learning(NeuralNetwork nn, double lambda, double alpha) {
        this.nn = nn;
        Ew = new double[this.nn.hidden[0].length][this.nn.hidden[1].length];
        Ev = new double[this.nn.input.length][this.nn.hidden[0].length][this.nn.hidden[1].length];
        LAMBDA = lambda;
        ALPHA = alpha*0.8;
        BETA = alpha*0.2;
    }

    public void train(int numIter) {

        System.out.println("Start training ...");

        Instant startTime, endTime;
        long timeElapsed;

        startTime = Instant.now();

        for(int i = 1; i <= numIter; i++) {

            if(i % 100 == 0) {
                System.out.print("|");
            }

//            System.out.print("|");
//            if(i % 10 == 0) {
//                System.out.print(" ");
//            }
//            System.out.println("Iteration: " + (i));
//            startTime = Instant.now();

            if(i % 1000 == 0) {
                System.out.println();
                System.out.println("Iteration: " + (i));
                endTime = Instant.now();
                timeElapsed = Duration.between(startTime, endTime).toMinutes();
                System.out.println("Time passed (total): " + timeElapsed + " min");
                playTestGames();
                try {
                    nn.writeTo("src/tdl_stuff/models/SavedNN_2P_TDL_vs_Simple");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

//            Game game = initGame();
            Game game = initGame2P();
            double[] currState = Utils.createStateVector(game);

            while (!game.isOver()) {

                double[] currStateOutput = nn.getValue(currState);

                Move nextMove = getNextMove(game);
                if (nextMove == null) {
                    game.advanceToNextAgentLearning();
                    continue;
                }
                GameLogic.makeMoveOnGame(game, nextMove);
                double[]nextState = Utils.createStateVector(game);

//                Instant startTime2, endTime2;
//                long timeElapsed2;
//                startTime2 = Instant.now();

                if(game.isOver()) {
                    double[] z = new double[nn.hidden[nn.hidden.length-1].length];
                    z[game.getActiveAgent()] = 1.0;
                    backprop(currState, currStateOutput, z);
                }else {
                    double[] nextStateOutput = nn.getValue(nextState);
                    backprop(currState, currStateOutput, nextStateOutput);
                }

//                endTime2 = Instant.now();
//                timeElapsed2 = Duration.between(startTime2, endTime2).toMillis();
//                System.out.println("Time for backprop: " + timeElapsed2 + " ms");

                currState = nextState.clone();
                game.advanceToNextAgentLearning();
            }

//            endTime = Instant.now();
//            timeElapsed = Duration.between(startTime, endTime).toSeconds();
//            System.out.println("Game over after " + timeElapsed + " s");
//            System.out.println(game.getBoard());
//            System.out.println();
        }

    }

    private void playTestGames() {
        int gamesWonByTDLAgent = 0;
        for(int i = 0; i < 100; i++) {
            Game game = initGameTest();
            while (!game.isOver()) {
                Move nextMove = getNextMove(game);
                if (nextMove == null) {
                    game.advanceToNextAgentLearning();
                    continue;
                }
                GameLogic.makeMoveOnGame(game, nextMove);
                if(!game.isOver()) {
                    game.advanceToNextAgentLearning();
                }
            }
            if (game.getAgent(game.getActiveAgent()) instanceof TDLAgent) {
                gamesWonByTDLAgent++;
            }
        }
//        System.out.println("Win rate of TDL-Agent against 3 SimpleAI's: " + (gamesWonByTDLAgent / 100.0));
        System.out.println("Win rate of TDL-Agent against SimpleAI: " + (gamesWonByTDLAgent / 100.0));
    }

    private Move getNextMove(Game game) {
        game.rollDice();
        int diceResult = game.getDiceResult();
        return ((ArtificialAgent) game.getAgent(game.getActiveAgent())).getNextMove(game, diceResult);
    }

    private Game initGame() {
        Game game = new Game();
        Agent[] agents = new Agent[4];
        int[] indices = new int[] {0, 1, 2, 3};
        shuffleArray(indices);

        agents[indices[0]] = new TDLAgent(Color.getColorById(indices[0]), game.getBoard(), nn);
        agents[indices[1]] = new TDLAgent(Color.getColorById(indices[1]), game.getBoard(), nn);
//        agents[indices[2]] = new TDLAgent(Color.getColorById(indices[2]), game.getBoard(), nn);
//        agents[indices[3]] = new TDLAgent(Color.getColorById(indices[3]), game.getBoard(), nn);
        agents[indices[2]] = new SimpleAI(Color.getColorById(indices[2]), game.getBoard());
        agents[indices[3]] = new SimpleAI(Color.getColorById(indices[3]), game.getBoard());

        game.setAgents(agents);
        game.initActiveAgent();

        return game;
    }

    private Game initGame2P() {
        Game game = new Game();
        Agent[] agents = new Agent[4];
        int[] indices = new int[] {0, 1, 2, 3};
        shuffleArray(indices);

        agents[indices[0]] = new TDLAgent(Color.getColorById(indices[0]), game.getBoard(), nn);
        agents[indices[1]] = new SimpleAI(Color.getColorById(indices[1]), game.getBoard());
        agents[indices[2]] = null;
        agents[indices[3]] = null;

        game.setAgents(agents);
        game.initActiveAgent();

        return game;
    }

    private Game initGameTest() {
        Game game = new Game();
        Agent[] agents = new Agent[4];
        int[] indices = new int[] {0, 1, 2, 3};
        shuffleArray(indices);

        agents[indices[0]] = new TDLAgent(Color.getColorById(indices[0]), game.getBoard(), nn);
        agents[indices[1]] = new SimpleAI(Color.getColorById(indices[1]), game.getBoard());
//        agents[indices[2]] = new SimpleAI(Color.getColorById(indices[2]), game.getBoard());
//        agents[indices[3]] = new SimpleAI(Color.getColorById(indices[3]), game.getBoard());
        agents[indices[2]] = null;
        agents[indices[3]] = null;

        game.setAgents(agents);
        game.initActiveAgent();

        return game;
    }

    private static double gradient(HiddenUnit hiddenUnit) {
        return hiddenUnit.getValue() * (1.0 - hiddenUnit.getValue());
    }

    /* Ew and Ev must be set up somewhere to the proper size and set to 0 */
    private void backprop(double[] in, double[] out, double[] expected) {
        /* compute eligibility traces */
        for (int j = 0; j < nn.hidden[0].length; j++) {
            for (int k = 0; k < out.length; k++) {
                /* ew[j][k] = (lambda * ew[j][k]) + (gradient(k)*hidden_j) */
                Ew[j][k] = (LAMBDA * Ew[j][k]) + (gradient(nn.hidden[1][k]) * nn.hidden[0][j].getValue());
                for (int i = 0; i < in.length; i++)
                    /* ev[i][j][k] = (lambda * ev[i][j][k]) + (gradient(k)+w[j][k]+gradient(j)+input_i)*/
                    Ev[i][j][k] = ( ( LAMBDA * Ev[i][j][k] ) + ( gradient(nn.hidden[1][k]) * nn.hidden[1][k].weights[j] * gradient(nn.hidden[0][j])* in[i]));
            }
        }
        double[] error = new double[out.length];
        for (int k =0; k < out.length; k++) {
            error[k] = expected[k] - out[k];
        }
        for (int j = 0; j < nn.hidden[0].length; j++) {
            for (int k = 0; k < out.length; k++) {
                /* weight from j to k, shown with learning param of BETA */
                nn.hidden[1][k].weights[j] += BETA * error[k] * Ew[j][k];
                for (int i = 0; i < in.length; i ++) {
                    nn.hidden[0][j].weights[i] += ALPHA * error[k] * Ev[i][j][k];
                }
            }
        }
    }

    // Implementing Fisher–Yates shuffle
    private static void shuffleArray(int[] ar) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

}
