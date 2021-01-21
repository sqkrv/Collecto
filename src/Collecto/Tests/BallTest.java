package Collecto.Tests;

import Collecto.Ball;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BallTest {
    
    static Ball blueBall;
    static Ball yellowBall;
    static Ball redBall;
    static Ball orangeBall;
    static Ball purpleBall;
    static Ball greenBall;
    static Ball whiteBall;

    public static final String ANSI_WHITE = "\u001B[0m";

    @BeforeAll
    static void setUp() {
        blueBall = Ball.BLUE;
        yellowBall = Ball.YELLOW;
        redBall = Ball.RED;
        orangeBall = Ball.ORANGE;
        purpleBall = Ball.PURPLE;
        greenBall = Ball.GREEN;
        whiteBall = Ball.WHITE;
    }

    @Test
    void testGetColour() {
        assertEquals(blueBall.getColour(), "BLUE");
        assertEquals(yellowBall.getColour(), "YELLOW");
        assertEquals(redBall.getColour(), "RED");
        assertEquals(orangeBall.getColour(), "ORANGE");
        assertEquals(purpleBall.getColour(), "PURPLE");
        assertEquals(whiteBall.getColour(), "WHITE");
    }

    @Test
    void testToString() {
        assertEquals(blueBall.toString(), "BLUE");
        assertNotEquals(blueBall.toString(), "\u001B[34m" + "BLUE" + ANSI_WHITE);
        assertEquals(yellowBall.toString(), "YELLOW");
        assertNotEquals(yellowBall.toString(), "\u001B[93m" + "YELLOW" + ANSI_WHITE);
        assertEquals(redBall.toString(), "RED");
        assertNotEquals(redBall.toString(), "\u001B[31m" + "RED" + ANSI_WHITE);
        assertEquals(orangeBall.toString(), "ORANGE");
        assertNotEquals(orangeBall.toString(), "\u001B[33m" + "ORANGE" + ANSI_WHITE);
        assertEquals(purpleBall.toString(), "PURPLE");
        assertNotEquals(purpleBall.toString(), "\u001B[35m" + "PURPLE" + ANSI_WHITE);
        assertEquals(whiteBall.toString(), "");
        assertNotEquals(whiteBall.toString(), "\u001B[37m" + "" + ANSI_WHITE);
    }

    @Test
    void testToColouredString() {
        assertEquals(blueBall.toColouredString(), "\u001B[34m" + "BLUE" + ANSI_WHITE);
        assertEquals(yellowBall.toColouredString(), "\u001B[93m" + "YELLOW" + ANSI_WHITE);
        assertEquals(redBall.toColouredString(), "\u001B[31m" + "RED" + ANSI_WHITE);
        assertEquals(orangeBall.toColouredString(), "\u001B[33m" + "ORANGE" + ANSI_WHITE);
        assertEquals(purpleBall.toColouredString(), "\u001B[35m" + "PURPLE" + ANSI_WHITE);
        assertEquals(whiteBall.toColouredString(), "\u001B[37m" + "" + ANSI_WHITE);
    }

    @Test
    void testValues() {
        Ball[] testArray = new Ball[]{Ball.WHITE, Ball.BLUE, Ball.YELLOW, Ball.RED, Ball.ORANGE,
                Ball.PURPLE, Ball.GREEN};
        assertArrayEquals(testArray, Ball.values());
    }
}