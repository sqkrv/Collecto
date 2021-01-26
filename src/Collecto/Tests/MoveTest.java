package Collecto.Tests;

import Collecto.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    private Move moveLine;
    private Move movePush;

    @BeforeEach
    void setUp() {
        moveLine = new Move(4, Move.Direction.LEFT);
        movePush = new Move(27);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6})
    void constructionLine(int line) {
        Move lineMove = new Move(line, Move.Direction.LEFT);
        assertEquals(line, lineMove.getLine());

        lineMove = new Move(line, Move.Direction.RIGHT);
        assertEquals(line, lineMove.getLine());

        lineMove = new Move(line, Move.Direction.UP);
        assertEquals(line, lineMove.getLine());

        lineMove = new Move(line, Move.Direction.DOWN);
        assertEquals(line, lineMove.getLine());
    }

    @Test
    void constructionLineIncorrect() {
        assertThrows(AssertionError.class, () -> new Move(-1, Move.Direction.UP));
        assertThrows(AssertionError.class, () -> new Move(7, Move.Direction.UP));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
            14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27})
    void constructionPush(int push) {
        Move lineMove = new Move(push);
        assertEquals(push, lineMove.push());

        lineMove = new Move(push);
        assertEquals(push, lineMove.push());

        lineMove = new Move(push);
        assertEquals(push, lineMove.push());

        lineMove = new Move(push);
        assertEquals(push, lineMove.push());
    }

    @Test
    void constructionPushIncorrect() {
        assertThrows(AssertionError.class, () -> new Move(-1));
        assertThrows(AssertionError.class, () -> new Move(28));
    }

    @Test
    void getDirection() {
        assertEquals(Move.Direction.LEFT, moveLine.getDirection());
        assertEquals(Move.Direction.DOWN, movePush.getDirection());
    }

    @Test
    void getLine() {
        assertEquals(4, moveLine.getLine());
        assertEquals(6, movePush.getLine());
    }

    @Test
    void push() {
        assertEquals(4, moveLine.push());
        assertEquals(27, movePush.push());
    }

    @Test
    void testToString() {
        assertEquals("5 LEFT", moveLine.toString());
        assertEquals("7 DOWN", movePush.toString());
    }

    @Test
    void testEquals() {
        assertEquals(moveLine, moveLine);
        assertNotEquals(moveLine, null);
        assertNotEquals(Move.Direction.DOWN, moveLine);
        assertEquals(moveLine, new Move(4));
    }
}