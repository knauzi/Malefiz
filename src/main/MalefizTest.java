package main;

import model.agents.Agent;
import model.agents.RandomAI;
import model.game.Game;
import model.utils.Color;

public class MalefizTest
{
    public static void main(String[] args)
    {
        Game game = new Game();

        Agent[] agents = new Agent[4];
        Agent red = new RandomAI(Color.RED, game.getBoard());
        Agent green = new RandomAI(Color.GREEN, game.getBoard());
        Agent yellow = new RandomAI(Color.YELLOW, game.getBoard());
        Agent blue = new RandomAI(Color.BLUE, game.getBoard());
        agents[0] = red;
        agents[1] = green;
        agents[2] = yellow;
        agents[3] = blue;

        System.out.println(game.getBoard());

        game.setAgents(agents);
        game.startGame();
    }
}
