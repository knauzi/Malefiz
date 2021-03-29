package model.agents;

import model.game.*;
import model.utils.Color;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class SimpleAI extends ArtificialAgent{

    private int totalNumberOfTurns;
    private int totalNumberOfMoves;

    public SimpleAI(Color color, Board board) {
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

        Optional<Move> goalTileMove = getGoalTileMove(possibleMoves);
        if(goalTileMove.isPresent()){
            return goalTileMove.get();
        }

        int minTotalDistOfFiguresAfterMove = getMinTotalDistOfFiguresToGoalAfterMove(game, possibleMoves);
        List<Move> relevantMoves = getRelevantMoves(game, possibleMoves, minTotalDistOfFiguresAfterMove);

        return getBestMove(game, relevantMoves);
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

            int distOfFirstFigure = Integer.MAX_VALUE;
            for (Figure figure : getFigures()) {
                int distOfFigure = figure.getPosition().getDist();
                if (distOfFigure < distOfFirstFigure) {
                    distOfFirstFigure = distOfFigure;
                }
            }

            int finalDistOfFirstFigure = distOfFirstFigure;
            List<Move> moves = moveBlockMoves.stream()
                    .filter((Move move) -> move.getTargetOfBlock().getDist() >= finalDistOfFirstFigure)
                    .collect(Collectors.toList());
            if (!moves.isEmpty()) {
                int randomMoveID = (new Random()).nextInt(moves.size());
                return Optional.of(moves.get(randomMoveID));
            }
        }

        return Optional.empty();
    }

    public int getAverageNumberOfMovesPerTurn(){
        return totalNumberOfMoves / totalNumberOfTurns;
    }
}
