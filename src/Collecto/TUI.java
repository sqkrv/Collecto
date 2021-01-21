package Collecto;

import java.util.HashMap;
import java.util.Map;

public class TUI {
    private static String grey(String string) {
        return "\u001B[37m" + string + "\u001B[0m";
    }

    public static String playersBoard(Player player1, Player player2) {
        StringBuilder string = new StringBuilder();
        HashMap<Ball, Integer> p1Balls = player1.showBalls();
        HashMap<Ball, Integer> p2Balls = player2.showBalls();

        int max_length = player1.getName().length();
        if (player2.getName().length() > max_length) max_length = player2.getName().length();

        string.append(" ".repeat(max_length+1)).append(grey("|"));
        for (Map.Entry<Ball, Integer> pair : p1Balls.entrySet()) {
            string.append(String.format("%-16s"+grey("|"), pair.getKey().toColouredString()));
        }
        string.append("Points ").append(grey("|"));

        string.append(player_row(player1, p1Balls, max_length));

        string.append(player_row(player2, p2Balls, max_length));

        string.append("\n");
        string.append(grey("-".repeat(max_length+1)+"+"));
        string.append(grey("-------+").repeat(7));

        return string.toString();
    }

    private static String player_row(Player player, HashMap<Ball, Integer> balls, int max_length) {
        StringBuilder string = new StringBuilder();

        string.append("\n");
        string.append(grey("-".repeat(max_length+1)+"+"));
        string.append(grey("-------+".repeat(7)));
        string.append("\n");
        string.append(String.format("%"+(max_length+1)+"s"+grey("|"), player.getName()));
        for (Map.Entry<Ball, Integer> pair : balls.entrySet()) {
            string.append(String.format("%-7s"+grey("|"), pair.getValue()));
        }
        string.append(String.format("%-7s", player.getPoints())).append(grey("|"));

        return string.toString();
    }

    public static String plainTextBoard(GridBoard gridBoard) {
        return plainTextBoard(gridBoard, false);
    }

    private static String plainTextBoard(GridBoard gridBoard, boolean coloured) {
        StringBuilder string = new StringBuilder();

        string.append("     |");
        for (int i = 1; i <= 7; i++) {
            string.append("   ").append(i).append("   |");
        }
        string.append("\n");
        for (int i = 1; i <= 7; i++) {
            string.append("-----+");
            string.append("-------+".repeat(7));
            string.append("\n");
            string.append("  ").append(i).append("  |");
            for (int j = 1; j <= 7; j++) {
                if (coloured) string.append(String.format("%-16s|", gridBoard.getField(i-1, j-1).toColouredString()));
                else string.append(String.format("%-7s|", gridBoard.getField(i-1, j-1)));
            }
            string.append("\n");
        }
        string.append("-----+");
        string.append("-------+".repeat(7));

        return string.toString();
    }

    public static String colouredBoard(GridBoard gridBoard) {
        return plainTextBoard(gridBoard, true);
    }

    public static String toString(GridBoard gridBoard) {
        StringBuilder string = new StringBuilder();

        for (int i = 1; i <= 7; i++) {
            string.append("+");
            string.append("-------+".repeat(7));
            string.append("\n|");
            for (int j = 1; j <= 7; j++) {
                string.append(String.format("%-7s|", gridBoard.getField(i-1, j-1)));
            }
            string.append("\n");
        }
        string.append("+");
        string.append("-------+".repeat(7));

        return string.toString();
    }

    public static void print(String string) {
        System.out.println(string);
    }

    public static void printError(String error) {
        String cerror = "["+"\u001B[31m" + "ERROR" + "\u001B[0m"+"]";
        System.out.println(Misc.logTime(false)+cerror+" "+error);
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
