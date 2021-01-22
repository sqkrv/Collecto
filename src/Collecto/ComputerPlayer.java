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
        assert level > 0 && level <= 3;
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
            case 3:
                return makeExpertMove();
            default:
                return null;
        }
    }

    private Move[] makeBeginnerMoveSingle(GridBoard board) {
        Move move;
        for (int i = 0; i < 7; i++) {
            for (GridBoard.Direction direction : GridBoard.Direction.values()) {
                move = new Move(i, direction);
                if (board.isMoveValid(move)) return new Move[]{move};
            }
        }
        return null;
    }

    public Move[] makeBeginnerMove(GridBoard board) {
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
                            if (copy.isMoveValid(new Move(j, direction2)))
                                return new Move[]{new Move(j, direction), new Move(k, direction2)};
                        }
                    }
                }
            }
        }
        return null;
    }

    private ArrayList<Ball> ballsFromMove(GridBoard board, Move move) {
        GridBoard copy = board.deepCopy();
        return copy.removeBalls(move.getLine(), move.getLine());
    }

    public Move[] makeIntermediateMove(GridBoard board) {
        // double move
//        GridBoard board = new GridBoard(); //TODO: change this to be better (e.g. use a copy of the actual board)
        HashMap<Move, Integer> movesResults = new HashMap<>();
        int max;
        ArrayList<Ball> balls;
        Move move1;
        Move move;
        GridBoard copy;
        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 4; i++) {
                copy = board.deepCopy();
                move1 = new Move(j, GridBoard.Direction.values()[i]);
//                copy.moveLine();
                if (!board.toString().equals(copy.toString())) {
                    for (int k = 0; k < 7; k++) {
                        max = 0;
                        for (GridBoard.Direction direction : GridBoard.Direction.values()) {
                            move = new Move(j, direction);
                            if (copy.isMoveValid(move)) {
                                balls = ballsFromMove(copy, move);
                                if (balls.size() > max) {
                                    max = balls.size();
                                    movesResults.put(move, balls.size());
                                }
                            }
                        }
                    }
                }
            }
        }

        Map.Entry<Move, Integer> maxEntry = null;
        for (Map.Entry<Move, Integer> entry : movesResults.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        assert maxEntry != null;
        return new Move[]{maxEntry.getKey()};
    }

    private Move[] makeExpertMove() {
        return null;
    }
}
