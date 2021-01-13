package Collecto;

import java.util.ArrayList;
import java.util.Arrays;

public class TUI {
//    private final String

    public static void main(String[] args) {
//        System.out.print("     |");
//        for (int i = 1; i <= 7; i++) {
//            System.out.print("       |");
//        }
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

        System.out.println();
        System.out.print("     |");
        for (int i = 1; i <= 7; i++) {
            System.out.print("   ");
            System.out.print(i);
            System.out.print("   |");
        }

        System.out.println();

        for (int i = 1; i <= 7; i++) {
            System.out.print("-----+");
            for (int j = 1; j <= 7; j++) {
                System.out.print("-------+");
            }
            System.out.println();
            System.out.print("  "+i+"  |");
            StringBuilder string = new StringBuilder();
            for (int j = 1; j <= 7; j++) {
                string.append(String.format("%-16s|", board.board.get(i-1).get(j-1)));
//                System.out.printf("%-7s ", board.board.get(i-1).get(j-1));
            }
            System.out.print(string);
            System.out.println();
        }
        System.out.print("-----+");
        for (int j = 1; j <= 7; j++) {
            System.out.print("-------+");
        }
    }
}
