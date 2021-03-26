package main;

import model.agents.NeuralNetwork;
import model.game.Board;

public class Test {

    public static void main(String[] args) {
//        INDArray matrix1 = Nd4j.ones(3, 3);
//        INDArray matrix2 = Nd4j.eye(3);
//        System.out.println(matrix1.mmul(matrix2));
        //Board board = new Board();
        //System.out.println(nn.createStateVector(board));
        //System.out.println(nn.predict(nn.createStateVector(board)));

        NeuralNetwork nn = new NeuralNetwork();
        nn.train(10);
    }

}
