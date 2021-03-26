package model.agents;

import model.game.*;
import model.utils.Color;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class TrainingHelpAgent extends ArtificialAgent{

    public TrainingHelpAgent(Color color, Board board) {
        super(color, board);
    }

    @Override
    public Move getNextMove(Game game, int diceResult) {

        List<Move> possibleMoves = GameLogic.getPossibleMovesOfAgent(game.getBoard(), this, diceResult);

        if(possibleMoves.isEmpty()){
            return null;
        }

        Optional<Move> goalTileMove = getGoalTileMove(possibleMoves);
        if(goalTileMove.isPresent()){
            return goalTileMove.get();
        }

        int minTotalDistOfFiguresAfterMove = getMinTotalDistOfFiguresToGoalAfterMove(game, possibleMoves);
        List<Move> relevantMoves = getRelevantMoves(game, possibleMoves, minTotalDistOfFiguresAfterMove);

        return getMove(game, relevantMoves);
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

    private Move getMove(Game game, List<Move> relevantMoves){

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
            int randomMoveID = (new Random()).nextInt(moves.size());
            if(!moves.isEmpty()) {
                return moves.get(randomMoveID);
            }
        }

        // else get random move

        int randomMoveID = (new Random()).nextInt(relevantMoves.size());
        return relevantMoves.get(randomMoveID);
    }
}
