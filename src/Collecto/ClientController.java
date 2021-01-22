package Collecto;

import java.net.InetAddress;
import java.util.Locale;
import java.util.Scanner;

public class ClientController extends Controller {
    private final Client client;

    public final static String COMMANDS = "LIST, QUEUE, MOVE";

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
        String[] params = input.trim().split(" ");
        params[0] = params[0].toUpperCase();
        switch (params[0]) {
            case "LIST":
            case "QUEUE":
                client.sendMessage(params[0]);
                break;
            case "MOVE":
                handleMove(params);
                break;
            case "DISCONNECT":
                client.disconnect();
                break;
            case "EXIT":
                client.disconnect();
                client.exit();
                break;
            case "LOGS":
                client.printLogs();
                break;
            default:
                TUI.printError("Unknown command: " + params[0]);
                TUI.print("Instead use: " + COMMANDS);
        }
        // TODO: add disconnect statement
    }

    private void handleMove(String[] params) {
        if (params.length <= 1) {
            TUI.print("Insufficient arguments, try again");
        } else {
            String message = params[0] + Server.DELIMITER + params[1];
            if (params.length >= 3) message += Server.DELIMITER + params[2];
            client.sendMessage(message);
        }
    }
}
