package main;

import model.agents.Agent;
import model.agents.RandomAI;
import model.agents.SimpleAI;
import model.game.Game;
import model.utils.Color;

public class MalefizArena{

    public static final int NUM_PLAYS = 1000;

    private static int totalNumberOfAverageMovesPerTurn = 0;

    public static void main(String[] args) {

        MalefizArena arena = new MalefizArena();
        arena.run();
//        try {
//            sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println();
        for(int i = 0; i < 4; i++){
            System.out.println("Spieler " + Color.getColorById(i) + ": " + Game.winStats[i] + " Siege");
        }

        System.out.println();
        System.out.println("Durchschnittliche Anzahl an Zügen: " + totalNumberOfAverageMovesPerTurn / (NUM_PLAYS * 3));
    }

    public void run() {
        for(int i = 0; i < NUM_PLAYS; i++){
            Game game = new Game();

            Agent[] agents = new Agent[4];
            agents[0] = new RandomAI(Color.RED, game.getBoard());
            agents[1] = null; //new SimpleAI(Color.GREEN, game.getBoard());
            agents[2] = new RandomAI(Color.YELLOW, game.getBoard());
            agents[3] = new RandomAI(Color.BLUE, game.getBoard());
            game.setAgents(agents);

            game.start();
            try {
                game.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(Agent agent : agents){
                if(agent != null){
//                    System.out.println(((RandomAI) agent).getAverageNumberOfMovesPerTurn());
                    totalNumberOfAverageMovesPerTurn += ((RandomAI) agent).getAverageNumberOfMovesPerTurn();
                }
            }
        }
    }

}
