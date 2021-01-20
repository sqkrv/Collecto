package Collecto;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private Player player;
    private static final String USAGE
            = "usage: java src.Collecto.Client <name> <address> <port>";

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out; // TODO again, Buffered or Print

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    private static InetAddress checkIP(String ip) {
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: host " + ip + " unknown");
            System.exit(0);
        }
        return null;
    }

    private static Integer checkPort(String port) {
        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: port " + port
                    + " is not an integer");
            System.exit(0);
        }
        return null;
    }

//    private static void clearConnection() {
//        socket = null;
//        in = null;
//        out = null;
//    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println(USAGE);
            System.exit(0);
        }

        String name = args[0];
        int port;
        InetAddress address;
        Socket sock = null;

        address = checkIP(args[1]);
        port = checkPort(args[2]);

        try {
            System.out.println("Connecting to " + address + ":" + port);
            sock = new Socket(address, port);
            System.out.println(Misc.logTime()+"Connected");
        } catch (IOException e) {
            System.out.println("suck");
            e.printStackTrace();
        }

        assert sock != null;
        Client client = new Client(sock);

        client.start();
    }

    private void start() throws IOException {
//        try {
//            clearConnection();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                String message;
                while ((message = scanner.nextLine()) != null) {
                    System.out.println(message);
                    out.write(message);
                    out.newLine();
                    out.flush();
                }
            }
//        } catch(IOException e) {
//            System.out.println("ok");
//        }
    }

//    public void start() {
//        boolean connecting = true;
//        while (connecting) {
//           try {
//              connect();
//              try {
//        }
//    }
//
//    public void connect() {
//        clearConnection();
//            while (ssoccket == null) {
//                try {
//    }

}
