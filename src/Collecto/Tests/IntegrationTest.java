package Collecto.Tests;

import Collecto.ComputerPlayer;
import Collecto.Game;
import Collecto.GridBoard;
import Collecto.Move;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest {
    private static final String AI1NAME = "AI1";
    private static final String AI2NAME = "AI2";
    private static final String DRAW = "Draw";
    private static final String BALLS = "balls";
    private static HashMap<String, Integer> stats;
    private Game randomGame;
    private Game sampleGame;

    @BeforeAll
    static void setUpBefore() {
        stats = new HashMap<>();
        stats.put(AI1NAME, 0);
        stats.put(AI2NAME, 0);
        stats.put(DRAW, 0);
        stats.put(AI1NAME + BALLS, 0);
        stats.put(AI2NAME + BALLS, 0);
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Random board winning stats:");
        System.out.println(AI1NAME + " - " + stats.get(AI1NAME)
                + " (" + stats.get(AI1NAME + BALLS) + " " + BALLS + ")");
        System.out.println(AI2NAME + " - " + stats.get(AI2NAME)
                + " (" + stats.get(AI2NAME + BALLS) + " " + BALLS + ")");
        System.out.println(DRAW + " - " + stats.get(DRAW));
    }

    @BeforeEach
    void setUp() {
        sampleGame = new Game(new GridBoard(Misc.sampleBoardArray()), "player 1", "player 2");
        randomGame = new Game(new GridBoard(), "random player 1", "random player 2");
    }

    private void constructRandomGame(String player1Name, String player2Name) {
        randomGame = new Game(player1Name, player2Name);
    }

    private void makeMoveAndAssert(ComputerPlayer ai, Game game) {
        Move[] moves = ai.makeMove(game.getBoard());
        if (moves.length == 2) {
            if (moves[1] != null) {
                assertFalse(game.possibleFirstMove());
            }
            assertTrue(game.isMoveValid(moves[0], moves[1]));
            game.makeMove(moves[0], moves[1]);
        } else {
            assertTrue(game.isMoveValid(moves[0], null));
            game.makeMove(moves[0]);
        }
    }

    private String playGame(Game game) {
        ComputerPlayer ai1 = new ComputerPlayer();
        ComputerPlayer ai2 = new ComputerPlayer(2);

        while (game.possibleMoves()) {
            makeMoveAndAssert(ai1, game);

            if (!game.possibleMoves()) {
                break;
            }

            makeMoveAndAssert(ai2, game);
        }

        return game.getWinner();
    }

    private void updateStats(String name) {
        if (name == null) {
            stats.put(DRAW, stats.get(DRAW) + 1);
        } else {
            stats.put(name, stats.get(name) + 1);
        }
    }

    private void updateBalls(String name, Collection<Integer> amounts) {
        int amount = 0;
        for (int colour : amounts) {
            amount += colour;
        }
        stats.put(name + BALLS, stats.get(name + BALLS) + amount);
    }

    @Test
    void sampleGame() {
        playGame(sampleGame);
    }

    @RepeatedTest(1000)
    void randomGame() {
        constructRandomGame(AI1NAME, AI2NAME);
        String winner = playGame(randomGame);
        updateStats(winner);

        updateBalls(AI1NAME, randomGame.getBalls(0).values());
        updateBalls(AI2NAME, randomGame.getBalls(1).values());
    }
}
