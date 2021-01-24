package Collecto;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * This class contains the server to which clients
 * can connect to play games against each other.
 * It creates a PlayerHandler object whenever a
 * client connects to handle further communication
 * and games with that client. It stores a queue
 * which players can join if they want to play a game,
 * an inGame list for players who are currently in
 * a game, and a list of all connected clients.
 * It also stores booleans for all additional support
 * that it offers. To start up the Server class
 * uses a Controller object to prompt the user for a
 * valid port to start the server on. It also uses
 * the TUI class to print logs of what is happening
 * on the server.
 *
 * @see PlayerHandler
 * @see Controller
 * @see TUI
 */
public class Server implements Runnable {
    private final int port;

    private final ArrayList<PlayerHandler> playerClients;
    private final LinkedList<PlayerHandler> queue = new LinkedList<>();
    private final ArrayList<PlayerHandler> inGame = new ArrayList<>();

    private static final Controller controller = new Controller();

    /**
     * Description of the server.
     */
    protected final static String DESCRIPTION = "the server of Stan and Hein";

    protected boolean chatSupport = false;
    protected boolean rankSupport = false;
    protected boolean authSupport = false;
    protected boolean cryptSupport = false;

    /**
     * Constructs a server with provided {@code port}.
     *
     * @param port port of the server
     */
    public Server(int port) {
        playerClients = new ArrayList<>();
        this.port = port;
    }

    /**
     * Override of {@code run()} method of Runnable interface.
     * <p>Being activated when {@link #main(String[])} method
     * successfully proceeds. Waits for connection of player,
     * starts a new thread of {@link PlayerHandler}
     * and after client disconnects server doesn't stop because of infinite loop.
     */
    @Override
    public void run() {
        try (ServerSocket ssocket = new ServerSocket(port)) {
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

    /**
     * Adds provided {@code player} to list of players.
     * Used when player successfully logs in.
     *
     * @param player player to add to list
     */
    protected void addPlayer(PlayerHandler player) {
        this.playerClients.add(player);
    }

    /**
     * Removes provided {@code player} from list of players.
     * Being used with connection closure of player.
     *
     * @param player player to remove from list
     */
    protected void removePlayer(PlayerHandler player) {
        this.playerClients.remove(player);
    }

    /**
     * Checks if provided {@code playerName} is already taken in this server.
     *
     * @param playerName name of the player to check
     * @return true if the name is already in the system, false otherwise
     */
    public boolean checkPlayer(String playerName) {
        for (PlayerHandler client : playerClients) {
            if (client.getName().equals(playerName)) return false;
        }
        return true;
    }

    /**
     * Checks if provided {@code client} is already in queue.
     *
     * @param client client to check queue status for
     * @return true if client is in queue, false otherwise
     */
    protected boolean queued(PlayerHandler client) {
        return (queue.contains(client));
    }

    /**
     * Checks if provided {@code client} is not currently in game
     * and if he is not, adds client to queue and calls {@link #startNewGame()}
     * to handle possible new game start.
     *
     * @requires {@code client != null}
     * @ensures client can be added only when not in game
     * @param client client to add to queue
     */
    protected void addToQueue(PlayerHandler client) {
        if (!inGame.contains(client)) {
            queue.add(client);
            startNewGame();
        }
    }

    /**
     * Removes provided {@code client} from queue if it is already in it.
     *
     * @requires {@code client != null}
     * @param client client to remove from queue
     */
    protected void removeFromQueue(PlayerHandler client) {
       if (queued(client)) {
           queue.remove(client);
       }
    }

    /**
     * Returns names of all players currently being on server.
     * 
     * @return array of names of all players currently being on server
     */
    protected ArrayList<String> getPlayers() {
        ArrayList<String> players = new ArrayList<>();
        for (PlayerHandler client : playerClients) {
            players.add(client.getName());
        }
        return players;
    }

    /**
     * Checks if there are at least 2 players in queue 
     * and if there are - handles process of creating new game:
     * randomly determines the first player to start, 
     * creates a new {@code Game} instance adds and sets these two player as in-game
     * players and calls {@link PlayerHandler#startNewGame(Game, PlayerHandler)} 
     * to trigger game start for both player clients.
     */
    private void startNewGame() {
        synchronized (queue) {
            if (queue.size() >= 2) {
            // TODO: check whether queue synchronisation works properly, possible add to other methods as well
                PlayerHandler player1 = queue.get(0);
                PlayerHandler player2 = queue.get(1);
                if (new Random().nextInt(2) == 1) {
                    player1 = queue.get(1);
                    player2 = queue.get(0);
                }
                Game game = new Game(player1.getName(), player2.getName());
                inGame.add(player1);
                inGame.add(player2);
                player1.startNewGame(game, player2);
                player2.startNewGame(game, player1);
                removeFromQueue(queue.get(0));
                removeFromQueue(queue.get(0));
            }
        }
    }

    /**
     * Ends game for provided {@code player} by removing
     * this player from {@code inGame} array.
     *
     * @requires {@code client != null}
     * @ensures client is removed from {@code inGame} is it was there
     * @param client client to remove from inGame
     */
    protected void gameEnded(PlayerHandler client) {
        if (isInGame(client)) {
            inGame.remove(client);
        }
    }

    /**
     * Determines provided {@code player} in-game status. 
     * Returns true if player is in game, false otherwise.
     * 
     * @requires {@code client != null}
     * @param client client to check status of
     * @return true if player is in game, false otherwise
     */
    protected boolean isInGame(PlayerHandler client) {
        return inGame.contains(client);
    }

    /**
     * Main method to start server. 
     * 
     * <p>Firstly checks for provided program argument.
     * If there is exactly one argument, tries to convert
     * this argument to port.
     * If other amount of arguments provided 
     * or provided argument cannot be converted to port, 
     * prompts user to input port manually until port 
     * is verified by starting dummy server on this port.
     * 
     * @param args array of argument. Supposed to contain port argument
     * @see Global#checkPort(String) 
     */
    public static void main(String[] args) {
        Integer port;

        if (args.length != 1) {
            // Port prompt
            port = controller.promptPort();
        } else {
            port = Global.checkPort(args[0]);
            if (port == null) port = controller.promptPort();
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
