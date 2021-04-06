package tdl_stuff.tdl.FourPlayer;

import tdl_stuff.net.NeuralNetwork;

public class Main4P {

    public static void main(String[] args) {

        NeuralNetwork neuralNetwork = new NeuralNetwork(96, new int[] {80, 4});

        Learning4P learning = new Learning4P(neuralNetwork, 0.7, 0.1);
        learning.train(100000);

    }

}
