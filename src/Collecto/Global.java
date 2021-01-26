package Collecto;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class contains methods, classes and a delimiter,
 * which are used by multiple other methods. It is meant
 * to be used by different classes to provide static and
 * miscellaneous functions to the caller. It is used mostly
 * in Server, PlayerHandler, Client and ClientController.
 *
 * @see Server
 * @see PlayerHandler
 * @see Client
 * @see ClientController
 */
public class Global {
    public static final String DELIMITER = "~";

    /**
     * Determines whether a move is within the limits of the board.
     *
     * @param push move in push form
     * @return true if {@code 0 <= push <= 27}
     */
    public static boolean isPushValid(int push) {
        return push >= 0 && push <= 27;
    }

    /**
     * Parses the String of a move in push form to an integer, and checks if that push is valid.
     *
     * @param stringPush push in String form
     * @return integer of push if it is valid, or null
     */
    public static Integer parsePush(String stringPush) {
        Integer push = parseInt(stringPush);
        if (push != null) {
            if (isPushValid(push)) {
                return push;
            }
        }
        return null;
    }

    /**
     * Checks whether a given String IP is correct and returns it as an InetAddress.
     *
     * @param ip IP address given in String form
     * @return InetAddress of IP if it is valid, null if it is not
     */
    public static InetAddress checkAddress(String ip) {
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException ignored) {
        }
        return null;
    }

    /**
     * Checks to see if the String of a port is correct and in use, and returns it as an Integer.
     *
     * @param stringPort String of the port
     * @return Integer if the port is usable, otherwise null
     */
    public static Integer checkPort(String stringPort) {
        int port;
        try {
            port = Integer.parseInt(stringPort);
            if (port > 0) {
                return port;
            }
            return null;
        } catch (NumberFormatException ignored) {
        }
        return null;
    }

    /**
     * Parses a String to an Integer.
     *
     * @param string given String
     * @return Integer if the String can be parsed, or null
     */
    protected static Integer parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    /**
     * This class contains classes holding strings that are used often
     * in the entirety of the Collecto program, as well as the TYPE_HELP string
     * which contains a statement referring to the help menu in clients.
     * @see Commands
     * @see Misc
     * @see Extensions
     * @see Win
     */
    public static class Protocol {
        public static final String TYPE_HELP = "Type HELP to see list of all commands";

        /**
         * This class contains simple commands to be used by clients
         * to check for input from their users.
         */
        public static class Commands {
            public static final String LIST = "LIST";
            public static final String HELP = "HELP";
            public static final String MOVE = "MOVE";
            public static final String HINT = "HINT";
            public static final String QUEUE = "QUEUE";
            public static final String LOGS = "LOGS";
            public static final String DISCONNECT = "DISCONNECT";
            public static final String BOARD = "BOARD";
        }

        /**
         * This class contains some miscellaneous commands to be used by clients
         * and servers to compare to outside input.
         */
        public static class Misc {
            public static final String HELLO = "HELLO";
            public static final String LOGIN = "LOGIN";
            public static final String ALREADY_LOGGED_IN = "ALREADYLOGGEDIN";
            public static final String NEWGAME = "NEWGAME";
            public static final String GAMEOVER = "GAMEOVER";
            public static final String ERROR = "ERROR";
        }

        /**
         * This class contains strings for the possible extensions that a server or client can have.
         */
        public static class Extensions {
            public static final String CHAT = "CHAT";
            public static final String RANK = "RANK";
            public static final String AUTH = "AUTH";
            public static final String CRYPT = "CRYPT";
        }

        /**
         * This class contains strings of reasons for winning a game.
         */
        public static class Win {
            public static final String DRAW = "DRAW";
            public static final String VICTORY = "VICTORY";
            public static final String DISCONNECT = "DISCONNECT";
        }
    }
}
