package view.gui.utils;

public class Point
{
    private final double x;
    private final double y;

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    @Override
    public int hashCode()
    {
        return (((int) x) << 8) + ((int ) y);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Point other = (Point) o;
        return this.x == other.getX() &&
                this.y == other.getY();
    }

}
