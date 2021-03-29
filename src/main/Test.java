package main;

import model.agents.*;
import model.game.Board;
import model.game.Game;
import model.utils.Color;

import java.io.IOException;

public class Test {

    public static void main(String[] args) {

        NeuralNetwork nn = new NeuralNetwork();
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
        nn.train(10);

//        try {
//            nn.save("nn.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        NeuralNetwork nn2 = new NeuralNetwork("nn.json");
//        System.out.println(nn2.w2);
//        System.out.println(nn2.predict(nn2.createStateVector(board)));
    }

}
