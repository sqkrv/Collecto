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

        System.out.println(board.toString());

        System.out.println("Is board the same: "+board.toString().equals(copy.toString()));
    }
}
