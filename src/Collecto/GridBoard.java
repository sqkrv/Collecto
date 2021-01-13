package Collecto;

import java.util.ArrayList;
import java.util.Random;

public class GridBoard {
    public ArrayList<ArrayList<Ball>> board;  // [[column, column],[],[]] // TODO change back to private

    /**
     * Constructor
     * @ensures the board is properly constructed
     */
    public GridBoard() {
//        construct();
    }

    /**
     * @ensures the board is initialized, where no adjacent balls are the same colour,
     * all fields hold a ball except for the middle, a legal move can be made.
     */
    public void construct() {
        Random randInt = new Random();
        int[] colours = {8, 8, 8, 8, 8, 8};
//        String[] colour = {"blue", "yellow", "red", "orange", "purple", "green", "WHITE"};
        Ball[] colour = new Ball[]{Ball.BLUE, Ball.YELLOW, Ball.RED, Ball.ORANGE, Ball.PURPLE, Ball.GREEN};
        Ball ball;
        for (int row = 0; row < 7; row++) {
            board.add(board.size(), new ArrayList<>());
            for (int col = 0; col < 7; col++) {
                while (true) {
                    int random = randInt.nextInt(7);
                    if (colours[random] > 0) {
                        ball = colour[random];
                        colours[random]--;
                        break;
                    }
                }

                board.get(row).add(ball);
            }
        }
        //TODO: finish this method
    }

    /**
     * @ensures return of a deepCopy of the current board
     * @return GridBoard
     */
    public GridBoard deepCopy() {
        GridBoard copy = new GridBoard();
        for (ArrayList<Ball> row : board) {
            for (Ball ball : row) {
//                copy.set
            }
        }
        return null; //TODO add more to this method
    }

    /**
     * @ensures that a certain move is a valid move
     */
    public void move(int row, int column, String direction) {
        if (!legalMoves(row, column, direction)) return;
        //TODO: add more to this method
    }

    /**
     * @requires valid indices
     * @ensures index is valid
     * @returns Ball from a specified field, or null if an index is invalid
     */
    public Ball getField(int row, int column) {
        if (validIndex(row) && validIndex(column)) {
            return board.get(row).get(column);
        }
        return null;
    }

    /**
     * @requires valid indices
     * @ensures a ball is set on a certain field
     */
    public void setField(int row, int column, Ball ball) {
        assert validIndex(row) && validIndex(column);
        board.get(row).set(column, ball);
    }

    /**
     * @ensures index is valid
     * @returns true for a valid index (index>1 || index<7),
     *      and false for a non-valid index (index<1 || index>7)
     */
    public boolean validIndex(int index) {
        return index >= 1 && index <= 7;
    }
    /**
     * @requires String input contains numbers as the first two characters
     * @ensures coordinates are valid
     * @returns Coordinates of a String input
     */
    public Coordinates getCoordinates(String input) {
        Coordinates coordinates = new Coordinates(input.charAt(0), input.charAt(1));
        if (validIndex(coordinates.row) && validIndex(coordinates.column)) {
            return coordinates;
        }
        return null;
    }

    /**
     * @requires row && column are valid indices
     * @ensures surroundings of indices are checked
     * @returns false if no surrounding ball has the same colour as the ball
     *  specified by the indices, true if at least 1 surrounding colour matches
     *  the ball specified by the indices
     */
    public boolean checkSurroundings(int row, int column) {
        assert validIndex(row) && validIndex(column);
        String colour = getField(row, column).getColour();
        String up = getField(row, column-1).getColour();
        String down = getField(row, column+1).getColour();
        String left = getField(row-1, column).getColour();
        String right = getField(row+1, column).getColour();
        return colour.equals(up) || colour.equals(down)
                || colour.equals(left) || colour.equals(right);
    }

    /**
     * @requires direction contains "up", "down", "left" or "right",
     *  row and column are valid indices
     * @ensures check if a move is legal
     * @returns true if a specified move is possible, false if it is impossible
     */
    public boolean legalMoves(int row, int column, String direction) {
        return false;
        //TODO
    }

    /**
     * @requires board != null, board != empty
     * @ensures check if there are valid moves possible
     * @returns true if there is a valid move within one or two moves,
     *  false if there are no valid moves possible within two moves
     */
    public boolean possibleMoves() {
        //TODO
        return false;
    }

    // clear the board (REDUNDANT)
//    public void clear() {
//        //TODO
//    }

    /**
     * Coordinates class
     * .....
     */
    public class Coordinates {
        public final int row;
        public final int column;
        /**
         * Constructor
         * @ensures the right coordinates are set
         */
        public Coordinates(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }
}
