package Collecto;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains a beginner and an intermediate
 * AI which can play Collecto instead of a regular user.
 * It is integrated into the Client class so that a player
 * can call on it to play a game for them.
 *
 * <p>The strategy of the beginner AI is to make the very first
 * possible move that it encounters.
 *
 * <p>The strategy of the intermediate AI is to calculate all
 * current possible moves, and see how many balls they would
 * gain by making that move, after which the move that gains the
 * the highest amount of balls is selected as the next move that
 * the AI makes.
 *
 * @see Client
 */
public class ComputerPlayer {
    private final int level;

    /**
     * Default constructor which constructs
     * a ComputerPlayer with beginner level difficulty.
     */
    public ComputerPlayer() {
        this(1);
    }

    /**
     * Constructs a ComputerPlayer with a given level of difficulty.
     *
     * @param level requested difficulty level for this ComputerPlayer
     * @requires level to be either {@code 1} and {@code 2}
     */
    public ComputerPlayer(int level) {
        assert 1 <= level && level <= 2;
        this.level = level;
    }

    /**
     * Private help method to determine if there is
     * a single move possible on board provided.
     *
     * <p>Works by iterating through all moves
     * and trying to find a possible move.
     * If such a move is found — returns this move,
     * if no possible moves found returns null.
     *
     * @param board the board used to determine a move
     * @requires board != null
     * @return first possible move or null
     */
    private static Move makeBeginnerMoveSingle(GridBoard board) {
        Move move;
        for (int i = 0; i < 7; i++) {
            for (Move.Direction direction : Move.Direction.values()) {
                move = new Move(i, direction);
                if (board.isMoveValid(move)) {
                    return move;
                }
            }
        }
        return null;
    }

    /**
     * Looks for possible single or double moves.
     * If no move is found returns null.
     * <p>Description of work:
     * <p>Firstly uses {@code makeBeginnerMoveSingle()}
     * to see whether there is any move possible with a single push.
     * If that method returns a move - return this move.
     * If that method returns null - try to find a move with two pushes
     * by iterating through all possible moves
     * and for each iteration looking for possible second moves
     * using the same algorithm as {@code makeBeginnerSingleMove()}.
     *
     * @param board board to find a move on
     * @return an array of either 1 or 2 moves or null if no moves are found
     */
    public static Move[] makeBeginnerMove(GridBoard board) {
        GridBoard copy;

        // check if single move exists
        Move move = makeBeginnerMoveSingle(board);
        if (move != null) {
            return new Move[]{move};
        }

        // if not - find two moves
        for (int j = 0; j < 7; j++) {
            for (Move.Direction direction : Move.Direction.values()) {
                copy = board.deepCopy();
                copy.moveLine(new Move(j, direction));
                if (!board.toString().equals(copy.toString())) {
                    for (int k = 0; k < 7; k++) {
                        for (Move.Direction direction2 : Move.Direction.values()) {
                            if (copy.isMoveValid(new Move(k, direction2))) {
                                return new Move[]{new Move(j, direction), new Move(k, direction2)};
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns the difficulty level of this ComputerPlayer.
     *
     * @return difficulty level of this ComputerPlayer
     */
    public int getLevel() {
        return level;
    }

    /**
     * Determines the next move of the computer player using {@code makeBeginnerMove()} or
     * {@code makeIntermediateMove()} depending on the {@code level} of this ComputerPlayer.
     *
     * <p>If {@code level == 1}, this method uses {@link #makeBeginnerMove(GridBoard)},
     * if {@code level == 2}, this method uses {@link #makeIntermediateMove(GridBoard)}.
     *
     * @param board the specified GridBoard on which the AI will attempt to make a move
     * @requires board != null
     * @ensures return is a valid move or null
     * @return a determined move
     */
    public Move[] makeMove(GridBoard board) {
        switch (level) {
            case 1:
                return makeBeginnerMove(board);
            case 2:
                return makeIntermediateMove(board);
            default:
                return null;
        }
    }

    /**
     * Determines the amount of balls gained from a certain move,
     * so that the intermediate level AI can use this
     * as a score to see what move is the best move.
     *
     * @param board the board on which the move is made
     * @param move the move that has to be checked
     * @return amount of balls gained by making this move
     */
    private int ballsFromMove(GridBoard board, Move move) {
        GridBoard copy = board.deepCopy();
        copy.moveLine(move);
        return copy.removeBalls(move).size();
    }

    /**
     * Private help method to determine if there is
     * a single move possible on a provided board by using the intermediate algorithm.
     *
     * <p>Works by iterating through all moves
     * and trying to find a possible move.
     * If no possible moves found returns null.
     * If any moves found — finds the best move
     * using {@link #ballsFromMove(GridBoard, Move)} and finds
     * this best move by comparing amount of balls gained from that specific move.
     * The best move is considered to be the move with most balls gained from this move.
     *
     * @param board the board on which the moves are made and compared
     * @return best move in one push or null
     */
    private Move[] makeIntermediateMoveSingle(GridBoard board) {
        Move move;
        int max = 0;
        int balls;
        Move maxMove = null;

        for (int i = 0; i < 7; i++) {
            for (Move.Direction direction : Move.Direction.values()) {
                move = new Move(i, direction);
                if (board.isMoveValid(move)) {
                    balls = ballsFromMove(board, move);
                    if (balls > max) {
                        max = balls;
                        maxMove = move;
                    }
                }
            }
        }

        return new Move[]{maxMove, new Move(max)};
    }

    /**
     * Looks for possible move in either one or two pushes.
     * If no such move is found returns null.
     * <p>Description of how this method works:
     * <p>Firstly uses {@code makeIntermediateMoveSingle()}
     * to see whether there is a move possible with a single push.
     * If it returns a move - returns this move and finishes.
     * If the method returns null - tries to find a move with double pushes
     * by iterating over all possible moves
     * and for each iteration looking for second possible moves
     * using the same algorithm as {@code makeIntermediateSingleMove()}.
     *
     * @param board the board on which the move is determined
     * @return an array of either 1 or 2 moves or null if no moves found
     */
    public Move[] makeIntermediateMove(GridBoard board) {
        GridBoard copy;

        // check if single move exists
        Move[] moves = new Move[]{makeIntermediateMoveSingle(board)[0]};
        if (moves[0] != null) {
            return moves;
        }

        // if not - find two moves
        HashMap<Move[], Integer> movesResults = new HashMap<>();
        Move move1;
        for (int j = 0; j < 7; j++) {
            for (Move.Direction direction : Move.Direction.values()) {
                copy = board.deepCopy();
                move1 = new Move(j, direction);
                copy.moveLine(move1);
                if (!board.toString().equals(copy.toString())) {
                    Move[] temp = makeIntermediateMoveSingle(copy);
                    Move maxMove = temp[0];
                    int max = temp[1].push();
                    movesResults.put(new Move[]{move1, maxMove}, max);
                }
            }
        }

        // determine a move with max amount of balls gained
        Map.Entry<Move[], Integer> maxEntry = null;
        for (Map.Entry<Move[], Integer> entry : movesResults.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        assert maxEntry != null;
        return maxEntry.getKey();
    }
}
