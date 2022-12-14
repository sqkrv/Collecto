package Collecto;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a player of the game, it stores the name of the player and tracks
 * all the balls which have been earned while playing a game. It is used by the Game class
 * by storing two Players who are playing that game.
 *
 * @see Game
 * @see Ball
 */
public class Player {
    /**
     * HashMap which stores balls of all colours, except for WHITE,
     * and their amounts.
     */
    private final HashMap<Ball, Integer> balls = new HashMap<>();
    private final String name;

    /**
     * Constructs a Player instance with the specified name.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;

        for (Ball ball : Ball.values()) {
            if (ball != Ball.WHITE) {
                balls.put(ball, 0);
            }
        }
    }

    /**
     * Adds all ball colours except for WHITE in a specified array to balls of this Player.
     *
     * @param ballsArray an array of balls
     * @ensures no WHITE balls will be added
     */
    public void addBalls(ArrayList<Ball> ballsArray) {
        for (Ball ball : ballsArray) {
            if (ball != Ball.WHITE) {
                this.balls.put(ball, this.balls.get(ball) + 1);
            }
        }
    }

    /**
     * Returns calculated total point for this Player, where 3 points are awarded for every
     * 3 balls of the same colour that this Player has acquired.
     *
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
     * Returns a HashMap of the different colour balls this player has obtained
     * and the corresponding amount of that colour ball that this player has acquired.
     *
     * @return player's balls
     */
    public HashMap<Ball, Integer> getBalls() {
        return balls;
    }

    /**
     * Returns the total amount of balls that this player currently has.
     *
     * @return amount of balls
     */
    public int getBallAmount() {
        int total = 0;
        for (Integer amount : balls.values()) {
            total += amount;
        }
        return total;
    }

    /**
     * Returns the name of this player.
     *
     * @return name of this player
     */
    public String getName() {
        return name;
    }
}
