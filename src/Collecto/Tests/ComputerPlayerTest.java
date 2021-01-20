package Collecto.Tests;

import Collecto.ComputerPlayer;
import Collecto.GridBoard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Collecto.Ball;
import java.util.*;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class ComputerPlayerTest {
    ComputerPlayer player;
    ArrayList<ArrayList<Ball>> emptyBoardArray = new ArrayList<>();
    ArrayList<ArrayList<Ball>> testBoardArray = new ArrayList<>();

    private ArrayList<ArrayList<Ball>> copyArray(ArrayList<ArrayList<Ball>> array) {
        ArrayList<ArrayList<Ball>> copy_array = new ArrayList<>();

        for (ArrayList<Ball> row : array) {
            copy_array.add((ArrayList<Ball>) row.clone());
        }

        return copy_array;
    }

    @BeforeAll
    void setEmptyBoardArray() {
        for (int i = 0; i < 7; i++) {
            emptyBoardArray.add(new ArrayList<>());
            for (int j = 0; j < 7; j++) {
                emptyBoardArray.get(i).add(Ball.WHITE);
            }
        }
    }

    @BeforeAll
    void setTestBoardArray() {
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

   @Test
    void makeBeginnerMove() {
        // assertEquals(player.makeMove(new GridBoard(testBoardArray))[0], 0);
   }
}