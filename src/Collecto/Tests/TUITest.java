package Collecto.Tests;

import Collecto.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TUITest {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream origOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(origOut);
    }

    @Test
    void currentDateTime() {
        assertTrue(TUI.currentDateTime().contains(":"));
        assertTrue(TUI.currentDateTime().contains("."));
    }

    @Test
    void logTime() {
        assertTrue(TUI.logTime().contains(Colour.GREY));
    }

    @Test
    void print() {
        TUI.print("test string");
        assertEquals(out.toString(), "test string\n");
    }

    @Test
    void printError() {
        String string = "test error string";
        TUI.printError(string);
        assertTrue(out.toString().contains("ERROR"));
        assertTrue(out.toString().contains(string));
    }

    @Test
    void logError() {
        String string = "test error string";
        assertTrue(TUI.logError(string).contains("ERROR"));
        assertTrue(TUI.logError(string).contains(string));
    }

    @Test
    void log() {
        String string = "test string";
        String log = TUI.log(string);
        assertTrue(log.contains(":"));
        assertTrue(log.contains("."));
        assertTrue(log.contains(string));
    }

    private String checkBasicNumBoard() {
        GridBoard board = new GridBoard(Misc.sampleBoardArray());
        String textBoard = TUI.textBoard(board);
        assertTrue(textBoard.contains("1") &&
                textBoard.contains("2") &&
                textBoard.contains("3") &&
                textBoard.contains("4") &&
                textBoard.contains("5") &&
                textBoard.contains("6") &&
                textBoard.contains("7"));
        assertTrue(textBoard.contains("-------+") && textBoard.contains("|"));
        assertTrue(textBoard.contains("BLUE"));

        return textBoard;
    }

    @Test
    void textBoard() {
        checkBasicNumBoard();
    }

    @Test
    void colouredBoard() {
        String textBoard = checkBasicNumBoard();
        assertTrue(textBoard.contains(Colour.BLUE));
        assertTrue(textBoard.contains(Colour.RED));
        assertTrue(textBoard.contains(Colour.RESET));
    }

    @Test
    void boardString() {
        GridBoard board = new GridBoard(Misc.sampleBoardArray());
        String textBoard = TUI.textBoard(board);
        assertTrue(textBoard.contains("-------+") && textBoard.contains("|"));
        assertTrue(textBoard.contains("BLUE"));
    }

    @Test
    void printHelpClient() {
        TUI.printHelpClient();
        assertTrue(out.toString().contains(Global.Protocol.Commands.LIST));
        assertTrue(out.toString().contains(Global.Protocol.Commands.HELP));
        assertTrue(out.toString().contains(Global.Protocol.Commands.MOVE));
        assertTrue(out.toString().contains(Global.Protocol.Commands.HINT));
        assertTrue(out.toString().contains(Global.Protocol.Commands.BOARD));
        assertTrue(out.toString().contains(Global.Protocol.Commands.QUEUE));
        assertTrue(out.toString().contains(Global.Protocol.Commands.LOGS));
        assertTrue(out.toString().contains(Global.Protocol.Commands.DISCONNECT));
    }

    @Test
    void ballColours() {
        ArrayList<Ball> balls = new ArrayList<>();

        assertEquals("", TUI.ballColours(balls));

        balls.add(Ball.BLUE);
        balls.add(Ball.RED);
        balls.add(Ball.RED);

        String temp = TUI.ballColours(balls);
        assertEquals(3, temp.length() - temp.replaceAll("\u25CF", "").length());
    }
}