package model.game;

import java.util.Arrays;
import java.util.StringJoiner;

public class Board
{
    /* dimensions of board */
    public static final int HEIGHT = 19;
    public static final int WIDTH = 17;

    /* indices of first tile of each players base in tiles array */
    public static final int[] BASE_POINTERS = new int[]{ 112, 117, 122, 127 };

    /* size of base (equal to number of figures every player has) */
    public static final int BASE_SIZE = 5;

    /* all tiles with id less than 16 are not blockable */
    public static final int FIRST_BLOCKABLE_TILE_ID = 17;

    /* id of goal tile */
    public static final int GOAL_TILE_ID = 111;

    /* containing all tiles of the board */
    private Tile[] tiles;

    public Board()
    {
        initBoard();
    }

    public Board(Board board)
    {
        assert board.tiles != null;
        tiles = Arrays.stream(board.tiles).map(Tile::new).toArray(Tile[]::new);
    }

    public Tile getTileById(int id)
    {
        return tiles[id];
    }

    public Tile[] getTiles()
    {
        return tiles;
    }

    @Override
    public String toString()
    {
        String[][] board = new String[HEIGHT][WIDTH];
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[0].length; j++)
            {
                board[i][j] = " ";
            }
        }

        for (Tile tile : tiles)
        {
            switch (tile.getState())
            {
                case EMPTY              -> board[tile.getYGrid()][tile.getXGrid()] = "O";
                case BLOCKED            -> board[tile.getYGrid()][tile.getXGrid()] = "X";
                case OCCUPIED_RED       -> board[tile.getYGrid()][tile.getXGrid()] = "R";
                case OCCUPIED_GREEN     -> board[tile.getYGrid()][tile.getXGrid()] = "G";
                case OCCUPIED_YELLOW    -> board[tile.getYGrid()][tile.getXGrid()] = "Y";
                case OCCUPIED_BLUE      -> board[tile.getYGrid()][tile.getXGrid()] = "B";
                default                 -> System.out.println("Error in switch statement, unknown state!");
            }
        }

        // convert string array to string
        StringJoiner sj = new StringJoiner(System.lineSeparator());
        for (String[] row : board) {
            sj.add(String.join(" ", row));
        }

        return sj.toString();
    }

    public void initBoard()
    {
        tiles = new Tile[132];

        // row 1
        tiles[  0] = new Tile(  0, 37,  0, 13,    Tile.State.EMPTY);
        tiles[  1] = new Tile(  1, 38,  1, 13,    Tile.State.EMPTY);
        tiles[  2] = new Tile(  2, 39,  2, 13,    Tile.State.EMPTY);
        tiles[  3] = new Tile(  3, 38,  3, 13,    Tile.State.EMPTY);
        tiles[  4] = new Tile(  4, 37,  4, 13,    Tile.State.EMPTY);
        tiles[  5] = new Tile(  5, 38,  5, 13,    Tile.State.EMPTY);
        tiles[  6] = new Tile(  6, 39,  6, 13,    Tile.State.EMPTY);
        tiles[  7] = new Tile(  7, 38,  7, 13,    Tile.State.EMPTY);
        tiles[  8] = new Tile(  8, 37,  8, 13,    Tile.State.EMPTY);
        tiles[  9] = new Tile(  9, 38,  9, 13,    Tile.State.EMPTY);
        tiles[ 10] = new Tile( 10, 39, 10, 13,    Tile.State.EMPTY);
        tiles[ 11] = new Tile( 11, 38, 11, 13,    Tile.State.EMPTY);
        tiles[ 12] = new Tile( 12, 37, 12, 13,    Tile.State.EMPTY);
        tiles[ 13] = new Tile( 13, 38, 13, 13,    Tile.State.EMPTY);
        tiles[ 14] = new Tile( 14, 39, 14, 13,    Tile.State.EMPTY);
        tiles[ 15] = new Tile( 15, 38, 15, 13,    Tile.State.EMPTY);
        tiles[ 16] = new Tile( 16, 37, 16, 13,    Tile.State.EMPTY);

        // row 2
        tiles[ 17] = new Tile( 17, 36,  0, 12,    Tile.State.EMPTY);
        tiles[ 18] = new Tile( 18, 36,  4, 12,    Tile.State.EMPTY);
        tiles[ 19] = new Tile( 19, 36,  8, 12,    Tile.State.EMPTY);
        tiles[ 20] = new Tile( 20, 36, 12, 12,    Tile.State.EMPTY);
        tiles[ 21] = new Tile( 21, 36, 16, 12,    Tile.State.EMPTY);

        // row 3
        tiles[ 22] = new Tile( 22, 35,  0, 11,  Tile.State.BLOCKED);
        tiles[ 23] = new Tile( 23, 34,  1, 11,    Tile.State.EMPTY);
        tiles[ 24] = new Tile( 24, 33,  2, 11,    Tile.State.EMPTY);
        tiles[ 25] = new Tile( 25, 34,  3, 11,    Tile.State.EMPTY);
        tiles[ 26] = new Tile( 26, 35,  4, 11,  Tile.State.BLOCKED);
        tiles[ 27] = new Tile( 27, 34,  5, 11,    Tile.State.EMPTY);
        tiles[ 28] = new Tile( 28, 33,  6, 11,    Tile.State.EMPTY);
        tiles[ 29] = new Tile( 29, 34,  7, 11,    Tile.State.EMPTY);
        tiles[ 30] = new Tile( 30, 35,  8, 11,  Tile.State.BLOCKED);
        tiles[ 31] = new Tile( 31, 34,  9, 11,    Tile.State.EMPTY);
        tiles[ 32] = new Tile( 32, 33, 10, 11,    Tile.State.EMPTY);
        tiles[ 33] = new Tile( 33, 34, 11, 11,    Tile.State.EMPTY);
        tiles[ 34] = new Tile( 34, 35, 12, 11,  Tile.State.BLOCKED);
        tiles[ 35] = new Tile( 35, 34, 13, 11,    Tile.State.EMPTY);
        tiles[ 36] = new Tile( 36, 33, 14, 11,    Tile.State.EMPTY);
        tiles[ 37] = new Tile( 37, 34, 15, 11,    Tile.State.EMPTY);
        tiles[ 38] = new Tile( 38, 35, 16, 11,  Tile.State.BLOCKED);

        // row 4
        tiles[ 39] = new Tile( 39, 32,  2, 10,    Tile.State.EMPTY);
        tiles[ 40] = new Tile( 40, 32,  6, 10,    Tile.State.EMPTY);
        tiles[ 41] = new Tile( 41, 32, 10, 10,    Tile.State.EMPTY);
        tiles[ 42] = new Tile( 42, 32, 14, 10,    Tile.State.EMPTY);

        // row 5
        tiles[ 43] = new Tile( 43, 31,  2,  9,    Tile.State.EMPTY);
        tiles[ 44] = new Tile( 44, 30,  3,  9,    Tile.State.EMPTY);
        tiles[ 45] = new Tile( 45, 29,  4,  9,    Tile.State.EMPTY);
        tiles[ 46] = new Tile( 46, 30,  5,  9,    Tile.State.EMPTY);
        tiles[ 47] = new Tile( 47, 31,  6,  9,    Tile.State.EMPTY);
        tiles[ 48] = new Tile( 48, 32,  7,  9,    Tile.State.EMPTY);
        tiles[ 49] = new Tile( 49, 33,  8,  9,    Tile.State.EMPTY);
        tiles[ 50] = new Tile( 50, 32,  9,  9,    Tile.State.EMPTY);
        tiles[ 51] = new Tile( 51, 31, 10,  9,    Tile.State.EMPTY);
        tiles[ 52] = new Tile( 52, 30, 11,  9,    Tile.State.EMPTY);
        tiles[ 53] = new Tile( 53, 29, 12,  9,    Tile.State.EMPTY);
        tiles[ 54] = new Tile( 54, 30, 13,  9,    Tile.State.EMPTY);
        tiles[ 55] = new Tile( 55, 31, 14,  9,    Tile.State.EMPTY);

        // row 6
        tiles[ 56] = new Tile( 56, 28,  4,  8,    Tile.State.EMPTY);
        tiles[ 57] = new Tile( 57, 28, 12,  8,    Tile.State.EMPTY);

        // row 7
        tiles[ 58] = new Tile( 58, 27,  4,  7,    Tile.State.EMPTY);
        tiles[ 59] = new Tile( 59, 26,  5,  7,    Tile.State.EMPTY);
        tiles[ 60] = new Tile( 60, 25,  6,  7,  Tile.State.BLOCKED);
        tiles[ 61] = new Tile( 61, 26,  7,  7,    Tile.State.EMPTY);
        tiles[ 62] = new Tile( 62, 27,  8,  7,    Tile.State.EMPTY);
        tiles[ 63] = new Tile( 63, 26,  9,  7,    Tile.State.EMPTY);
        tiles[ 64] = new Tile( 64, 25, 10,  7,  Tile.State.BLOCKED);
        tiles[ 65] = new Tile( 65, 26, 11,  7,    Tile.State.EMPTY);
        tiles[ 66] = new Tile( 66, 27, 12,  7,    Tile.State.EMPTY);

        // row 8
        tiles[ 67] = new Tile( 67, 24,  6,  6,    Tile.State.EMPTY);
        tiles[ 68] = new Tile( 68, 24, 10,  6,    Tile.State.EMPTY);

        // row 9
        tiles[ 69] = new Tile( 69, 23,  6,  5,    Tile.State.EMPTY);
        tiles[ 70] = new Tile( 70, 22,  7,  5,    Tile.State.EMPTY);
        tiles[ 71] = new Tile( 71, 21,  8,  5,  Tile.State.BLOCKED);
        tiles[ 72] = new Tile( 72, 22,  9,  5,    Tile.State.EMPTY);
        tiles[ 73] = new Tile( 73, 23, 10,  5,    Tile.State.EMPTY);

        // row 10
        tiles[ 74] = new Tile( 74, 20,  8,  4,  Tile.State.BLOCKED);

        // row 11
        tiles[ 75] = new Tile( 75, 11,  0,  3,    Tile.State.EMPTY);
        tiles[ 76] = new Tile( 76, 12,  1,  3,    Tile.State.EMPTY);
        tiles[ 77] = new Tile( 77, 13,  2,  3,    Tile.State.EMPTY);
        tiles[ 78] = new Tile( 78, 14,  3,  3,    Tile.State.EMPTY);
        tiles[ 79] = new Tile( 79, 15,  4,  3,    Tile.State.EMPTY);
        tiles[ 80] = new Tile( 80, 16,  5,  3,    Tile.State.EMPTY);
        tiles[ 81] = new Tile( 81, 17,  6,  3,    Tile.State.EMPTY);
        tiles[ 82] = new Tile( 82, 18,  7,  3,    Tile.State.EMPTY);
        tiles[ 83] = new Tile( 83, 19,  8,  3,  Tile.State.BLOCKED);
        tiles[ 84] = new Tile( 84, 18,  9,  3,    Tile.State.EMPTY);
        tiles[ 85] = new Tile( 85, 17, 10,  3,    Tile.State.EMPTY);
        tiles[ 86] = new Tile( 86, 16, 11,  3,    Tile.State.EMPTY);
        tiles[ 87] = new Tile( 87, 15, 12,  3,    Tile.State.EMPTY);
        tiles[ 88] = new Tile( 88, 14, 13,  3,    Tile.State.EMPTY);
        tiles[ 89] = new Tile( 89, 13, 14,  3,    Tile.State.EMPTY);
        tiles[ 90] = new Tile( 90, 12, 15,  3,    Tile.State.EMPTY);
        tiles[ 91] = new Tile( 91, 11, 16,  3,    Tile.State.EMPTY);

        // row 12
        tiles[ 92] = new Tile( 92, 10,  0,  2,    Tile.State.EMPTY);
        tiles[ 93] = new Tile( 93, 10, 16,  2,    Tile.State.EMPTY);

        // row 13
        tiles[ 94] = new Tile( 94,  9,  0,  1,    Tile.State.EMPTY);
        tiles[ 95] = new Tile( 95,  8,  1,  1,    Tile.State.EMPTY);
        tiles[ 96] = new Tile( 96,  7,  2,  1,    Tile.State.EMPTY);
        tiles[ 97] = new Tile( 97,  6,  3,  1,    Tile.State.EMPTY);
        tiles[ 98] = new Tile( 98,  5,  4,  1,    Tile.State.EMPTY);
        tiles[ 99] = new Tile( 99,  4,  5,  1,    Tile.State.EMPTY);
        tiles[100] = new Tile(100,  3,  6,  1,    Tile.State.EMPTY);
        tiles[101] = new Tile(101,  2,  7,  1,    Tile.State.EMPTY);
        tiles[102] = new Tile(102,  1,  8,  1,  Tile.State.BLOCKED);
        tiles[103] = new Tile(103,  2,  9,  1,    Tile.State.EMPTY);
        tiles[104] = new Tile(104,  3, 10,  1,    Tile.State.EMPTY);
        tiles[105] = new Tile(105,  4, 11,  1,    Tile.State.EMPTY);
        tiles[106] = new Tile(106,  5, 12,  1,    Tile.State.EMPTY);
        tiles[107] = new Tile(107,  6, 13,  1,    Tile.State.EMPTY);
        tiles[108] = new Tile(108,  7, 14,  1,    Tile.State.EMPTY);
        tiles[109] = new Tile(109,  8, 15,  1,    Tile.State.EMPTY);
        tiles[110] = new Tile(110,  9, 16,  1,    Tile.State.EMPTY);

        // row 14 (goal)
        tiles[111] = new Tile(111,  0,  8,  0,    Tile.State.EMPTY);

        // base red
        tiles[112] = new Tile(112, 40,  2, 14,    Tile.State.OCCUPIED_RED);
        tiles[113] = new Tile(113, 40,  2, 15,    Tile.State.OCCUPIED_RED);
        tiles[114] = new Tile(114, 40,  2, 16,    Tile.State.OCCUPIED_RED);
        tiles[115] = new Tile(115, 40,  2, 17,    Tile.State.OCCUPIED_RED);
        tiles[116] = new Tile(116, 40,  2, 18,    Tile.State.OCCUPIED_RED);

        // base green
        tiles[117] = new Tile(117, 40,  6, 14,  Tile.State.OCCUPIED_GREEN);
        tiles[118] = new Tile(118, 40,  6, 15,  Tile.State.OCCUPIED_GREEN);
        tiles[119] = new Tile(119, 40,  6, 16,  Tile.State.OCCUPIED_GREEN);
        tiles[120] = new Tile(120, 40,  6, 17,  Tile.State.OCCUPIED_GREEN);
        tiles[121] = new Tile(121, 40,  6, 18,  Tile.State.OCCUPIED_GREEN);

        // base yellow
        tiles[122] = new Tile(122, 40, 10, 14, Tile.State.OCCUPIED_YELLOW);
        tiles[123] = new Tile(123, 40, 10, 15, Tile.State.OCCUPIED_YELLOW);
        tiles[124] = new Tile(124, 40, 10, 16, Tile.State.OCCUPIED_YELLOW);
        tiles[125] = new Tile(125, 40, 10, 17, Tile.State.OCCUPIED_YELLOW);
        tiles[126] = new Tile(126, 40, 10, 18, Tile.State.OCCUPIED_YELLOW);

        // base blue
        tiles[127] = new Tile(127, 40, 14, 14,   Tile.State.OCCUPIED_BLUE);
        tiles[128] = new Tile(128, 40, 14, 15,   Tile.State.OCCUPIED_BLUE);
        tiles[129] = new Tile(129, 40, 14, 16,   Tile.State.OCCUPIED_BLUE);
        tiles[130] = new Tile(130, 40, 14, 17,   Tile.State.OCCUPIED_BLUE);
        tiles[131] = new Tile(131, 40, 14, 18,   Tile.State.OCCUPIED_BLUE);

        // connect tiles by defining the neighbours of each tile

        // row 1
        tiles[  0].setNeighbours(new Tile[]{ tiles[1],  tiles[17] });
        tiles[  1].setNeighbours(new Tile[]{ tiles[0],  tiles[2] });
        tiles[  2].setNeighbours(new Tile[]{ tiles[1],  tiles[3] });
        tiles[  3].setNeighbours(new Tile[]{ tiles[2],  tiles[4] });
        tiles[  4].setNeighbours(new Tile[]{ tiles[3],  tiles[5], tiles[18] });
        tiles[  5].setNeighbours(new Tile[]{ tiles[4],  tiles[6] });
        tiles[  6].setNeighbours(new Tile[]{ tiles[5],  tiles[7] });
        tiles[  7].setNeighbours(new Tile[]{ tiles[6],  tiles[8] });
        tiles[  8].setNeighbours(new Tile[]{ tiles[7],  tiles[9], tiles[19] });
        tiles[  9].setNeighbours(new Tile[]{ tiles[8],  tiles[10] });
        tiles[ 10].setNeighbours(new Tile[]{ tiles[9],  tiles[11] });
        tiles[ 11].setNeighbours(new Tile[]{ tiles[10], tiles[12] });
        tiles[ 12].setNeighbours(new Tile[]{ tiles[11], tiles[13], tiles[20] });
        tiles[ 13].setNeighbours(new Tile[]{ tiles[12], tiles[14] });
        tiles[ 14].setNeighbours(new Tile[]{ tiles[13], tiles[15] });
        tiles[ 15].setNeighbours(new Tile[]{ tiles[14], tiles[16] });
        tiles[ 16].setNeighbours(new Tile[]{ tiles[15], tiles[21] });

        // row 2
        tiles[ 17].setNeighbours(new Tile[]{ tiles[0],  tiles[22] });
        tiles[ 18].setNeighbours(new Tile[]{ tiles[4],  tiles[26] });
        tiles[ 19].setNeighbours(new Tile[]{ tiles[8],  tiles[30] });
        tiles[ 20].setNeighbours(new Tile[]{ tiles[12], tiles[34] });
        tiles[ 21].setNeighbours(new Tile[]{ tiles[16], tiles[38] });

        // row 3
        tiles[ 22].setNeighbours(new Tile[]{ tiles[17], tiles[23] });
        tiles[ 23].setNeighbours(new Tile[]{ tiles[22], tiles[24] });
        tiles[ 24].setNeighbours(new Tile[]{ tiles[23], tiles[25], tiles[39] });
        tiles[ 25].setNeighbours(new Tile[]{ tiles[24], tiles[26] });
        tiles[ 26].setNeighbours(new Tile[]{ tiles[18], tiles[25], tiles[27] });
        tiles[ 27].setNeighbours(new Tile[]{ tiles[26], tiles[28] });
        tiles[ 28].setNeighbours(new Tile[]{ tiles[27], tiles[29], tiles[40] });
        tiles[ 29].setNeighbours(new Tile[]{ tiles[28], tiles[30] });
        tiles[ 30].setNeighbours(new Tile[]{ tiles[19], tiles[29], tiles[31] });
        tiles[ 31].setNeighbours(new Tile[]{ tiles[30], tiles[32] });
        tiles[ 32].setNeighbours(new Tile[]{ tiles[31], tiles[33], tiles[41] });
        tiles[ 33].setNeighbours(new Tile[]{ tiles[32], tiles[34] });
        tiles[ 34].setNeighbours(new Tile[]{ tiles[20], tiles[33], tiles[35] });
        tiles[ 35].setNeighbours(new Tile[]{ tiles[34], tiles[36] });
        tiles[ 36].setNeighbours(new Tile[]{ tiles[35], tiles[37], tiles[42] });
        tiles[ 37].setNeighbours(new Tile[]{ tiles[36], tiles[38] });
        tiles[ 38].setNeighbours(new Tile[]{ tiles[21], tiles[37] });

        // row 4
        tiles[ 39].setNeighbours(new Tile[]{ tiles[24], tiles[43] });
        tiles[ 40].setNeighbours(new Tile[]{ tiles[28], tiles[47] });
        tiles[ 41].setNeighbours(new Tile[]{ tiles[32], tiles[51] });
        tiles[ 42].setNeighbours(new Tile[]{ tiles[36], tiles[55] });

        // row 5
        tiles[ 43].setNeighbours(new Tile[]{ tiles[39], tiles[44] });
        tiles[ 44].setNeighbours(new Tile[]{ tiles[43], tiles[45] });
        tiles[ 45].setNeighbours(new Tile[]{ tiles[44], tiles[46], tiles[56] });
        tiles[ 46].setNeighbours(new Tile[]{ tiles[45], tiles[47] });
        tiles[ 47].setNeighbours(new Tile[]{ tiles[40], tiles[46], tiles[48] });
        tiles[ 48].setNeighbours(new Tile[]{ tiles[47], tiles[49] });
        tiles[ 49].setNeighbours(new Tile[]{ tiles[48], tiles[50] });
        tiles[ 50].setNeighbours(new Tile[]{ tiles[49], tiles[51] });
        tiles[ 51].setNeighbours(new Tile[]{ tiles[41], tiles[50], tiles[52] });
        tiles[ 52].setNeighbours(new Tile[]{ tiles[51], tiles[53] });
        tiles[ 53].setNeighbours(new Tile[]{ tiles[52], tiles[54], tiles[57] });
        tiles[ 54].setNeighbours(new Tile[]{ tiles[53], tiles[55] });
        tiles[ 55].setNeighbours(new Tile[]{ tiles[42], tiles[54] });

        // row 6
        tiles[ 56].setNeighbours(new Tile[]{ tiles[45], tiles[58] });
        tiles[ 57].setNeighbours(new Tile[]{ tiles[53], tiles[66] });

        // row 7
        tiles[ 58].setNeighbours(new Tile[]{ tiles[56], tiles[59] });
        tiles[ 59].setNeighbours(new Tile[]{ tiles[58], tiles[60] });
        tiles[ 60].setNeighbours(new Tile[]{ tiles[59], tiles[61], tiles[67] });
        tiles[ 61].setNeighbours(new Tile[]{ tiles[60], tiles[62] });
        tiles[ 62].setNeighbours(new Tile[]{ tiles[61], tiles[63] });
        tiles[ 63].setNeighbours(new Tile[]{ tiles[62], tiles[64] });
        tiles[ 64].setNeighbours(new Tile[]{ tiles[63], tiles[65], tiles[68] });
        tiles[ 65].setNeighbours(new Tile[]{ tiles[64], tiles[66] });
        tiles[ 66].setNeighbours(new Tile[]{ tiles[57], tiles[65] });

        // row 8
        tiles[ 67].setNeighbours(new Tile[]{ tiles[60], tiles[69] });
        tiles[ 68].setNeighbours(new Tile[]{ tiles[64], tiles[73] });

        // row 9
        tiles[ 69].setNeighbours(new Tile[]{ tiles[67], tiles[70] });
        tiles[ 70].setNeighbours(new Tile[]{ tiles[69], tiles[71] });
        tiles[ 71].setNeighbours(new Tile[]{ tiles[70], tiles[72], tiles[74] });
        tiles[ 72].setNeighbours(new Tile[]{ tiles[71], tiles[73] });
        tiles[ 73].setNeighbours(new Tile[]{ tiles[68], tiles[72] });

        // row 10
        tiles[ 74].setNeighbours(new Tile[]{ tiles[71], tiles[83] });

        // row 11
        tiles[ 75].setNeighbours(new Tile[]{ tiles[76], tiles[92] });
        tiles[ 76].setNeighbours(new Tile[]{ tiles[75], tiles[77] });
        tiles[ 77].setNeighbours(new Tile[]{ tiles[76], tiles[78] });
        tiles[ 78].setNeighbours(new Tile[]{ tiles[77], tiles[79] });
        tiles[ 79].setNeighbours(new Tile[]{ tiles[78], tiles[80] });
        tiles[ 80].setNeighbours(new Tile[]{ tiles[79], tiles[81] });
        tiles[ 81].setNeighbours(new Tile[]{ tiles[80], tiles[82] });
        tiles[ 82].setNeighbours(new Tile[]{ tiles[81], tiles[83] });
        tiles[ 83].setNeighbours(new Tile[]{ tiles[74], tiles[82], tiles[84] });
        tiles[ 84].setNeighbours(new Tile[]{ tiles[83], tiles[85] });
        tiles[ 85].setNeighbours(new Tile[]{ tiles[84], tiles[86] });
        tiles[ 86].setNeighbours(new Tile[]{ tiles[85], tiles[87] });
        tiles[ 87].setNeighbours(new Tile[]{ tiles[86], tiles[88] });
        tiles[ 88].setNeighbours(new Tile[]{ tiles[87], tiles[89] });
        tiles[ 89].setNeighbours(new Tile[]{ tiles[88], tiles[90] });
        tiles[ 90].setNeighbours(new Tile[]{ tiles[89], tiles[91] });
        tiles[ 91].setNeighbours(new Tile[]{ tiles[90], tiles[93] });

        // row 12
        tiles[ 92].setNeighbours(new Tile[]{ tiles[75], tiles[94] });
        tiles[ 93].setNeighbours(new Tile[]{ tiles[91], tiles[110] });

        // row 13
        tiles[ 94].setNeighbours(new Tile[]{ tiles[92],  tiles[95] });
        tiles[ 95].setNeighbours(new Tile[]{ tiles[94],  tiles[96] });
        tiles[ 96].setNeighbours(new Tile[]{ tiles[95],  tiles[97] });
        tiles[ 97].setNeighbours(new Tile[]{ tiles[96],  tiles[98] });
        tiles[ 98].setNeighbours(new Tile[]{ tiles[97],  tiles[99] });
        tiles[ 99].setNeighbours(new Tile[]{ tiles[98],  tiles[100] });
        tiles[100].setNeighbours(new Tile[]{ tiles[99],  tiles[101] });
        tiles[101].setNeighbours(new Tile[]{ tiles[100], tiles[102] });
        tiles[102].setNeighbours(new Tile[]{ tiles[101], tiles[103], tiles[111] });
        tiles[103].setNeighbours(new Tile[]{ tiles[102], tiles[104] });
        tiles[104].setNeighbours(new Tile[]{ tiles[103], tiles[105] });
        tiles[105].setNeighbours(new Tile[]{ tiles[104], tiles[106] });
        tiles[106].setNeighbours(new Tile[]{ tiles[105], tiles[107] });
        tiles[107].setNeighbours(new Tile[]{ tiles[106], tiles[108] });
        tiles[108].setNeighbours(new Tile[]{ tiles[107], tiles[109] });
        tiles[109].setNeighbours(new Tile[]{ tiles[108], tiles[110] });
        tiles[110].setNeighbours(new Tile[]{ tiles[93],  tiles[109] });

        // row 14 (goal)
        tiles[111].setNeighbours(new Tile[]{ tiles[102] });

        // base red
        tiles[112].setNeighbours(new Tile[]{ tiles[2] });
        tiles[113].setNeighbours(new Tile[]{ tiles[2] });
        tiles[114].setNeighbours(new Tile[]{ tiles[2] });
        tiles[115].setNeighbours(new Tile[]{ tiles[2] });
        tiles[116].setNeighbours(new Tile[]{ tiles[2] });

        // base green
        tiles[117].setNeighbours(new Tile[]{ tiles[6] });
        tiles[118].setNeighbours(new Tile[]{ tiles[6] });
        tiles[119].setNeighbours(new Tile[]{ tiles[6] });
        tiles[120].setNeighbours(new Tile[]{ tiles[6] });
        tiles[121].setNeighbours(new Tile[]{ tiles[6] });

        // base yellow
        tiles[122].setNeighbours(new Tile[]{ tiles[10] });
        tiles[123].setNeighbours(new Tile[]{ tiles[10] });
        tiles[124].setNeighbours(new Tile[]{ tiles[10] });
        tiles[125].setNeighbours(new Tile[]{ tiles[10] });
        tiles[126].setNeighbours(new Tile[]{ tiles[10] });

        // base blue
        tiles[127].setNeighbours(new Tile[]{ tiles[14] });
        tiles[128].setNeighbours(new Tile[]{ tiles[14] });
        tiles[129].setNeighbours(new Tile[]{ tiles[14] });
        tiles[130].setNeighbours(new Tile[]{ tiles[14] });
        tiles[131].setNeighbours(new Tile[]{ tiles[14] });
    }

}
