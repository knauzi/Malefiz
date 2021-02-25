package model.agents;

import model.game.Board;
import model.game.Game;
import model.game.GameLogic;
import model.game.Move;
import model.utils.Color;

import java.util.ArrayList;
import java.util.Random;

public class RandomAI extends ArtificialAgent
{
    public RandomAI(Color color, Board board)
    {
        super(color, board);
    }

    @Override
    public Move getNextMove(Game game, int diceResult)
    {
        ArrayList<Move> possibleMoves = GameLogic.getPossibleMovesOfAgent(game.getBoard(), this, diceResult);
        int randomMoveID = (new Random()).nextInt(possibleMoves.size());
        return possibleMoves.isEmpty() ? null : possibleMoves.get(randomMoveID);
    }
}
