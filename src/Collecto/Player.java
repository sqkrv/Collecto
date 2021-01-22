package Collecto;

import java.util.ArrayList;
import java.util.HashMap;

import static Collecto.Misc.Move;

public class Player {
//    private final ArrayList<Ball> balls;
    private final HashMap<Ball, Integer> count = new HashMap<>();

    private String name;

    /**
     * Player class constructor
     */
    public Player(String name) {
        this.name = name;

//        balls = new ArrayList<>();
        for (Ball ball : Ball.values()) {
            if (ball != Ball.WHITE) count.put(ball, 0);
        }
    }

    public Move[] makeMove() {
        //TODO: add this method
        return null;
    }

    /**
     * @requires ball != Ball.WHITE
     * @param balls a Ball to add to the player balls
     */
    public void addBalls(ArrayList<Ball> balls) {
//        this.balls.addAll(balls);
        for (Ball ball : balls) {
            if (ball != Ball.WHITE) count.put(ball, count.get(ball) + 1);
        }
    }

    /**
     * @return total points for this player
     */
    public int getPoints() {
        int points = 0;
        for (int value : count.values()) {
            points += value / 3;
        }
        return points;
    }

    public HashMap<Ball, Integer> showBalls() {
        return count;
    }

    public String getName() {
        return name;
    }
}
