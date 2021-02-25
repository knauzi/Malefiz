package model.utils;

public enum Color
{
    RED, GREEN, YELLOW, BLUE;

    public static Color getColorById(int id)
    {
        Color color = null;
        switch (id)
        {
            case 0 -> color = RED;
            case 1 -> color = GREEN;
            case 2 -> color = YELLOW;
            case 3 -> color = BLUE;
            default -> System.out.println("No matching color to id!");
        }
        return color;
    }

    public static String getColorAsStringById(int id)
    {
        String colorAsString = null;
        switch (id)
        {
            case 0 -> colorAsString = "Rot";
            case 1 -> colorAsString = "Grün";
            case 2 -> colorAsString = "Gelb";
            case 3 -> colorAsString = "Blau";
            default -> System.out.println("No matching color to id!");
        }
        return colorAsString;
    }
}
