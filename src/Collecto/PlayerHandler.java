package Collecto;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

import static Collecto.Global.DELIMITER;
import static Collecto.Colour.*;
import static Collecto.Global.Protocol.*;
import static Collecto.Global.Protocol.Misc.*;
import static Collecto.Global.Protocol.Commands.*;

public class PlayerHandler implements Runnable {
    
    private final BufferedReader in;
    private final BufferedWriter out;
    private final Socket socket;

    private Game game = null;
    private final Server server;
    private PlayerHandler opponent = null;

    protected String name;

    private boolean chatSupport = false;
    private boolean rankSupport = false;
    private boolean authSupport = false;
    private boolean cryptSupport = false;
    private boolean saidHello = false;
    private boolean loggedIn = false;
    private boolean myTurn = false;


    public PlayerHandler(Socket socket, Server server) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // TODO not sure about Buffered or Print
        this.socket = socket;
        this.server = server;
    }

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
        TUI.print(TUI.log("("+getName()+") — Client disconnected"));
        // this is where the client is fully disconnected from the server
        if (game != null) {
            opponent.handleGameOver(Win.DISCONNECT);
        }
    }

    private void handleCommand(String message) {
        TUI.print(TUI.log( "[" + purple("IN") + " ] " + "("+getName()+") — "+message));
        message = message.strip();
        if (message == null) {
            sendError("suck"); // TODO perhaps change this vulgar wording or smth
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
                sendError("Server could not recognize message \""+message+"\"");
        }
    }

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

    private void respondHello() {
        String response = HELLO + DELIMITER + Server.DESCRIPTION;
        if (server.chatSupport) response += DELIMITER + Extensions.CHAT;
        if (server.rankSupport) response += DELIMITER + Extensions.RANK;
        if (server.authSupport) response += DELIMITER + Extensions.AUTH;
        if (server.cryptSupport) response += DELIMITER + Extensions.CRYPT;
        sendMessage(response);
    }

    private void handleLogin(String[] params) {
        // CRYPT stuff if we do the bonus
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

    private void handleMove(String[] params) {
        if (!myTurn) {
            sendError("Not your turn");
        } else if (params.length != 2 && params.length != 3) {
            sendError("Invalid amount of arguments provided");
        } else if (game == null) {
            sendError("You are not in a game");
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

    private void handleGameOver() {
        String winner = game.getWinner();
        if (winner == null) {
            handleGameOver(Win.DRAW);
        } else {
            handleGameOver(Win.VICTORY);
        }
    }

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

    private void respondMove(Move firstMove, Move secondMove) {
        if (secondMove == null) {
            sendMessage(MOVE + DELIMITER + firstMove.push());
        } else {
            sendMessage(MOVE + DELIMITER + firstMove.push() + DELIMITER + secondMove.push());
        }
    }

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

    protected void startNewGame(Game game, PlayerHandler opponent) {
        this.game = game;
        String appendage;
        if (game.getPlayerName(0).equals(this.name)) {
            appendage = DELIMITER + this.name + DELIMITER + opponent.name;
            myTurn = true;
        } else {
            appendage = DELIMITER + opponent.name + DELIMITER + this.name;
        }
        this.opponent = opponent;
        sendMessage(NEWGAME + game.getBoardString() + appendage);
    }

    private void sendError(String error) {
        error = ERROR + DELIMITER + error;
        sendMessage(error);
    }

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

    public String getName() {
        if (name != null) return name;
        else return "";
    }
}
