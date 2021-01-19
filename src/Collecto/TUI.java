package Collecto;

public class TUI {
    public static String plainTextBoard(GridBoard gridBoard) {
        return plainTextBoard(gridBoard, false);
    }

    public static String plainTextBoard(GridBoard gridBoard, boolean coloured) {
        StringBuilder string = new StringBuilder();

        string.append("     |");
        for (int i = 1; i <= 7; i++) {
            string.append("   ").append(i).append("   |");
        }
        string.append("\n");
        for (int i = 1; i <= 7; i++) {
            string.append("-----+");
            for (int j = 1; j <= 7; j++) {
                string.append("-------+");
            }
            string.append("\n");
            string.append("  ").append(i).append("  |");
            for (int j = 1; j <= 7; j++) {
                if (coloured) string.append(String.format("%-16s|", gridBoard.getField(i-1, j-1).toColouredString()));
                else string.append(String.format("%-7s|", gridBoard.getField(i-1, j-1)));
            }
            string.append("\n");
        }
        string.append("-----+");
        for (int j = 1; j <= 7; j++) {
            string.append("-------+");
        }

        return string.toString();
    }

    public static String colouredBoard(GridBoard gridBoard) {
        return plainTextBoard(gridBoard, true);
    }

    public static String toString(GridBoard gridBoard) {
        StringBuilder string = new StringBuilder();

        for (int i = 1; i <= 7; i++) {
            string.append("+");
            for (int j = 1; j <= 7; j++) {
                string.append("-------+");
            }
            string.append("\n|");
            for (int j = 1; j <= 7; j++) {
                string.append(String.format("%-7s|", gridBoard.getField(i-1, j-1)));
            }
            string.append("\n");
        }
        string.append("+");
        for (int j = 1; j <= 7; j++) {
            string.append("-------+");
        }

        return string.toString();
    }

    public static void main(String[] args) {
        GridBoard board = new GridBoard();
        GridBoard copy = board.deepCopy();

//        board.moveLine(4, 4, GridBoard.Direction.DOWN);
//        System.out.println(board.isMoveValid(4,4, GridBoard.Direction.DOWN));

//        System.out.println(board.checkSurroundings(0, 0));
//        System.out.println(board.possibleMoves());

        System.out.println(toString(board));
        System.out.println(plainTextBoard(board));
        System.out.println(colouredBoard(board));

        System.out.println("Is board the same: "+board.toString().equals(copy.toString()));
    }
}
