package Collecto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Misc {
    static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM");

    public static String logTime() {
        return logTime(true);
    }

    public static String logTime(boolean dash) {
        String string = "["+currentDateTime()+"] ";
        if (dash) return string+"— ";
        return string;
    }

    public static String currentDateTime() {
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void main(String[] args) {
        System.out.println(logTime());
    }

    public static class Move {
        private final GridBoard.Direction direction;
        private final int line;

        public Move(int line, GridBoard.Direction direction) {
            assert 0 <= line && line < 7;
            this.direction = direction;
            this.line = line;
        }

        public GridBoard.Direction getDirection() {
            return direction;
        }

        public int getLine() {
            return line;
        }
    }
}
