package Collecto.Tests;

import Collecto.ComputerPlayer;
import Collecto.GridBoard;
import org.junit.jupiter.api.*;
import Collecto.Ball;
import Collecto.Move;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import static Collecto.Tests.Misc.copyArray;

class ComputerPlayerTest {
    ComputerPlayer player;
    static ArrayList<ArrayList<Ball>> emptyBoardArray = new ArrayList<>();
    static ArrayList<ArrayList<Ball>> testBoardArray = new ArrayList<>();
    private static ArrayList<ArrayList<Ball>> sampleBoardArray;

    @BeforeAll
    static void setEmptyBoardArray() {
        for (int i = 0; i < 7; i++) {
            emptyBoardArray.add(new ArrayList<>());
            for (int j = 0; j < 7; j++) {
                emptyBoardArray.get(i).add(Ball.WHITE);
            }
        }
    }

    @BeforeAll
    static void setTestBoardArray() {
        testBoardArray = copyArray(emptyBoardArray);
        testBoardArray.get(0).set(0, Ball.YELLOW);
        testBoardArray.get(0).set(6, Ball.YELLOW);

        testBoardArray.get(0).set(1, Ball.RED);
        testBoardArray.get(0).set(3, Ball.RED);
        testBoardArray.get(0).set(5, Ball.RED);

        testBoardArray.get(0).set(0, Ball.BLUE);
        testBoardArray.get(0).set(2, Ball.BLUE);
        testBoardArray.get(0).set(4, Ball.BLUE);
        testBoardArray.get(0).set(6, Ball.BLUE);
    }

    @BeforeAll
    public static void setSampleBoardArray() {
        sampleBoardArray = Misc.sampleBoardArray();
    }

    @BeforeEach
    void setUp() {
        player = new ComputerPlayer();
    }

    @Test
    void getLevel() {
        assertEquals(player.getLevel(), 1);
        assertThrows(AssertionError.class, () -> new ComputerPlayer(4));
        player = new ComputerPlayer(3);
        assertEquals(player.getLevel(), 3);
    }

    @Nested
    @DisplayName("Beginner moves")
    class beginnerMoves {
        @Test
        void makeBeginnerMoveSingle() {
            ArrayList<ArrayList<Ball>> array = copyArray(emptyBoardArray);
            array.get(0).set(0, Ball.BLUE);
            array.get(0).set(6, Ball.BLUE);

            Move[] moves = player.makeBeginnerMove(new GridBoard(array));
            assertTrue(
                    moves[0].equals(new Move(0, Move.Direction.LEFT)) ||
                            moves[0].equals(new Move(0, Move.Direction.RIGHT))
            );
        }

        @Test
        void makeBeginnerMoveDouble() {
            ArrayList<ArrayList<Ball>> array = copyArray(emptyBoardArray);
            array.get(0).set(0, Ball.BLUE);
            array.get(0).set(3, Ball.RED);
            array.get(0).set(6, Ball.BLUE);

            Move[] moves = player.makeBeginnerMove(new GridBoard(array));
            assertEquals(moves[0], new Move(3, Move.Direction.DOWN));
            assertTrue(
                    moves[1].equals(new Move(0, Move.Direction.LEFT)) ||
                            moves[1].equals(new Move(0, Move.Direction.RIGHT))
            );
        }
    }

    @Nested
    @DisplayName("Intermediate moves")
    class intermediateMoves {
        @Test
        void makeIntermediateMoveSingle() {
            ArrayList<ArrayList<Ball>> array = copyArray(sampleBoardArray);

            Move[] moves = player.makeIntermediateMove(new GridBoard(array));
            assertEquals(moves[0], new Move(3, Move.Direction.LEFT));

            array.get(0).set(3, Ball.RED);
            array.get(1).set(3, Ball.PURPLE);
            moves = player.makeIntermediateMove(new GridBoard(array));
            assertEquals(moves[0], new Move(3, Move.Direction.DOWN));
        }

        @Test
        void makeIntermediateMoveDouble() {
            ArrayList<ArrayList<Ball>> array = copyArray(emptyBoardArray);
            array.get(0).set(0, Ball.BLUE);
            array.get(0).set(3, Ball.RED);
            array.get(0).set(6, Ball.BLUE);

            Move[] moves = player.makeIntermediateMove(new GridBoard(array));
            assertEquals(moves[0], new Move(3, Move.Direction.DOWN));
            assertTrue(
                    moves[1].equals(new Move(0, Move.Direction.LEFT)) ||
                            moves[1].equals(new Move(0, Move.Direction.RIGHT))
            );
        }
    }
}
