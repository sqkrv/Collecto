package Collecto;

import java.util.ArrayList;

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
     * @return int of move
     */
    @Override
    public int[] makeMove() {
        int[] move = new int[2];
        if (level == 1) {
            move[0] = makeBeginnerMove();
            if (move[0] != -1) {
                return move;
            } else {
                return makeBeginnerMoveDouble();
            }
        } else if (level == 2)  {
            return makeIntermediateMove();
        } else {
            return makeExpertMove();
        }
    }

    private int makeBeginnerMove() {
        GridBoard board = new GridBoard(); //TODO: change this to be better (e.g. use a copy of the actual board)
        // single move
        for (int i = 0; i < 7; i++) {
            if (board.isMoveValid(new Misc.Move(i, GridBoard.Direction.RIGHT))) return (i);
            if (board.isMoveValid(new Misc.Move(i, GridBoard.Direction.LEFT))) return (7 + i);
            if (board.isMoveValid(new Misc.Move(i, GridBoard.Direction.UP))) return (14 + i);
            if (board.isMoveValid(new Misc.Move(i, GridBoard.Direction.DOWN))) return (21 + i);
        }
        return -1;
    }

    private int[] makeBeginnerMoveDouble() {
        GridBoard board = new GridBoard(); //TODO: change this to be better (e.g. use a copy of the actual board)
        // double move
        GridBoard copy;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                copy = board.deepCopy();
                copy.moveLine(new Misc.Move(j, GridBoard.Direction.values()[i]));
                if (!board.toString().equals(copy.toString())) {
                    for (int k = 0; k < 7; k++) {
                        if (copy.isMoveValid(new Misc.Move(j, GridBoard.Direction.RIGHT))) return new int[]{j + i*7, k};
                        if (copy.isMoveValid(new Misc.Move(j, GridBoard.Direction.LEFT))) return new int[]{j + i*7, 7 + k};
                        if (copy.isMoveValid(new Misc.Move(j, GridBoard.Direction.UP))) return new int[]{j + i*7, 14 + k};
                        if (copy.isMoveValid(new Misc.Move(j, GridBoard.Direction.DOWN))) return new int[]{j + i*7, 21 + k};
                    }
                }
            }
        }
        return null;
    }

//    private ArrayList<Ball> ballsFromMove(GridBoard board, Misc.Move move) {
//        GridBoard copy = board.deepCopy();
//        copy.removeBalls(move.getLine(), move.getLine());
//    }

    private int[] makeIntermediateMove() {
        GridBoard board = new GridBoard(); //TODO: change this to be better (e.g. use a copy of the actual board)
        // double move
        GridBoard copy;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                copy = board.deepCopy();
                copy.moveLine(new Misc.Move(j, GridBoard.Direction.values()[i]));
                if (!board.toString().equals(copy.toString())) {
                    for (int k = 0; k < 7; k++) {
                        if (copy.isMoveValid(new Misc.Move(j, GridBoard.Direction.RIGHT))) return new int[]{j + i*7, k};
                        if (copy.isMoveValid(new Misc.Move(j, GridBoard.Direction.LEFT))) return new int[]{j + i*7, 7 + k};
                        if (copy.isMoveValid(new Misc.Move(j, GridBoard.Direction.UP))) return new int[]{j + i*7, 14 + k};
                        if (copy.isMoveValid(new Misc.Move(j, GridBoard.Direction.DOWN))) return new int[]{j + i*7, 21 + k};
                    }
                }
            }
        }
        return new int[]{1};
    }

    private int[] makeExpertMove() {
        return null;
    }
}
