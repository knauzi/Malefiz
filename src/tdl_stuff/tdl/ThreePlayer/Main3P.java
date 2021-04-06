package tdl_stuff.tdl.ThreePlayer;

import tdl_stuff.net.NeuralNetwork;

public class Main3P {

    public static void main(String[] args) {

        NeuralNetwork neuralNetwork = new NeuralNetwork(72, new int[] {110, 3});

        Learning3P learning = new Learning3P(neuralNetwork, 0.7, 0.1);
        learning.train(100000);

    }

}
