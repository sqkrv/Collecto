package Collecto;

import java.util.*;

/**
 * This class contains the implementation of a game of Collecto.
 * It contains a GridBoard object, two players who play the game,
 * tracking of whose turn it is, and methods to return or change
 * attributes related to the game. It is used in PlayerHandler
 * and Client to keep track of their current game being played.
 * @see Client
 * @see PlayerHandler
 */
public class Game {
    private final GridBoard board;
    private Player[] players = new Player[2];
    private boolean turnPlayer1 = true;

    /**
     * Constructs a new Game object and a new GridBoard given the names of the 2 players of this game.
     * @requires player1Name != null && player2Name != null
     * @param player1Name first player name
     * @param player2Name second player name
     */
    public Game(String player1Name, String player2Name) {
        this(new GridBoard(), player1Name, player2Name);
    }

    /**
     * Constructs a new Game object with a given GridBoard, and the names of the 2 players of this game.
     * @requires board != null && player1Name != null && player2Name != null
     * @param board given GridBoard containing a predetermined initial board
     * @param player1Name first player name
     * @param player2Name second player name
     */
    public Game(GridBoard board, String player1Name, String player2Name) {
        players[0] = new Player(player1Name);
        players[1] = new Player(player2Name);
        this.board = board;
    }

    /**
     * If the provided move is valid on the board of this game, makes that move on the board.
     * Gives the balls gained by making this move to player whose turn it is.
     * Uses {@link GridBoard#isMoveValid(Move)}, {@link GridBoard#moveLine(Move)} and {@link GridBoard#removeBalls(Move)}
     * @requires board.isMoveValid(move) == true
     * @param move
     */
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

    /**
     * If the provided double move is valid, makes a move by first making the first move,
     * and invoking the method {@link #makeMove(Move)} to make the second move.
     * @param first provided first move
     * @param second provided second move
     */
    public void makeMove(Move first, Move second) {
        if (board.isMoveValid(first, second)) {
            board.moveLine(first);
            makeMove(second);
        }
    }

    /**
     * Uses the TUI to print the current state of the board on the terminal of the caller.
     */
    public void printBoard() {
        TUI.print(TUI.colouredBoard(board));
    }

    /**
     * Returns the current state of the board as a String by invoking the {@link GridBoard#getBoardString()} method.
     * @return String of the current board
     */
    public String getBoardString() {
        return board.getBoardString();
    }

    /**
     * Checks if there are still possible moves to be made on the current board by invoking the
     * {@link GridBoard#possibleMoves()} method.
     * @return true if there are possible moves left to be made, false if there are no moves left
     */
    public boolean possibleMoves() {
        return board.possibleMoves();
    }

    /**
     * Checks if there is still a single move that can be played by invoking the
     * {@link GridBoard#possibleMoves(boolean)} method.
     * @return true if a single possible move can be made, false if there is no single possible move left
     */
    public boolean possibleFirstMove() {
        return board.possibleMoves(true);
    }

    /**
     * Determines which player would be the winner of the game in the current state, based on the players points.
     * If the points are equal the winner is the player with the most balls. If the points and amount of balls are
     * equal for both players, it is a draw.
     * @return name of player who won, or null if it is a draw.
     */
    public String getWinner() {
        int points1 = players[0].getPoints();
        int points2 = players[1].getPoints();
        if (points1 > points2) {
            return getPlayerName(0);
        } else if (points2 > points1) {
            return getPlayerName(1);
        } else {
            int balls1 = players[0].getBallAmount();
            int balls2 = players[1].getBallAmount();
            if (balls1 > balls2) {
                return getPlayerName(0);
            } else if (balls2 > balls1) {
                return getPlayerName(1);
            }
        }
        return null;
    }

    /**
     * Returns the board of this game in its current state.
     * @return board of this game
     */
    public GridBoard getBoard() {
        return board;
    }

    /**
     * Determines if a given single or double move is valid, meaning it places two balls of the
     * same colour next to each other. If secondMove == null, the move is considered a single move.
     * @requires firstMove != null
     * @param firstMove first move to be checked
     * @param secondMove optional second move to be checked
     * @return true if the move made is valid, false if it is not
     */
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
     * Fetches the balls that a player has acquired this game in a HashMap where each Ball colour maps
     * to an integer value containing the amount that the player holds of that ball colour.
     * @requires playerIndex to be 0 or 1
     * @param playerIndex index of the player
     * @return current amount of balls of each colour player has
     */
    public HashMap<Ball, Integer> getBalls(int playerIndex) {
        return players[playerIndex].getBalls();
    }

    /**
     * Fetches the name of the player given by the index.
     * @requires playerIndex to be 0 or 1
     * @param playerIndex index of the player
     * @return the String which is the name of the player with the index passed
     */
    public String getPlayerName(int playerIndex) {
        return players[playerIndex].getName();
    }
}
