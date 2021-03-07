package view.gui.utils;

import model.game.Board;
import model.game.Tile;
import view.gui.utils.Point;

import java.util.HashMap;

public class PositionMap
{
    private HashMap<Integer, Point> idToPointMap;

    public PositionMap(Board board)
    {
        initIdToPointMap(board);
    }

    private void initIdToPointMap(Board board)
    {
        idToPointMap = new HashMap<>();

        // tiles 0 to 110 -> arranged in grid
        for (int id = 0; id < 111; id++)
        {
            Tile tile = board.getTileById(id);
            double x = 33 + tile.getXGrid() * 33.5;
            double y = 66 + (tile.getYGrid() - 1) * 33.5;
            idToPointMap.put(tile.getId(), new Point(x, y));
        }

        // goal tile 111
        idToPointMap.put(111, new Point(301, 25));

        // base tiles 122 to 131
        for (int id = 112, i = 0; id < 132; id+=5, i++)
        {
            idToPointMap.put(id,     new Point( 99.5 + i * 134, 501));
            idToPointMap.put(id + 1, new Point(  140 + i * 134, 530));
            idToPointMap.put(id + 2, new Point(  125 + i * 134, 578));
            idToPointMap.put(id + 3, new Point(   76 + i * 134, 578));
            idToPointMap.put(id + 4, new Point(   60 + i * 134, 530));
        }
    }

    public Point getPoint(int id)
    {
        return idToPointMap.get(id);
    }
}
