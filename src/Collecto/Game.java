package Collecto;

import java.util.*;

public class Game {
    public static final int NUMBER_PLAYERS = 2;

    private GridBoard board;
    private Player[] players = new Player[2];
    private Scanner input = new Scanner(System.in);

//    public Game(Player p1, Player p2) {
//        board = new GridBoard();
//        players[0] = p1;
//        players[1] = p2;
//    }

    public Game() {
        board = new GridBoard();
    }

    /**
     * Constructor that uses a custom board for the game
     * @param customBoardArray
     */
    public Game(ArrayList<ArrayList<Ball>> customBoardArray) {
        this.board = new GridBoard(customBoardArray);
    }

    /**
     * sets up the start of a new game
     */
    public void start() {
        boolean nextGame = true;
        while (nextGame) {
            play();
            System.out.println("Play again?");
            nextGame = input.nextLine().equals("y");
        }
    }

    /**
      * resets the game and some attributes
     */
    public void reset() {

    }

    /**
     * any actual games play out within this method
     */
    public void play() {
        while (true) {
            System.out.println("Playing playing playing");
            break;
        }
    }

    /**
     * sets up all the pre-game data, such as player names, bot difficulty, .....
     */
    public void setup() {

    }

    public static void main(String[] args) {
        Game game = new Game();
        game.setup();
        game.start();
        game.play();
    }
}
