package Collecto.Tests;

import Collecto.Ball;
import Collecto.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;

    @BeforeEach
    void setUp() {
        player = new Player("test");
    }

    @Test
    void addBalls() {
        ArrayList<Ball> balls = new ArrayList<>();
        HashMap<Ball, Integer> resultBalls = new HashMap<>();

        balls.add(Ball.BLUE);
        for (Ball ball : Ball.values()) {
            if (ball != Ball.WHITE) {
                resultBalls.put(ball, 0);
            }
        }
        resultBalls.put(Ball.BLUE, 1);

        assertNotEquals(player.getBalls(), resultBalls);

        player.addBalls(balls);
        assertEquals(player.getBalls(), resultBalls);

        balls.addAll(Arrays.asList(Ball.BLUE, Ball.BLUE, Ball.YELLOW));

        player.addBalls(balls);
        resultBalls.put(Ball.BLUE, 4);
        resultBalls.put(Ball.YELLOW, 1);

        assertEquals(player.getBalls(), resultBalls);
    }

    @Test
    void addWhiteBall() {
        ArrayList<Ball> balls = new ArrayList<>();
        HashMap<Ball, Integer> resultBalls = new HashMap<>();
        for (Ball ball : Ball.values()) if (ball != Ball.WHITE) resultBalls.put(ball, 0);

        balls.add(Ball.WHITE);
        player.addBalls(balls);
        assertEquals(player.getBalls(), resultBalls);
    }

    @Test
    void calculatePoints() {
        assertEquals(player.getPoints(), 0);
        for (int i = 0; i <= 10; i++) {
            player.addBalls(new ArrayList<>(Collections.singleton(Ball.BLUE)));
            player.addBalls(new ArrayList<>(Collections.singleton(Ball.BLUE)));
            player.addBalls(new ArrayList<>(Collections.singleton(Ball.YELLOW)));
        }
        // 11*2*blue balls = 22/3 points = 7 points;
        // 10*yellow balls = 10/3 points = 3 points;
        assertEquals(player.getPoints(), 10);
    }

    @Test
    void getName() {
        assertEquals(player.getName(), "test");
        String name = "second test player";
        Player player1 = new Player(name);
        assertEquals(player1.getName(), name);
    }

    @Test
    void getBallAmount() {
        assertEquals(player.getBallAmount(), 0);

        ArrayList<Ball> balls = new ArrayList<>();
        balls.add(Ball.PURPLE);

        player.addBalls(balls);
        assertEquals(player.getBallAmount(), 1);

        balls.addAll(Arrays.asList(Ball.BLUE, Ball.BLUE, Ball.RED));
        player.addBalls(balls);

        assertEquals(player.getBallAmount(), 5);

        ArrayList<Ball> balls2 = new ArrayList<>();
        balls2.add(Ball.WHITE);
        player.addBalls(balls2);

        assertEquals(player.getBallAmount(), 5);
    }
}