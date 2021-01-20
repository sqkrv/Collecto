package Collecto;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class PlayerHandler implements Runnable {

    public static final char DIVISOR = '~';

    private BufferedReader in;
    private BufferedWriter out;
    private Socket socket;
    protected String name;
    private String description;
    private boolean chatSupport = false;
    private boolean rankSupport = false;
    private boolean authSupport = false;
    private boolean cryptSupport = false;
    private boolean saidHello = false;
    private boolean loggedIn = false;
    private boolean myTurn = false;
    private Game game;

    private Server server;

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
                out.newLine();
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCommand(String message) {
        System.out.println(Misc.logTime()+message); // TODO DEBUG
        if (message == null) {
            // TODO: send error to connected client
            return;
        }
        String[] params = message.split(Character.toString(DIVISOR));

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
                sendError("Could not recognize message");
                // TODO: remove sout below after testing is complete
                System.out.println("Could not recognize players message, sorry!");
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
            respondHello();
            saidHello = true;
        }
    }

    private void respondHello() {
        String response = "HELLO" + DIVISOR + server.DESCRIPTION;
        if (server.chatSupport) response += DIVISOR + "CHAT";
        if (server.rankSupport) response += DIVISOR + "RANK";
        if (server.authSupport) response += DIVISOR + "AUTH";
        if (server.cryptSupport) response += DIVISOR + "CRYPT";
        sendMessage(response);
    }

    private void handleLogin(String[] params) {
        // CRYPT stuff if we do the bonus
        if (!saidHello) {
            sendError("Please say Hello first");
        } else if (params.length < 1) {
            sendError("Insufficient parameters provided");
        } else if (server.checkPlayer(params[1])) {
            this.name = params[1];
            this.loggedIn = true;
            sendMessage("LOGIN");
        } else {
            sendMessage("ALREADYLOGGEDIN");
        }
    }

    private void handleList() {
         if (!loggedIn) {
             sendError("You are not yet logged in!");
             return;
         }
         String playerList = "LIST";
         ArrayList<String> players = server.getPlayers();
         for (String player : players) {
             playerList += DIVISOR + player;
         }
         sendMessage(playerList);
    }

    private void handleQueue() {
        // TODO: protect the queue with synchronisation so that it works properly
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
            return;
        }
        int firstMove = -1;
        int secondMove = -1;
        // TODO: add turn check (probably in Game)
        // add isPossible() check
        // add move parsing

        respondMove(firstMove, secondMove);
    }

    private void respondMove(int firstMove, int secondMove) {

        // send the last played move to all players.
    }

    private void sendMessage(String message) {
        try {
            out.write(message);
        } catch (IOException e) {
            // TODO: handle exception
        }
    }

    protected void startNewGame(Game game, boolean starter, String opponent) {
        this.game = game;
        myTurn = starter;
        String appendage;
        if (starter) {
            appendage = DIVISOR + name + DIVISOR + opponent;
        } else {
            appendage = DIVISOR + opponent + DIVISOR + name;
        }
        sendMessage("NEWGAME" + game.getBoardString() + appendage);
        System.out.println("GREAT SUCCES!"); // TODO: remove this line after debugging
    }

    private void sendError(String error) {
        error = "ERROR" + DIVISOR + error;
        try {
            out.write(error);
        } catch (IOException e) {
            // TODO: handle exception
        }
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
