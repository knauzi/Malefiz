package tdl_stuff.tdl.TwoPlayer;

import tdl_stuff.net.NeuralNetwork;

import java.io.IOException;

public class Main2PMix {

    public static void main(String[] args) {

//        NeuralNetwork neuralNetwork = new NeuralNetwork(48, new int[] {80, 2});
        NeuralNetwork neuralNetwork = null;
        try {
            neuralNetwork = NeuralNetwork.readFrom("src/tdl_stuff/models/TwoPlayer/SavedNN_2P_TDL_Mix");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Learning2P learning = new Learning2P(neuralNetwork, 0.7, 0.05);
        learning.train(50000);

//        try {
//            neuralNetwork.writeTo("src/tdl_stuff/models/TwoPlayer/SavedNN_2P_TDL_SP_Million");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

}
