package tdl_stuff.tdl.FourPlayer;

import tdl_stuff.net.NeuralNetwork;

import java.io.IOException;

public class Main4PCopy {

    public static void main(String[] args) {

//        NeuralNetwork neuralNetwork = new NeuralNetwork(96, new int[] {80, 4});

        NeuralNetwork neuralNetwork = null;
        try {
            neuralNetwork = NeuralNetwork.readFrom("src/tdl_stuff/models/FourPlayer/SavedNN_4P_Expert_Random");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Learning4P learning = new Learning4P(neuralNetwork, 0.7, 0.05);
        learning.train(50000);

    }

}
