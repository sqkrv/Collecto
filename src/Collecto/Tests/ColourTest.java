package Collecto.Tests;

import Collecto.Colour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ColourTest {
    private final String testString = "test string";

    @Test
    void grey() {
        String result = Colour.grey(testString);
        assertTrue(result.contains(Colour.GREY));
        assertTrue(result.contains(testString));
        assertTrue(result.contains(Colour.RESET));
        assertEquals(Colour.GREY + testString + Colour.RESET, result);
    }

    @Test
    void red() {
        String result = Colour.red(testString);
        assertTrue(result.contains(Colour.RED));
        assertTrue(result.contains(testString));
        assertTrue(result.contains(Colour.RESET));
        assertEquals(Colour.RED + testString + Colour.RESET, result);
    }

    @Test
    void green() {
        String result = Colour.green(testString);
        assertTrue(result.contains(Colour.GREEN));
        assertTrue(result.contains(testString));
        assertTrue(result.contains(Colour.RESET));
        assertEquals(Colour.GREEN + testString + Colour.RESET, result);
    }

    @Test
    void yellow() {
        String result = Colour.yellow(testString);
        assertTrue(result.contains(Colour.YELLOW));
        assertTrue(result.contains(testString));
        assertTrue(result.contains(Colour.RESET));
        assertEquals(Colour.YELLOW + testString + Colour.RESET, result);
    }

    @Test
    void blue() {
        String result = Colour.blue(testString);
        assertTrue(result.contains(Colour.BLUE));
        assertTrue(result.contains(testString));
        assertTrue(result.contains(Colour.RESET));
        assertEquals(Colour.BLUE + testString + Colour.RESET, result);
    }

    @Test
    void purple() {
        String result = Colour.purple(testString);
        assertTrue(result.contains(Colour.PURPLE));
        assertTrue(result.contains(testString));
        assertTrue(result.contains(Colour.RESET));
        assertEquals(Colour.PURPLE + testString + Colour.RESET, result);
    }

    @Test
    void cyan() {
        String result = Colour.cyan(testString);
        assertTrue(result.contains(Colour.CYAN));
        assertTrue(result.contains(testString));
        assertTrue(result.contains(Colour.RESET));
        assertEquals(Colour.CYAN + testString + Colour.RESET, result);
    }

    @Test
    void orange() {
        String result = Colour.orange(testString);
        assertTrue(result.contains(Colour.ORANGE));
        assertTrue(result.contains(testString));
        assertTrue(result.contains(Colour.RESET));
        assertEquals(Colour.ORANGE + testString + Colour.RESET, result);
    }
}