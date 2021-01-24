package Collecto;

import java.util.*;

/**
 * This class contains the actual game board and handles all
 * operations made on its attributes. It is mainly used by
 * the Game class, and makes use of the Ball class.
 * @see Game
 * @see Ball
 */
public class GridBoard {
    private ArrayList<ArrayList<Ball>> board;

    public GridBoard(ArrayList<ArrayList<Ball>> customBoard) {
        this.board = new ArrayList<>(customBoard);
    }

    private GridBoard(boolean construct) {
        if (construct) construct();
    }

    /**
     * Constructor
     */
    public GridBoard() {
        this(true);
    }

    public void construct() {
        Random rand = new Random();
        board = new ArrayList<>();

        ArrayList<Ball> balls = new ArrayList<>(Arrays.asList(Ball.BLUE, Ball.YELLOW, Ball.RED, Ball.ORANGE, Ball.PURPLE, Ball.GREEN));
        Collections.shuffle(balls);
        board.add(balls);
        for (int row = 1; row < 7; row++) {
            board.add(new ArrayList<>());
            balls = new ArrayList<>(Arrays.asList(Ball.BLUE, Ball.YELLOW, Ball.RED, Ball.ORANGE, Ball.PURPLE, Ball.GREEN));
            Collections.shuffle(balls);
            while (!balls.isEmpty()) {
                Ball ball = balls.get(0);
                if (getField(row - 1, 6 - balls.size()) == ball) {
                    if (balls.size() == 1) {
                        int random = rand.nextInt(5);
                        board.get(row).add(getField(row, random));
                        setField(row, random, ball);
//                        board.get(row).add(board.get(row).get(random));
//                        board.get(row).set(random, ball);
                    } else {
                        balls.add(ball);
                    }
                } else {
                    board.get(row).add(ball);
                }
                balls.remove(0);
            }
        }

        balls = new ArrayList<>(Arrays.asList(Ball.BLUE, Ball.YELLOW, Ball.RED, Ball.ORANGE, Ball.PURPLE, Ball.GREEN));
        Collections.shuffle(balls);
        Ball ball = getField(3, 3);
        if (balls.get(balls.size() - 1) == ball) {
            int random = rand.nextInt(4);
            balls.add(balls.get(random));
            balls.set(random, ball);
        } else balls.add(ball);
        setField(3, 3, Ball.WHITE);
//        board.get(3).set(3, Ball.WHITE);
        for (int row = 0; row < 7; row++) {
            ball = balls.get(0);
            while (getField(row - 1, 6) == ball || getField(row, 5) == ball) {
                if (balls.size() != 1) {
                    balls.add(balls.get(0));
                    balls.remove(0);
                    ball = balls.get(0);
                } else {
                    while (true) {
                        int random = rand.nextInt(4);
                        if (getField(random, 5) != ball && getField(random - 1, 6) != ball
                                && getField(random + 1, 6) != ball) {
                            Ball temp = getField(random, 6);
                            setField(6, random, ball);
//                            board.get(6).set(random, ball);
                            ball = temp;
                            break;
                        }
                    }
                }
            }
            board.get(row).add(ball);
        }
        if (!possibleMoves()) {
            board = null;
            construct();
        }
    }

    public ArrayList<Ball> removeBalls(Move move) {
        ArrayList<Ball> balls = new ArrayList<>();
        if (move.getDirection() == Move.Direction.UP || move.getDirection() == Move.Direction.DOWN) {
            for (int ro = 0; ro < 7; ro++) {
                balls.addAll(removeBalls(ro, move.getLine()));
            }
        } else {
            for (int col = 0; col < 7; col++) {
                balls.addAll(removeBalls(move.getLine(), col));
            }
        }
        return balls;
    }

    private ArrayList<Ball> removeBalls(int row, int column) {
        Ball ball;
        ArrayList<Ball> balls = new ArrayList<>();
        if (checkSurroundings(row , column)) {
            ball = getField(row, column);
            for (int j = -1; j <= 1; j += 2) {
                if (getField(row, column + j) == ball) {
                    if (getField(row, column) != Ball.WHITE) balls.add(ball);
                    setField(row, column, Ball.WHITE);
                    if (checkSurroundings(row, column + j)) {
                        balls.addAll(removeBalls(row, column + j));
                    } else {
                        balls.add(ball);
                        setField(row, column + j, Ball.WHITE);
                    }
                }
                if (getField(row + j, column) == ball) {
                    if (getField(row, column) != Ball.WHITE) balls.add(ball);
                    setField(row, column, Ball.WHITE);
                    if (checkSurroundings(row + j, column)) {
                        balls.addAll(removeBalls(row + j, column));
                    } else {
                        balls.add(ball);
                        setField(row + j, column, Ball.WHITE);
                    }
                }
            }
        }
        return balls;
    }

    /*
    public ArrayList<Ball> removeBalls(int row, int column, Direction direction) {
        ArrayList<Ball> balls = new ArrayList<>();
        Ball ball;
        //region RIP code
//        if (direction == Direction.UP || direction == Direction.DOWN) {
//            for (row = 0; row < 7; row++) {
////                while (checkSurroundings(row, column)) {
//                ball = getField(row, column);
//
//                if (getField(row - 1, column) == ball) {
//                    balls.add(ball);
//                    setField(row, column, Ball.WHITE);
//                }
//                if (getField(row, column - 1) == ball) {
//                    balls.add(ball);
//                    setField(row, column - 1, Ball.WHITE);
//                }
//                if (getField(row, column + 1) == ball) {
//                    balls.add(ball);
//                    setField(row, column + 1, Ball.WHITE);
//                }
//                if (getField(row + 1, column) != ball && getField(row + 1, column + 1) != getField(row + 1, column) && getField(row + 1, column - 1) != getField(row + 1, column) && getField(row + 2, column) != getField(row + 1, column)) {
//                    balls.add(ball);
//                    setField(row + 1, column, Ball.WHITE);
//                }
////            }
//        }
//        } else {
//            for (int col = 0; col < 7; col++) {
//                while (checkSurroundings(row, col)) {
//                    ball = getField(row, col);
//                    if (getField(row - 1, col) == ball) {
//                        balls.add(ball);
//                        setField(row - 1, col, Ball.WHITE);
//                    }
//                    if (getField(row + 1, col) == ball) {
//                        balls.add(ball);
//                        setField(row + 1, col, Ball.WHITE);
//                    }
//                    balls.add(ball);
//                    setField(row, col, Ball.WHITE);
//                }
//            }
//        }
        //endregion



        if (direction == Direction.UP || direction == Direction.DOWN) {
            // move top to bottom
            for (int i = 0; i < 7; i++) {
                // if the current ball is not white AND there is a matching ball around
                if (checkSurroundings(i, column)) {
                    ball = getField(i, column);
                    // j=-1 or j=1
                    for (int j = -1; j <= 1; j += 2) {
                        // check left and right of the selected ball
                        if (getField(i, column + j) == ball) {
                            balls.add(ball);
                            // clear the selected ball
                            setField(i, column, Ball.WHITE);
                            // check if the adjacent ball has matching balls around
                            if (checkSurroundings(i, column + j)) {
                                // if it does, use recursion for that ball, add the result to the list
                                 removeBalls(i, column + j, direction);
                            } else {
                                // if it does not, add it to the list and clear it
                                getField(i, column + j);
                                setField(i, column + j, Ball.WHITE);
                            }
                        }
                        if (getField(i + j, column) == ball) {
                            balls.add(ball);
                            setField(i, column, Ball.WHITE);
                            removeBalls(i + j, column, direction);
                            if (checkSurroundings(i + j, column)) {
                                removeBalls(i + j, column, direction);
                            } else {
                                balls.add(getField(i + j, column));
                                setField(i + j, column, Ball.WHITE);
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 7; i++) {
                if (checkSurroundings(row, i)) {
                    ball = getField(row, i);
                    for (int j = -1; j <= 1; j += 2) {
                        if (getField(row + j, i) == ball) {
                            balls.add(ball);
                            setField(row, i, Ball.WHITE);
                            if (checkSurroundings(row + j, i)) {
                                removeBalls(row + j, i, direction);
                            } else {
                                balls.add(getField(row + j, i));
                                setField(row + j, i, Ball.WHITE);
                            }
                        }
                        if (getField(row, i + j) == ball) {
                            balls.add(ball);
                            setField(row, i, Ball.WHITE);
                            removeBalls(row, i + j, direction);
                            if (checkSurroundings(row, i + j)) {
                                removeBalls(row, i + j, direction);
                            } else {
                                balls.add(getField(row, i + j));
                                setField(row, i + j, Ball.WHITE);
                            }
                        }
                    }
                }
            }
        }
        return balls;
    }
    */

    /**
     * @return a deep copy of current board
     */
    public GridBoard deepCopy() {
        GridBoard copy = new GridBoard(false);
        copy.board = new ArrayList<>();
        for (ArrayList<Ball> row : board) {
            copy.board.add(new ArrayList<>(row));
        }
        return copy;
    }

    public void moveLine(Move move) {
//        if (!legalMoves(row, column, direction)) return;
//        TODO: move this check to parent method which will firstly check if first or second move
//         is valid by using this function and then perform this move by using the same (this) function
        int removed;
        if (move.getDirection() == Move.Direction.UP) {
            for (int i=0; i < 7; i++) {
                if (board.get(i).get(move.getLine()) == Ball.WHITE) {
                    for (int j=i; j < 7-1; j++) {
                        for (int k=j+1; k < 7; k++) {
                            if (board.get(k).get(move.getLine()) != Ball.WHITE) {
                                board.get(j).set(move.getLine(), board.get(k).get(move.getLine()));
                                board.get(k).set(move.getLine(), Ball.WHITE);
                                break;
                            }
                        }
                    }
                    board.get(6).set(move.getLine(), Ball.WHITE);
                }
            }
        } else if (move.getDirection() == Move.Direction.DOWN) {
            for (int i=6; i >= 0; i--) {
                if (board.get(i).get(move.getLine()) == Ball.WHITE) {
                    for (int j=i; j >= 1; j--) {
                        for (int k=j-1; k >= 0; k--) {
                            if (board.get(k).get(move.getLine()) != Ball.WHITE) {
                                board.get(j).set(move.getLine(), board.get(k).get(move.getLine()));
                                board.get(k).set(move.getLine(), Ball.WHITE);
                                break;
                            }
                        }
                    }
                    board.get(0).set(move.getLine(), Ball.WHITE);
                }
            }
        } else {
            if (board.get(move.getLine()).contains(Ball.WHITE)) {
                removed = board.get(move.getLine()).size();
                board.get(move.getLine()).removeAll(Collections.singleton(Ball.WHITE));
                removed = removed - board.get(move.getLine()).size();

                if (move.getDirection() == Move.Direction.LEFT) {
                    board.get(move.getLine()).addAll(7 - removed, Collections.nCopies(removed, Ball.WHITE));
                } else if (move.getDirection() == Move.Direction.RIGHT) {
                    board.get(move.getLine()).addAll(0, Collections.nCopies(removed, Ball.WHITE));
                }
            }
        }
    }

    /**
     * @requires valid indices
     * @ensures index is valid
     * @return Ball from a specified field, or null if an index is invalid
     */
    public Ball getField(int row, int column) {
        if (validIndex(row) && validIndex(column)) {
            return board.get(row).get(column);
        }
        return null;
    }

    /**
     * @requires (validIndex(row) && validIndex(column)) == true
     * @param row index of the row of the ball
     * @param column index of the column of the ball
     * @param ball the specified ball to be set
     */
    private void setField(int row, int column, Ball ball) {
        assert validIndex(row) && validIndex(column);
        board.get(row).set(column, ball);
    }

    /**
     * @ensures index is valid
     * @return true for a valid index (index>1 && index<7),
     *      and false for a non-valid index
     */
    protected boolean validIndex(int index) {
        return index >= 0 && index < 7;
    }

    /**
     * @requires String input contains numbers as the first two characters
     * @ensures coordinates are valid
     * @return Coordinates of a String input
     */
//    protected Coordinates getCoordinates(String input) {
//        Coordinates coordinates = new Coordinates(input.charAt(0), input.charAt(1));
//        if (validIndex(coordinates.row) && validIndex(coordinates.column)) {
//            return coordinates;
//        }
//        return null;
//        //TODO: maybe move this method to a different class, since parsing is not the job of the board
//    }

    /**
     * Checks the surroundings of a specified ball to see if any of the 4 adjacent balls in the
     * cardinal directions are of the same colour or not.
     *
     * @requires both {@code validIndex(row)} and {@code validIndex(column)} to be true
     * @ensures surroundings of specified fields are checked
     * @return false if no surrounding ball has the same colour as the ball
     * specified by the indices, true if at least 1 surrounding colour matches
     *  the ball specified by the indices
     * @param row row index
     * @param column column index
     */
    public boolean checkSurroundings(int row, int column) {
        assert validIndex(row) && validIndex(column);
        Ball colour = getField(row, column);
        if (colour == Ball.WHITE) return false;
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
     * @return true if a specified move is valid, false if it is invalid
     * @param move new move being made
     */
    public boolean isMoveValid(Move move) {
        GridBoard copy = deepCopy();
        return isMoveValidCheck(move, copy);
    }

    public boolean isMoveValid(Move first, Move second) {
        GridBoard copy = deepCopy();
        copy.moveLine(first);
        return isMoveValidCheck(second, copy);
    }

    private boolean isMoveValidCheck(Move move, GridBoard copy) {
        copy.moveLine(move);
        if (move.getDirection() == Move.Direction.UP || move.getDirection() == Move.Direction.DOWN) {
            for (int i=0; i < 7; i++) {
                if (copy.checkSurroundings(i, move.getLine())) return true;
            }
        } else if (move.getDirection() == Move.Direction.LEFT || move.getDirection() == Move.Direction.RIGHT) {
            for (int i=0; i < 7; i++) {
                if (copy.checkSurroundings(move.getLine(), i)) return true;
            }
        }
        return false;
    }

    /**
     * Calls {@link #possibleMoves(boolean)} with a false boolean argument to check if there are any single or
     * double moves possible on the board of this GridBoard object.
     *
     * @return true if there is possible move, false otherwise
     */
    public boolean possibleMoves() {
        return possibleMoves(false);
    }

    /**
     * Depending on parameter isSecondTime checks if there are any single moves possible on the board,
     * or if there are any single or double moves possible on the board of this GridBoard object.
     *
     * @param isSecondTime boolean to define if check is for possibility of single or double moves
     * @ensures checks only a single move if {@code isSecondTime} is true, checks for single and double moves if
     * {@code isSecondTime} is false
     * @return true if there is a valid move within one or two moves, false if there are no valid moves possible within two moves
     */
    public boolean possibleMoves(boolean isSecondTime) {
        for (int i = 0; i < 7; i++) {
            if (
                    isMoveValid(new Move(i, Move.Direction.LEFT)) ||
                    isMoveValid(new Move(i, Move.Direction.RIGHT)) ||
                    isMoveValid(new Move(i, Move.Direction.UP)) ||
                    isMoveValid(new Move(i, Move.Direction.DOWN))
            ) {
                return true;
            }
        }

        if (isSecondTime) return false;

        GridBoard copy;
        for (int i = 0; i < 7; i++) {
            copy = deepCopy();
            copy.moveLine(new Move(i, Move.Direction.LEFT));
            if (copy.possibleMoves(true)) return true;

            copy = deepCopy();
            copy.moveLine(new Move(i, Move.Direction.RIGHT));
            if (copy.possibleMoves(true)) return true;

            copy = deepCopy();
            copy.moveLine(new Move(i, Move.Direction.UP));
            if (copy.possibleMoves(true)) return true;

            copy = deepCopy();
            copy.moveLine(new Move(i, Move.Direction.DOWN));
            if (copy.possibleMoves(true)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return TUI.boardString(this);
    }

    /**
     * Returns string representation of the board according to the project protocol.
     * This protocol means that every ball on the board is represented by a number and
     * placed in an array, where empty fields are represented as 0, and all numbers are
     * separated by a delimiter as defined in {@link Global}.
     *
     * @return protocol representation of the board
     */
    public String getBoardString() {
        StringBuilder result = new StringBuilder();
        for (ArrayList<Ball> array : board) {
            for (Ball ball : array) {
                result.append(Global.DELIMITER).append(ball.ordinal());
            }
        }
        return result.toString();
    }
}
