package model.agents;

import freemarker.core.OptInTemplateClassResolver;
import model.game.*;
import model.utils.Color;

import java.util.*;
import java.util.stream.Collectors;

public class ExpertAI extends ArtificialAgent{

    private int totalNumberOfTurns;
    private int totalNumberOfMoves;

    public ExpertAI(Color color, Board board) {
        super(color, board);
        totalNumberOfTurns = 0;
        totalNumberOfMoves = 0;
    }

    @Override
    public Move getNextMove(Game game, int diceResult) {

        List<Move> possibleMoves = GameLogic.getPossibleMovesOfAgent(game.getBoard(), this, diceResult);
        totalNumberOfMoves += possibleMoves.size();
        totalNumberOfTurns++;

        if(possibleMoves.isEmpty()){
            return null;
        }

        // win game
        Optional<Move> goalTileMove = getGoalTileMove(possibleMoves);
        if(goalTileMove.isPresent()){
            return goalTileMove.get();
        }

        // defeat enemy figure near the goal
        Optional<Move> defeatEnemyNearOrInHouse = getDefeatEnemyNearOrInHouseMove(possibleMoves);
        if(defeatEnemyNearOrInHouse.isPresent()){
            return defeatEnemyNearOrInHouse.get();
        }

        // choose other best move after minimizing total dist of figures to goal
        int minTotalDistOfFiguresAfterMove = getMinTotalDistOfFiguresToGoalAfterMove(game, possibleMoves);
        List<Move> relevantMoves = getRelevantMoves(game, possibleMoves, minTotalDistOfFiguresAfterMove);

        return getBestMove(game, relevantMoves);
    }

    private Optional<Move> getDefeatEnemyNearOrInHouseMove(List<Move> possibleMoves) {
        List<Move> defeatEnemyMoves = possibleMoves.stream()
                .filter((Move move) -> move.getBeatenColor() != null)
                .collect(Collectors.toList());
        if(defeatEnemyMoves.isEmpty()){
            return Optional.empty();
        }

        ArrayList<Move> defeatEnemyNearOrInHouseMoves = new ArrayList<>();
        for(Move move : defeatEnemyMoves){
            if(move.getTargetOfFigure().getDist() <= 24){
                defeatEnemyNearOrInHouseMoves.add(move);
            }
        }
        if(defeatEnemyNearOrInHouseMoves.isEmpty()){
            return Optional.empty();
        }
        int randomId = (new Random()).nextInt(defeatEnemyNearOrInHouseMoves.size());
        return Optional.of(defeatEnemyNearOrInHouseMoves.get(randomId));
    }

    private Optional<Move> getGoalTileMove(List<Move> possibleMoves) {
        return possibleMoves.stream()
                .filter((Move move) -> move.getTargetOfFigure().getId() == Board.GOAL_TILE_ID)
                .findFirst();
    }

    private List<Move> getRelevantMoves(Game game, List<Move> possibleMoves, int minTotalDistOfFiguresAfterMove) {
        return possibleMoves.stream()
                .filter((Move move) -> getTotalDistOfFiguresToGoalAfterMove(game, move) == minTotalDistOfFiguresAfterMove)
                .collect(Collectors.toList());
    }

    private int getMinTotalDistOfFiguresToGoalAfterMove(Game game, List<Move> possibleMoves) {
        int minTotalDist = Integer.MAX_VALUE;
        for(Move move : possibleMoves){
            int totalDistOfFiguresToGoal = getTotalDistOfFiguresToGoalAfterMove(game, move);
            if(totalDistOfFiguresToGoal < minTotalDist){
                minTotalDist = totalDistOfFiguresToGoal;
            }
        }
        return minTotalDist;
    }

    private int getTotalDistOfFiguresToGoalAfterMove(Game game, Move move) {
        int totalDistOfFiguresToGoal = 0;
        GameLogic.makeMoveOnGame(game, move);
        for(Figure figure : getFigures()){
            totalDistOfFiguresToGoal += figure.getPosition().getDist();
        }
        GameLogic.undoMoveOnGame(game, move);
        return totalDistOfFiguresToGoal;
    }

    private Move getBestMove(Game game, List<Move> relevantMoves){

        // 1. defeat other players figure if possible
        Optional<Move> defeatOtherPlayersFigureMove = relevantMoves.stream()
                                                            .filter((Move move) -> move.getBeatenColor() != null)
                                                            .findFirst();
        if(defeatOtherPlayersFigureMove.isPresent()){
            return defeatOtherPlayersFigureMove.get();
        }

        // 2. move block if possible
        Optional<Move> moveBlockMove = getMoveBlockMove(game, relevantMoves);
        if(moveBlockMove.isPresent()){
            return moveBlockMove.get();
        }

        // 3. some random move
        Random random = new Random();
        return relevantMoves.get(random.nextInt(relevantMoves.size()));
    }

    private Optional<Move> getMoveBlockMove(Game game, List<Move> relevantMoves) {

        // if move block available
        List<Move> moveBlockMoves = relevantMoves.stream()
                .filter((Move move) -> move.getTargetOfBlock() != null)
                .collect(Collectors.toList());

        if (!moveBlockMoves.isEmpty()) {
            Tile blockTarget = null;

            Figure leadingFigureOfEnemyPlayersIfInHouse = getLeadingFigureOfEnemyPlayersIfInHouse(game);
            int stepCounter = 0;
            if(leadingFigureOfEnemyPlayersIfInHouse != null){
                Tile position = leadingFigureOfEnemyPlayersIfInHouse.getPosition();
                while(position.getId() != Board.GOAL_TILE_ID && stepCounter++ < 3) {
                    position = getNextPositionTowardsGoal(position);
                    if(position.getState() == Tile.State.EMPTY) {
                        blockTarget = position;
                        break;
                    }
                }
            }

            if(blockTarget != null){ // block leading enemy figure (if in house) and not already 3 barricades are in front
                for(Move blockMove : moveBlockMoves){
                    if(blockMove.getTargetOfBlock().equals(blockTarget)){
                        return Optional.of(blockMove);
                    }
                }
            } else { // block random enemy figure if possible
                Figure enemyFigure = getRandomEnemyFigureNotInBase(game);
                if(enemyFigure == null){
                    return Optional.empty();
                }
                Tile position = enemyFigure.getPosition();
                while(position.getId() != Board.GOAL_TILE_ID) {
                    position = getNextPositionTowardsGoal(position);
                    if(position.getState() == Tile.State.EMPTY && position.getDist() <= 36) {
                        blockTarget = position;
                        break;
                    }
                }
//                System.out.println(blockTarget);
                for(Move blockMove : moveBlockMoves){
                    if(blockMove.getTargetOfBlock().equals(blockTarget)){
                        return Optional.of(blockMove);
                    }
                }
            }
        }

        return Optional.empty();
    }

    public int getAverageNumberOfMovesPerTurn(){
        return totalNumberOfMoves / totalNumberOfTurns;
    }

    private Figure getLeadingFigureOfEnemyPlayersIfInHouse(Game game){
        Figure minDistFigure = null;
        int minDist = Integer.MAX_VALUE;
        for(Agent agent : game.getAgents()) {
            if(agent != null && agent.getColor() != this.getColor()){
                for(Figure figure : agent.getFigures()){
                    if(figure.getPosition().getDist() < minDist) {
                        minDistFigure = figure;
                        minDist = figure.getPosition().getDist();
                    }
                }
            }
        }
        if (minDist < 19){ // one step into house
            return minDistFigure;
        }
        return null;
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
        return nextTile;
    }

    private Figure getRandomEnemyFigureNotInBase(Game game) {
        ArrayList<Figure> enemyFigures = new ArrayList<>();
        for(Agent agent : game.getAgents()) {
            if(agent != null && agent.getColor() != this.getColor()){
                for(Figure figure : agent.getFigures()){
                    if(figure.getPosition().getDist() < 40){
                        enemyFigures.add(figure);
                    }
                }
            }
        }
        if(enemyFigures.isEmpty()){
            return null;
        }
        int randomId = (new Random()).nextInt(enemyFigures.size());
        return enemyFigures.get(randomId);
    }
}
