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
    }
}
