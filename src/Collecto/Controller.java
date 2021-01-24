package Collecto;

import java.net.InetAddress;
import java.util.Scanner;

public class Controller {
    protected final Scanner scanner;

    /**
     * Constructs a Controller
     */
    public Controller() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints provided message and then returns line input by the user after the message containing no leading and trailing spaces
     * @param message message to output before prompting the user
     * @ensures line contains no leading and trailing spaces
     * @return input line
     */
    protected synchronized String promptUser(String message) {
        TUI.print(message);
        return promptUser();
    }

    /**
     * Returns line input by the user containing no leading and trailing spaces
     * @ensures line contains no leading and trailing spaces
     * @return input line
     */
    protected synchronized String promptUser() {
        return scanner.nextLine().trim();
    }

    /**
     * Prompts user for an host address until it verifies validness of this address. Then returns this address.
     * @ensures host address is valid InetAddress
     * @return InetAddress to use in Sockets
     */
    protected InetAddress promptAddress() {
        String prompt;
        InetAddress ip;
        while (true) {
            prompt = promptUser("Please enter host address:");
            ip = Global.checkAddress(prompt);
            if (ip != null) break;
            TUI.print("Wrong address provided");
        }
        return ip;
    }

    /**
     * Prompts user for a port until it verifies validness of this port. Then returns this port.
     * @ensures port is valid positive integer
     * @return port
     */
    protected Integer promptPort() {
        String prompt;
        Integer port;
        while (true) {
            prompt = promptUser("Please enter port of the server:");
            port = Global.checkPort(prompt);
            if (port != null) break;
            TUI.print("Wrong port provided");
        }
        return port;
    }
}
