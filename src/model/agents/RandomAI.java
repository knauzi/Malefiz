package model.agents;

import model.game.Board;
import model.game.Game;
import model.game.GameLogic;
import model.game.Move;
import model.utils.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI extends ArtificialAgent
{
    private int totalNumberOfTurns;
    private int totalNumberOfMoves;

    public RandomAI(Color color, Board board)
    {
        super(color, board);
        totalNumberOfTurns = 0;
        totalNumberOfMoves = 0;
    }

    @Override
    public synchronized Move getNextMove(Game game, int diceResult)
    {
        List<Move> possibleMoves = GameLogic.getPossibleMovesOfAgent(game.getBoard(), this, diceResult);
        totalNumberOfMoves += possibleMoves.size();
        totalNumberOfTurns++;

        int randomMoveID = (new Random()).nextInt(possibleMoves.size());
        return possibleMoves.isEmpty() ? null : possibleMoves.get(randomMoveID);
    }

    public int getAverageNumberOfMovesPerTurn(){
        return totalNumberOfMoves / totalNumberOfTurns;
    }
}
