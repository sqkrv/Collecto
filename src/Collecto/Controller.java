package Collecto;

import java.util.Locale;
import java.util.Scanner;

public class Controller {
    private final Scanner scanner;
    private final Client client;

    public final static String COMMANDS = "LIST, QUEUE, MOVE";

    public Controller(Client client) {
        this.client = client;
        scanner = new Scanner(System.in);
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
        switch (params[0].toUpperCase()) {
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

    protected String promptUser() {
        return scanner.nextLine().trim();
    }

}
