package Collecto.Tests;

import Collecto.Ball;
import Collecto.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
    }

    @Test
    void makeMove() {
        //TODO: implement this method
    }

    @Test
    void addBalls() {
        ArrayList<Ball> balls = new ArrayList<>();
//        balls.addAll(Ball.BLUE);
//        assertNotEquals(player, new Player());
        //TODO: check if this can be tested somehow
    }

    @Test
    void calculatePoints() {
        assertEquals(player.calculatePoints(), 0);
        for (int i = 0; i <= 10; i++) {
            player.addBalls(new ArrayList<>(Collections.singleton(Ball.BLUE)));
            player.addBalls(new ArrayList<>(Collections.singleton(Ball.BLUE)));
            player.addBalls(new ArrayList<>(Collections.singleton(Ball.YELLOW)));
        }
        // 11*2*blue balls = 22/3 points = 7 points;
        // 10*yellow balls = 10/3 points = 3 points;
        assertEquals(player.calculatePoints(), 10);
    }
}