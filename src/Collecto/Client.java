package Collecto;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static Collecto.Colour.*;
import static Collecto.Global.Protocol.Commands.*;
import static Collecto.Global.Protocol.Misc.*;
import static Collecto.Global.DELIMITER;

public class Client implements Runnable {
    public final static String DESCRIPTION = "Client of Hein and Stan";

    private Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final ClientController controller;

    private Game game = null;
    private final ArrayList<String> logs = new ArrayList<>();
    private String playerName;

    protected boolean chatSupport = false;
    protected boolean rankSupport = false;
    protected boolean authSupport = false;
    protected boolean cryptSupport = false;

    protected boolean serverChatSupport = false;
    protected boolean serverRankSupport = false;
    protected boolean serverAuthSupport = false;
    protected boolean serverCryptSupport = false;

    private boolean saidHello = false;
    private boolean loggedIn = false;
    protected boolean useAI = false;
    private boolean myTurn = false;
    protected boolean choosingAI = false;

    private ComputerPlayer AI;

    /**
     * Initializes communication with server and input from the user
     * @requires socket != null
     * @param socket socket connected to a server
     * @throws IOException if the socket fails
     */
    public Client(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        controller = new ClientController(this);
    }

    /**
     * @requires serverInput != null
     * @ensures serverInput is handled correctly
     * @param serverInput String input from the server
     */
    public void handleCommandIn(String serverInput) {
        if (serverInput == null) {
            return;
        }
        String[] params = serverInput.split(DELIMITER);
        switch (params[0]) {
            case HELLO:
                handleHelloServer(params);
                break;
            case LOGIN:
                handleLoginServer();
                break;
            case ALREADY_LOGGED_IN:
                handleAlreadyLoggedIn();
                break;
            case LIST:
                handleListServer(params);
                break;
            case NEWGAME:
                handleNewGame(params);
                break;
            case MOVE:
                handleMoveServer(params);
                break;
            case GAMEOVER:
                handleGameOver(params);
                break;
            case ERROR:
                handleError(params);
                break;
            default:
                TUI.printError("Server sent unknown command: "+params[0]);
        }
    }

    /**
     * Handles errors sent by the server, wakes up other threads if not logged in
     * @param params server input
     */
    private void handleError(String[] params) {
        if (params.length <= 1) {
            TUI.printError("(no description of the error provided)");
        } else {
            if (!saidHello) {
                TUI.printError("No " + HELLO + " response received");
                synchronized (this) {
                    notify();
                }
            } else if (!loggedIn) {
                TUI.printError("No " + LOGIN + " response received");
                synchronized (this) {
                    notify();
                }
            } else {
                TUI.printError(params[1]);
            }
        }
    }

    /**
     * handles the hello handshake response from a server
     * @requires !saidHello
     * @param params server input
     */
    private void handleHelloServer(String[] params) {
        if (!saidHello) {
            for (int i = 2; i < params.length; i++) {
                switch (params[i]) {
                    case "CHAT":
                        this.serverChatSupport = true;
                        break;
                    case "RANK":
                        this.serverRankSupport = true;
                        break;
                    case "AUTH":
                        this.serverAuthSupport = true;
                        break;
                    case "CRYPT":
                        this.serverCryptSupport = true;
                        break;
                }
            }
            saidHello = true;
            synchronized (this) {
                notify();
            }
        } else {
            TUI.printError("Server said hello again");
        }
    }

    /**
     * handles the login response from a server
     * @requires !loggedIn
     */
    private void handleLoginServer() {
        if (!loggedIn) {
            loggedIn = true;
            TUI.print("You are now logged in to the server");
            synchronized (this) {
                notify();
            }
        } else {
            TUI.printError("Logged into the server again");
        }
    }

    /**
     * handles server responding that this name is already logged in
     * @requires !loggedIn
     */
    private void handleAlreadyLoggedIn() {
        if (!loggedIn) {
            TUI.print("Username already taken, try again");
            synchronized (this) {
                notify();
            }
        } else {
            TUI.printError("Server sent ALREADYLOGGEDIN again");
        }
    }

    /**
     * handles server responding with a list of online players
     * @requires params.length > 1
     * @param params input from server
     */
    private void handleListServer(String[] params) {
        if (params.length <= 1) {
            TUI.printError("Server sent empty List to print");
        } else {
            StringBuilder players = new StringBuilder();
            for (int i = 1; i < params.length; i++) {
                players.append("\n").append(i).append(". ").append(params[i]);
            }
            TUI.print("Current players on server:" + players);
        }
    }

    /**
     * handles server responding that a game is over
     * @requires params.length >= 2 && (params[2] == "DRAW" ||
     *      params[2] == "VICTORY" || params[2] == "DISCONNECT")
     * @param params server input
     */
    private void handleGameOver(String[] params) {
        TUI.print("Game has ended");
        if (params.length <= 2) {
            if (params.length == 2 && params[1].equals("DRAW")) {
                TUI.print("Draw, no winner");
            } else {
                TUI.printError("Insufficient arguments, winner unknown");
            }
        } else {
            if (params[1].equals("DISCONNECT")) {
                TUI.print("Opponent disconnected");
            } else if (!params[1].equals("VICTORY")) {
                TUI.printError("Server sent unknown reason for game end");
                return;
            }
            TUI.print(params[2] + " has won!");
        }
        game = null;
        useAI = false;
        myTurn = false;
    }

    /**
     * handles server responding with a move
     * @requires params.length == 2 || params.length == 3
     * @param params server input
     */
    private void handleMoveServer(String[] params) {
        if (params.length != 2 && params.length != 3) {
            TUI.printError("Wrong length move response");
        } else {
            moveServer(params);
        }
        printBoard();
        showScore();
        if (!useAI && myTurn) TUI.print("It's your turn");
        if (useAI && myTurn) {
            AIMove();
        }
    }

    /**
     * checks if the move made by the server is correct, and if so prints it
     * @param params server input
     */
    private void moveServer(String[] params) {
        Integer push;
        Integer push2;
        Move move;
        Move move2 = null;
        push = Global.parsePush(params[1]);
        if (push == null) {
            TUI.printError("Server sent incorrect move");
            return;
        }
        move = new Move(push);
        if (params.length == 3) {
            push2 = Global.parsePush(params[2]);
            if (push2 == null) {
                TUI.printError("Server sent incorrect move");
                return;
            } else {
                move2 = new Move(push2);
            }
        }
        if (game.isMoveValid(move, move2)) {
            String output = "Move made: [" + move + "]";
            if (move2 != null) {
                game.makeMove(move, move2);
                output = "Double move " + output + " and [" + move2 + "]";
            } else {
                game.makeMove(move);
            }
            TUI.print(output);
            myTurn = !myTurn;
        } else {
            TUI.printError("Server sent incorrect move");
        }
    }

    /**
     * handles a move input from the use using a client
     * @requires game != null && params.length == 3 || params.length == 5
     * @param params user input
     */
    protected void handleMove(String[] params) {
        if (game == null) {
            TUI.print("You are not in a game");
            return;
        }
        if (params.length != 3 && params.length != 5) {
            TUI.print("Invalid amount of arguments, please try again");
        } else {
            String message = params[0];
            GridBoard.Direction direction;
            GridBoard.Direction direction2 = null;
            try {
                direction = GridBoard.Direction.valueOf(params[2].toUpperCase());
                if (params.length == 5) {
                    direction2 = GridBoard.Direction.valueOf(params[4].toUpperCase());
                    if (game.possibleFirstMove()) {
                        TUI.print("Single move can still be played");
                        return;
                    }
                }
            } catch (IllegalArgumentException i) {
                TUI.print("Direction is wrong, please try again or type help");
                return;
            } // TODO: handle what happens when the input is wrong
            Integer line = Global.parseInt(params[1]);
            if (line == null) {
                TUI.print("Line parameter is wrong, please try again");
                return;
            }
            Move firstMove = new Move(line - 1, direction);
            Move secondMove = null;
            message += DELIMITER + firstMove.push();
            if (params.length >= 4) {
                line = Global.parseInt(params[3]);
                if (line == null) {
                    TUI.print("Line parameter is wrong, please try again");
                    return;
                }
                secondMove = new Move(line - 1, direction2);
                message += DELIMITER + secondMove.push();
            }
            if (game.isMoveValid(firstMove, secondMove)) {
                sendMessage(message);
            } else {
                TUI.print("Move is not valid, please try again or ask for a hint");
            }
        }
    }

    /**
     * handles the server responding with a new game
     * @requires game == null
     * @ensures params[1] up to and including params[49] are parsed into a new board for the game
     * @param params server input
     */
    private void handleNewGame(String[] params) {
        if (game != null) {
            TUI.printError("Still in a game, can't start a new game");
        } else if (params.length != 52) {
            TUI.printError("Incorrect number of arguments received from server");
        } else {
            game = new Game(parseStringBoard(params), params[50], params[51]);
            TUI.print("New game: " + params[50] + " versus " + params[51]);
            useAI();
            if (params[50].equals(playerName)) myTurn = true;
            if (!useAI) {
                printBoard();
                if (myTurn) TUI.print("It's your turn");
            } else {
                if (myTurn) AIMove();
            }
        }
    }


    /**
     * lets an AI make a move
     * @requires game.possibleMoves() == true
     */
    private void AIMove() {
        if (!game.possibleMoves()) return;
        Move[] moves = AI.makeMove(game.getBoard());
        if (moves == null) {
            TUI.print("AI couldn't find a move");
            return;
        } else if (moves[0] == null) {
            addLog(TUI.log("No move move"));
            return;
        }
        String message = "MOVE" + DELIMITER + moves[0].push();
        if (moves.length == 2) message += DELIMITER + moves[1].push();
        sendMessage(message);
    }

    /**
     * waits for a user to select an AI or decide to play for themself when a new game starts
     * @ensures the server input thread waits until the AI selection is done
     */
    private void useAI() {
        choosingAI = true;
        if (!useAI) {
            TUI.print("Do you want to use smartass computer to play for you? (y/n)");
        }
        try {
            synchronized (this) {
                this.wait();
            }
        } catch (InterruptedException e) {
            addLog(TUI.log("InterruptedException while waiting for useAI sequence"));
        }
    }

    /**
     * lets the user choose to play with an AI or not
     * @requires answer[0] == "Y" || answer[0] == "N"
     * @ensures thread is notified if answer[0] == "N"
     * @param answer user input
     */
    protected void chooseAI(String[] answer) {
        if (answer[0].equals("Y")) {
            useAI = true;
            TUI.print("Please specify the level of the ai with 1 or 2");
        } else if (answer[0].equals("N")) {
            useAI = false;
            TUI.print("AI will not be used. It's in your hands my friend");
            choosingAI = false;
            synchronized (this) {
                notify();
            }
        } else {
            TUI.print("Please specify if you will use an AI with y or n");
        }
    }

    /**
     * lets the user choose a difficulty for their AI
     * @requires answer[0] == "1" || answer[0] == "2"
     * @ensures AI = new ComputerPlayer(1) || AI = new ComputerPlayer(2)
     * @param answer user input
     */
    protected void chooseDifficulty(String[] answer) {
        if (answer[0].equals("1")) {
            AI = new ComputerPlayer(1);
        } else if (answer[0].equals("2")) {
            AI = new ComputerPlayer(2);
        } else {
            TUI.print("Please specify the level of the ai with 1 or 2");
            return;
        }
        synchronized (this) {
            choosingAI = false;
            notify();
        }
        TUI.print("AI engaged. Sit back and enjoy");
    }

    /**
     * shows the score and balls of players in a game
     * @requires game != null
     */
    private void showScore() {
        if (game != null) {
            for (int i = 0; i < 2; i++) {
                HashMap<Ball, Integer> ballMap = game.getBalls(i);
                ArrayList<Ball> balls = new ArrayList<>();
                for (Ball ball : ballMap.keySet()) {
                    for (int j = 0; j < ballMap.get(ball); j++) {
                        balls.add(ball);
                    }
                }
                TUI.print(game.getPlayerName(i) + "[" + game.getScore(i) + "]:" + TUI.ballColours(balls));
            }
        }
    }

    /**
     * parses the String of a board to make a new GridBoard object containing that board
     * @requires fields[1] up to and including fields[49] are integers
     * @param fields String containing a board
     * @return GridBoard with a board of the given String
     */
    private GridBoard parseStringBoard(String[] fields) {
        if (fields.length != 52) {
            return null;
        }
        ArrayList<ArrayList<Ball>> board = new ArrayList<>();
        for (int row = 0; row < 7; row++) {
            board.add(new ArrayList<>());
            for (int column = 0; column < 7; column++) {
                try {
                    board.get(row).add(Ball.values()[Integer.parseInt(fields[7*row+column + 1])]);
                } catch (IllegalArgumentException e) {
                    return null;
                } catch (NullPointerException e) {
                    TUI.printError("Server sent wrong arguments for new game");
                }
            }
        }
        return new GridBoard(board);
    }

    /**
     * generates a hint for the player and displays it
     * @requires game != null
     */
    protected void hint() {
        if (game == null) {
            return;
        }
        TUI.print("Your friendly neighbourhood client is generating a hint, hold on...");
        if (game.possibleMoves()) {
            Move[] moves = ComputerPlayer.makeBeginnerMove(game.getBoard());
            String answer = "You can still do " + (moves[0].getLine() + 1) + " " + moves[0].getDirection();
            if (moves.length == 2) {
                answer += " and then " + (moves[1].getLine() + 1) + " " + moves[1].getDirection();
            }
            TUI.print(answer);
        } else {
            TUI.print("No more moves possible!");
        }
    }

    /**
     * sends a String message to a connected server
     * @requires message != null
     * @param message message to be sent to the server
     */
    protected synchronized void sendMessage(String message) {
        if (message == null) {
            return;
        }
        try {
            out.write(message);
            out.newLine();
            out.flush();
            addLog(TUI.log("[" + cyan("OUT") + "] " + message));
        } catch (IOException e) {
            TUI.printError("IOException while sending a message to the server");
        }
    }

    /**
     * prints the board of a current game
     * @requires game != null
     */
    protected void printBoard() {
        if (game != null) game.printBoard();
        else TUI.print("You are not in a game");
    }

    /**
     * prints the logs for the user to see
     */
    protected void printLogs() {
        for (String log : logs) {
            TUI.print(log);
        }
    }

    /**
     * disconnects from a server
     * @requires socket != null
     */
    protected void disconnect() {
        if (socket != null) {
            try {
                socket.close();
                in.close();
                out.close();
                socket = null;
                game = null;
            } catch (IOException e) {
                TUI.printError("IOException while disconnecting from server");
            }
            TUI.print("Connection to server lost");
        }
        printLogs(); //TODO: remove this when done with debugging
        System.exit(0);
    }

    /**
     * prints a help menu for the user to read
     */
    protected void printHelp() {
        TUI.printHelpClient();
    }

    /**
     * adds a log to logs
     * @param log log String to be added
     */
    private synchronized void addLog(String log) {
        logs.add(log);
    }

    /**
     * prompts the user for a login name until the server confirms that it is not taken
     */
    private void requestLogin() {
        String answer = null;
        while (!loggedIn) {
            try {
                TUI.print("Please enter a name to log in to the server:");
                answer = controller.promptUser();
                answer = answer.replaceAll(" +", " ");
                this.playerName = answer;
                sendMessage("LOGIN" + DELIMITER + answer);
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException e) {
                TUI.printError("requestlogin");
                e.printStackTrace();
            }
        }
        TUI.print("You have been logged in under the name: " + answer);
    }

    /**
     * starts up the client and runs the controller
     */
    private void startUpClient() {
        TUI.print("Setting up connection, please wait...");
        handleSetup();
        this.controller.start();
    }

    /**
     * handles the initial HELLO handshake response from a server
     * @requires !saidHello
     * @ensures information about server support is stored
     */
    private void handleSetup() {
        if (saidHello) {
            TUI.printError("Already received HELLO confirmation");
            return;
        }
        String message;
        message = "HELLO" + DELIMITER + DESCRIPTION;
        if (chatSupport) message += DELIMITER + "CHAT";
        if (rankSupport) message += DELIMITER + "RANK";
        if (authSupport) message += DELIMITER + "AUTH";
        if (cryptSupport) message += DELIMITER + "CRYPT";
        sendMessage(message);
        while (!saidHello) {
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException e) {
                TUI.printError("handlesetup");
                e.printStackTrace();
            }
        }
        requestLogin();
    }

    /**
     * listens to the server once the connection has been set up
     */
    @Override
    public void run() {
        // Server messages listener
        try {
            String line;
            while ((line = in.readLine()) != null) {
                addLog(TUI.log("[" + purple("IN") + " ] " + line));
                handleCommandIn(line);
            }
        } catch (IOException e) {
            TUI.printError("IOException while listening to server");
        }
        disconnect();
    }

    public static void main(String[] args) throws IOException {
        InetAddress ip;
        Integer port;
        Controller controller = new Controller();

        if (args.length != 2) {
            // IP prompt
            ip = controller.promptAddress();

            // Port prompt
            port = controller.promptPort();
        } else {
            ip = Global.checkAddress(args[0]);
            port = Global.checkPort(args[1]);
            if (ip == null) ip = controller.promptAddress();
            if (port == null) port = controller.promptPort();
        }

        Socket sock;

        while (true) {
            try {
                TUI.print("Trying to connect to " + ip + ":" + port);
                sock = new Socket(ip, port);
                TUI.print("Connected");
                break;
            } catch (IOException e) {
                TUI.printError("IOException while trying to connect");
            }
            TUI.print("Cannot connect to server on "+ip+":"+port+". Try different parameters.");
            ip = controller.promptAddress();
            port = controller.promptPort();
        }

        Client client = new Client(sock);
        Thread InputHandler = new Thread(client);
        InputHandler.start();
        client.startUpClient();
    }
}


