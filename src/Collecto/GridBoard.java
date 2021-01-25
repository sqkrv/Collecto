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

    /**
     * Constructs a new GridBoard where the board attribute is set to be a provided custom board.
     *
     * @param customBoard provided board to be used for this GridBoard
     */
    public GridBoard(ArrayList<ArrayList<Ball>> customBoard) {
        this.board = new ArrayList<>(customBoard);
    }

    /**
     * Calls {@link #GridBoard(boolean)} with true boolean passed
     * which constructs the GridBoard with a randomised board.
     *
     * @see #construct()
     */
    public GridBoard() {
        this(true);
    }

    /**
     * Constructor that creates a new GridBoard object without constructing a new board. This method is
     * set to private because it it only used by the {@link #deepCopy()} method to properly create a copy
     * without causing infinite loops.
     *
     * @param construct whether board should be constructed
     */
    private GridBoard(boolean construct) {
        if (construct) construct();
    }

    /**
     * Constructs a new valid pseudorandom 7*7 board and stores it in the board attribute of this GridBoard.
     * <p>The board is constructed by first taking an array with 1 ball of each colour. This array is then randomised,
     * and distributed over the first 6 fields of the first row. For each subsequent row the same is done, while
     * checking to see that the balls no balls are placed next to balls of the same colour in the row above.
     * Finally for the last column, which by then is the last empty column, a similar array is made, except the
     * ball from the middle of the board is extracted from the middle and added to this array. The array is then
     * placed into the last row randomly until no balls match the colour of their neighbours.
     */
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

    /**
     * Goes over the entire row or column in the direction of a given move, removes all balls that have
     * neighbours of the same colour by calling the {@link #removeBalls(int, int)} method and returning
     * all removed balls in an ArrayList.
     *
     * @param move Move along which the {@code removeBalls()} method looks
     * @return ArrayList of balls containing all removed balls
     */
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

    /**
     * Starts at the location on the board defined by row and column, selects that ball and looks
     * whether there are neighbouring balls of the same colour. If there are, the method recursively
     * moves on to those balls while removing and storing them, until all adjacent balls of the colour
     * of the original ball are removed, and returns an ArrayList of all removed balls.
     *
     * <p>This method should not be called directly, instead use {@link #removeBalls(Move)}.
     *
     * @param row row on the board where this method starts
     * @param column column on the board where this method starts
     * @requires {@code row} and {@code column} to be between 0 and 7
     * @return all removed balls as an ArrayList of balls
     */
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

    /**
     * Creates and returns a new GridBoard object with copy of the board
     * on which this method has been called on.
     *
     * @return a deep copy of this GridBoard
     */
    public GridBoard deepCopy() {
        GridBoard copy = new GridBoard(false);
        copy.board = new ArrayList<>();
        for (ArrayList<Ball> row : board) {
            copy.board.add(new ArrayList<>(row));
        }
        return copy;
    }

    /**
     * Moves all balls in a row or column to one side,
     * where the line and direction are given in the provided {@code move} parameter.
     *
     * @requires {@code {@link #isMoveValid(Move)} == true}
     * @param move move containing the direction and line to be moved
     */
    public void moveLine(Move move) {
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
     * Retrieves a ball on a specified field.
     *
     * @requires {@code validIndex(row) == true}, {@code validIndex(column) == true}
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
     * Puts a given ball on a specified field on the board.
     *
     * @requires {@code validIndex(row) == true}, {@code validIndex(column) == true}
     * @param row index of the row of the ball
     * @param column index of the column of the ball
     * @param ball the specified ball to be set
     */
    private void setField(int row, int column, Ball ball) {
        assert validIndex(row) && validIndex(column);
        board.get(row).set(column, ball);
    }

    /**
     * Checks if a given index is valid. Index is valid if {@code 0 <= index < 7}.
     *
     * @return true is index is valid, false otherwise
     */
    protected boolean validIndex(int index) {
        return index >= 0 && index < 7;
    }

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
     * Checks provided {@code move} for validness.
     * Move is valid if it results in two balls of the same colour being next to each other when made.
     *
     * @requires both {@code validIndex(row)} and {@code validIndex(column)} to be true
     * @param move new move being made
     * @return true if a specified move is valid, false otherwise
     */
    public boolean isMoveValid(Move move) {
        GridBoard copy = deepCopy();
        return isMoveValidCheck(move, copy);
    }

    /**
     * Checks if a specified double move is valid.
     * Double move is valid if it results in only second move being valid,
     * which means that only second move should result in balls of the same colour
     * being adjacent to each other.
     *
     * <p>Check is being made by making both moves and checking the resulting move for validness
     * by invoking the {@link #isMoveValidCheck(Move, GridBoard)} method on the last move.
     * @param first first move
     * @param second second move
     * @return true if after making first move, second move is valid, false if second move is invalid
     */
    public boolean isMoveValid(Move first, Move second) {
        GridBoard copy = deepCopy();
        copy.moveLine(first);
        return isMoveValidCheck(second, copy);
    }

    /**
     * Checks provided {@code move} to be valid on provided {@code board}.
     * Move is valid if it results in two balls of the same colour being next to each other when made.
     *
     * <p>This method should not be called directly because it affects the board passed.
     * Instead use {@link #isMoveValid(Move)} or {@link #isMoveValid(Move, Move)}.
     *
     * @param move move made on the board along which the check takes place
     * @param copy copied board with the move made on it
     * @return true if move is valid, false otherwise
     */
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

    /**
     * Overrides toString to instead return nice visual string of the board of this GridBoard built by
     * {@link TUI#boardString(GridBoard)} using this GridBoard as a parameter.
     *
     * @return String representation of this GridBoard
     */
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
