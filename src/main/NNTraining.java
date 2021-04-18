package main;

import model.agents.NeuralNetwork;

import java.io.IOException;

public class NNTraining {

    public static void main(String[] args) {

//        NeuralNetwork nn = new NeuralNetwork("nn.json");
        NeuralNetwork nn = new NeuralNetwork();

        for(int i = 1; i <= 1000; i++) {
            nn.train(100);
            try {
                nn.save("nn.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Iterations: " + (i*100));
        }

    }

}
