package Collecto;

import static Collecto.Global.Protocol.TYPE_HELP;
import static Collecto.Global.Protocol.Commands.*;

public class ClientController extends Controller {
    private final Client client;

    public ClientController(Client client) {
        this.client = client;
    }

    public void start() {
        String input;
        while ((input = scanner.nextLine()) != null) {
            handleCommand(input);
        }
    }

    private void handleCommand(String input) {
        if (input == null) {
            return;
        }
//        input = input.replaceAll(" +", " ");
        String[] params = input.trim().split(" ");
        params[0] = params[0].toUpperCase();

        if (client.choosingAI) {
            if (client.useAI) {
                client.chooseDifficulty(params);
            } else {
                client.chooseAI(params);
            }
            return;
        }

        switch (params[0]) {
            case HELP:
                client.printHelp();
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
            case DISCONNECT:
                client.disconnect();
                break;
            default:
                if (!client.useAI) {
                    switch (params[0]) {
                        case HINT:
                            client.hint();
                            break;
                        case QUEUE:
                            client.sendMessage(params[0]);
                            break;
                        case MOVE:
                            client.handleMove(params);
                            break;
                        default:
                            TUI.printError("Unknown command: " + params[0]);
                            TUI.print(TYPE_HELP);
                    }
                } else {
                    TUI.printError("You can't use this command while using AI");
                }
        }
    }
}
