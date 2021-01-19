package Collecto;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private final ArrayList<Ball> balls;

    /**
     * Player class constructor
     */
    public Player() {
        balls = new ArrayList<>();
    }

    public void makeMove() {
        //TODO: add this method
    }

    /**
     * @requires ball != Ball.WHITE
     * @param ball a Ball to add to the player balls
     */
    public void addBalls(ArrayList<Ball> balls) {
        assert !balls.contains(Ball.WHITE);
        this.balls.addAll(balls);
    }

    /**
     * @return total points for this player
     */
    public int calculatePoints() {
        HashMap<Ball, Integer> count = new HashMap<>();
        for (Ball ball : balls) {
            if (!count.containsKey(ball)) count.put(ball, 0);
            count.replace(ball, count.get(ball) + 1);
        }

        int points = 0;
        for (int value : count.values()) {
            points += value / 3;
        }
        return points;
    }
}
