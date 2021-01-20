package Collecto;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Server implements Runnable {
    private final int port;
    private final ArrayList<PlayerHandler> playerClients;
    private ArrayList<String> players = new ArrayList<>();
    private Queue<String> queue = new LinkedList<>();
    private ArrayList<Game> games;
    protected final static String DESCRIPTION = "the server of Stan and Hein";
    protected boolean chatSupport = false;
    protected boolean rankSupport = false;
    protected boolean authSupport = false;
    protected boolean cryptSupport = false;

    public Server(int port) {
        playerClients = new ArrayList<>();
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket ssocket = new ServerSocket(port, 0, InetAddress.getByName("127.0.0.1"))) {
//        try (ServerSocket ssocket = new ServerSocket(port)) {  // for external server
            while (true) {
                Socket socket = ssocket.accept();
                System.out.println(Misc.logTime()+"New player connected");
                PlayerHandler player = new PlayerHandler(socket, this);
                new Thread(player).start();
                playerClients.add(player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void removePlayer(PlayerHandler player) {
        this.playerClients.remove(player);
    }

    /**
     * checks if a playerName is already registered in this server
     * @param playerName name of the player to check
     * @return true if the name is already in the system, false
     * if the name is not yet registered in the system
     */
    public boolean checkPlayer(String playerName) {
        for (String player : players) {
            if (player.equals(playerName)) return false;
        }
        return true;
    }

    protected boolean queued(String name) {
        return (queue.contains(name));
    }

    protected void addToQueue(String name) {
        queue.add(name);
    }

    protected void removeFromQueue(String name) {
        queue.remove(name);
    }

    protected ArrayList<String> getPlayers() {
        return players;
    }

    private void startNewGame() {
        // TODO: implement
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("dude");
            System.exit(0);
        }

        int port = 0;

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("dude");
            System.out.println("your port "+args[0]+" sucks");
            System.exit(0);
        }

        Server server = new Server(port);
        System.out.println(Misc.logTime()+"Server starting");
        new Thread(server).start();
        System.out.println(Misc.logTime()+"Server started on port "+port);
    }
}
