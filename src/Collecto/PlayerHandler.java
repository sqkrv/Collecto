package Collecto;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

import static Collecto.Server.DELIMITER;
import static Collecto.Misc.Move;

public class PlayerHandler implements Runnable {
    
    private final BufferedReader in;
    private BufferedWriter out;
    private Socket socket;

    private Game game;
    private Server server;
    private PlayerHandler opponent = null;

    protected String name;
    private String description;

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
        } catch (SocketException e) {
            // TODO handle player disconnect
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Misc.logTime(false)+"("+name+") — Client disconnected");
        // this is where the client is fully disconnected from the server
        opponent.handleGameOver("DISCONNECT");
    }

    private void handleCommand(String message) {
        System.out.println(Misc.logTime(false)+"("+name+") — "+message); // TODO DEBUG
        message = message.strip();
        if (message == null) {
            sendError("suck");
            return;
        }
        String[] params = message.split(Character.toString(DELIMITER));

        switch (params[0].strip()) {
            case "HELLO":
                handleHello(params);
                break;
            case "LOGIN":
                handleLogin(params);
                break;
            case "LIST":
                handleList();
                break;
            case "QUEUE":
                handleQueue();
                break;
            case "MOVE":
                handleMove(params);
                break;
            default:
                sendError("Server could not recognize message \""+message+"\"");
                System.out.println("Could not recognize players message, sorry!"); // TODO DEBUG
        }
    }

    private void handleHello(String[] params) {
        if (params.length <= 1) {
            sendError("Insufficient parameters provided");
            return;
        }
        this.description = params[1];
        for (int i = 2; i < params.length; i++) {
            switch (params[i]) {
                case "CHAT":
                    this.chatSupport = true;
                    break;
                case "RANK":
                    this.rankSupport = true;
                    break;
                case "AUTH":
                    this.authSupport = true;
                    break;
                case "CRYPT":
                    this.cryptSupport = true;
                    break;
            }
        }

        respondHello();
        saidHello = true;
    }

    private void respondHello() {
        String response = "HELLO" + DELIMITER + Server.DESCRIPTION;
        if (server.chatSupport) response += DELIMITER + "CHAT";
        if (server.rankSupport) response += DELIMITER + "RANK";
        if (server.authSupport) response += DELIMITER + "AUTH";
        if (server.cryptSupport) response += DELIMITER + "CRYPT";
        sendMessage(response);
    }

    private void handleLogin(String[] params) {
        // CRYPT stuff if we do the bonus
        if (!saidHello) {
            sendError("Please say Hello before trying to log in");
        } else if (params.length < 1) {
            sendError("Insufficient parameters provided");
        } else if (server.checkPlayer(params[1])) {
            this.name = params[1];
            this.loggedIn = true;
            server.addPlayer(this);
            sendMessage("LOGIN");
        } else {
            sendError("You are already logged in");
        }
    }

    private void handleList() {
         if (!loggedIn) {
             sendError("You are not yet logged in!");
             return;
         }
         StringBuilder playerList = new StringBuilder("LIST");
         ArrayList<String> players = server.getPlayers();
         for (String player : players) {
             playerList.append(DELIMITER).append(player);
         }
         sendMessage(playerList.toString());
    }

    private void handleQueue() {
        if (!loggedIn) {
            sendError("You are not yet logged in!");
        }
        if (!server.queued(this)) {
            server.addToQueue(this);
        } else {
            server.removeFromQueue(this);
        }
    }

    private void handleMove(String[] params) {
        if (!myTurn) {
            sendError("Not your turn");
        } else if (params.length <= 1) {
            sendError("Insufficient arguments provided");
        } else {
            int firstMove;
            int secondMove = -1;
            firstMove = parseInt(params[0]);
            if (params.length > 2) {
                secondMove = parseInt(params[1]);
            }
            if (!isPushValid(firstMove) ||
                    (secondMove != -1 && !isPushValid(secondMove))) {
                sendError("invalid");
            } else {
                respondMove(firstMove, secondMove);
                Move move = new Move(firstMove);
                if (secondMove == -1) {
                    opponent.game.makeMove(move);
                    game.makeMove(move);
                } else {
                    Move move2 = new Move(secondMove);
                    opponent.game.makeMove(move, move2);
                    game.makeMove(move, move2);
                }
                if (!game.possibleMoves()) {
                    opponent.handleGameOver();
                    handleGameOver();
                }
            }
        }
    }

    private boolean isPushValid(int push) {
        return 0 <= push && push <= 27;
    }

    private void handleGameOver() {
        String winner = game.getWinner();
        if (winner == null) {
            handleGameOver("DRAW");
        } else {
            handleGameOver("VICTORY");
        }
    }

    private void handleGameOver(String reason) {
        String message = "GAMEOVER" + DELIMITER + reason;
        if (!reason.equals("DRAW")) {
            message += DELIMITER;
            if (reason.equals("DISCONNECT")) {
                message += this.name;
            } else {
                message += game.getWinner();
            }
        }
        sendMessage(message);
    }

    private void respondMove(int firstMove, int secondMove) {
        if (secondMove == -1) {
            sendMessage("MOVE" + DELIMITER + firstMove);
        } else {
            sendMessage("MOVE" + DELIMITER + firstMove + DELIMITER + secondMove);
        }
    }

    private void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            // TODO: handle exception
        }
    }

    protected void startNewGame(Game game, PlayerHandler opponent) {
        this.game = game;
        String appendage;
        if (game.player1.getName().equals(this.name)) {
            appendage = DELIMITER + this.name + DELIMITER + opponent;
            myTurn = true;
        } else {
            appendage = DELIMITER + opponent.name + DELIMITER + this.name;
        }
        this.opponent = opponent;
        sendMessage("NEWGAME" + game.getBoardString() + appendage);
    }

    private void sendError(String error) {
        error = "ERROR" + DELIMITER + error;
        sendMessage(error);
    }

    protected Integer parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (IllegalArgumentException e) {
            sendError("Wrong arguments provided");
        }
        return null;
    }

    protected void closeConnection() {
        System.out.println(Misc.logTime());
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removePlayer(this);
    }
}
