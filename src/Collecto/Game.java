package Collecto;

import java.util.*;

import static Collecto.Misc.Move;

public class Game {
    private final GridBoard board;
    public Player player1;
    public Player player2;
    private boolean turnPlayer1 = true;

    public Game(String player1Name, String player2Name) {
        this(new GridBoard(), player1Name, player2Name);
    }

    public Game(GridBoard board, String player1Name, String player2Name) {
        player1 = new Player(player1Name);
        player2 = new Player(player2Name);
        this.board = board;
    }

    /**
     * Constructor that uses a custom board for the game
     * @param customBoardArray custom board to game on
     */
    public Game(ArrayList<ArrayList<Ball>> customBoardArray) {
        this.board = new GridBoard(customBoardArray);
    }

    public boolean makeMove(Move move) {
        if (board.isMoveValid(move)) {
            board.moveLine(move);
            if (turnPlayer1) {
                player1.addBalls(board.removeBalls(move));
            } else {
                player2.addBalls(board.removeBalls(move));
            }
            turnPlayer1 = !turnPlayer1;
            return true;
        } else {
            return false;
        }
    }

    public boolean makeMove(Move first, Move second) {
        if (board.isMoveValid(first, second)) {
            board.moveLine(first);
            return makeMove(second);
        } else {
            return false;
        }
    }

    public void printBoard() {
        TUI.print(TUI.colouredBoard(board));
    }

    public String getBoardString() {
        return board.getBoardString();
    }

    public boolean possibleMoves() {
        return board.possibleMoves();
    }

    public String getWinner() {
        if (player1.getPoints() > player2.getPoints()) {
            return player1.getName();
        } else if (player2.getPoints() > player1.getPoints()) {
            return player2.getName();
        } else if (player1.showBalls().size() > player2.showBalls().size()) {
            return player1.getName();
        } else if (player2.showBalls().size() > player1.showBalls().size()) {
            return player2.getName();
        } else {
            return null;
        }
    }

    public GridBoard getBoard() {
        return board;
    }

    public boolean isMoveValid(Move firstMove, Move secondMove) {
        if (secondMove == null) {
            return board.isMoveValid(firstMove);
        } else {
            return board.isMoveValid(firstMove, secondMove);
        }
    }


    //    private final Player[] players = new Player[2];
//    private final Scanner input = new Scanner(System.in);
//    public boolean myTurn = false;
//
//    public Game(Player p1, Player p2) {
//        board = new GridBoard();
//        players[0] = p1;
//        players[1] = p2;
//
//    /**
//     * sets up the start of a new game
//     */
//    public void start() {
//        boolean nextGame = true;
//        while (nextGame) {
//            play();
//            System.out.println("Play again?");
//            nextGame = input.nextLine().equals("y");
//        }
//    }
//
//    /**
//     * resets the game and some attributes
//     */
//    public void reset() {
//
//    }
//
//        game.setup();
//        Game game = new Game();
//    public static void main(String[] args) {
//    /**
//     * any actual games play out within this method
//     */
//    public void play() {
//        String answer;
//        String[] answers;
//        int line;
//        int column;
//        boolean inTwoMoves;
//        GridBoard.Direction direction;
//        Player player = players[(new Random()).nextInt(2)];

//        while (inTwoMoves = board.possibleMoves()) {
//            System.out.println(TUI.colouredBoard(board));
//            System.out.println(player.getName() + ", make a move");
//            System.out.println("Format = [row|column direction]");
//            answer = input.nextLine();
//            answer = answer.trim().replaceAll(" +", " ");
//            answers = answer.split(" ");
//            if (answers.length == 1) {
//                answers = new String[2];
//                answers[0] = answer.substring(0, 1);
//                answers[1] = answer.substring(1);
//            }
//            try {
//                line = Integer.parseInt(answers[0]) - 1;
//                answers[1] = answers[1].toUpperCase();
//
//                try {
//                    direction = GridBoard.Direction.valueOf(answers[1]);
//                } catch (IllegalArgumentException i) {
//                    switch (answers[1]) {
//                        case "U":
//                            direction = GridBoard.Direction.UP;
//                            break;
//                        case "D":
//                            direction = GridBoard.Direction.DOWN;
//                            break;
//                        case "L":
//                            direction = GridBoard.Direction.LEFT;
//                            break;
//                        case "R":
//                            direction = GridBoard.Direction.RIGHT;
//                            break;
//                        default:
//                            System.out.println("Incorrect input");
//                            continue;
//                    }
//                }
//            } catch (IllegalArgumentException a) {
//                System.out.println("Incorrect input");
//                continue;
//            }
//            if (board.isMoveValid(new Move(line, direction))) {
//                board.moveLine(new Move(line, direction));
//                ArrayList<Ball> balls = board.removeBalls(new Move(line, direction));
//                player.addBalls(balls);
//                if (player.equals(players[0])) {
//                    player = players[1];
//                } else {
//                    player = players[0];
//                }
//            } else {
//                System.out.println("Invalid move");
//            }
//            System.out.println(TUI.playersBoard(players[0], players[1]));
//            System.out.println();
//        }
//    }
//
//    /**
//     * sets up all the pre-game data, such as player names, bot difficulty, .....
//     */
//    public void setup() {
//        String answer;
//        System.out.println("Enter name of player 1");
//        answer = input.nextLine();
//        players[0] = new HumanPlayer(answer.trim());
//        while (true) {
//            System.out.println("Enter name of player 2");
//            answer = input.nextLine();
//            answer = answer.trim();
//            if (!answer.equals(players[0].getName())) {
//                players[1] = new HumanPlayer(answer);
//                break;
//            }
//            System.out.println("Name is already taken by the first player");
//        }
//    }
//
//        game.start();
//        game.play();
//    }

}
