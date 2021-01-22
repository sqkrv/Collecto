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

    public Move[] makeMove() {
        switch (level) {
            case 1:
                Move[] move = makeBeginnerMove();
                if (move != null) {
                    return move;
                } else {
                    return makeBeginnerMoveDouble();
                }
            case 2:
                return makeIntermediateMove();
            case 3:
                return makeExpertMove();
            default:
                return null;
        }
    }

    private Move[] makeBeginnerMove() {
        GridBoard board = new GridBoard(); //TODO: change this to be better (e.g. use a copy of the actual board)
        // single move
        Move move;
        for (int i = 0; i < 7; i++) {
            for (GridBoard.Direction direction : GridBoard.Direction.values()) {
                move = new Move(i, direction);
                if (board.isMoveValid(move)) return new Move[]{move};
            }
        }
        return null;
    }

    private Move[] makeBeginnerMoveDouble() {
        GridBoard board = new GridBoard(); //TODO: change this to be better (e.g. use a copy of the actual board)
        // double move
        GridBoard copy;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                copy = board.deepCopy();
                copy.moveLine(new Move(j, GridBoard.Direction.values()[i]));
                if (!board.toString().equals(copy.toString())) {
                    for (int k = 0; k < 7; k++) {
                        if (copy.isMoveValid(new Move(j, GridBoard.Direction.RIGHT))) return new Move[]{new Move(j + i*7), new Move(k, GridBoard.Direction.RIGHT)};
//                        else if (copy.isMoveValid(new Move(j, GridBoard.Direction.LEFT))) return new int[]{j + i*7, 7 + k};
//                        else if (copy.isMoveValid(new Move(j, GridBoard.Direction.UP))) return new int[]{j + i*7, 14 + k};
//                        else if (copy.isMoveValid(new Move(j, GridBoard.Direction.DOWN))) return new int[]{j + i*7, 21 + k};
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

    private Move[] makeIntermediateMove() {
        // double move
        GridBoard board = new GridBoard(); //TODO: change this to be better (e.g. use a copy of the actual board)
        HashMap<Move, Integer> movesResults = new HashMap<>();
        int max = 0;
        ArrayList<Ball> balls;
        Move move;
        GridBoard copy;
        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 4; i++) {
                copy = board.deepCopy();
                copy.moveLine(new Move(j, GridBoard.Direction.values()[i]));
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

        return new Move[]{maxEntry.getKey()};
    }

    private Move[] makeExpertMove() {
        return null;
    }
}
