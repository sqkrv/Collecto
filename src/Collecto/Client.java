package Collecto;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static Collecto.Colour.cyan;
import static Collecto.Colour.purple;
import static Collecto.Global.DELIMITER;
import static Collecto.Global.Protocol.Commands.LIST;
import static Collecto.Global.Protocol.Commands.MOVE;
import static Collecto.Global.Protocol.Misc.*;
import static Collecto.Global.validIndex;

/**
 * This class contains the client used by users who want to connect to a server and play a game.
 * Upon running this class it will prompt the user for an IP and a port to connect to a server, and
 * it will then provide further functionality to communicate with the server, play the game, let
 * an AI play the game, provide hints, and disconnect. It also uses the TUI class to print states
 * of the game for the user to see.
 * It stores a socket for communication with a sever, a game to play, logs to print if you encounter
 * errors, the name by which the player is logged in to a sever and a ClientController to receive
 * user input.
 *
 * @see Server
 * @see TUI
 * @see Game
 * @see ClientController
 */
public class Client implements Runnable {
    public final static String DESCRIPTION = "Client of Hein and Stan";
    private final ClientController controller;
    private final ArrayList<String> logs = new ArrayList<>();
    protected boolean chatSupport = false;
    protected boolean rankSupport = false;
    protected boolean authSupport = false;
    protected boolean cryptSupport = false;
    protected boolean serverChatSupport = false;
    protected boolean serverRankSupport = false;
    protected boolean serverAuthSupport = false;
    protected boolean serverCryptSupport = false;
    protected boolean useAI = false;
    protected boolean choosingAI = false;
    protected Game game = null;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String playerName;
    private ComputerPlayer ai;
    private boolean saidHello = false;
    private boolean loggedIn = false;
    private boolean myTurn = false;

    /**
     * Initializes communication with server and input from the user by creating
     * a new BufferedReader, BufferedWriter, and ClientController,
     * and using the socket given in the parameter.
     *
     * @param socket socket connected to a server
     * @requires socket != null
     */
    public Client(Socket socket) {
        setupSocket(socket);
        controller = new ClientController(this);
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
            if (ip == null) {
                ip = controller.promptAddress();
            }
            if (port == null) {
                port = controller.promptPort();
            }
        }

        Socket sock;

        while (true) {
            try {
                TUI.print("Trying to connect to " + ip + ":" + port);
                sock = new Socket(ip, port);
                TUI.print("Connected");
                break;
            } catch (IOException ignored) {
            }
            TUI.print("Cannot connect to server on " + ip + ":" + port + ". " +
                    "Try different parameters.");
            ip = controller.promptAddress();
            port = controller.promptPort();
        }

        Client client = new Client(sock);
        Thread inputHandler = new Thread(client);
        inputHandler.start();
        client.startUpClient();
    }

    private void setupSocket(Socket sock) {
        try {
            this.socket = sock;
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (IOException e) {
            TUI.printError("Error while setting up input and output");
        }
    }

    /**
     * Handles commands sent by a connected server. Splits the serverInput on the delimiter as
     * defined in {@link Global#DELIMITER} and assumes the first word is the command sent
     * by the server, and the rest of the serverInput is information on what to do
     * with that command. Based on the command this method calls other methods in this class
     * to carry out the functionality. If the command is not recognized, the client prints
     * an error by default stating it did not understand what command the server sent.
     *
     * @param serverInput String input from the server
     * @requires {@code serverInput != null}
     * @ensures serverInput is handled correctly
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
                TUI.printError("Server sent unknown command: " + params[0]);
        }
    }

    /**
     * Handles errors sent by the server. Adds the error description provided in params[1]
     * to the logs and notifies the user that an error has been received. Never immediately
     * shows the contents of the error to the user. Notifies the user if the hello-handshake
     * sequence or the login sequence have not yet been completed, and wakes up the sleeping
     * thread waiting for the server response about hello or login.
     *
     * @param params server input split into an array of strings
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
            }
            addLog(params[1]);
            TUI.printError("Server sent error, see logs for more info");
        }
    }

    /**
     * Handles the hello-handshake response from a server. Stores all the support that the
     * server offers as specified in the parameter, and notifies the sleeping thread waiting
     * for the server to send a hello response.
     *
     * @param params server input containing the HELLO command and all server support
     * @requires {@code saidHello == false}
     * @ensures all server support is stored, sleeping thread is woken up, {@code saidHello == true}
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
     * Handles the login response from a server. Notifies the user that he is logged
     * in to the server, and wakes up the sleeping thread waiting for
     * the LOGIN command response from the server.
     *
     * @requires {@code loggedIn == false}
     * @ensures sleeping thread is woken up, {@code loggedIn == true}
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
     * Handles server responding that this name is already logged in.
     * Notifies the user that the username they tried to log in with
     * is already taken, and wakes up the sleeping thread waiting for the LOGIN
     * command response from the server.
     *
     * @requires {@code loggedIn == false}
     * @ensures sleeping thread is woken up, {@code loggedIn == true}
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
     * Handles server responding with a list of online players. Takes the list of players provided
     * by the server and prints them onscreen in orderly fashion for the user to see.
     *
     * @param params input from server containing the LIST command and all online players in an
     *               array of strings.
     * @requires {@code params.length > 1}
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
     * Handles server responding that a game is over. Notifies the user of the reason
     * of the game being over, as well as the winner if there is one.
     * Resets all game related parameters to be ready for a new game.
     *
     * @param params server input
     * @requires {@code params.length == 2 || params.length == 3, (params[2] == "DRAW" ||
     * params[2] == "VICTORY" || params[2] == "DISCONNECT")}
     * @ensures {@code game == null}, {@code useAI == false},
     * {@code myTurn == false}, user is notified of game end
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
     * Handles server responding with a move. Checks if the move is correct
     * and subsequently adds that move to the Game object stored on this client,
     * by invoking the {@link #moveServer(String[])} method.
     *
     * @param params server input
     * @requires {@code params.length == 2 || params.length == 3}
     * @ensures board is printed, score is shown, AI makes move if useAI==true
     */
    private void handleMoveServer(String[] params) {
        if (params.length != 2 && params.length != 3) {
            TUI.printError("Wrong length move response");
        } else {
            moveServer(params);
        }
        printBoard();
        showScore();
        if (!useAI && myTurn) {
            TUI.print("It's your turn");
        }
        if (useAI && myTurn && !choosingAI) {
            aiMove();
        }
    }

    /**
     * Checks if the single or double move made by the server is correct, and adds that move
     * to the current game object of this client. Invokes the {@link Game#isMoveValid(Move, Move)}
     * and the {@link Game#makeMove(Move)} methods to do so. Displays an error to the user
     * if the move made by the server is invalid for the current game.
     *
     * @param params array containing the MOVE command and the move or moves themselves
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
     * Handles a move input from the user using this client. Checks whether the move is valid and
     * parses it to a push format according to the protocol, meaning an integer between 0 and 27.
     * Also checks if a double move is made, whether
     * a single move could still be made, and if so notifies the user.
     * Ultimately sends the move to the server if it passes all clientside checks.
     *
     * @param params user input
     * @requires {@code game != null, params.length == 3 || params.length == 5}
     */
    protected void handleMove(String[] params) {
        if (game == null) {
            TUI.print("You are not in a game");
        } else if (!myTurn) {
            TUI.print("Not your turn");
        } else {
            if (params.length != 3 && params.length != 5) {
                TUI.print("Invalid amount of arguments, please try again");
            } else {
                String message = params[0];
                Move.Direction direction;
                Move.Direction direction2 = null;
                try {
                    direction = Move.Direction.valueOf(params[2].toUpperCase());
                    if (params.length == 5) {
                        direction2 = Move.Direction.valueOf(params[4].toUpperCase());
                        if (game.possibleFirstMove()) {
                            TUI.print("Single move can still be played");
                            return;
                        }
                    }
                } catch (IllegalArgumentException i) {
                    TUI.print("Direction is wrong, please try again or type help");
                    return;
                }
                Integer line = Global.parseInt(params[1]);
                if (line == null || !validIndex(line)) {
                    TUI.print("Line parameter is wrong, please try again");
                    return;
                }
                Move firstMove = new Move(line - 1, direction);
                Move secondMove = null;
                message += DELIMITER + firstMove.push();
                if (params.length >= 4) {
                    line = Global.parseInt(params[3]);
                    if (line == null || !validIndex(line)) {
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
    }

    /**
     * Handles the server responding with a new game. Parses the strings containing the new board
     * of the game to GridBoard object using the {@link #parseStringBoard(String[])} method, and
     * creates a new Game object with it which is stored in the {@code game} attribute, along with
     * the names of the players also provided in the parameter of this method. Prints the board for
     * the user and if it is their turn, notifies them or lets the AI make a move using the
     * {@link #aiMove()} method.
     *
     * @param params server input
     * @requires {@code game == null, params.length == 52}
     * @ensures params[1] up to and including params[49] are parsed into a new board for the game
     */
    protected void handleNewGame(String[] params) {
        if (game != null) {
            TUI.printError("Still in a game, can't start a new game");
        } else if (params.length != 52) {
            TUI.printError("Incorrect number of arguments received from server");
        } else {
            TUI.print("New game: " + params[50] + " versus " + params[51]);
            game = new Game(parseStringBoard(params), params[50], params[51]);
            if (params[50].equals(playerName)) {
                myTurn = true;
            }
            useAI();
        }
    }

    /**
     * Lets an AI make a move. The AI determines a move based on its difficulty, and if it can
     * successfully find a move, sends it to the server. If it can not find a move, notifies the
     * user of this client.
     *
     * @requires {@code game.possibleMoves() == true}, {@code AI != null}
     */
    private void aiMove() {
        if (!game.possibleMoves()) {
            return;
        }
        Move[] moves = ai.makeMove(game.getBoard());
        if (moves == null) {
            TUI.print("AI couldn't find a move");
            return;
        } else if (moves[0] == null) {
            addLog(TUI.log("No AI move"));
            return;
        }
        String message = "MOVE" + DELIMITER + moves[0].push();
        if (moves.length == 2) {
            message += DELIMITER + moves[1].push();
        }
        sendMessage(message);
    }

    /**
     * Waits for a user to select an AI or decide to play for themself.
     * This method is triggered whenever a new game starts, and it asks the user if they want to
     * use an AI, after which this thread, which is the thread that handles server input, sleeps
     * until the user has completed AI sequence and has either chosen an AI level or decided not
     * to use an AI.
     *
     * @requires triggered only when a new game starts
     * @ensures the server input thread waits until the AI selection is done
     */
    private void useAI() {
        choosingAI = true;
        if (!useAI) {
            TUI.print("Do you want to use computer to play for you? (y/n)");
        }
    }

    /**
     * Checks the answers of the user to the question of whether they want to play with
     * an AI or not. If not, the AI choosing sequence is ended and the sleeping server input
     * thread is woken up. Otherwise, the user is asked to choose the level of the AI, and
     * continues to the {@link #chooseDifficulty(String[])} method.
     *
     * @param answer user input
     * @requires {@code answer[0] == "Y" || answer[0] == "N"}
     * @ensures thread is notified if answer[0] == "N"
     */
    protected void chooseAI(String[] answer) {
        if (answer[0].equals("Y")) {
            useAI = true;
            TUI.print("Please specify the level of the AI with 1 or 2");
        } else if (answer[0].equals("N")) {
            useAI = false;
            TUI.print("AI will not be used. It's in your hands my friend");
            choosingAI = false;
            printBoard();
            if (myTurn) {
                TUI.print("It's your turn");
            }
        } else {
            TUI.print("Please specify if you will use an AI with y or n");
        }
    }

    /**
     * Handles user input for AI difficulty level specification.
     * Will not initialise AI until answer will be either {@code 1} or {@code 2}.
     * When a correct answer is chosen, ends the AI choosing sequence and wakes up the
     * sleeping server input thread.
     *
     * @param answer array of split user answer
     */
    protected void chooseDifficulty(String[] answer) {
        if (answer[0].equals("1")) {
            ai = new ComputerPlayer(1);
        } else if (answer[0].equals("2")) {
            ai = new ComputerPlayer(2);
        } else {
            TUI.print("Please specify the level of the ai with 1 or 2");
            return;
        }
        choosingAI = false;
        if (myTurn) {
            aiMove();
        }
        TUI.print("AI engaged. Sit back and enjoy");
    }

    /**
     * Prints score and string representation of all balls for each player in game.
     * The output is of the following format: {@code username[score]: balls}
     *
     * @requires {@code game != null}
     * @see TUI#print(String)
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
                TUI.print(game.getPlayerName(i) + "[" + game.getScore(i) + "]:"
                        + TUI.ballColours(balls));
            }
        }
    }

    /**
     * Parses the protocol representation of the board and converts it to the {@link GridBoard}
     * class instance with converted board. Protocol board representation means that in the
     * subarray of fields[1] to fields[49] all strings must contain only integers from 0-6.
     *
     * @param fields protocol representation of the board
     * @return GridBoard with a board of the given String
     * @requires fields[1] up to and including fields[49] to be valid according to protocol,
     * fields.length == 52
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
                    board.get(row).add(Ball.values()
                            [Integer.parseInt(fields[7 * row + column + 1])]);
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
     * Generates a hint using {@link ComputerPlayer} for the current game board and displays it.
     * Uses the beginner AI to show to the user the first possible single move, or the first
     * possible double move if no single move is possible.
     *
     * @requires {@code game != null}
     * @see ComputerPlayer#makeBeginnerMove(GridBoard)
     */
    protected void hint() {
        if (game == null) {
            return;
        }
        TUI.print("Your friendly neighbourhood client is generating a hint, hold on...");
        if (game.possibleMoves()) {
            Move[] moves = ComputerPlayer.makeBeginnerMove(game.getBoard());
            if (moves == null) {
                TUI.print("No hints could be generated, sorry!");
                return;
            }
            String answer = "You can still do " + (moves[0].getLine() + 1)
                    + " " + moves[0].getDirection();
            if (moves.length == 2) {
                answer += " and then " + (moves[1].getLine() + 1) + " " + moves[1].getDirection();
            }
            TUI.print(answer);
        } else {
            TUI.print("No more moves possible!");
        }
    }

    /**
     * Sends a String message to a connected server. Adds a log of every outgoing message.
     *
     * @param message message to be sent to the server
     * @requires {@code message != null}
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

    protected void status() {
        StringBuilder string = new StringBuilder();
        string.append("Connection: ");
        if (!socket.isClosed()) {
            string.append("connected to ");
            string.append(socket.getInetAddress()).append(":").append(socket.getPort());
        } else {
            string.append("not connected");
        }
        string.append("\n");
        string.append("In game: ");
        if (inGame()) {
            string.append("yes");
        } else {
            string.append("no");
        }
        TUI.print(string.toString());
    }

    /**
     * Disconnects from a server. Closes the socket, reader and writer, and clears the socket and
     * the game of this client, and then gracefully terminates the program.
     *
     * @requires {@code socket != null}
     */
    protected synchronized void disconnect() {
        if (socket != null) {
            try {
                socket.close();
                in.close();
                out.close();
                socket = null;
                game = null;
            } catch (IOException e) {
                addLog("IOException while disconnecting from server");
            }
            TUI.print("Connection to server lost");
        }
        System.exit(0);
    }

    /**
     * Prints the board of a current game for the user to see.
     *
     * @requires {@code game != null}
     */
    protected void printBoard() {
        if (game != null) {
            game.printBoard();
        } else {
            TUI.print("You are not in a game");
        }
    }

    /**
     * Prints all recorded logs for the user to see. This includes incoming and outgoing messages,
     * internal errors, errors sent by the server, and other noteworthy events.
     */
    protected void printLogs() {
        for (String log : logs) {
            TUI.print(log);
        }
    }

    /**
     * Prints a help menu for the user to read.
     */
    protected void printHelp() {
        TUI.printHelpClient();
    }

    /**
     * Adds a log to the logs ArrayList of this client.
     *
     * @param log String that is added to the logs
     */
    private synchronized void addLog(String log) {
        logs.add(log);
    }

    /**
     * Prompts the user for a login name to return to the server. Repeats this process until
     * the server sends confirmation that the username was not already in use, and the user
     * was registered. Makes the user input thread sleep while it waits for server response.
     */
    private void requestLogin() {
        String answer = null;
        while (!loggedIn) {
            try {
                TUI.print("Please enter a name to log in to the server:");
                answer = controller.promptUser();
                answer = answer.replaceAll(" +", " ");
                if (answer.isBlank()) {
                    TUI.print("You entered an empty name, please enter a valid name.");
                    continue;
                }
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
     * Starts up the client by using the {@link #handleSetup()} method, and starts the
     * controller after the setup is complete.
     */
    private void startUpClient() {
        TUI.print("Setting up connection, please wait...");
        handleSetup();
        this.controller.start();
    }

    /**
     * Handles the initial HELLO handshake response with a server. This method is triggered
     * in the client start up sequence when a connection with a server has been established.
     * It handles the HELLO handshake with the connected server without required user input,
     * by sending a HELLO command to the server followed by all services that this client
     * supports. It lets the user input thread sleep while it waits for the server to respond,
     * after which it triggers the {@link #requestLogin()} method.
     *
     * @requires {@code saidHello == false}
     * @ensures information about client services support is sent to the server
     */
    private void handleSetup() {
        if (saidHello) {
            TUI.printError("Already received HELLO confirmation");
            return;
        }
        String message;
        message = "HELLO" + DELIMITER + DESCRIPTION;
        if (chatSupport) {
            message += DELIMITER + "CHAT";
        }
        if (rankSupport) {
            message += DELIMITER + "RANK";
        }
        if (authSupport) {
            message += DELIMITER + "AUTH";
        }
        if (cryptSupport) {
            message += DELIMITER + "CRYPT";
        }
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
     * Checks to see if the client is currently playing a game.
     *
     * @return true if {@code game != null}, false if {@code game == null}
     */
    protected boolean inGame() {
        return game != null;
    }

    /**
     * Listens to the server. This command overrides the run() method from the Runnable
     * interface, and is triggered in the main method when the server input thread is
     * started. It then continuously listens to the server input until this client or
     * the server disconnects. In case of the server disconnecting, it triggers the
     * {@link #disconnect()} method to terminate this client. It also adds a log
     * for every incoming message.
     */
    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                addLog(TUI.log("[" + purple("IN") + " ] " + line));
                handleCommandIn(line);
            }
        } catch (IOException e) {
            addLog("IOException while listening to server");
        }
        disconnect();
    }
}


