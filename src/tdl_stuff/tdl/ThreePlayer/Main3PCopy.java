package tdl_stuff.tdl.ThreePlayer;

import tdl_stuff.net.NeuralNetwork;

import java.io.IOException;

public class Main3PCopy {

    public static void main(String[] args) {

//        NeuralNetwork neuralNetwork = new NeuralNetwork(72, new int[] {80, 3});

        NeuralNetwork neuralNetwork = null;
        try {
            neuralNetwork = NeuralNetwork.readFrom("src/tdl_stuff/models/ThreePlayer/SavedNN_3P_TDL_Expert_Random");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Learning3P learning = new Learning3P(neuralNetwork, 0.7, 0.05);
        learning.train(50000);

    }

}
