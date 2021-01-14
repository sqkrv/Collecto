package Collecto;

import java.util.ArrayList;
import java.util.Arrays;

public class TUI {
    public static void main(String[] args) {
        GridBoard board = new GridBoard();
        GridBoard copy = board.deepCopy();

//        board.moveLine(4, 4, GridBoard.Direction.DOWN);
        System.out.println(board.isMoveValid(4,4, GridBoard.Direction.DOWN));

//        System.out.println(board.checkSurroundings(0, 0));
        System.out.println(board.possibleMoves());

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
                System.out.printf("%-16s|", board.getField(i-1, j-1));
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
