package Collecto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TUI {
    static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM");

    public static String logTime() {
        return "["+currentDateTime()+"] ";
    }

    public static String currentDateTime() {
        Date date = new Date();
        return dateFormat.format(date);
    }

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
        error = "["+"\u001B[31m" + "ERROR" + "\u001B[0m"+"] " + error;
        log(error);
    }

    public static String log(String log) {
        return Misc.logTime() + log;
    }

    public static void printHelpClient() {
        print("Valid inputs are: " + ClientController.COMMANDS + "\n" +
                "These can be upper or lowercase\n" +
                "To make a move, typ: MOVE [row/column] [direction]\n" +
                "For a double move: MOVE [row/column] [direction] [row/column] [direction]\n" +
                "Where [row/column] is an integer, and direction is UP/DOWN/LEFT/RIGHT");
    }

    public static String ballColours(ArrayList<Ball> balls) {
        String output = "";
        for (Ball ball : balls) {
            output += (ball.ballColour() + "\u25CF" + Ball.WHITE.ballColour());
        }
        return output;
    }
}
