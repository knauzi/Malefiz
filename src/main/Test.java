package main;

import model.agents.*;
import model.game.Board;
import model.game.Game;
import model.game.GameLogic;
import model.game.Move;
import model.utils.Color;
import tdl_stuff.net.NeuralNetwork;
import tdl_stuff.tdl.Learning;
import tdl_stuff.tdl.TDLAgent;
import tdl_stuff.tdl.Utils;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Test {

    public static void main(String[] args) {

//        NeuralNetwork nn = new NeuralNetwork();
//        NeuralNetwork2 nn = new NeuralNetwork2();

//        Game game = new Game();
//        Agent[] agents = new Agent[4];
//        int[] indices = new int[] {0, 1, 2, 3};
//        agents[indices[0]] = new TDLAgent(Color.getColorById(indices[0]), game.getBoard(), nn);
//        agents[indices[1]] = new TDLAgent(Color.getColorById(indices[1]), game.getBoard(), nn);
//        agents[indices[2]] = new SimpleAI(Color.getColorById(indices[2]), game.getBoard());
//        agents[indices[3]] = new SimpleAI(Color.getColorById(indices[3]), game.getBoard());
////            agents[indices[2]] = new TDLAgent(Color.getColorById(indices[2]), game.getBoard(), this);
////            agents[indices[3]] = new TDLAgent(Color.getColorById(indices[3]), game.getBoard(), this);
//        game.setAgents(agents);
//        game.initActiveAgent();
//
//        System.out.println(nn.createStateVector(game));
//        System.out.println(nn.predict(nn.createStateVector(game)));
//        nn.train(10);

//        try {
//            nn.save("nn.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        NeuralNetwork nn2 = new NeuralNetwork("nn.json");
//        System.out.println(nn2.w2);
//        System.out.println(nn2.predict(nn2.createStateVector(board)));

//        Instant startTime, endTime;
//        long timeElapsed;
//        startTime = Instant.now();

        playTestGames();

//        endTime = Instant.now();
//        timeElapsed = Duration.between(startTime, endTime).toSeconds();
//        System.out.println("Time passed (total): " + timeElapsed + " s");

//        NeuralNetwork neuralNetwork = new NeuralNetwork(566, new int[] {80, 4});
//        Learning learning = new Learning(neuralNetwork, 0.9, 2);
//
//        learning.train(10);


    }

    private static void playTestGames() {
        int gamesWonByTDLAgent = 0;
        for(int i = 0; i < 1; i++) {
            Game game = initGameTest();

            int counter = 0;
            while (!game.isOver()) {

                if(counter % 25 == 0) {
                    System.out.println(game.getBoard());
                    System.out.println();
                    for(double k = 0; k < 190; k++){
                        System.out.print(k + " ");
                    }
                    System.out.println();
                    for(double j : Utils.createStateVector(game)){
                        System.out.print(j + " ");
                    }
                    System.out.println();
                    System.out.println();
                }
                counter++;

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
    }

    private static Move getNextMove(Game game) {
//        Instant startTime, endTime;
//        long timeElapsed;
//        startTime = Instant.now();

        game.rollDice();
        int diceResult = game.getDiceResult();
        Move nextMove = ((ArtificialAgent) game.getAgent(game.getActiveAgent())).getNextMove(game, diceResult);

//        endTime = Instant.now();
//        timeElapsed = Duration.between(startTime, endTime).toNanos();
//        System.out.println("Time passed (total): " + timeElapsed + " ns");

        return nextMove;
    }


    private static Game initGameTest() {
        Game game = new Game();
        Agent[] agents = new Agent[4];
        int[] indices = new int[] {0, 1, 2, 3};
        shuffleArray(indices);

//        agents[indices[0]] = new TDLAgent(Color.getColorById(indices[0]), game.getBoard(), new NeuralNetwork(566, new int[] {80, 4}));
//        agents[indices[0]] = new RandomAI(Color.getColorById(indices[0]), game.getBoard());
//        agents[indices[1]] = new RandomAI(Color.getColorById(indices[1]), game.getBoard());
        agents[indices[0]] = null;
        agents[indices[1]] = null;
        agents[indices[2]] = new SimpleAI(Color.getColorById(indices[2]), game.getBoard());
        agents[indices[3]] = new SimpleAI(Color.getColorById(indices[3]), game.getBoard());

        game.setAgents(agents);
        game.initActiveAgent();

        return game;
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
