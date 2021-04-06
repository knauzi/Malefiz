package heuristic_stuff;

import tdl_stuff.net.NeuralNetwork;
import tdl_stuff.tdl.TwoPlayer.Learning2P;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {

    private final Random random;

    public SimulatedAnnealing(){
        random = new Random();
    }

    public double[] find2P() {
        double rand = 1.0;
        ArrayList<double[]> heuristicArrays = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            heuristicArrays.add(createRandomHeuristicArray());
        }
        int gamesPlayed = 0;
        int count = 20;
        ArrayList<double[]> winnerArrays = new ArrayList<>();
        while (rand >= 0.0){

            int i = random.nextInt(count);
            double[] heuristicParameters1 = heuristicArrays.remove(i);
            count--;
            i = random.nextInt(count);
            double[] heuristicParameters2 = heuristicArrays.remove(i);
            count--;

            double[] winnerArray = findWinner(heuristicParameters1, heuristicParameters2);
            winnerArrays.add(winnerArray);
            gamesPlayed++;

            if(gamesPlayed == 10) {
                for(double[] heuristicArray : winnerArrays){
                    double[] newHeuristicArray = createNewHeuristicArrayFrom(heuristicArray, rand);
                    heuristicArrays.add(heuristicArray);
                    heuristicArrays.add(newHeuristicArray);
                }
                winnerArrays.clear();
                rand -= 0.01;
                gamesPlayed = 0;
                count = 20;
            }
        }
        return playout(winnerArrays);
    }

    public double[] find3P() {
        return null;
    }

    public double[] find4P() {
        return null;
    }

    private double[] playout(ArrayList<double[]> winnerArrays) {
        return null;
    }

    private double[] createNewHeuristicArrayFrom(double[] heuristicArray, double rand) {
        return null;
    }

    private double[] findWinner(double[] heuristicParameters1, double[] heuristicParameters2) {
        return null;
    }

    private double[] createRandomHeuristicArray() {
        return null;
    }

}
