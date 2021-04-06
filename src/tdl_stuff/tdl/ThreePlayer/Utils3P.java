package tdl_stuff.tdl.ThreePlayer;

import model.agents.Agent;
import model.game.*;
import model.utils.Color;

import java.util.*;
import java.util.stream.Collectors;

public class Utils3P {

    public static double calcUtility(double[] output, Game game, Agent agent) {
        int indexAgent = get3PIndex(game, agent);
        double agentWinProp = output[indexAgent];
        return agentWinProp - (sum(output) - output[indexAgent]);
    }

    public static double[] createStateVector(Game game) {
        double[] stateVec = new double[72];

        stateVec[get3PIndexOfActivePlayer(game)] = 1.0;

        ArrayList<Agent> agents = get3PAgents(game);
        for (int i = 0; i < 3; i++) {
            Agent agent = agents.get(i);

            // smart features
            Figure bestFigure = getBestFigure(agent);
            stateVec[3 + (i * 23)    ] = bestFigure.getPosition().getDist();
            stateVec[3 + (i * 23) + 1] = getNumBarricadesBetweenBestFigureAndGoal(bestFigure);
            stateVec[3 + (i * 23) + 2] = getAverageNumberOfStepsFiguresCanMoveTowardsGoal(agent);
            stateVec[3 + (i * 23) + 3] = getAverageDistToGoal(agent);
            stateVec[3 + (i * 23) + 4] = getNumVulnerableFigures(agent);
            stateVec[3 + (i * 23) + 5] = getNumEnemyFiguresCloserToGoalThanBestFigure(game, agent, bestFigure.getPosition().getDist());
            stateVec[3 + (i * 23) + 6] = getHitProbabilityByEnemyFigures(game, agent);
            stateVec[3 + (i * 23) + 7] = getNumFiguresInBase(agent);
            stateVec[3 + (i * 23) + 8] = getNumFiguresInHouse(agent);

            // positional features
            double[] rowCounts = getRowCounts(agent);
            for(int j = 0; j < rowCounts.length; j++) {
                stateVec[3 + (i * 23) + 9 + j] = rowCounts[j];
            }

        }

//        // barricades
//        // TODO consider reordering
//        Board board = game.getBoard();
//        for (int i = 96; i < 190; i++) {
//            if (board.getTileById(i - 96 + 17).getState() == Tile.State.BLOCKED) {
//                stateVec[i] = 1.0;
//            }
//        }

        return stateVec;
    }

    private static int get3PIndex(Game game, Agent agent) {
        ArrayList<Agent> agents = get3PAgents(game);
        Color agentColor = agent.getColor();
        for(int i = 0; i < 3; i++){
            if(agentColor == agents.get(i).getColor()){
                return i;
            }
        }
        return -1;
    }

    private static ArrayList<Agent> get3PAgents(Game game) {
        ArrayList<Agent> agents = new ArrayList<>();
        for(Agent agent : game.getAgents()){
            if(agent != null){
                agents.add(agent);
            }
        }
        return agents;
    }

    public static int get3PIndexOfActivePlayer(Game game) {
        ArrayList<Agent> agents = get3PAgents(game);
        Color activeColor = game.getAgent(game.getActiveAgent()).getColor();
        for(int i = 0; i < 3; i++){
            if(activeColor == agents.get(i).getColor()){
                return i;
            }
        }
        return -1;
    }

    private static double[] getRowCounts(Agent agent) {
        double[] rowCounts = new double[14];
        for (Figure figure : agent.getFigures()) {
            int pos = figure.getPosition().getId();
            if      (pos < 17)   rowCounts[0]++;
            else if (pos < 22)   rowCounts[1]++;
            else if (pos < 39)   rowCounts[2]++;
            else if (pos < 43)   rowCounts[3]++;
            else if (pos < 56)   rowCounts[4]++;
            else if (pos < 58)   rowCounts[5]++;
            else if (pos < 67)   rowCounts[6]++;
            else if (pos < 69)   rowCounts[7]++;
            else if (pos < 74)   rowCounts[8]++;
            else if (pos < 75)   rowCounts[9]++;
            else if (pos < 92)   rowCounts[10]++;
            else if (pos < 94)   rowCounts[11]++;
            else if (pos < 111)  rowCounts[12]++;
            else if (pos < 112)  rowCounts[13]++;
        }
        return rowCounts;
    }

    private static double getHitProbabilityByEnemyFigures(Game game, Agent agent) {
        double totalHits = 0.0;
        int agentColor = agent.getColor().ordinal();
        for(Agent enemyAgent : game.getAgents()) {
            if(enemyAgent != null && enemyAgent.getColor() != agent.getColor()) {
                for(int i = 1; i <= 6; i++) {
                    for(Figure enemyFigure : enemyAgent.getFigures()) {
                        if(containsTileOccupiedByAgent(GameLogic.getPossibleTargetTilesOfFigure(enemyFigure, i), agentColor)) {
                            totalHits++;
                            break;
                        }

                    }
                }
            }
        }
        return totalHits / (12.0);
    }

    private static boolean containsTileOccupiedByAgent(ArrayList<Tile> possibleTargetTiles, int agentColor) {
        for(Tile targetTile : possibleTargetTiles) {
            if(targetTile.getState().ordinal() == agentColor) {
                return true;
            }
        }
        return false;
    }

    private static double getNumFiguresInHouse(Agent agent) {
        double numFiguresInHouse = 0.0;
        for(Figure figure : agent.getFigures()) {
            if(figure.getPosition().getId() > 74 && figure.getPosition().getId() < Board.GOAL_TILE_ID) {
                numFiguresInHouse++;
            }
        }
        return numFiguresInHouse;
    }

    private static double getNumFiguresInBase(Agent agent) {
        double numFiguresInBase = 0.0;
        for(Figure figure : agent.getFigures()) {
            if(figure.getPosition().getId() > Board.GOAL_TILE_ID) {
                numFiguresInBase++;
            }
        }
        return numFiguresInBase;
    }

    private static double getNumEnemyFiguresCloserToGoalThanBestFigure(Game game, Agent agent, int bestDist) {
        double totalNumEnemyFiguresCloserToGoal = 0.0;
        int agentColor = agent.getColor().ordinal();
        for(Agent enemyAgent : game.getAgents()) {
            if(enemyAgent != null && enemyAgent.getColor().ordinal() != agentColor) {
                for(Figure enemyFigure : enemyAgent.getFigures()) {
                    if(enemyFigure.getPosition().getDist() < bestDist) {
                        totalNumEnemyFiguresCloserToGoal++;
                    }
                }
            }
        }
        return totalNumEnemyFiguresCloserToGoal;
    }

    private static double getNumVulnerableFigures(Agent agent) {
        double numVulnerableFigures = 0.0;
        int agentColor = agent.getColor().ordinal();
        for(Figure figure : agent.getFigures()) {
            if(isVulnerable(figure)){
                numVulnerableFigures++;
            }
        }
        return numVulnerableFigures;
    }

    private static double getAverageDistToGoal(Agent agent) {
        double totalDist = 0.0;
        for(Figure figure : agent.getFigures()) {
            totalDist += figure.getPosition().getDist();
        }
        return totalDist / 5.0;
    }

    private static double getAverageNumberOfStepsFiguresCanMoveTowardsGoal(Agent agent) {
        double totalSteps = 0.0;
        for(Figure figure : agent.getFigures()) {
            Tile position = figure.getPosition();
            int stepsLeft = 6;
            while(stepsLeft > 0 && position.getState() != Tile.State.BLOCKED && position.getId() != Board.GOAL_TILE_ID) {
                position = getNextPositionTowardsGoal(position);
                totalSteps++;
                stepsLeft--;
            }
        }
        return totalSteps / 5.0;
    }

    private static double getNumBarricadesBetweenBestFigureAndGoal(Figure bestFigure) {
        double numBarricades = 0.0;
        Tile position = bestFigure.getPosition();
        while(position.getId() != Board.GOAL_TILE_ID) {
            position = getNextPositionTowardsGoal(position);
            if(position.getState() == Tile.State.BLOCKED) {
                numBarricades++;
            }
        }
        return numBarricades;
    }

    private static Tile getNextPositionTowardsGoal(Tile position) {
        Tile[] neighbours = position.getNeighbours();
        int minDist = Integer.MAX_VALUE;
        Tile nextTile = null;
        for(Tile neighbour : neighbours) {
            int dist = neighbour.getDist();
            if(dist < minDist) {
                nextTile = neighbour;
                minDist = dist;
            }
        }
        for(Tile neighbour : neighbours){
            if(neighbour.getDist() == minDist && !neighbour.equals(nextTile) && minDist != 38){
                return getBestNeighbour(neighbour, nextTile);
            }
        }
        return nextTile;
    }

    private static Tile getBestNeighbour(Tile neighbour1, Tile neighbour2) {
        Tile n1 = neighbour1, n2 = neighbour2;
        int lookahead;
        if (n1.getDist() == 34) {
            lookahead = 4;
        } else if (n1.getDist() == 18){
            lookahead = 17;
        } else {
            return neighbour1;
        }
        int blockCount1 = 0, blockCount2 = 0;
        for(int i = 0; i < lookahead; i++){
            n1 = getNextPositionTowardsGoal(n1);
            if(n1.getState() == Tile.State.BLOCKED){
                blockCount1++;
            }
            n2 = getNextPositionTowardsGoal(n2);
            if(n2.getState() == Tile.State.BLOCKED){
                blockCount2++;
            }
        }
        return blockCount1 <= blockCount2 ? neighbour1 : neighbour2;
    }

    private static Figure getBestFigure(Agent agent) {
        int minDist = Integer.MAX_VALUE;
        Figure bestFigure = null;
        for(Figure figure : agent.getFigures()) {
            int dist = figure.getPosition().getDist();
            if(dist < minDist) {
                bestFigure = figure;
                minDist = dist;
            }
        }
        return bestFigure;
    }

    private static double sum(double...values) {
        double result = 0;
        for (double value:values)
            result += value;
        return result;
    }

    private static boolean isVulnerable(Figure figure) {
        int diceResult = 6;
        int figureColor = figure.getColor().ordinal();
        Tile root = figure.getPosition();                        // start search from figures position
        HashSet<Tile> visited = new HashSet<>();                 // keep track of which tiles were already visited
        ArrayDeque<Tile> queue = new ArrayDeque<>();             // queue of nodes to traverse next
        queue.push(root);

        Tile currentTile;
        int currentDepth = 0;
        int tilesAtCurrentDepth = 1;
        while (!queue.isEmpty()) {
            currentTile = queue.remove();

            if (currentTile.getState() != Tile.State.EMPTY &&
                    currentTile.getState() != Tile.State.BLOCKED &&
                    currentTile.getState().ordinal() != figureColor) {
                return true;
            }

            if(currentDepth == diceResult){
                continue;
            }

            if (currentTile.getState() != Tile.State.BLOCKED) {
                for (Tile neighbour : currentTile.getNeighbours()) {
                    if (!visited.contains(neighbour)) {
                        queue.add(neighbour);
                    }
                }
            }

            visited.add(currentTile);
            tilesAtCurrentDepth--;

            if (tilesAtCurrentDepth == 0) {
                currentDepth++;
                tilesAtCurrentDepth = queue.size();
            }
        }

        return false;
    }

}
