package Collecto;

import java.util.*;

public class GridBoard {
    public enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }

    private ArrayList<ArrayList<Ball>> board;  // [[column, column],[],[]] // TODO change back to private

    public GridBoard(ArrayList<ArrayList<Ball>> customBoard) {
        this.board = new ArrayList<>(customBoard);
    } //TODO: figure out a better way to test this with Junit

    /**
     * Constructor
     */
    public GridBoard() {
        constructPreset();
    }

//    /**
//     * Constructs a board (places Balls on board) according to game rules
//     * @ensures the board is initialized, where no adjacent balls are the same colour,
//     * all fields hold a ball except for the middle, a legal move can be made.
//     */
//    public void construct() {
//        Random randInt = new Random();
//        int[] colours = {8, 8, 8, 8, 8, 8};
////        String[] colour = {"blue", "yellow", "red", "orange", "purple", "green", "WHITE"};
//        Ball[] colour = new Ball[]{Ball.BLUE, Ball.YELLOW, Ball.RED, Ball.ORANGE, Ball.PURPLE, Ball.GREEN};
//        Ball ball;
//        for (int row = 0; row < 7; row++) {
//            board.add(board.size(), new ArrayList<>());
//            for (int col = 0; col < 7; col++) {
//                while (true) {
//                    int random = randInt.nextInt(7);
//                    if (colours[random] > 0) {
//                        ball = colour[random];
//                        colours[random]--;
//                        break;
//                    }
//                }
//
//                board.get(row).add(ball);
//            }
//        }
//        //TODO: finish this method
//    }
//
//
//    // Possible different implementation, requires a change in checkSurroundings()
//
//    public void construct2() {
//        for (int i = 0; i < 6; i++) {
//            board.add(new ArrayList<Ball>());
//            for (int j = 0; j < 6; j++) {
//                board.get(i).add(board.get(i).size(), Ball.WHITE);
//            }
//        }
//        Random rand = new Random();
//        Ball[] ballColours = new Ball[]{Ball.BLUE, Ball.YELLOW, Ball.RED, Ball.ORANGE, Ball.PURPLE, Ball.GREEN};
//        int row;
//        int column;
//        int ball;
//        for (Ball b : ballColours) {
//            for (int i = 0; i < 8; i++) {
//                row = rand.nextInt(8);
//                column = rand.nextInt(8);
//                checkSurroundings(row, column, toString(b));
//            }
//        }
//    }

    public void constructPreset() {
        board = new ArrayList<>();
        for (int i=0; i < 7; i++) {
            board.add(i, new ArrayList<>());
        }
        board.get(0).addAll(
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
        board.get(1).addAll(
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
        board.get(2).addAll(
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
        board.get(3).addAll(
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
        board.get(4).addAll(
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
        board.get(5).addAll(
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
        board.get(6).addAll(
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
    }

    /**
     * @return a deep copy of current board
     */
    public GridBoard deepCopy() {
        GridBoard copy = new GridBoard();
        copy.board = new ArrayList<>();
        for (ArrayList<Ball> row : board) {
            copy.board.add((ArrayList<Ball>) row.clone());
        }
        return copy;
    }

//    public void moveLine(int row, int column, Direction direction) {
//        moveLine(row, column, direction, this.board);
//    }

    /**
     * Moves balls in line according to game rules
     * @param row row index (being used only if Direction is LEFT ot RIGHT)
     * @param column column index (being used only if Direction is UP ot DOWN)
     * @param direction direction of movement
     * @ensures that a certain move is a valid move
     */
    public void moveLine(int row, int column, Direction direction) {
//        row--;
//        column--;
//        if (!legalMoves(row, column, direction)) return;
//        TODO move this check to parent method which will firstly check if first or second move is valid by using this function and then perform this move by using the same (this) function
        int removed;
        if (direction == Direction.UP) {
            for (int i=0; i < 7; i++) {
                if (board.get(i).get(column) == Ball.WHITE) {
                    for (int j=i; j < 7-1; j++) {
                        for (int k=j+1; k < 7; k++) {
                            if (board.get(k).get(column) != Ball.WHITE) {
                                board.get(j).set(column, board.get(k).get(column));
                                board.get(k).set(column, Ball.WHITE);
                                break;
                            }
                        }
                    }
                    board.get(6).set(column, Ball.WHITE);
                }
            }
        } else if (direction == Direction.DOWN) {
            for (int i=6; i >= 0; i--) {
                if (board.get(i).get(column) == Ball.WHITE) {
                    for (int j=i; j >= 1; j--) {
                        for (int k=j-1; k >= 0; k--) {
                            if (board.get(k).get(column) != Ball.WHITE) {
                                board.get(j).set(column, board.get(k).get(column));
                                board.get(k).set(column, Ball.WHITE);
                                break;
                            }
                        }
                    }
                    board.get(0).set(column, Ball.WHITE);
                }
            }
        } else {
            if (board.get(row).contains(Ball.WHITE)) {
                removed = board.get(row).size();
                board.get(row).removeAll(Collections.singleton(Ball.WHITE));
                removed = removed - board.get(row).size();

                if (direction == Direction.LEFT) {
                    board.get(row).addAll(7 - removed, (Collection) Collections.nCopies(removed, Ball.WHITE));
                } else if (direction == Direction.RIGHT) {
                    board.get(row).addAll(0, (Collection) Collections.nCopies(removed, Ball.WHITE));
                }
            }
        }
    }

    /**
     * @requires valid indices
     * @ensures index is valid
     * @returns Ball from a specified field, or null if an index is invalid
     */
    private Ball getField(int row, int column) {
        if (validIndex(row) && validIndex(column)) {
            return board.get(row).get(column);
        }
        return null;
    }

//    /**
//     * @requires (validIndex(row) && validIndex(column)) == true
//     * @param row
//     * @param column
//     * @param ball
//     */
//    private void setField(int row, int column, Ball ball) {
//        assert validIndex(row) && validIndex(column);
//        board.get(row).set(column, ball);
//    }

    /**
     * @ensures index is valid
     * @returns true for a valid index (index>1 && index<7),
     *      and false for a non-valid index
     */
    protected boolean validIndex(int index) {
        return index >= 0 && index < 7;
    }

    /**
     * @requires String input contains numbers as the first two characters
     * @ensures coordinates are valid
     * @returns Coordinates of a String input
     */
    protected Coordinates getCoordinates(String input) {
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
     * @param row row index
     * @param column column index
     */
    public boolean checkSurroundings(int row, int column) {
        assert validIndex(row) && validIndex(column);
        Ball colour = getField(row, column);
        Ball up = getField(row-1, column);
        Ball down = getField(row+1, column);
        Ball left = getField(row, column-1);
        Ball right = getField(row, column+1);
        return colour == up || colour == down ||
                colour == left || colour == right;
    }

    /**
     * Checks if a certain move is legal
     * @requires row and column to be valid indices
     * @returns true if a specified move is valid, false if it is invalid
     * @param row row index
     * @param column column index
     * @param direction direction of the move
     */
    public boolean isMoveValid(int row, int column, Direction direction) {
        GridBoard copy = deepCopy();
        copy.moveLine(row, column, direction);
        if (direction == Direction.UP || direction == Direction.DOWN) {
            for (int i=0; i < 7; i++) {
                if (copy.checkSurroundings(i, column)) return true;
            }
        } else if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            for (int i=0; i < 7; i++) {
                if (copy.checkSurroundings(row, i)) return true;
            }
        }
        return false;
    }

    /**
     * @requires board != null, board != empty
     * @ensures check if there are valid moves possible
     * @returns true if there is a valid move within one or two moves,
     *  false if there are no valid moves possible within two moves
     */
    public boolean possibleMoves() {
        for (int row=0; row < 7; row++) {
            if (!board.get(row).contains(Ball.WHITE)) continue;
            for (int col=0; col < 7; col++) {
                Ball ball = getField(row, col);
                if (ball == Ball.WHITE) {
                    for (Direction direction : Direction.values()) {
                        if (isMoveValid(row, col, direction)) {
                            return true;
                        }
                    }
                }
            }
        }
        // No valid move in one turn

        //TODO add (recursive) check for second move

        return false;

    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        string.append("     |");
        for (int i = 1; i <= 7; i++) {
            string.append("   ").append(i).append("   |");
        }
        string.append("\n");
        for (int i = 1; i <= 7; i++) {
            string.append("-----+");
            for (int j = 1; j <= 7; j++) {
                string.append("-------+");
            }
            string.append("\n");
            string.append("  ").append(i).append("  |");
            for (int j = 1; j <= 7; j++) {
                string.append(String.format("%-16s|", getField(i-1, j-1)));
            }
            string.append("\n");
        }
        string.append("-----+");
        for (int j = 1; j <= 7; j++) {
            string.append("-------+");
        }
        
        return string.toString();
    }

    /**
     * Coordinates class
     * Being used for storing coordinates of a ball — row and column
     */
    public class Coordinates {
        public final int row;
        public final int column;

        /**
         * Constructor
         */
        public Coordinates(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }
}
