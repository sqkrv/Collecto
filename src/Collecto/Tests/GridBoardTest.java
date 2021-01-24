package Collecto.Tests;

import Collecto.Ball;
import Collecto.GridBoard;
import Collecto.TUI;
import Collecto.Move;
import org.junit.jupiter.api.*;

import java.util.*;

import static Collecto.Tests.Misc.copyArray;

import static org.junit.jupiter.api.Assertions.*;

class GridBoardTest {
    private static GridBoard board;
    private static GridBoard sampleBoard;
    private static GridBoard emptyBoard;
    private static ArrayList<ArrayList<Ball>> sampleBoardArray;
    private static final ArrayList<ArrayList<Ball>> emptyBoardArray = new ArrayList<>();

    @BeforeAll
    static void setSampleBoardArray() {
        sampleBoardArray = Misc.sampleBoardArray();
    }

    @BeforeAll
    static void setEmptyBoard() {
        for (int i = 0; i < 7; i++) {
            emptyBoardArray.add(new ArrayList<>());
            for (int j = 0; j < 7; j++) {
                emptyBoardArray.get(i).add(Ball.WHITE);
            }
        }
//        emptyBoardArray.addAll(0, Collections.nCopies(7, Collections.nCopies(7, Ball.WHITE)));
        emptyBoard = new GridBoard(emptyBoardArray);
    }

    @BeforeEach
    void setUp() {
        board = new GridBoard();
        sampleBoard = new GridBoard(copyArray(sampleBoardArray));
    }

    @RepeatedTest(1000)
    void boardConstruction() {
        assertEquals(board.getField(3, 3), Ball.WHITE);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                assertFalse(board.checkSurroundings(i, j));
            }
        }
        assertTrue(board.possibleMoves());
    }

    @Test
    void deepCopy() {
        GridBoard copy = board.deepCopy();
        assertEquals(board.toString(), copy.toString());
        copy.moveLine(new Move(3, GridBoard.Direction.UP));
        assertNotEquals(board.toString(), copy.toString());
    }

//    @Nested
//    @DisplayName("Tests for method moveLine")
//    class moveLine {

        @Nested
        @DisplayName("Tests for rows and columns with blank spaces")
        class moveLineWithBlank {

            @Test
            @DisplayName("moveLine UP")
            void moveLineWBUP() {
                ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
                sampleBoard.moveLine(new Move(3, GridBoard.Direction.UP));
                assertNotEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
                copy.get(3).set(3, Ball.GREEN);
                copy.get(4).set(3, Ball.YELLOW);
                copy.get(5).set(3, Ball.RED);
                copy.get(6).set(3, Ball.WHITE);
                assertEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
            }

            @Test
            @DisplayName("moveLine DOWN")
            void moveLineWBDOWN() {
                ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
                sampleBoard.moveLine(new Move(3, GridBoard.Direction.DOWN));
                assertNotEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
                copy.get(0).set(3, Ball.WHITE);
                copy.get(1).set(3, Ball.GREEN);
                copy.get(2).set(3, Ball.ORANGE);
                copy.get(3).set(3, Ball.BLUE);
                assertEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
            }

            @Test
            @DisplayName("moveLine LEFT")
            void moveLineWBLEFT() {
                ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
                sampleBoard.moveLine(new Move(3, GridBoard.Direction.LEFT));
                assertNotEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
                copy.get(3).set(3, Ball.BLUE);
                copy.get(3).set(4, Ball.RED);
                copy.get(3).set(5, Ball.GREEN);
                copy.get(3).set(6, Ball.WHITE);
                assertEquals(sampleBoard.toString(), new GridBoard(copy).toString());
            }

            @Test
            @DisplayName("moveLine RIGHT")
            void moveLineWBRIGHT() {
                ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
                sampleBoard.moveLine(new Move(3, GridBoard.Direction.RIGHT));
                assertNotEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
                copy.get(3).set(0, Ball.WHITE);
                copy.get(3).set(1, Ball.PURPLE);
                copy.get(3).set(2, Ball.YELLOW);
                copy.get(3).set(3, Ball.GREEN);
                assertEquals(sampleBoard.toString(), new GridBoard(copy).toString());
            }
        }

        @Nested
        @DisplayName("Tests for rows and columns without spaces")
        class moveLineWithoutBlank {
            @Test
            @DisplayName("moveLine (without WHITE) UP")
            void moveLineWOBUP() {
                ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
                sampleBoard.moveLine(new Move(6, GridBoard.Direction.UP));
                assertEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
                copy.get(6).set(6, Ball.WHITE);
                assertNotEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
            }

            @Test
            @DisplayName("moveLine (without WHITE) DOWN")
            void moveLineWOBDOWN() {
                ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
                sampleBoard.moveLine(new Move(6, GridBoard.Direction.DOWN));
                assertEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
                copy.get(6).set(6, Ball.WHITE);
                assertNotEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
            }

            @Test
            @DisplayName("moveLine (without WHITE) LEFT")
            void moveLineWOBLEFT() {
                ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
                sampleBoard.moveLine(new Move(6, GridBoard.Direction.LEFT));
                assertEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
                copy.get(6).set(6, Ball.WHITE);
                assertNotEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
            }

            @Test
            @DisplayName("moveLine (without WHITE) RIGHT")
            void moveLineWOBRIGHT() {
                ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
                sampleBoard.moveLine(new Move(6, GridBoard.Direction.RIGHT));
                assertEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
                copy.get(6).set(6, Ball.WHITE);
                assertNotEquals(sampleBoard.toString(), (new GridBoard(copy)).toString());
            }
        }
//    }

    @Nested
    @DisplayName("isMoveValid tests")
    class isMoveValidTests {
        @Test
        @DisplayName("Moves at (3,3)")
        void isMoveValidMiddle() {
            assertTrue(sampleBoard.isMoveValid(new Move(3, GridBoard.Direction.UP)));
            assertTrue(sampleBoard.isMoveValid(new Move(3, GridBoard.Direction.DOWN)));
            assertTrue(sampleBoard.isMoveValid(new Move(3, GridBoard.Direction.RIGHT)));
            assertTrue(sampleBoard.isMoveValid(new Move(3, GridBoard.Direction.LEFT)));
        }

        @Test
        @DisplayName("Moves at (0,0)")
        void isMoveValid() {
            assertFalse(sampleBoard.isMoveValid(new Move(0, GridBoard.Direction.UP)));
            assertFalse(sampleBoard.isMoveValid(new Move(0, GridBoard.Direction.DOWN)));
            assertFalse(sampleBoard.isMoveValid(new Move(0, GridBoard.Direction.RIGHT)));
            assertFalse(sampleBoard.isMoveValid(new Move(0, GridBoard.Direction.LEFT)));
        }

        @Test
        @DisplayName("Moves at (0,0)")
        void isMoveValidMiddleInvalid() {
            ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
            assertTrue((new GridBoard(copy)).isMoveValid(new Move(3, GridBoard.Direction.UP)));
            copy.get(4).set(3, Ball.RED);
            assertFalse((new GridBoard(copy)).isMoveValid(new Move(3, GridBoard.Direction.UP)));
        }
    }

//    @Test
//    void getField() {
//    }

//    @Test
//    void setField() {
//    }

//    @Test
//    void validIndex() {
//    }
//
//    @Test
//    void getCoordinates() {
//
//    }

    @Nested
    class possibleMove {
        @Test
        void possibleMovesSample() {
            assertTrue(sampleBoard.possibleMoves());
            ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
            copy.get(3).set(3, Ball.RED);
            assertFalse((new GridBoard(copy)).possibleMoves());
        }

        @Test
        void possibleMovesEmpty() {
            ArrayList<ArrayList<Ball>> copy = copyArray(emptyBoardArray);
            assertFalse(emptyBoard.possibleMoves());
            copy.get(0).set(0, Ball.GREEN);
            copy.get(0).set(6, Ball.GREEN);
            assertTrue((new GridBoard(copy)).possibleMoves());
        }

        @Test
        void testTwoMoves() {
            ArrayList<ArrayList<Ball>> copy = copyArray(emptyBoardArray);
            copy.get(0).set(0, Ball.GREEN);
            copy.get(6).set(6, Ball.GREEN);
            assertTrue((new GridBoard(copy)).possibleMoves());
        }

        @Test
        void testTwoMovesWrong() {
            ArrayList<ArrayList<Ball>> copy = copyArray(emptyBoardArray);
            // encase the first green ball in the top left
            copy.get(0).set(0, Ball.GREEN);
            copy.get(0).set(1, Ball.YELLOW);
            copy.get(1).set(0, Ball.BLUE);
            // encase the second green ball in the bottom right
            copy.get(6).set(6, Ball.GREEN);
            copy.get(6).set(5, Ball.RED);
            copy.get(5).set(6, Ball.PURPLE);
            assertFalse((new GridBoard(copy)).possibleMoves());
        }
    }

    @Nested
    @DisplayName("checkSurroundings tests")
    class checkSurroundings {
        @Test
        @DisplayName("check false surroundings")
        void checkSurroundingsFalse() {
            assertFalse(sampleBoard.checkSurroundings(3,3));
            assertFalse(sampleBoard.checkSurroundings(0,0));
            assertFalse(sampleBoard.checkSurroundings(5,1));
        }

        @Test
        @DisplayName("check correct surroundings")
        void checkSurroundingsTrue() {
            ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
            copy.get(3).set(3, Ball.GREEN);
            assertTrue((new GridBoard(copy)).checkSurroundings(3,3));
            copy.get(0).set(1, Ball.GREEN);
            assertTrue((new GridBoard(copy)).checkSurroundings(0,0));
            copy.get(5).set(1, Ball.GREEN);
            assertTrue((new GridBoard(copy)).checkSurroundings(5,1));
        }
    }

    @Nested
    @DisplayName("removeBalls tests")
    class removeBalls {
        @Nested
        class editedSampleBoard {
            @Test
            void removeBallsLeft() {
                ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
                copy.get(4).set(4, Ball.RED);
                sampleBoard = new GridBoard(copy);
                sampleBoard.moveLine(new Move(3, GridBoard.Direction.LEFT));
                assertEquals(Arrays.asList(Ball.BLUE, Ball.BLUE, Ball.RED, Ball.RED, Ball.RED), sampleBoard.removeBalls(new Move(3, GridBoard.Direction.LEFT)));
            }

            @Test
            void removeBallsRight() {
                ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
                copy.get(2).set(1, Ball.PURPLE);
                sampleBoard = new GridBoard(copy);
                sampleBoard.moveLine(new Move(3, GridBoard.Direction.RIGHT));
                assertEquals(Arrays.asList(Ball.PURPLE, Ball.PURPLE, Ball.PURPLE, Ball.GREEN, Ball.GREEN), sampleBoard.removeBalls(new Move(3, GridBoard.Direction.RIGHT)));
            }

            @Test
            void removeBallsUp() {
                ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
                copy.get(2).set(1, Ball.PURPLE);
                sampleBoard = new GridBoard(copy);
                sampleBoard.moveLine(new Move(3, GridBoard.Direction.UP));
                assertEquals(Arrays.asList(Ball.GREEN, Ball.GREEN), sampleBoard.removeBalls(new Move(3, GridBoard.Direction.UP)));
            }
        }

        @Test
        void removeBallsTwoMoves() {
            ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
            copy.get(0).set(4, Ball.ORANGE);
            copy.get(3).set(4, Ball.YELLOW);
            sampleBoard = new GridBoard(copy);
            sampleBoard.moveLine(new Move(3, GridBoard.Direction.DOWN));
            assertEquals(Collections.emptyList(), sampleBoard.removeBalls(new Move(3, GridBoard.Direction.DOWN)));
            sampleBoard.moveLine(new Move(0, GridBoard.Direction.LEFT));
            assertEquals(Arrays.asList(Ball.ORANGE, Ball.ORANGE), sampleBoard.removeBalls(new Move(0, GridBoard.Direction.LEFT)));
        }

        @Test
        void removeBallsLeft() {
            sampleBoard.moveLine(new Move(3, GridBoard.Direction.LEFT));
            assertEquals(Arrays.asList(Ball.BLUE, Ball.BLUE, Ball.RED, Ball.RED), sampleBoard.removeBalls(new Move(3, GridBoard.Direction.LEFT)));
        }

        @Test
        void removeBallsRight() {
            sampleBoard.moveLine(new Move(3, GridBoard.Direction.RIGHT));
            assertEquals(Arrays.asList(Ball.GREEN, Ball.GREEN), sampleBoard.removeBalls(new Move(3, GridBoard.Direction.RIGHT)));
        }

        @Test
        void removeBallsUp() {
//            sampleBoard.moveLine(3, 3, GridBoard.Direction.UP);
//            assertEquals(Arrays.asList(Ball.GREEN, Ball.GREEN), sampleBoard.removeBalls(3, 3, GridBoard.Direction.UP));
            ArrayList<ArrayList<Ball>> copy = copyArray(sampleBoardArray);
            copy.get(3).set(3, Ball.YELLOW);
            copy.get(4).set(3, Ball.WHITE);
            sampleBoard = new GridBoard(copy);
            sampleBoard.moveLine(new Move(3, GridBoard.Direction.UP));
            assertEquals(Arrays.asList(Ball.YELLOW, Ball.YELLOW), sampleBoard.removeBalls(new Move(3, GridBoard.Direction.UP)));
            System.out.println(TUI.colouredBoard(sampleBoard));
        }

        @Test
        void removeBallsDown() {
            sampleBoard.moveLine(new Move(3, GridBoard.Direction.DOWN));
            assertEquals(Arrays.asList(Ball.BLUE, Ball.BLUE), sampleBoard.removeBalls(new Move(3, GridBoard.Direction.DOWN)));
        }

        @Test
        void removeThreeRowsLEFT() {
            ArrayList<ArrayList<Ball>> copy = copyArray(emptyBoardArray);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 7; j++) {
                    copy.get(i).set(j, Ball.YELLOW);
                }
            }
//            copy.get(1).set(6, Ball.WHITE);
            GridBoard newEmptyBoard = new GridBoard(copy);
            assertNotEquals(newEmptyBoard.toString(), emptyBoard.toString());
            newEmptyBoard.removeBalls(new Move(1, GridBoard.Direction.LEFT));
            assertEquals(newEmptyBoard.toString(), emptyBoard.toString());
        }

        @Test
        void removeThreeRowsUP() {
            ArrayList<ArrayList<Ball>> copy = copyArray(emptyBoardArray);
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 3; j++) {
                    copy.get(i).set(j, Ball.GREEN);
                }
            }
//            copy.get(3).set(1, Ball.WHITE);
            GridBoard newEmptyBoard = new GridBoard(copy);
            assertNotEquals(newEmptyBoard.toString(), emptyBoard.toString());
            newEmptyBoard.removeBalls(new Move(1, GridBoard.Direction.UP));
            assertEquals(newEmptyBoard.toString(), emptyBoard.toString());
        }

        @Test
        void removeThreeBalls() {
            ArrayList<ArrayList<Ball>> copy = copyArray(emptyBoardArray);
            copy.get(0).set(3, Ball.YELLOW);
            copy.get(2).set(3, Ball.YELLOW);
            copy.get(4).set(3, Ball.YELLOW);
            GridBoard threeBallBoard = new GridBoard(copy);
            threeBallBoard.moveLine(new Move(3, GridBoard.Direction.UP));
            assertEquals(threeBallBoard.removeBalls(new Move(3, GridBoard.Direction.UP)).size(), 3);
            assertEquals(threeBallBoard.toString(), emptyBoard.toString());
        }

        @Test
        void removeFourBalls() {
            ArrayList<ArrayList<Ball>> copy = copyArray(emptyBoardArray);
            copy.get(0).set(3, Ball.YELLOW);
            copy.get(2).set(3, Ball.YELLOW);
            copy.get(4).set(3, Ball.YELLOW);
            copy.get(6).set(3, Ball.YELLOW);
            GridBoard fourBallBoard = new GridBoard(copy);
            fourBallBoard.moveLine(new Move(3, GridBoard.Direction.UP));
            assertEquals(fourBallBoard.removeBalls(new Move(3, GridBoard.Direction.UP)).size(), 4);
            assertEquals(fourBallBoard.toString(), emptyBoard.toString());
        }

        @Test
        void removeThreeAdjacent() {
            ArrayList<ArrayList<Ball>> copy = copyArray(emptyBoardArray);
            copy.get(6).set(0, Ball.YELLOW);
            copy.get(6).set(1, Ball.YELLOW);
            copy.get(6).set(2, Ball.YELLOW);
            GridBoard threeAdjBoard  = new GridBoard(copy);
            assertEquals(threeAdjBoard.removeBalls(new Move(1, GridBoard.Direction.DOWN)).size(), 3);
        }
    }
}