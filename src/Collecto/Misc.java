package Collecto;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Misc {
    static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM");

    public static String logTime() {
        return "["+currentDateTime()+"] ";
    }

    public static String currentDateTime() {
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static InetAddress checkIP(String ip) {
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
//            TUI.print(message);
        }
        return null;
    }

    public static Integer checkPort(String string_port) {
        int port;
        try {
            port = Integer.parseInt(string_port);
            if (port > 0) return port;
            return null;
        } catch (NumberFormatException e) {
//            TUI.print(message);
        }
        return null;
    }

    protected static Integer parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (IllegalArgumentException e) {
//            sendError("Wrong arguments provided");
        }
        return null;
    }

//    public static void main(String[] args) {
//        System.out.println(logTime());
//    }

    public static class Move {
        private final GridBoard.Direction direction;
        private final int line;

        public Move(int line, GridBoard.Direction direction) {
            assert 0 <= line && line < 7;
            this.line = line;
            this.direction = direction;
        }

        public Move(int push) {
            assert 0 <= push && push <= 27;
            this.line = push % 7;
            switch (push / 7) {
                case 0:
                    direction = GridBoard.Direction.LEFT;
                    break;
                case 1:
                    direction = GridBoard.Direction.RIGHT;
                    break;
                case 2:
                    direction = GridBoard.Direction.UP;
                    break;
                case 3:
                    direction = GridBoard.Direction.DOWN;
                    break;
                default:
                    direction = null;
            }
        }

        public GridBoard.Direction getDirection() {
            return direction;
        }

        public int getLine() {
            return line;
        }

        public int push() {
            return line + direction.ordinal()*7;
        }

        @Override
        public String toString() {
            return (line+1)+" "+direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Move move = (Move) o;
            return line == move.line && direction == move.direction;
        }
    }
}
