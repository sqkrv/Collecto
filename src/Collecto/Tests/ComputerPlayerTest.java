package Collecto.Tests;

import Collecto.Ball;
import Collecto.ComputerPlayer;
import Collecto.GridBoard;
import Collecto.Move;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static Collecto.Tests.Misc.copyArray;
import static org.junit.jupiter.api.Assertions.*;

class ComputerPlayerTest {
    static ArrayList<ArrayList<Ball>> emptyBoardArray = new ArrayList<>();
    static ArrayList<ArrayList<Ball>> testBoardArray = new ArrayList<>();
    private static ArrayList<ArrayList<Ball>> sampleBoardArray;
    ComputerPlayer ai1;
    ComputerPlayer ai2;

    @BeforeAll
    static void setEmptyBoardArray() {
        emptyBoardArray = Misc.emptyBoardArray();
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
        ai1 = new ComputerPlayer();
        ai2 = new ComputerPlayer(2);
    }

    @Test
    void getLevel() {
        assertEquals(ai1.getLevel(), 1);
        assertThrows(AssertionError.class, () -> new ComputerPlayer(4));
        ai1 = new ComputerPlayer(2);
        assertEquals(ai1.getLevel(), 2);
    }

    @Nested
    @DisplayName("Beginner moves")
    class BeginnerMoves {
        @Test
        void makeBeginnerMoveSingle() {
            ArrayList<ArrayList<Ball>> array = copyArray(emptyBoardArray);
            array.get(0).set(0, Ball.BLUE);
            array.get(0).set(6, Ball.BLUE);

            Move[] moves = ai1.makeMove(new GridBoard(array));
            assertTrue(
                    moves[0].equals(new Move(0, Move.Direction.LEFT)) ||
                            moves[0].equals(new Move(0, Move.Direction.RIGHT))
            );
        }

        @Test
        void makeBeginnerMoveDouble() {
            checkMove(ai1);
        }

        @Test
        void makeBeginnerMoveEmpty() {
            ArrayList<ArrayList<Ball>> array = copyArray(emptyBoardArray);

            Move[] moves = ai1.makeMove(new GridBoard(array));
            assertNull(moves);
        }
    }

    private void checkMove(ComputerPlayer ai) {
        ArrayList<ArrayList<Ball>> array = copyArray(emptyBoardArray);
        array.get(0).set(0, Ball.BLUE);
        array.get(0).set(3, Ball.RED);
        array.get(0).set(6, Ball.BLUE);

        Move[] moves = ai.makeMove(new GridBoard(array));
        assertEquals(moves[0], new Move(3, Move.Direction.DOWN));
        assertTrue(
                moves[1].equals(new Move(0, Move.Direction.LEFT)) ||
                        moves[1].equals(new Move(0, Move.Direction.RIGHT))
        );
    }

    @Nested
    @DisplayName("Intermediate moves")
    class IntermediateMoves {
        @Test
        void makeIntermediateMoveSingle() {
            ArrayList<ArrayList<Ball>> array = copyArray(sampleBoardArray);

            Move[] moves = ai2.makeMove(new GridBoard(array));
            assertEquals(moves[0], new Move(3, Move.Direction.LEFT));

            array.get(0).set(3, Ball.RED);
            array.get(1).set(3, Ball.PURPLE);
            moves = ai2.makeMove(new GridBoard(array));
            assertEquals(moves[0], new Move(3, Move.Direction.DOWN));
        }

        @Test
        void makeIntermediateMoveDouble() {
            checkMove(ai2);
        }

        @Test
        void makeBeginnerMoveEmpty() {
            ArrayList<ArrayList<Ball>> array = copyArray(emptyBoardArray);

            Move[] moves = ai2.makeMove(new GridBoard(array));
            assertNull(moves);
        }
    }
}
