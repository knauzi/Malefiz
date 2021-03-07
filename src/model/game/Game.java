package model.game;

import model.agents.ArtificialAgent;
import model.agents.Agent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
    private Agent[] agents;     // array containing all agents
    private int numAgents;      // number of active agents
    private Phase phase;              // phase in which the game is currently
    private int activeAgent;          // index of currently active agent
    private Move lastMove;            // last move for resetting
    private int diceResult;

    /* observer pattern */
    private final PropertyChangeSupport pcs;

    /** initialisation of the game */

    public Game()
    {
        board = new Board();
        this.phase = Phase.WAITING_FOR_ROLL;
        randomGenerator = new Random();
        pcs = new PropertyChangeSupport(this);
    }

    public void setAgents(Agent[] agents)
    {
        this.agents = agents;
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

    /* has to be called first after setAgents !! ...chooses random agent to start */
    public void startGame()
    {
        if (agents == null)
        {
            System.err.println("No agents initialized! -> Failed to start game!");
            System.exit(-1);
        }
        do
        {
            activeAgent = randomGenerator.nextInt(agents.length);
        } while (agents[activeAgent] == null);

        requestMoveFromActiveAgent();
    }

    /** observer pattern */

    public void addObserver(PropertyChangeListener listener)
    {
        pcs.addPropertyChangeListener(listener);
    }

    /** game related methods */

    public void rollDice()
    {
        diceResult = randomGenerator.nextInt(6) + 1;
    }

    private void requestMoveFromActiveAgent()
    {
        if (agents[activeAgent] instanceof ArtificialAgent)
        {
            rollDice();
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

            System.out.println();
            System.out.println(board);
            System.out.println("Dice: " + diceResult);
            System.out.println("Agent: " + activeAgent);

            setLastMove(nextMove);
            advanceToNextAgent();
        }
    }

    public void advanceToNextAgent()
    {
        if (isGameOver())
        {
            System.out.println("Agent " + activeAgent + " has won the game!");
            return;
        }
        do
        {
            activeAgent = ++activeAgent % agents.length;
        } while (agents[activeAgent] == null);

        setPhase(Phase.WAITING_FOR_ROLL);
        requestMoveFromActiveAgent();
    }

    public boolean isGameOver()
    {
        return board.getTileById(Board.GOAL_TILE_ID).getState() != Tile.State.EMPTY;
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

    public Phase getPhase()
    {
        return phase;
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

    public int getActiveAgent()
    {
        return activeAgent;
    }

    public int getDiceResult()
    {
        return diceResult;
    }
}
