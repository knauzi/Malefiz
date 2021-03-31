package model.game;

import model.agents.Agent;
import model.utils.Color;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

public class GameLogic
{
    public static void makeMoveOnGame(Game game, Move move)
    {
        Figure movingFigure = move.getFigure();

        // original location of figure to move is now empty
        movingFigure.getPosition().setState(Tile.State.EMPTY);

        // move block if there was one at target tile
        if (move.getTargetOfBlock() != null)
        {
            move.getTargetOfBlock().setState(Tile.State.BLOCKED);
        }

        // beat figure of other agent if there was one at target tile
        if (move.getBeatenColor() != null)
        {
            for (Figure figure : game.getAgent(move.getBeatenColor().ordinal()).getFigures())
            {
                if (figure.getPosition().equals(move.getTargetOfFigure()))
                {
                    sendFigureBackToBase(game, figure);
                }
            }
        }

        // target location is now occupied by figure
        movingFigure.setPosition(move.getTargetOfFigure());
        switch (movingFigure.getColor())
        {
            case RED    -> move.getTargetOfFigure().setState(Tile.State.OCCUPIED_RED);
            case GREEN  -> move.getTargetOfFigure().setState(Tile.State.OCCUPIED_GREEN);
            case YELLOW -> move.getTargetOfFigure().setState(Tile.State.OCCUPIED_YELLOW);
            case BLUE   -> move.getTargetOfFigure().setState(Tile.State.OCCUPIED_BLUE);
            default     -> System.out.println("Couldn't set state of target tile cuz of unknown figure color!");
        }

        game.setLastMove(move);
        game.setPhase(Game.Phase.WAITING_FOR_ROLL);
    }

    public static void undoMoveOnGame(Game game, Move move)
    {
        if (game.getLastMove().equals(move))
        {
            Figure movingFigure = move.getFigure();
            Board board = game.getBoard();

            // put figure back to original location
            movingFigure.setPosition(move.getOldPositionOfFigure());
            move.getTargetOfFigure().setState(Tile.State.EMPTY);
            switch (movingFigure.getColor())
            {
                case RED    -> move.getOldPositionOfFigure().setState(Tile.State.OCCUPIED_RED);
                case GREEN  -> move.getOldPositionOfFigure().setState(Tile.State.OCCUPIED_GREEN);
                case YELLOW -> move.getOldPositionOfFigure().setState(Tile.State.OCCUPIED_YELLOW);
                case BLUE   -> move.getOldPositionOfFigure().setState(Tile.State.OCCUPIED_BLUE);
                default     -> System.out.println("Couldn't set state of old tile cuz of unknown figure color!");
            }

            // undo block move if there was any
            if (move.getTargetOfBlock() != null)
            {
                move.getTargetOfBlock().setState(Tile.State.EMPTY);
                move.getTargetOfFigure().setState(Tile.State.BLOCKED);
            }

            // undo beating of figure if there was any
            if (move.getBeatenColor() != null)
            {
                // target tile of moving figure to occupied by beaten color
                switch (move.getBeatenColor())
                {
                    case RED    -> move.getTargetOfFigure().setState(Tile.State.OCCUPIED_RED);
                    case GREEN  -> move.getTargetOfFigure().setState(Tile.State.OCCUPIED_GREEN);
                    case YELLOW -> move.getTargetOfFigure().setState(Tile.State.OCCUPIED_YELLOW);
                    case BLUE   -> move.getTargetOfFigure().setState(Tile.State.OCCUPIED_BLUE);
                    default     -> System.out.println("Couldn't reset state of target tile cuz of unknown figure color!");
                }

                // get tile of beaten color agents base
                int baseTileID = -1;
                int basePointer = Board.BASE_POINTERS[move.getBeatenColor().ordinal()];
                for (int id = basePointer; id < basePointer + Board.BASE_SIZE; id++)
                {
                    if (board.getTileById(id).getState() != Tile.State.EMPTY)
                    {
                        baseTileID = id;
                        board.getTileById(id).setState(Tile.State.EMPTY);
                        break;
                    }
                }
                if (baseTileID == -1)
                {
                    System.err.println("Tried to get figure from base but it was empty");
                    System.exit(-1);
                }

                // put figure from base tile with id=baseTileID to moving figures target tile
                for (Figure figure : game.getAgent(move.getBeatenColor().ordinal()).getFigures())
                {
                    if (figure.getPosition().getId() == baseTileID)
                    {
                        figure.setPosition(move.getTargetOfFigure());
                        break;
                    }
                }
            }
        }
        else
        {
            System.err.println("Last move made is not equal to the move which is supposed to be undone!");
            System.exit(-1);
        }
    }

    public static boolean isValidMove(Game game, Move move)
    {
        ArrayList<Move> validMoves = getPossibleMovesOfFigure(game.getBoard(), move.getFigure(), move.getDiceResult());
        return validMoves.contains(move);
    }

    public static void sendFigureBackToBase(Game game, Figure figure)
    {
        Board board = game.getBoard();

        // find free tile in base to send figure to
        int baseTileID = -1;
        int basePointer = Board.BASE_POINTERS[figure.getColor().ordinal()];
        for (int id = basePointer; id < basePointer + Board.BASE_SIZE; id++)
        {
            if (board.getTileById(id).getState() == Tile.State.EMPTY)
            {
                baseTileID = id;
                break;
            }
        }
        if (baseTileID == -1)
        {
            System.err.println("Tried to send figure of color " + Color.getColorAsStringById(figure.getColor().ordinal())+ " back to base but base is full!");
            System.exit(-1);
        }

        // change game accordingly
        figure.getPosition().setState(Tile.State.EMPTY);
        figure.setPosition(board.getTileById(baseTileID));
        switch (figure.getColor())
        {
            case RED    -> board.getTileById(baseTileID).setState(Tile.State.OCCUPIED_RED);
            case GREEN  -> board.getTileById(baseTileID).setState(Tile.State.OCCUPIED_GREEN);
            case YELLOW -> board.getTileById(baseTileID).setState(Tile.State.OCCUPIED_YELLOW);
            case BLUE   -> board.getTileById(baseTileID).setState(Tile.State.OCCUPIED_BLUE);
            default     -> System.err.println("Unknown color of figure!");
        }
    }

    /* get all possible target tiles of the figure with given dice result (steps) */
    public static ArrayList<Tile> getPossibleTargetTilesOfFigure(Figure figure, int diceResult)
    {
        Tile root = figure.getPosition();          // start search from figures position
        HashSet<Tile> visited = new HashSet<>();                 // keep track of which tiles were already visited
        ArrayDeque<Tile> queue = new ArrayDeque<>();             // queue of nodes to traverse next
        queue.push(root);

        ArrayList<Tile> possibleTargetTiles = new ArrayList<>(); // save all possible target tile of figure

        Tile currentTile;
        int currentDepth = 0;
        int tilesAtCurrentDepth = 1;
        while (!queue.isEmpty())
        {
            currentTile = queue.remove();

            if (currentDepth == diceResult)
            {
                // if tile is occupied by figure of the same color -> not a possible target tile
                if (currentTile.getState().ordinal() != figure.getColor().ordinal())
                {
                    possibleTargetTiles.add(currentTile);
                }
            }
            else
            {
                if (currentTile.getState() != Tile.State.BLOCKED)
                {
                    for (Tile neighbour : currentTile.getNeighbours())
                    {
                        if (!visited.contains(neighbour))
                        {
                            queue.add(neighbour);
                        }
                    }
                }
            }

            visited.add(currentTile);
            tilesAtCurrentDepth--;

            if (tilesAtCurrentDepth == 0)
            {
                currentDepth++;
                tilesAtCurrentDepth = queue.size();
            }
        }

        return possibleTargetTiles;
    }

    /* get all possible tiles where to move block at given tile */
    public static ArrayList<Tile> getPossibleTargetTilesOfBlock(Board board, Tile blockTile, Figure figure)
    {
        ArrayList<Tile> possibleTargetTilesofBlock = new ArrayList<>();

        Tile[] tiles = board.getTiles();
        for (int i = Board.FIRST_BLOCKABLE_TILE_ID; i < Board.GOAL_TILE_ID; i++)
        {
            if (tiles[i].getState() == Tile.State.EMPTY)
            {
                possibleTargetTilesofBlock.add(tiles[i]);
            }
        }
        //possibleTargetTilesofBlock.add(figure.getPosition());

        return possibleTargetTilesofBlock;
    }

    /* get all possible moves of the figure with given dice result (steps) */
    public static ArrayList<Move> getPossibleMovesOfFigure(Board board, Figure figure, int diceResult)
    {
        ArrayList<Tile> possibleTargetTiles = getPossibleTargetTilesOfFigure(figure, diceResult);

        ArrayList<Move> possibleMoves = new ArrayList<>(); // save all possible moves of figure

        // create moves based on possible target tiles
        for (Tile targetTile : possibleTargetTiles)
        {
            switch (targetTile.getState())
            {
                case BLOCKED -> {
                    Tile[] tiles = board.getTiles();
                    for (int i = Board.FIRST_BLOCKABLE_TILE_ID; i < Board.GOAL_TILE_ID; i++)
                    {
                        if (tiles[i].getState() == Tile.State.EMPTY)
                        {
                            possibleMoves.add(new Move(figure, targetTile, diceResult, tiles[i]));
                        }
                    }
                    //possibleMoves.add(new Move(figure, targetTile, diceResult, figure.getPosition()));
                }
                case EMPTY              -> possibleMoves.add(new Move(figure, targetTile, diceResult));
                case OCCUPIED_RED       -> possibleMoves.add(new Move(figure, targetTile, diceResult, Color.RED));
                case OCCUPIED_GREEN     -> possibleMoves.add(new Move(figure, targetTile, diceResult, Color.GREEN));
                case OCCUPIED_YELLOW    -> possibleMoves.add(new Move(figure, targetTile, diceResult, Color.YELLOW));
                case OCCUPIED_BLUE      -> possibleMoves.add(new Move(figure, targetTile, diceResult, Color.BLUE));
                default -> System.out.println("Couldn't add move to possible moves cuz of unknown target tile state!");
            }
        }

        return possibleMoves;
    }

    /* get all possible moves of the agent with given dice result (steps) */
    public static ArrayList<Move> getPossibleMovesOfAgent(Board board, Agent agent, int diceResult)
    {
        ArrayList<Move> possibleAgentMoves = new ArrayList<>();

        for (Figure figure : agent.getFigures())
        {
            possibleAgentMoves.addAll(getPossibleMovesOfFigure(board, figure, diceResult));
        }

        return possibleAgentMoves;
    }

}
