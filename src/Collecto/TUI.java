package Collecto;

import java.util.ArrayList;
import java.util.Arrays;

public class TUI {
    public static void main(String[] args) {
        //region sample board
        GridBoard board = new GridBoard();
        board.board = new ArrayList<>(7);
        for (int i=0; i < 7; i++) {
            board.board.add(i, new ArrayList<>(7));
        }
        board.board.get(0).addAll(
                Arrays.asList(
                        Ball.GREEN,
                        Ball.YELLOW,
                        Ball.ORANGE,
                        Ball.GREEN,
                        Ball.PURPLE,
                        Ball.RED,
                        Ball.ORANGE
                )
        );
        board.board.get(1).addAll(
                Arrays.asList(
                        Ball.YELLOW,
                        Ball.BLUE,
                        Ball.RED,
                        Ball.ORANGE,
                        Ball.YELLOW,
                        Ball.GREEN,
                        Ball.PURPLE
                )
        );
        board.board.get(2).addAll(
                Arrays.asList(
                        Ball.BLUE,
                        Ball.RED,
                        Ball.PURPLE,
                        Ball.BLUE,
                        Ball.RED,
                        Ball.YELLOW,
                        Ball.ORANGE
                )
        );
        board.board.get(3).addAll(
                Arrays.asList(
                        Ball.PURPLE,
                        Ball.YELLOW,
                        Ball.GREEN,
                        Ball.WHITE,
                        Ball.BLUE,
                        Ball.RED,
                        Ball.GREEN
                )
        );
        board.board.get(4).addAll(
                Arrays.asList(
                        Ball.ORANGE,
                        Ball.GREEN,
                        Ball.BLUE,
                        Ball.GREEN,
                        Ball.PURPLE,
                        Ball.BLUE,
                        Ball.RED
                )
        );
        board.board.get(5).addAll(
                Arrays.asList(
                        Ball.YELLOW,
                        Ball.PURPLE,
                        Ball.ORANGE,
                        Ball.YELLOW,
                        Ball.ORANGE,
                        Ball.YELLOW,
                        Ball.PURPLE
                )
        );
        board.board.get(6).addAll(
                Arrays.asList(
                        Ball.ORANGE,
                        Ball.GREEN,
                        Ball.BLUE,
                        Ball.RED,
                        Ball.BLUE,
                        Ball.PURPLE,
                        Ball.RED
                )
        );
        //endregion

        GridBoard copy = board.deepCopy();

        board.moveLine(4, 4, GridBoard.Direction.UP);

        System.out.println();
        System.out.print("     |");
        for (int i = 1; i <= 7; i++) {
            System.out.print("   "+i+"   |");
        }

        System.out.println();

        for (int i = 1; i <= 7; i++) {
            System.out.print("-----+");
            for (int j = 1; j <= 7; j++) {
                System.out.print("-------+");
            }
            System.out.println();
            System.out.print("  "+i+"  |");
            for (int j = 1; j <= 7; j++) {
                System.out.printf("%-16s|", board.board.get(i-1).get(j-1));
            }
            System.out.println();
        }
        System.out.print("-----+");
        for (int j = 1; j <= 7; j++) {
            System.out.print("-------+");
        }

        System.out.println("  Is board the same: "+board.board.equals(copy.board));
    }
}
