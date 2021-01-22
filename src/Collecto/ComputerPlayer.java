package Collecto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Collecto.Misc.Move;

public class ComputerPlayer extends Player {
    private int level;

    /**
     * Constructs a ComputerPlayer class with first level of difficulty
     */
    public ComputerPlayer() {
        this(1);
    }

    /**
     * @requires level > 0 && level <= 3
     * @param level requested difficulty level for this ComputerPlayer
     */
    public ComputerPlayer(int level) {
        super("smartass");
        assert level > 0 && level <= 2;
        this.level = level;
    }

    /**
     * @return difficulty level of the ComputerPLayer
     */
    public int getLevel() {
        return level;
    }

    /**
     * determines the next move of the computer player
     * @requires it is the turn of this computer player
     * @ensures int move >= 0 && int move <= 27
     * @return Move
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

    private static Move[] makeBeginnerMoveSingle(GridBoard board) {
        Move move;
        for (int i = 0; i < 7; i++) {
            for (GridBoard.Direction direction : GridBoard.Direction.values()) {
                move = new Move(i, direction);
                if (board.isMoveValid(move)) return new Move[]{move};
            }
        }
        return null;
    }

    public static Move[] makeBeginnerMove(GridBoard board) {
        GridBoard copy;

        // check if single move exists
        Move[] moves = makeBeginnerMoveSingle(board);
        if (moves != null) return moves;

        // if not - find two moves
        for (int j = 0; j < 7; j++) {
            for (GridBoard.Direction direction : GridBoard.Direction.values()) {
                copy = board.deepCopy();
                copy.moveLine(new Move(j, direction));
                if (!board.toString().equals(copy.toString())) {
                    for (int k = 0; k < 7; k++) {
                        for (GridBoard.Direction direction2 : GridBoard.Direction.values()) {
                            if (copy.isMoveValid(new Move(k, direction2)))
                                return new Move[]{new Move(j, direction), new Move(k, direction2)};
                        }
                    }
                }
            }
        }
        return null;
    }

    private int ballsFromMove(GridBoard board, Move move) {
        GridBoard copy = board.deepCopy();
        copy.moveLine(move);
        return copy.removeBalls(move).size();
    }

    private Move[] makeIntermediateMoveSingle(GridBoard board) {
        Move move;
        int max = 0;
        int balls;
        Move max_move = null;

        for (int i = 0; i < 7; i++) {
            for (GridBoard.Direction direction : GridBoard.Direction.values()) {
                move = new Move(i, direction);
                if (board.isMoveValid(move)) {
                    balls = ballsFromMove(board, move);
                    if (balls > max) {
                        max = balls;
                        max_move = move;
                    }
                }
            }
        }

        return new Move[]{max_move, new Move(max)};
    }

    public Move[] makeIntermediateMove(GridBoard board) {
        GridBoard copy;

        // check if single move exists
        Move[] moves = new Move[]{makeIntermediateMoveSingle(board)[0]};
        if (moves[0] != null) return moves;

        // if not - find two moves
        HashMap<Move[], Integer> movesResults = new HashMap<>();
        Move move1;
        for (int j = 0; j < 7; j++) {
            for (GridBoard.Direction direction : GridBoard.Direction.values()) {
                copy = board.deepCopy();
                move1 = new Move(j, direction);
                copy.moveLine(move1);
                if (!board.toString().equals(copy.toString())) {
                    Move[] temp = makeIntermediateMoveSingle(copy);
                    Move max_move = temp[0];
                    int max = temp[1].push();
                    movesResults.put(new Move[]{move1, max_move}, max);
                }
            }
        }

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
