package Collecto;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server implements Runnable {
    private final int port;

    private final ArrayList<PlayerHandler> playerClients;
    private final LinkedList<PlayerHandler> queue = new LinkedList<>();
    private final ArrayList<PlayerHandler> inGame = new ArrayList<>();

    private static final ServerController controller = new ServerController();

    protected final static String DESCRIPTION = "the server of Stan and Hein";
    public static final char DELIMITER = '~';

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
        try (ServerSocket ssocket = new ServerSocket(port)) {
//        try (ServerSocket ssocket = new ServerSocket(port)) {  // for external server
            while (true) {
                Socket socket = ssocket.accept();
                TUI.print(TUI.log("New player connected"));
                PlayerHandler player = new PlayerHandler(socket, this);
                new Thread(player).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void addPlayer(PlayerHandler player) {
        this.playerClients.add(player);
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
        for (PlayerHandler client : playerClients) {
            if (client.name.equals(playerName)) return false;
        }
        return true;
    }

    protected boolean queued(PlayerHandler client) {
        return (queue.contains(client));
    }

    protected void addToQueue(PlayerHandler client) {
        if (!inGame.contains(client)) {
            queue.add(client);
            startNewGame();
        }
    }

    protected void removeFromQueue(PlayerHandler client) {
       queue.remove(client);
    }

    protected ArrayList<String> getPlayers() {
        ArrayList<String> players = new ArrayList<>();
        for (PlayerHandler client : playerClients) {
            players.add(client.name);
        }
        return players;
    }

    private void startNewGame() {
        synchronized (queue) {
            if (queue.size() >= 2) {
            // TODO: check whether queue synchronisation works properly, possible add to other methods as well
                PlayerHandler player1 = queue.get(0);
                PlayerHandler player2 = queue.get(1);
                Game game = new Game(player1.name, player2.name);
                inGame.add(player1);
                inGame.add(player2);
                player1.startNewGame(game, player2);
                player2.startNewGame(game, player1);
                removeFromQueue(queue.get(0));
                removeFromQueue(queue.get(0));
            }
        }
    }

    public static void main(String[] args) {
        Integer port = 0;

        if (args.length != 1) {
            // Port prompt
            port = controller.promptPort();
        }

        // Port check
        while (true) {
            try {
                ServerSocket s = new ServerSocket(port);
                s.close();
                break;
            } catch (BindException e) {
                TUI.print("Cannot start server on port "+port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            port = controller.promptPort();
        }

        TUI.print(TUI.log("Server starting"));
        Server server = new Server(port);
        new Thread(server).start();
        TUI.print(TUI.log("Server started on port "+port));
    }
}
