package Collecto;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server implements Runnable {
    private final int port;
    private final InetAddress ip;

    private final ArrayList<PlayerHandler> playerClients;
    private final LinkedList<PlayerHandler> queue = new LinkedList<>();
    private final ArrayList<Game> games = new ArrayList<>();

    protected final static String DESCRIPTION = "the server of Stan and Hein";
    public static final char DELIMITER = '~';

    protected boolean chatSupport = false;
    protected boolean rankSupport = false;
    protected boolean authSupport = false;
    protected boolean cryptSupport = false;

    public Server(InetAddress ip, int port) {
        playerClients = new ArrayList<>();
        this.port = port;
        this.ip = ip;
    }

    @Override
    public void run() {
        try (ServerSocket ssocket = new ServerSocket(port, 0, ip)) {
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

    protected void addPlayer(PlayerHandler player) {
        this.playerClients.add(player);
        this.players.add(player.name);
    }

    protected void removePlayer(PlayerHandler player) {
        this.playerClients.remove(player);
        this.players.remove(player.name);
        // TODO: improve this
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

    protected boolean queued(PlayerHandler client) {
        return (queue.contains(client));
    }

    protected void addToQueue(PlayerHandler client) {
        queue.add(client);
        startNewGame();
    }

    protected void removeFromQueue(PlayerHandler client) {
        queue.remove(client);
    }

    protected ArrayList<String> getPlayers() {
        return players;
    }

    private void startNewGame() {
        if (queue.size() >= 2) {
            Game game = new Game();
            games.add(game);
            queue.get(0).startNewGame(game, true, queue.get(1).name);
            queue.get(1).startNewGame(game, false, queue.get(0).name);
            removeFromQueue(queue.get(0));
            removeFromQueue(queue.get(0));
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("dude");
            System.exit(0);
        }

        InetAddress ip = null;
        try {
            ip = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            System.out.println("dude");
            System.out.println("your ip "+args[0]+" sucks");
            System.exit(0);
        }

        int port = 0;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("dude");
            System.out.println("your port "+args[1]+" sucks");
            System.exit(0);
        }

        Server server = new Server(ip, port);
        System.out.println(Misc.logTime()+"Server starting");
        new Thread(server).start();
        System.out.println(Misc.logTime()+"Server started on "+ip+":"+port);
    }
}
