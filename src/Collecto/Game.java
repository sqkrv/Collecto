package Collecto;

import java.util.*;

public class Game {
    private final GridBoard board;
    private Player[] players = new Player[2];
    private boolean turnPlayer1 = true;

    public Game(String player1Name, String player2Name) {
        this(new GridBoard(), player1Name, player2Name);
    }

    public Game(GridBoard board, String player1Name, String player2Name) {
        players[0] = new Player(player1Name);
        players[1] = new Player(player2Name);
        this.board = board;
    }

    public void makeMove(Move move) {
        if (board.isMoveValid(move)) {
            board.moveLine(move);
            if (turnPlayer1) {
                players[0].addBalls(board.removeBalls(move));
            } else {
                players[1].addBalls(board.removeBalls(move));
            }
            turnPlayer1 = !turnPlayer1;
        }
    }

    public void makeMove(Move first, Move second) {
        if (board.isMoveValid(first, second)) {
            board.moveLine(first);
        }
    }

    public void printBoard() {
        TUI.print(TUI.colouredBoard(board));
    }

    public String getBoardString() {
        return board.getBoardString();
    }

    public boolean possibleMoves() {
        return board.possibleMoves();
    }

    public boolean possibleFirstMove() {
        return board.possibleMoves(true);
    }

    public String getWinner() {
        if (players[0].getPoints() > players[1].getPoints()) {
            return players[0].getName();
        } else if (players[1].getPoints() > players[0].getPoints()) {
            return players[1].getName();
        } else if (players[0].getBalls().size() > players[1].getBalls().size()) {
            return players[0].getName();
        } else if (players[1].getBalls().size() > players[0].getBalls().size()) {
            return players[1].getName();
        } else {
            return null;
        }
    }

    public GridBoard getBoard() {
        return board;
    }

    public boolean isMoveValid(Move firstMove, Move secondMove) {
        if (secondMove == null) {
            return board.isMoveValid(firstMove);
        } else {
            return board.isMoveValid(firstMove, secondMove);
        }
    }

    /**
     * Calculates the score of the player
     * @requires playerIndex to be 0 or 1
     * @param playerIndex index of the player
     * @return current score of the player
     */
    public int getScore(int playerIndex) {
        return players[playerIndex].getPoints();
    }

    /**
     * @requires playerIndex to be 0 or 1
     * @param playerIndex index of the player
     * @return current amount of balls of each colour player has
     */
    public HashMap<Ball, Integer> getBalls(int playerIndex) {
        return players[playerIndex].getBalls();
    }

    /**
     * @requires playerIndex to be 0 or 1
     * @param playerIndex index of the player
     * @return the String which is the name of the player with the index passed
     */
    public String getPlayerName(int playerIndex) {
        return players[playerIndex].getName();
    }
}
