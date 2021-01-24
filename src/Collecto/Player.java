package Collecto;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private final HashMap<Ball, Integer> balls = new HashMap<>();

    private final String name;

    /**
     * Constructs a Player instance with the specified name
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;

        for (Ball ball : Ball.values()) {
            if (ball != Ball.WHITE) balls.put(ball, 0);
        }
    }

    /**
     * Adds balls in specified array to all player's balls
     * @ensures no WHITE balls will be added
     * @param balls an array of balls
     */
    public void addBalls(ArrayList<Ball> balls) {
        for (Ball ball : balls) {
            if (ball != Ball.WHITE) this.balls.put(ball, this.balls.get(ball) + 1);
        }
    }

    /**
     * Returns calculated total point for this player
     * @return total points for this player
     */
    public int getPoints() {
        int points = 0;
        for (int value : balls.values()) {
            points += value / 3;
        }
        return points;
    }

    /**
     * Returns HashMap of player's balls with colours and corresponding amount of balls
     * @return player's balls
     */
    public HashMap<Ball, Integer> getBalls() {
        return balls;
    }

    /**
     * Returns the name of this player
     * @return name of this player
     */
    public String getName() {
        return name;
    }
}
