package Collecto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static Collecto.Colour.red;

public class TUI {
    public static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM");

    private static String currentDateTime() {
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String logTime() {
        return Colour.grey("["+currentDateTime()+"] ");
    }

    public static void print(String string) {
        System.out.println(string);
    }

    public static void printError(String error) {
        print(logError(error));
    }

    public static String logError(String error) {
        error = "[" + red("ERROR") + "] " + error;
        return log(error);
    }

    public static String log(String log) {
        return logTime() + log;
    }

    private static String textBoard(GridBoard gridBoard, boolean coloured) {
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
        return textBoard(gridBoard, true);
    }

    public static String boardString(GridBoard gridBoard) {
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

    public static void printHelpClient() {
        print(
                "Valid inputs are: " + "LIST, QUEUE, MOVE, LOGS, HELP, DISCONNECT" + "\n" +
                "These can be upper or lowercase\n" +
                "To make a move, typ: MOVE [row/column] [direction]\n" +
                "For a double move: MOVE [row/column] [direction] [row/column] [direction]\n" +
                "Where [row/column] is an integer, and direction is UP/DOWN/LEFT/RIGHT"
        );
    }

    public static String ballColours(ArrayList<Ball> balls) {
        StringBuilder output = new StringBuilder();
        for (Ball ball : balls) {
            output.append(ball.ballColour()).append("\u25CF").append(Ball.WHITE.ballColour());
        }
        return output.toString();
    }
}
