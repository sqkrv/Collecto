package Collecto;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import static Collecto.Colour.cyan;
import static Collecto.Colour.purple;
import static Collecto.Global.DELIMITER;
import static Collecto.Global.Protocol.Commands.*;
import static Collecto.Global.Protocol.Extensions;
import static Collecto.Global.Protocol.Misc.*;
import static Collecto.Global.Protocol.Win;

/**
 * This class handles clients that connect to a server.
 * A server creates a PlayerHandler object for each
 * client that connects to that server. The PlayerHandler
 * then communicates with the client, and handles any
 * communication with the client as well as games and
 * opponents of that client. It also stores all necessary
 * information about the client, and makes sure that the
 * connection is cleanly terminated when a client disconnects.
 * It communicates with its server only to check on whatever
 * that must be stored on the server, meaning queues,
 * login names, and adding or removing players. It only starts
 * new games after the server tells it to do so. It is able
 * to print log messages on the terminal of the user running
 * the server using the TUI class.
 *
 * @see Server
 * @see Client
 * @see Game
 * @see TUI
 */
public class PlayerHandler implements Runnable {

    private final BufferedReader in;
    private final BufferedWriter out;
    private final Socket socket;
    private final Server server;
    protected String name;
    private Game game = null;
    private PlayerHandler opponent = null;
    private boolean chatSupport = false;
    private boolean rankSupport = false;
    private boolean authSupport = false;
    private boolean cryptSupport = false;
    private boolean saidHello = false;
    private boolean loggedIn = false;
    private boolean myTurn = false;

    /**
     * Constructs a new PlayerHandler with a given socket and server.
     * Creates a new BufferedReader and BufferedWriter with this socket.
     *
     * @param socket socket connected to a client
     * @param server the server to which this PlayerHandler belongs
     * @throws IOException for input/output errors
     * @requires socket has been connected to a client
     */
    public PlayerHandler(Socket socket, Server server) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.socket = socket;
        this.server = server;
    }

    /**
     * Listens to messages from the client. This method overrides the run() method from
     * the Runnable interface, and it is triggered when a new thread belonging to this
     * PlayerHandler is started. It continuously listens to input from the user, and
     * handles it using the {@link #handleCommand(String)} method. When a client
     * disconnects, it closes the connection using {@link #closeConnection()} and
     * removes the player from the list of players on the server, printing a log
     * serverside to display the player disconnecting. It then also grants the victory
     * of any open game to the opponent of the current player.
     */
    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                handleCommand(message);
            }
        } catch (SocketException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.closeConnection();
        server.removePlayer(this);
        TUI.print(TUI.log("(" + getName() + ") — Client disconnected"));
        // this is where the client is fully disconnected from the server
        if (game != null) {
            opponent.handleGameOver(Win.DISCONNECT);
        }
    }

    /**
     * Handles the command sent by a client. Splits the message parameter based on the
     * {@link Global#DELIMITER} and looks at the first string to determine what command
     * the client has sent. Invokes different methods in PlayerHandler to carry out these
     * commands. Sends an error back if the command is not recognized.
     *
     * @param msg String containing the command and details of the command, separated
     *            by a delimiter
     */
    private void handleCommand(String msg) {
        TUI.print(TUI.log("[" + purple("IN") + " ] " + "(" + getName() + ") — " + msg));
        String message = msg.strip();
        if (message == null) {
            sendError("Sent null to server");
            TUI.print(TUI.log("Client " + this.name + " sent null"));
            return;
        }
        String[] params = message.split(DELIMITER);

        switch (params[0].strip()) {
            case HELLO:
                handleHello(params);
                break;
            case LOGIN:
                handleLogin(params);
                break;
            case LIST:
                handleList();
                break;
            case QUEUE:
                handleQueue();
                break;
            case MOVE:
                handleMove(params);
                break;
            default:
                sendError("Server could not recognize message \"" + message + "\"");
        }
    }

    /**
     * Handles the HELLO command sent by a client. Goes over all support that the client
     * offers, and stores it in booleans inside this PlayerHandler. Sends back a HELLO
     * command to the client.
     *
     * @param params an array of split strings from protocol message by client
     * @requires {@code params.length > 1}
     * @ensures responds to the client with the HELLO command
     */
    private void handleHello(String[] params) {
        if (params.length <= 1) {
            sendError("Insufficient parameters provided");
            return;
        }
        for (int i = 2; i < params.length; i++) {
            switch (params[i]) {
                case Extensions.CHAT:
                    this.chatSupport = true;
                    break;
                case Extensions.RANK:
                    this.rankSupport = true;
                    break;
                case Extensions.AUTH:
                    this.authSupport = true;
                    break;
                case Extensions.CRYPT:
                    this.cryptSupport = true;
                    break;
            }
        }

        respondHello();
        saidHello = true;
    }

    /**
     * Responds to a client with the HELLO command. Adds all services that this server
     * supports to the response.
     */
    private void respondHello() {
        String response = HELLO + DELIMITER + Server.DESCRIPTION;
        if (server.chatSupport) {
            response += DELIMITER + Extensions.CHAT;
        }
        if (server.rankSupport) {
            response += DELIMITER + Extensions.RANK;
        }
        if (server.authSupport) {
            response += DELIMITER + Extensions.AUTH;
        }
        if (server.cryptSupport) {
            response += DELIMITER + Extensions.CRYPT;
        }
        sendMessage(response);
    }

    /**
     * Handles a client attempting to log in to the server to which this PlayerHandler belongs.
     * Checks if the client has said {@value Global.Protocol.Misc#HELLO} first,
     * and if the client is not already logged in.
     * Checks if the name of the client is already registered on the server, in which case it
     * sends back the {@value Global.Protocol.Misc#ALREADY_LOGGED_IN} command.
     * If it is not registered, it confirms the login by sending back
     * the {@value Global.Protocol.Misc#LOGIN} command.
     *
     * @param params LOGIN command and the name of the player, in an array of strings
     * @requires loggedIn == false, {@code params.length == 2}
     */
    private void handleLogin(String[] params) {
        if (!saidHello) {
            sendError("Please say Hello before trying to log in");
        } else if (loggedIn) {
            sendError("You cannot log in twice with the same client");
        } else if (params.length != 2) {
            sendError("Invalid number of parameters provided");
            return;
        }
        params[1] = params[1].replaceAll(" +", " ");
        if (params[1].isBlank()) {
            sendError("Username is blank");
        } else if (params[1].length() > 32) {
            sendError("Login name is too long");
        } else if (!server.checkPlayer(params[1])) {
            sendMessage(ALREADY_LOGGED_IN);
        } else {
            this.name = params[1];
            server.addPlayer(this);
            this.loggedIn = true;
            sendMessage(LOGIN);
        }
    }

    /**
     * Handles the player sending the {@value Global.Protocol.Misc#HELLO} command.
     * Checks if the player is logged in, and sends the connected client
     * a list of all currently connected players on the
     * server belonging to this PlayerHandler.
     *
     * @requires loggedIn == true
     */
    private void handleList() {
        if (!loggedIn) {
            sendError("You are not yet logged in!");
            return;
        }
        StringBuilder playerList = new StringBuilder(LIST);
        ArrayList<String> players = server.getPlayers();
        for (String player : players) {
            playerList.append(DELIMITER).append(player);
        }
        sendMessage(playerList.toString());
    }

    /**
     * Handles a client sending the {@value Global.Protocol.Commands#QUEUE} command.
     * Checks if the client is logged in, and not currently in a game.
     * If the client is not in queue on the server, places the client in queue
     * to await the start of a new game. If the client is
     * already in queue, removes the client from the queue.
     *
     * @requires {@code loggedIn == true}, {@code game != null}
     */
    private void handleQueue() {
        if (!loggedIn) {
            sendError("You are not yet logged in!");
        } else if (game != null) {
            sendError("You are in a game, can't rejoin queue");
        } else {
            if (!server.queued(this)) {
                server.addToQueue(this);
                TUI.print(TUI.log("added " + getName() + " to queue"));
            } else {
                server.removeFromQueue(this);
                TUI.print(TUI.log("removed " + getName() + " from queue"));
            }
        }
    }

    /**
     * Handles a client sending the {@value Global.Protocol.Commands#MOVE} command.
     * Checks if it is the turn of the client, if the client is in a game,
     * and if there is any move possible for the current state of the board.
     * Checks the validity of the move, and executes that move on the board
     * of the current game, and notifies both clients playing the game that the move has
     * successfully been made. Finally checks whether there are any moves still possible
     * in the new state of the game, and if there are not, triggers {@link #handleGameOver()}.
     *
     * @param params the MOVE command followed by a single or double move
     * @requires {@code game != null}, {@code myTurn == true},
     * {@code params.length == 2 || params.length == 3}
     */
    private void handleMove(String[] params) {
        if (game == null) {
            sendError("You are not in a game");
        } else if (!myTurn) {
            sendError("Not your turn");
        } else if (params.length != 2 && params.length != 3) {
            sendError("Invalid amount of arguments provided");
        } else if (params.length == 3 && game.possibleFirstMove()) {
            sendError("Single move still possible");
        } else {
            int push;
            Move firstMove;
            Move secondMove = null;
            try {
                push = Integer.parseInt(params[1]);
                if (!Global.isPushValid(push)) {
                    sendError("Out of bounds move");
                    return;
                }

                firstMove = new Move(push);
                if (params.length > 2) {
                    push = Integer.parseInt(params[2]);
                    if (!Global.isPushValid(push)) {
                        sendError("Out of bounds move");
                        return;
                    }
                    secondMove = new Move(push);
                }
            } catch (IllegalArgumentException e) {
                sendError("Wrong move argument provided");
                return;
            }

            if (!game.isMoveValid(firstMove, secondMove)) {
                sendError("Move is not valid");
            } else {
                respondMove(firstMove, secondMove);
                opponent.respondMove(firstMove, secondMove);
                opponent.myTurn = true;
                myTurn = false;
                if (secondMove == null) {
                    game.makeMove(firstMove);
                } else {
                    game.makeMove(firstMove, secondMove);
                }
                if (!game.possibleMoves()) {
                    opponent.handleGameOver();
                    handleGameOver();
                }
            }
        }
    }

    /**
     * Handles a game ending. Triggers the {@link #handleGameOver(String)} method with the
     * reason {@value Global.Protocol.Win#VICTORY} if the game has a winner,
     * or with {@value Global.Protocol.Win#DRAW} if there is no winner.
     *
     * @requires {@code game != null}
     */
    private void handleGameOver() {
        String winner = game.getWinner();
        if (winner == null) {
            handleGameOver(Win.DRAW);
        } else {
            handleGameOver(Win.VICTORY);
        }
    }

    /**
     * Handles a game ending. Sends a message to the connected client with the
     * {@value Global.Protocol.Misc#GAMEOVER} command followed by the reason,
     * followed by the winner if the reason is not {@value Global.Protocol.Win#DRAW}.
     *
     * @param reason the reason why the game has ended
     * @requires {@code game != null}, reason is
     * {@value Global.Protocol.Win#DRAW}, {@value Global.Protocol.Win#VICTORY},
     * or {@value Global.Protocol.Win#DISCONNECT}
     */
    private void handleGameOver(String reason) {
        String message = GAMEOVER + DELIMITER + reason;
        if (!reason.equals(Win.DRAW)) {
            message += DELIMITER;
            if (reason.equals(Win.DISCONNECT)) {
                message += this.name;
            } else {
                message += game.getWinner();
            }
        }
        sendMessage(message);
        game = null;
        server.gameEnded(this);
    }

    /**
     * Response to a successful move made by a client. Sends the move to the client
     * in the correct format, meaning the {@value Global.Protocol.Commands#MOVE} command,
     * followed by a delimiter, followed by the move itself as an integer between 0-27,
     * and if it is a double move, another delimiter and integer for the second move.
     *
     * @param firstMove  first move made by a client
     * @param secondMove potential second move made by a client or null
     * @requires firstMove is valid, secondMove is valid or null
     */
    private void respondMove(Move firstMove, Move secondMove) {
        if (secondMove == null) {
            sendMessage(MOVE + DELIMITER + firstMove.push());
        } else {
            sendMessage(MOVE + DELIMITER + firstMove.push() + DELIMITER + secondMove.push());
        }
    }

    /**
     * Sends a message to the client connected to this PlayerHandler. Prints the outgoing
     * message on the terminal of the server to which this PlayerHandler belongs.
     *
     * @param message String which is sent to the client
     * @requires {@code message != null}
     */
    private synchronized void sendMessage(String message) {
        try {
            TUI.print(TUI.log("[" + cyan("OUT") + "] " + message));
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            TUI.print(TUI.log("IOException while trying to send a message to " + getName()));
        }
    }

    /**
     * Triggers the start of a new game. Stores the reference to the new game given by the parameter
     * as well as the opponent given by the parameter. Sends a message to the client connected to
     * this PlayerHandler with the {@value Global.Protocol.Misc#NEWGAME} command,
     * followed by the game in String form, and the names of the players,
     * where the player who has the first turn is mentioned first.
     *
     * @param newGame        new game shared by the PlayerHandler and its opponent
     * @param opponentPlayer PlayerHandler of new opponent with whom the new game is shared
     * @requires {@code game == null}
     */
    protected void startNewGame(Game newGame, PlayerHandler opponentPlayer) {
        this.game = newGame;
        String appendage;
        if (newGame.getPlayerName(0).equals(this.name)) {
            appendage = DELIMITER + this.name + DELIMITER + opponentPlayer.name;
            myTurn = true;
        } else {
            appendage = DELIMITER + opponentPlayer.name + DELIMITER + this.name;
        }
        this.opponent = opponentPlayer;
        sendMessage(NEWGAME + newGame.getBoardString() + appendage);
    }

    /**
     * Sends an error to the connected client.
     *
     * @param error description of the error
     */
    private void sendError(String error) {
        String formattedError = ERROR + DELIMITER + error;
        sendMessage(formattedError);
    }

    /**
     * Closes the connection with the connected client.
     * Removes this PlayerHandler from the queue if it is
     * in queue, removes it from the list of online players
     * on the server and tells the server that any game it
     * was playing has ended.
     */
    protected void closeConnection() {
        TUI.print(TUI.log("Closed connection with " + getName()));
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.gameEnded(this);
        server.removeFromQueue(this);
        server.removePlayer(this);
    }

    /**
     * Returns the name of the player (client) connected to this Player.
     * Returns empty string if player name is null.
     *
     * @return name of the client connected to this PlayerHandler or empty string if name is null
     */
    public String getName() {
        if (name != null) return name;
        else return "";
    }
}
