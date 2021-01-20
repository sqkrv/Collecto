package Collecto;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class PlayerHandler implements Runnable {
    private BufferedReader in;
    private BufferedWriter out;
    private Socket socket;
    private String name;
    private String description;
    private boolean chatSupport = false;
    private boolean rankSupport = false;
    private boolean authSupport = false;
    private boolean cryptSupport = false;
    private boolean loggedIn = false;

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
        String[] params = message.split("~");

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
                // TODO remove sout below after testing is complete
                System.out.println("Could not recognize players message, sorry!");
        }
    }

    private void handleHello(String[] params) {
        if (params.length < 1) {
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
        }
    }

    private void respondHello() {
        String response = "HELLO~" + server.DESCRIPTION;
        if (server.chatSupport) response = response + "~CHAT";
        if (server.rankSupport) response = response + "~RANK";
        if (server.authSupport) response = response + "~AUTH";
        if (server.cryptSupport) response = response + "~CRYPT";
        sendMessage(response);
    }

    private void handleLogin(String[] params) {
        // CRYPT stuff if we do the bonus
        if (params.length < 1) {
            sendError("Insufficient parameters provided");
            return;
        }
        if (server.checkPlayer(params[1])) {
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
             playerList = playerList + "~" + player;
         }
         sendMessage(playerList);
    }

    private void handleQueue() {
        // TODO: protect the queue with synchronisation so that it works properly
        if (!loggedIn) {
            sendError("You are not yet logged in!");
        }
        if (!server.queued(this.name)) {
            server.addToQueue(this.name);
        } else {
            server.removeFromQueue(this.name);
        }
    }

    private void handleMove(String[] params) {
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

    private void sendError(String error) {
        error = "ERROR~" + error;
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
