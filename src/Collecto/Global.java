package Collecto;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Global {
    public static final String DELIMITER = "~";

    public static boolean isPushValid(int push) {
        return 0 <= push && push <= 27;
    }

    public static Integer parsePush(String string_push) {
        Integer push = parseInt(string_push);
        if (push != null) {
            if (isPushValid(push)) {
                return push;
            }
        }
        return null;
    }

    public static InetAddress checkAddress(String ip) {
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException ignored) {
        }
        return null;
    }

    public static Integer checkPort(String string_port) {
        int port;
        try {
            port = Integer.parseInt(string_port);
            if (port > 0) return port;
            return null;
        } catch (NumberFormatException ignored) {
        }
        return null;
    }

    protected static Integer parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    static class Protocol {
        static class Commands {
            public static final String LIST = "LIST";
            public static final String HELP = "HELP";
            public static final String MOVE = "MOVE";
            public static final String HINT = "HINT";
            public static final String QUEUE = "QUEUE";
            public static final String LOGS = "LOGS";
            public static final String DISCONNECT = "DISCONNECT";
            public static final String BOARD = "BOARD";
        }

        static class Misc {
            public static final String HELLO = "HELLO";
            public static final String LOGIN = "LOGIN";
            public static final String ALREADY_LOGGED_IN = "ALREADYLOGGEDIN";
            public static final String NEWGAME = "NEWGAME";
            public static final String GAMEOVER = "GAMEOVER";
            public static final String ERROR = "ERROR";
        }

        static class Extensions {
            public static final String CHAT = "CHAT";
            public static final String RANK = "RANK";
            public static final String AUTH = "AUTH";
            public static final String CRYPT = "CRYPT";
        }

        static class Win {
            public static final String DRAW = "DRAW";
            public static final String VICTORY = "VICTORY";
            public static final String DISCONNECT = "DISCONNECT";
        }

        public static final String TYPE_HELP = "Type HELP to see list of all commands";
    }
}
