package model.game;

import java.util.Arrays;

public class Tile
{
    public enum State
    {
        OCCUPIED_RED, OCCUPIED_GREEN, OCCUPIED_YELLOW, OCCUPIED_BLUE, EMPTY, BLOCKED
    }

    /* unique identifier of this tile */
    private final int id;

    /* position in grid (representation of board in console) */
    private final int xGrid;
    private final int yGrid;

    /* distance to goal tile */
    private final int dist;

    /* current state of the tile */
    private Tile.State state;

    /* id's of neighbours */
    private Tile[] neighbours;

    public Tile(int id, int dist, int xGrid, int yGrid, Tile.State state)
    {
        this.id = id;
        this.dist = dist;
        this.xGrid = xGrid;
        this.yGrid = yGrid;
        this.state = state;
    }

    public Tile(Tile tile)
    {
        this.id = tile.getId();
        this.dist = tile.getDist();
        this.xGrid = tile.getXGrid();
        this.yGrid = tile.getYGrid();
        this.state = tile.getState();
        this.neighbours = Arrays.stream(tile.getNeighbours()).map(Tile::new).toArray(Tile[]::new);
    }

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    public int getDist()
    {
        return dist;
    }

    public int getYGrid()
    {
        return yGrid;
    }

    public int getXGrid()
    {
        return xGrid;
    }

    public int getId()
    {
        return id;
    }

    public Tile[] getNeighbours()
    {
        return neighbours;
    }

    public void setNeighbours(Tile[] neighbours)
    {
        this.neighbours = neighbours;
    }

    @Override
    public int hashCode()
    {
        return id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Tile other = (Tile) o;
        return this.id == other.id;
    }

    @Override
    public String toString()
    {
        String state;
        switch (this.state)
        {
            case EMPTY            -> state = "empty";
            case BLOCKED          -> state = "blocked";
            case OCCUPIED_RED     -> state = "occupied by red";
            case OCCUPIED_GREEN   -> state = "occupied by gree";
            case OCCUPIED_YELLOW  -> state = "occupied by yellow";
            case OCCUPIED_BLUE    -> state = "occupied by blue";
            default               -> state = "unknown state";
        }

        StringBuilder neighbours = new StringBuilder("" + this.neighbours[0].getId());
        for (int i = 1; i < this.neighbours.length; i++)
        {
            neighbours.append(", ").append(this.neighbours[i].getId());
        }

        return "Tile " + id + ": \n\txGrid: " + xGrid + "; yGrid: " + yGrid + "\n\tdistance: "
                + dist + "\n\tstate: " + state + "\n\tneighbours: " + neighbours;
    }
}
