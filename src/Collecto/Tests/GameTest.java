package Collecto.Tests;

import Collecto.Ball;
import Collecto.Game;
import Collecto.GridBoard;
import Collecto.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private GridBoard sampleBoard;

    @BeforeEach
    void setUp() {
        game = new Game(new GridBoard(Misc.sampleBoardArray()), "player 1", "player 2");
        sampleBoard = new GridBoard(Misc.sampleBoardArray());
    }

    @Test
    void construct() {
        Game game1 = new Game("player 100", "player 299");
        assertTrue(game1.possibleMoves());
        assertEquals(game1.getPlayerName(0), "player 100");
        assertEquals(game1.getPlayerName(1), "player 299");
    }

    @Test
    void makeMove() {
        game.makeMove(new Move(3, Move.Direction.LEFT));

        assertNotEquals(game.getBoard(), new GridBoard(Misc.sampleBoardArray()));
        ArrayList<ArrayList<Ball>> copy = Misc.sampleBoardArray();
        copy.get(2).set(3, Ball.WHITE);
        copy.get(2).set(4, Ball.WHITE);

        copy.get(3).set(4, Ball.WHITE);
        copy.get(3).set(5, Ball.GREEN);
        copy.get(3).set(6, Ball.WHITE);

        assertEquals(game.getBoard().toString(), new GridBoard(copy).toString());
    }

    @Test
    void testMakeMove() {
        ArrayList<ArrayList<Ball>> copy = Misc.copyArray(Misc.emptyBoardArray());

        copy.get(0).set(0, Ball.BLUE);
        copy.get(0).set(3, Ball.RED);
        copy.get(0).set(6, Ball.BLUE);

        Game emptyBoardGame = new Game(new GridBoard(copy), "player 11", "player 22");
        emptyBoardGame.makeMove(new Move(3, Move.Direction.DOWN), new Move(0, Move.Direction.LEFT));

        ArrayList<ArrayList<Ball>> copy2 = Misc.copyArray(Misc.emptyBoardArray());
        copy2.get(6).set(3, Ball.RED);
        assertEquals(emptyBoardGame.getBoard().toString(), new GridBoard(copy2).toString());
    }

    @Test
    void printBoard() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        game.printBoard();
        assertTrue(out.toString().contains("-------+"));
        System.setOut(System.out);
    }

    @Test
    void getBoardString() {
        assertEquals(game.getBoardString(), sampleBoard.getBoardString());
    }

    @Test
    void possibleMoves() {
        assertEquals(game.possibleMoves(), sampleBoard.possibleMoves());
    }

    @Test
    void possibleFirstMove() {
        assertEquals(game.possibleFirstMove(), sampleBoard.possibleMoves(true));
    }

    @Test
    void getWinner() {
        game.makeMove(new Move(3, Move.Direction.LEFT));
        assertEquals("player 1", game.getWinner());

        game.makeMove(new Move(3, Move.Direction.LEFT));
        assertEquals("player 2", game.getWinner());

        game.makeMove(new Move(2, Move.Direction.DOWN));
        assertEquals("player 1", game.getWinner());

        game.makeMove(new Move(2, Move.Direction.LEFT));
        assertEquals("player 1", game.getWinner());

        game.makeMove(new Move(3, Move.Direction.UP));
        assertEquals("player 1", game.getWinner());

        game.makeMove(new Move(6, Move.Direction.LEFT));
        assertEquals("player 1", game.getWinner());
    }

    private ArrayList<ArrayList<Ball>> setUpTestBoard() {
        ArrayList<ArrayList<Ball>> array = Misc.emptyBoardArray();
        array.get(0).set(0, Ball.RED);
        array.get(0).set(3, Ball.RED);
        array.get(0).set(6, Ball.RED);

        array.get(1).set(0, Ball.BLUE);

        return array;
    }

    @Test
    void getWinner2() {
        ArrayList<ArrayList<Ball>> array = setUpTestBoard();
        array.get(1).set(2, Ball.BLUE);
        array.get(1).set(4, Ball.BLUE);
        winnerAssertions(array);
        assertEquals("p2", game.getWinner());
    }

    private void winnerAssertions(ArrayList<ArrayList<Ball>> array) {
        array.get(1).set(6, Ball.BLUE);

        game = new Game(new GridBoard(array), "p1", "p2");
        game.makeMove(new Move(0, Move.Direction.RIGHT));
        assertEquals("p1", game.getWinner());
        game.makeMove(new Move(1, Move.Direction.RIGHT));
    }

    @Test
    void getWinner3() {
        ArrayList<ArrayList<Ball>> array = setUpTestBoard();
        array.get(1).set(3, Ball.BLUE);
        winnerAssertions(array);
        assertNull(game.getWinner());
    }

    @Test
    void getBoard() {
        assertEquals(game.getBoard().toString(), sampleBoard.toString());
    }

    @Test
    void isMoveValid() {
        ArrayList<ArrayList<Ball>> copy = Misc.copyArray(Misc.emptyBoardArray());

        copy.get(0).set(0, Ball.BLUE);
        copy.get(0).set(6, Ball.BLUE);

        Game emptyBoardGame = new Game(new GridBoard(copy), "player 11", "player 22");

        assertTrue(emptyBoardGame.isMoveValid(new Move(0, Move.Direction.LEFT), null));
        assertFalse(emptyBoardGame.isMoveValid(new Move(1, Move.Direction.LEFT), null));

        copy.get(0).set(3, Ball.RED);
        emptyBoardGame = new Game(new GridBoard(copy), "player 11", "player 22");

        assertTrue(emptyBoardGame.isMoveValid(
                new Move(3, Move.Direction.DOWN), new Move(0, Move.Direction.LEFT)));
        assertFalse(emptyBoardGame.isMoveValid(
                new Move(0, Move.Direction.LEFT), new Move(3, Move.Direction.DOWN)));
    }

    @Test
    void getScore() {
        game.makeMove(new Move(3, Move.Direction.LEFT));
        assertEquals(game.getScore(0), 0);
        assertEquals(game.getScore(1), 0);

        game.makeMove(new Move(3, Move.Direction.LEFT));
        assertEquals(game.getScore(0), 0);
        assertEquals(game.getScore(1), 1);

        game.makeMove(new Move(2, Move.Direction.DOWN));
        assertEquals(game.getScore(0), 1);
        assertEquals(game.getScore(1), 1);
    }

    @Test
    void getBalls() {
        HashMap<Ball, Integer> balls1 = new HashMap<>();
        HashMap<Ball, Integer> balls2 = new HashMap<>();
        for (Ball ball : Ball.values()) {
            if (ball != Ball.WHITE) {
                balls1.put(ball, 0);
                balls2.put(ball, 0);
            }
        }

        balls1.put(Ball.BLUE, 2);
        balls1.put(Ball.RED, 2);

        game.makeMove(new Move(3, Move.Direction.LEFT));
        assertEquals(game.getBalls(0), balls1);
        assertEquals(game.getBalls(1), balls2);

        balls2.put(Ball.GREEN, 3);

        game.makeMove(new Move(3, Move.Direction.LEFT));
        assertEquals(game.getBalls(0), balls1);
        assertEquals(game.getBalls(1), balls2);

        balls1.put(Ball.ORANGE, 2);
        balls1.put(Ball.RED, 4);

        game.makeMove(new Move(2, Move.Direction.DOWN));
        assertEquals(game.getBalls(0), balls1);
        assertEquals(game.getBalls(1), balls2);
    }

    @Test
    void getPlayerName() {
        assertEquals(game.getPlayerName(0), "player 1");
        assertEquals(game.getPlayerName(1), "player 2");
    }
}