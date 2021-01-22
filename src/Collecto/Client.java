package Collecto;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static Collecto.Server.DELIMITER;
import static Collecto.Misc.*;

public class Client implements Runnable {
    private Player player; //TODO: find use for this
    private static final String USAGE = "usage: <name> <address> <port>";
    public final static String DESCRIPTION = "Client of Hein and Stan";

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out; // TODO again, Buffered or Print
    private final Scanner scanner;
    private final ClientController controller;

    private Game game = null;
    private ArrayList<String> logs = new ArrayList<>();

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

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        scanner = new Scanner(System.in);
        controller = new ClientController(this);
    }

//    private static void clearConnection() {
//        socket = null;
//        in = null;
//        out = null;
//    }

    public void handleCommandIn(String serverInput) {
        if (serverInput == null) {
            return;
        }
        String[] params = serverInput.split(Character.toString(DELIMITER));
        switch (params[0]) {
            case "HELLO":
                handleHelloServer(params);
                break;
            case "LOGIN":
                handleLoginServer();
                break;
            case "ALREADYLOGGEDIN":
                handleAlreadyLoggedIn();
                break;
            case "LIST":
                handleListServer(params);
                break;
            case "NEWGAME":
                handleNewGame(params);
                break;
            case "MOVE":
                handleMoveServer(params);
                break;
            case "GAMEOVER":
                handleGameOver(params);
                break;
            case "ERROR":
                handleError(params);
                break;
            default:
                TUI.printError("Server sent unknown command: ");
        }
    }

    private void handleError(String[] params) {
        if (params.length <= 1) {
            TUI.printError("(no description of the error provided)");
        } else {
            TUI.printError("Server sent an error");
        }
        if (!saidHello) {
            TUI.printError("No HELLO response received");
            synchronized (this) {
                notify();
            }
        } else if (!loggedIn) {
            TUI.printError("No LOGIN response received");
            synchronized (this) {
                notify();
            }
        }
    }

    private void handleHelloServer(String[] params) {
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
    }

    private void handleLoginServer() {
        loggedIn = true;
        TUI.print("You are now logged in to the server");
        synchronized (this) {
            notify();
        }
    }

    private void handleAlreadyLoggedIn() {
        TUI.print("Username already taken, try again");
        synchronized (this) {
            notify();
        }
    }

    private void handleListServer(String[] params) {
        if (params.length <= 1) {
            TUI.printError("Server sent empty List to print");
        } else {
            String players = params[1];
            for (int i = 2; i < params.length; i++) {
                players += " " + player;
            }
            TUI.print("Current players on server:\n" + players);
        }
    }

    private void handleGameOver(String[] params) {
        TUI.print("Game has ended");
        if (params.length <= 2) {
            if (params.length == 2 && params[1].equals("DRAW")) {
                TUI.print("Draw, no winner");
            } else {
                TUI.printError("Insufficient arguments, winner unknown");
            }
        } else {
            if (params[1].equals("DISCONNECT")) TUI.print("Opponent disconnected");
            TUI.print(params[2] + " has won!");
        }
        game = null;
    }

    private void handleMoveServer(String[] params) {
        if (params.length  <= 1) {
            TUI.printError("Unknown move made");
        } else {
            if (params.length == 2) {
                if (game.makeMove(new Move(Integer.parseInt(params[1])))) {
                    TUI.print("New move made: [" + params[1] + "]");
                } else {
                    TUI.printError("Server sent incorrect move");
                }
            } else {
                if (game.makeMove(new Move(Integer.parseInt(params[1])), new Move(Integer.parseInt(params[2])))) {
                    TUI.print("Double move made: [" + params[1] + "] " +
                            "and [" + params[2] + "]");
                } else {
                    TUI.printError("Server sent incorrect move");
                }
            }
        }
        game.printBoard();
    }

    private void handleNewGame(String[] params) {
        if (game != null) {
            TUI.printError("Still in a game, can't start a new game");
        } else if (params.length != 52) {
            TUI.printError("Incorrect number of arguments received from server");
        } else {
            game = new Game(parseStringBoard(params), params[50], params[51]);
            TUI.print("New game: " + params[50] + " versus " + params[51]);
            game.printBoard();
        }
    }

//    /**
//     * handles commands sent by the user using the terminal
//     * @requires userInput != null
//     * @ensures userInput is handled correctly, or an error is displayed
//     * @param userInput String with input from the user
//     */
//    public void handleCommandOut(String userInput) {
//        TUI.print(Misc.logTime()+userInput);
//        String[] params = userInput.split(" ");
//        switch (params[0]) {
//            case "LIST":
//            case "QUEUE":
//                sendMessage(params[0]);
//                break;
//            case "MOVE":
//                handleMove(params);
//                break;
//            default:
//                TUI.printError("Unknown command: " + params[0]);
//                TUI.print("Instead use: " + Controller.COMMANDS);
//        }
//    }
//
//    private void handleMove(String[] params) {
//        if (params.length <= 1) {
//            TUI.print("Insufficient arguments, try again");
//        } else {
//            String message = params[0] + Server.DELIMITER + params[1];
//            if (params.length >= 3) message += Server.DELIMITER + params[2];
//            sendMessage(message);
//        }
//    }

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

    protected void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
            logs.add(TUI.log("You sent    - " + message));
        } catch (IOException e) {
            TUI.printError("sendmessage");
            e.printStackTrace();
        }
    }

    protected void printLogs() {
        for (String log : logs) {
            TUI.print(log);
        }
    }

    protected void disconnect() {
        if (socket != null) {
            try {
                socket.close();
                in.close();
                out.close();
                socket = null;
//                in = null;
//                out = null;
//                socket = null;
            } catch (IOException e) {
                TUI.printError("IOException while disconnecting from server");
            }
            logs.add(TUI.log("Disconnected from server"));
        }
    }

    protected void exit() {
        System.exit(0);
    }

    @Override
    public void run() {
        // Server messages listener
        try {
            String line;
            while ((line = in.readLine()) != null) {
                logs.add(TUI.log("Server sent - "+line));
                handleCommandIn(line);
            }
        } catch (IOException e) {
            TUI.printError("IOException while listening to server");
            disconnect();
            exit();
        }
    }

    private void startUpClient() {
        TUI.print("Setting up connection, please wait...");
        handleSetup();
        this.controller.start();
    }

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

    private void requestLogin() {
        String answer = null;
        while (!loggedIn) {
            try {
                TUI.print("Please enter a name to log in to the server:");
                answer = controller.promptUser();
                sendMessage("LOGIN" + Server.DELIMITER + answer);
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

    public static void main(String[] args) throws IOException {
        InetAddress ip;
        Integer port;
        Controller controller = new Controller();

        if (args.length != 2) {
            // IP prompt
            ip = controller.promptIP();

            // Port prompt
            port = controller.promptPort();
        } else {
            ip = checkIP(args[0]);
            port = checkPort(args[1]);
            if (ip == null) ip = controller.promptIP();
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
            ip = controller.promptIP();
            port = controller.promptPort();
        }

        Client client = new Client(sock);
        Thread InputHandler = new Thread(client);
        InputHandler.start();
        client.startUpClient();
    }
}


