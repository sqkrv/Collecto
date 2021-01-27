package Collecto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static Collecto.Colour.red;
import static Collecto.Global.Protocol.Commands.*;

/**
 * This class contains the Textual User Interface
 * used by all other classes in the Collecto program
 * to print anything on the screen of a user.
 * It contains methods to return logs with
 * dates and time added to them, print strings,
 * print errors, print a board, print a help menu
 * and return a string with coloured balls. It is
 * used by many classes, mainly the Game, Client,
 * Server and PlayerHandler classes.
 *
 * @see Game
 * @see Client
 * @see Server
 * @see PlayerHandler
 */
public class TUI {
    static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM");

    /**
     * Returns current date and time in formatted datetime string.
     *
     * @return current datetime string
     */
    public static String currentDateTime() {
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Returns grey coloured formatted current date and time in brackets for logging purposes.
     *
     * @return log format datetime string
     */
    public static String logTime() {
        return Colour.grey("[" + currentDateTime() + "] ");
    }

    /**
     * Prints a string to the console of the user.
     *
     * @param string string to print
     */
    public static void print(String string) {
        System.out.println(string);
    }

    /**
     * Prints an error to the console of the user in log format.
     *
     * @param error error message to print
     */
    public static void printError(String error) {
        print(logError(error));
    }

    /**
     * Returns a formatted error message for logging purposes.
     *
     * @param error error message
     * @return formatted log string
     */
    public static String logError(String error) {
        String formattedError = "[" + red("ERROR") + "] " + error;
        return log(formattedError);
    }

    /**
     * Adds formatted datetime to the provided String and returns the resulting String.
     *
     * @param log text of the log
     * @return provided log with datetime at the beginning
     */
    public static String log(String log) {
        return logTime() + log;
    }

    /**
     * Returns a coloured string representation of a provided board.
     * Returned representation has line numeration starting from 1, and ANSI coloured balls names.
     * <p>This method should not be called directly, instead use {@link #colouredBoard(GridBoard)}.
     *
     * @param gridBoard GridBoard to be represented as a string
     * @return coloured string representation of the provided board
     * @see Ball
     */
    public static String textBoard(GridBoard gridBoard) {
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
                string.append(String.format(
                        "%-16s|", gridBoard.getField(i - 1, j - 1).toColouredString()));
            }
            string.append("\n");
        }
        string.append("-----+");
        string.append("-------+".repeat(7));

        return string.toString();
    }

    /**
     * Returns coloured representation of the GridBoard provided.
     * Returned representation has line numeration starting from 1 and ANSI coloured balls names.
     *
     * @param gridBoard GridBoard to represent as a string
     * @return coloured string representation of the given GridBoard
     */
    public static String colouredBoard(GridBoard gridBoard) {
        return textBoard(gridBoard);
    }

    /**
     * Returns a basic text representation of the provided {@code GridBoard}.
     * Returned representation does not have line numeration and colourisation.
     *
     * @param gridBoard board to make a representation of
     * @return text representation of provided GridBoard as a String
     */
    static String boardString(GridBoard gridBoard) {
        StringBuilder string = new StringBuilder();

        for (int i = 1; i <= 7; i++) {
            string.append("+");
            string.append("-------+".repeat(7));
            string.append("\n|");
            for (int j = 1; j <= 7; j++) {
                string.append(String.format("%-7s|", gridBoard.getField(i - 1, j - 1)));
            }
            string.append("\n");
        }
        string.append("+");
        string.append("-------+".repeat(7));

        return string.toString();
    }

    /**
     * Prints a help menu belonging to a client.
     * Help contains all commands that a client can directly use and their descriptions.
     */
    public static void printHelpClient() {
        print(
                "Below is a list of all available commands. All commands are case-insensitive." +
                        "\n • " + LIST + " — request list of all players on the server" +
                        "\n • " + HELP + " — print this help" +
                        "\n • " + MOVE + " — make a move (works only in game, when not using AI)" +
                        "\n\tUsage: move <line> <direction> [<line> <direction>]" +
                        "\n • " + HINT + " — get a hint on current game board " +
                        "(works only in game, when not using AI)" +
                        "\n • " + BOARD + " — print current board state (works only in game)" +
                        "\n • " + QUEUE + " — join server's queue" +
                        "\n • " + LOGS + " — print all logs" +
                        "\n • " + STATUS + " — print status of the client" +
                        "\n • " + DISCONNECT + " — disconnect from the server " +
                        "and gracefully terminate the client"
        );
    }

    /**
     * Converts provided array of balls to a string, where every ball in the array
     * is represented as a special symbol (●) with a set colour of this ball.
     * Used to show the balls of a player.
     *
     * @param balls array of balls to be represented as a string
     * @return string with balls in provided array represented as coloured symbols
     */
    public static String ballColours(ArrayList<Ball> balls) {
        StringBuilder output = new StringBuilder();
        for (Ball ball : balls) {
            output.append(ball.ballColour()).append("\u25CF").append(Ball.WHITE.ballColour());
        }
        return output.toString();
    }
}
