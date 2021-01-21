package Collecto;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import static Collecto.Server.DELIMITER;

public class Client implements Runnable {
    private Player player; //TODO: find use for this
    private static final String USAGE = "usage: <name> <address> <port>";
    public final static String DESCRIPTION = "Client of Hein and Stan";

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out; // TODO again, Buffered or Print
    private final Scanner scanner;
    private final Controller controller;

    private Game game = null;

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
        controller = new Controller(this);
    }

    private static InetAddress checkIP(String ip) {
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: host " + ip + " unknown");
            System.exit(0);
        }
        return null;
    }

    private static Integer checkPort(String port) {
        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException e) {
            TUI.print(USAGE);
            TUI.printError("port " + port + " is not an integer");
            System.exit(0);
        }
        return null;
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
            notifyAll();
        } else if (!loggedIn) {
            TUI.printError("No LOGIN response received");
            notifyAll();
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
            for (String player : params) {
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
        // TODO: add the move to the Game variable
        if (params.length  <= 1) {
            TUI.printError("Unknown move made");
        } else {
            if (params.length == 2) {
                TUI.print("New move made: [" + params[1] + "]");
            } else {
                TUI.print("Double move made: [" + params[1] + "] " +
                        "and [" + params[2] + "]");
            }
        }
    }

    private void handleNewGame(String[] params) {
        if (game == null) {
            TUI.printError("Still in a game, can't start a new game");
        } else if (params.length != 52) {
            TUI.printError("Incorrect number of arguments received from server");
        } else {
            game = new Game(parseStringBoard(params));
            TUI.print("New game: " + params[50] + " versus " + params[51]);
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
            TUI.print("You sent: " + message);
        } catch (IOException e) {
            TUI.printError("sendmessage");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            TUI.print(USAGE);
            System.exit(0);
        }

        String name = args[0];
        int port;
        InetAddress address;
        Socket sock = null;

        address = checkIP(args[1]);
        port = checkPort(args[2]);

        try {
            TUI.print("Connecting to " + address + ":" + port);
            sock = new Socket(address, port);
            TUI.print(Misc.logTime()+"Connected");
        } catch (IOException e) {
            TUI.printError("IOException while trying to connect");
            e.printStackTrace();
            System.exit(0);
        }

        Client client = new Client(sock);
        Thread InputHandler = new Thread(client);
        InputHandler.start();
        client.startUpClient();
    }

    @Override
    public void run() {
        // Server messages listener
        try {
            String line;
            while ((line = in.readLine()) != null) {
                TUI.print(Misc.logTime()+"Server sent - "+line);
                handleCommandIn(line);
            }
        } catch (IOException e) {
            TUI.printError("IOException while listening to server");
        }
    }

    private void startUpClient() {
        TUI.print("Setting up connection, please wait...");
        handleSetup();
        this.controller.start();
//        while (true) {
//            String message;
//            while ((message = scanner.nextLine()) != null) {
//                message = message.strip();
//                TUI.print("You sent: " + message);
//                handleCommandOut(message);
////                out.write(message);
////                out.newLine();
////                out.flush();
//            }
//        }
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
                answer = controller.handleLogin();
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
}
