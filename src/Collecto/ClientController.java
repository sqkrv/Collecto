package Collecto;

public class ClientController extends Controller {
    private final Client client;
    protected boolean choosingAI = false;

    public final static String COMMANDS = "LIST, QUEUE, MOVE, LOGS, HELP, DISCONNECT, EXIT";

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

        if (choosingAI) {
            client.chooseAI(params[0]);
        }

        switch (params[0]) {
            case "DISCONNECT":
                client.disconnect();
                break;
            case "EXIT":
                client.disconnect();
                client.exit();
                break;
            case "HELP":
                client.printHelp();
                break;
            case "HINT":
                client.hint();
                break;
            case "LOGS":
                client.printLogs();
                break;
            case "MOVE":
                client.handleMove(params);
                break;
            case "QUEUE":
            case "LIST":
                client.sendMessage(params[0]);
                break;
            default:
                TUI.printError("Unknown command: " + params[0]);
                TUI.print("Instead use: " + COMMANDS);
        }
    }
}
