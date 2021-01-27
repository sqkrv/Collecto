package Collecto;

import static Collecto.Global.Protocol.Commands.*;
import static Collecto.Global.Protocol.TYPE_HELP;

/**
 * This class is being used for controlling the Client
 * and consists of a handler of all user inputs. This includes parsing
 * and passing these inputs to corresponding commands.
 * This is a subclass of the Controller class.
 *
 * @see Controller
 * @see Client
 */
public class ClientController extends Controller {
    private Client client;

    /**
     * Constructs ClientController with a client provided in the parameter.
     *
     * @param client client reference
     */
    public ClientController(Client client) {
        setClient(client);
    }

    protected void setClient(Client client) {
        this.client = client;
    }

    /**
     * Starts this ClientController and continually listens for input
     * and then passes this input to the {@link #handleCommand(String)} method.
     */
    public void start() {
        String input;
        while ((input = scanner.nextLine()) != null) {
            handleCommand(input);
        }
    }

    /**
     * Handles the command entered by the user by invoking the correct method corresponding to it,
     * or invokes the method {@link Client#chooseAI(String[])} or
     * {@link Client#chooseDifficulty(String[])} while the user is still deciding on using an AI.
     *
     * @param input input from the user as detected by the {@code start()} method
     * @requires {@code input != null}
     * @see Collecto.Global.Protocol.Commands
     */
    protected void handleCommand(String input) {
        if (input == null) {
            return;
        }

        String[] params = input.trim().replaceAll(" +", " ").split(" ");
        params[0] = params[0].toUpperCase();

        switch (params[0]) {
            case HELP:
                client.printHelp();
                break;
            case QUEUE:
                if (!client.inGame()) {
                    TUI.print("Sending QUEUE command to server");
                    client.sendMessage(params[0]);
                } else {
                    TUI.print("Cannot join queue, you are in a game already");
                }
                break;
            case LIST:
                client.sendMessage(params[0]);
                break;
            case LOGS:
                client.printLogs();
                break;
            case BOARD:
                client.printBoard();
                break;
            case STATUS:
                client.status();
                break;
            case DISCONNECT:
                client.disconnect();
                break;
            default:
                if (client.choosingAI) {
                    if (client.useAI) {
                        client.chooseDifficulty(params);
                    } else {
                        client.chooseAI(params);
                    }
                    return;
                }
                if (!client.useAI && client.inGame()) {
                    switch (params[0]) {
                        case HINT:
                            client.hint();
                            break;
                        case MOVE:
                            client.handleMove(params);
                            break;
                        default:
                            TUI.print("Unknown command: " + params[0]);
                            TUI.print(TYPE_HELP);
                    }
                } else {
                    if (params[0].equals(HINT) || params[0].equals(MOVE)) {
                        TUI.print("You are not in a game");
                    } else {
                        TUI.print("Unknown command: " + params[0]);
                        TUI.print(TYPE_HELP);
                    }
                }
        }
    }
}
