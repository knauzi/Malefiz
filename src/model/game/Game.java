package model.game;

import javafx.application.Platform;
import model.agents.ArtificialAgent;
import model.agents.Agent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

public class Game extends Thread{

    public enum Phase {
        WAITING_FOR_ROLL, FIGURE_SELECTION, TARGET_SELECTION, MOVING_BLOCK, GAME_OVER
    }

    /* random number generator for rolling the dice */
    Random randomGenerator;

    /* for evaluation */
    public static int[] winStats = new int[] {0, 0, 0, 0};

    /* all data corresponding to current game state */
    private Board board;        // instance of board (official board; ai player make copies)
    private Agent[] agents;     // array containing all agents
    private int numAgents;      // number of active agents
    private Phase phase;              // phase in which the game is currently
    private int activeAgent;          // index of currently active agent
    private Move lastMove;            // last move for resetting
    private int diceResult;
    private boolean running;

    /* observer pattern */
    private final PropertyChangeSupport pcs;

    /**
     * initialisation of the game
     */

    public Game() {
        board = new Board();
        this.phase = Phase.WAITING_FOR_ROLL;
        randomGenerator = new Random();
        pcs = new PropertyChangeSupport(this);
    }

    public synchronized void setAgents(Agent[] agents) {
        this.agents = agents;
        numAgents = getNumAgents(agents);
    }

    private synchronized int getNumAgents(Agent[] agents) {
        int numAgents = 0;
        for (Agent agent : agents) {
            if (agent != null) {
                numAgents++;
            }
        }

        if (numAgents < 2) {
            System.err.println("There must be more than one active agent to play the game!");
            System.exit(-1);
        }

        return numAgents;
    }

    /* has to be called first after setAgents !! ...chooses random agent to start */
    @Override
    public synchronized void run() {
        if (agents == null) {
            System.err.println("No agents initialized! -> Failed to start game!");
            System.exit(-1);
        }
        initActiveAgent();
        running = true;
        requestMoveFromActiveAgent();
    }

    private synchronized void initActiveAgent() {
        do {
            activeAgent = randomGenerator.nextInt(agents.length);
        } while (agents[activeAgent] == null);
    }

    /**
     * observer pattern
     */

    public void addObserver(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * game related methods
     */

    public synchronized void rollDice() {
        diceResult = randomGenerator.nextInt(6) + 1;
    }

    private synchronized void requestMoveFromActiveAgent() {
        if (agents[activeAgent] instanceof ArtificialAgent) {
            rollDice();
            Move nextMove = ((ArtificialAgent) agents[activeAgent]).getNextMove(this, diceResult);

            if (nextMove == null) {
                System.out.println("No valid Move found for Player " + agents[activeAgent].getColor());
                System.out.println("Advance to next player...");
            }
            else if (!GameLogic.isValidMove(this, nextMove)) {
                System.err.println("Artificial Agent made invalid Move in current game state! -> Exit");
                System.exit(-1);
            }

            update(nextMove);

//            System.out.println();
//            System.out.println(board);
//            System.out.println("Dice: " + diceResult);
//            System.out.println("Agent: " + activeAgent);

            advanceToNextAgent();
        }
    }

    public synchronized void advanceToNextAgent() {
        if (isGameOver()) {
            return;
        }
        do {
            activeAgent = ++activeAgent % agents.length;
        } while (agents[activeAgent] == null);

        setPhase(Phase.WAITING_FOR_ROLL);

//        new Thread(() -> {
//            try  { Thread.sleep( 1000 ); }
//            catch (InterruptedException ie)  {}
//            requestMoveFromActiveAgent();
//        }).start();
        requestMoveFromActiveAgent();
    }

    public synchronized boolean isGameOver() {
        return board.getTileById(Board.GOAL_TILE_ID).getState() != Tile.State.EMPTY;
    }

    public synchronized void update(Move move) {
        if(move == null){
            return;
        }

        GameLogic.makeMoveOnGame(this, move);
        if (isGameOver()) {
            // System.out.println("Agent " + activeAgent + " has won the game!");
            setPhase(Phase.GAME_OVER);
            synchronized (this) {
                Game.winStats[activeAgent]++;
            };
            running = false;
            interrupt();
        }
        pcs.firePropertyChange("game", null, this);
        // System.out.println("update");
    }

    public synchronized void undoLastMove() {
        GameLogic.undoMoveOnGame(this, lastMove);
        pcs.firePropertyChange("game", null, this);
    }

    public synchronized void resetGame() {
        board = new Board();
        resetAgents(board);
        initActiveAgent();
        this.phase = Phase.WAITING_FOR_ROLL;
        pcs.firePropertyChange("game", null, this);
        requestMoveFromActiveAgent();
    }

    private synchronized void resetAgents(Board board) {
        for(Agent agent : agents){
            if(!(agent == null)) {
                agent.initFigures(board);
            }
        }
    }

    /**
     * getters and setters
     */

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Phase getPhase() {
        return phase;
    }

    public Board getBoard() {
        return board;
    }

    public Agent[] getAgents() {
        return agents;
    }

    public Agent getAgent(int index) {
        return agents[index];
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public int getActiveAgent() {
        return activeAgent;
    }

    public int getDiceResult() {
        return diceResult;
    }

    public boolean isRunning(){
        return running;
    }
}
