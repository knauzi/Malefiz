package model.game;

import model.agents.ArtificialAgent;
import model.utils.Color;
import model.agents.Agent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Game
{
    public enum Phase
    {
        WAITING_FOR_ROLL, FIGURE_SELECTION, TARGET_SELECTION, MOVING_BLOCK
    }

    /* random number generator for rolling the dice */
    Random randomGenerator;

    /* all data corresponding to current game state */
    private final Board board;        // instance of board (official board; ai player make copies)
    private final Agent[] agents;     // array containing all agents
    private final int numAgents;      // number of active agents
    private Phase phase;              // phase in which the game is currently
    private int activeAgent;          // index of currently active agent
    private Move lastMove;            // last move for resetting

    /* observer pattern */
    private final PropertyChangeSupport pcs;

    /** initialisation of the game */

    public Game(Agent[] agents)
    {
        board = new Board();
        this.agents = agents;
        this.phase = Phase.WAITING_FOR_ROLL;
        randomGenerator = new Random();
        pcs = new PropertyChangeSupport(this);
        numAgents = getNumAgents(agents);
    }

    private int getNumAgents(Agent[] agents)
    {
        int numAgents = 0;
        for (Agent agent : agents)
        {
            if (agent != null)
            {
                numAgents++;
            }
        }

        if (numAgents < 2)
        {
            System.err.println("There must be more than one active agent to play the game!");
            System.exit(-1);
        }

        return numAgents;
    }

    /* has to be called first !! ...chooses random agent to start */
    public void startGame()
    {
        do
        {
            activeAgent = randomGenerator.nextInt(agents.length);
        } while (agents[activeAgent] == null);

        requestMoveFromActiveAgent();
    }

    public void advanceToNextAgent()
    {
        do
        {
            activeAgent = activeAgent++ % agents.length;
        } while (agents[activeAgent] == null);

        setPhase(Phase.WAITING_FOR_ROLL);
        requestMoveFromActiveAgent();
    }

    /** observer pattern */

    public void addObserver(PropertyChangeListener listener)
    {
        pcs.addPropertyChangeListener(listener);
    }

    /** game related methods */

    public int rollDice()
    {
        return randomGenerator.nextInt(6) + 1;
    }

    private void requestMoveFromActiveAgent()
    {
        if (agents[activeAgent] instanceof ArtificialAgent)
        {
            int diceResult = rollDice();
            Move nextMove = ((ArtificialAgent) agents[activeAgent]).getNextMove(this, diceResult);

            if (!GameLogic.isValidMove(this, nextMove))
            {
                System.err.println("Artificial Agent made invalid Move in current game state! -> Exit");
                System.exit(-1);
            }
            if (nextMove == null)
            {
                System.out.println("No valid Move found for Player " + agents[activeAgent].getColor());
                System.out.println("Advance to next player...");
            }

            update(nextMove);
            setLastMove(nextMove);
            advanceToNextAgent();
        }
    }

    public void update(Move move)
    {
        GameLogic.makeMoveOnGame(this, move);
        pcs.firePropertyChange("game", null, this);
    }

    public void undoLastMove()
    {
        GameLogic.undoMoveOnGame(this, lastMove);
        pcs.firePropertyChange("game", null, this);
    }

    public void resetGame()
    {
        // TODO
        pcs.firePropertyChange("game", null, this);
    }

    /** getters and setters */

    public void setPhase(Phase phase)
    {
        this.phase = phase;
    }

    public Board getBoard()
    {
        return board;
    }

    public Agent[] getAgents()
    {
        return agents;
    }

    public Agent getAgent(int index)
    {
        return agents[index];
    }

    public void setLastMove(Move lastMove)
    {
        this.lastMove = lastMove;
    }

    public Move getLastMove()
    {
        return lastMove;
    }
}
