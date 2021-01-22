package Collecto;

import java.net.InetAddress;
import java.util.Scanner;

public class Controller {
    protected final Scanner scanner;

    public Controller() {
        this.scanner = new Scanner(System.in);
    }

    protected synchronized String promptUser(String message) {
        TUI.print(message);
        return promptUser();
    }

    protected synchronized String promptUser() {
        return scanner.nextLine().trim();
    }

    protected InetAddress promptIP() {
        String prompt;
        InetAddress ip;
        while (true) {
            TUI.print("Please enter IP address:");
            prompt = promptUser();
            ip = Misc.checkIP(prompt);
            if (ip != null) break;
            TUI.print("Wrong IP provided");
        }
        return ip;
    }

    protected Integer promptPort() {
        String prompt;
        Integer port;
        while (true) {
            TUI.print("Please enter port of the server:");
            prompt = promptUser();
            port = Misc.checkPort(prompt);
            if (port != null) break;
            TUI.print("Wrong port provided");
        }
        return port;
    }
}
