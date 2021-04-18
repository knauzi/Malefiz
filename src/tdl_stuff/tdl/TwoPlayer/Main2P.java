package tdl_stuff.tdl.TwoPlayer;

import tdl_stuff.net.NeuralNetwork;

import java.io.IOException;

public class Main2P {

    public static void main(String[] args) {

        // Expert random mix
        NeuralNetwork neuralNetwork = new NeuralNetwork(48, new int[] {160, 2});
//        NeuralNetwork neuralNetwork = null;
//        try {
//            neuralNetwork = NeuralNetwork.readFrom("src/tdl_stuff/models/TwoPlayer/SavedNN_2P_Expert_Random_Mix");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        Learning2P learning = new Learning2P(neuralNetwork, 0.7, 0.1);
        learning.train(1000000);

//        try {
//            neuralNetwork.writeTo("src/tdl_stuff/models/TwoPlayer/SavedNN_2P_TDL_SP_New_Fail");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

}
