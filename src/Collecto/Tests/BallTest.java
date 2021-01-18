package Collecto.Tests;

import Collecto.Ball;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BallTest {

    Ball blueBall;
    Ball yellowBall;
    Ball redBall;
    Ball orangeBall;
    Ball purpleBall;
    Ball greenBall;
    Ball whiteBall;
    public static final String ANSI_WHITE = "\u001B[0m";

    @BeforeEach
    void setUp() {
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
        assertNotEquals(blueBall.toString(), "BLUE");
        assertEquals(blueBall.toString(), "\u001B[34m" + "BLUE" + ANSI_WHITE);
        assertNotEquals(yellowBall.toString(), "YELLOW");
        assertEquals(yellowBall.toString(), "\u001B[93m" + "YELLOW" + ANSI_WHITE);
        assertNotEquals(redBall.toString(), "RED");
        assertEquals(redBall.toString(), "\u001B[31m" + "RED" + ANSI_WHITE);
        assertNotEquals(orangeBall.toString(), "ORANGE");
        assertEquals(orangeBall.toString(), "\u001B[33m" + "ORANGE" + ANSI_WHITE);
        assertNotEquals(purpleBall.toString(), "PURPLE");
        assertEquals(purpleBall.toString(), "\u001B[35m" + "PURPLE" + ANSI_WHITE);
        assertNotEquals(whiteBall.toString(), "WHITE");
        assertEquals(whiteBall.toString(), "\u001B[37m" + "" + ANSI_WHITE);
    }

    @Test
    void testValues() {
        Ball[] testArray = new Ball[]{Ball.BLUE, Ball.YELLOW, Ball.RED, Ball.ORANGE,
                Ball.PURPLE, Ball.GREEN, Ball.WHITE};
        assertArrayEquals(testArray, Ball.values());
    }
}