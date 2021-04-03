package tdl_stuff.tdl;

import tdl_stuff.net.NeuralNetwork;

public class Main {

    public static void main(String[] args) {

        NeuralNetwork neuralNetwork = new NeuralNetwork(96, new int[] {80, 4});
        Learning learning = new Learning(neuralNetwork, 0.8, 0.2);

        learning.train(50000);
    }

}
