package Collecto;

import Collecto.Client;
import Collecto.ClientController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.net.Socket;

import static Collecto.Global.DELIMITER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    private ClientController controller;
    private String answer;
    private String result;
    Socket clientSocket;
    Socket serverSocket;

    public class SocketFactory implements Runnable {

        private Socket socket1 = null;
        private Socket socket2 = null;
        private ServerSocket serverSocket;
        private int port;

        public SocketFactory(int port) {
            this.port = port;
            setServerSocket();
        }
        private void setServerSocket() {
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException ignored) {

            }
        }

        @Override
        public void run() {
            while (socket1 == null) {
                try {
                    socket1 = new Socket(InetAddress.getByName("localhost"), port);
                } catch (IOException ignored) {
                }
            }
        }

        public Socket[] newSockets() {
            try {
                Thread thread = new Thread(this);
                thread.start();
                socket2 = serverSocket.accept();
                thread.join();
            } catch (IOException | InterruptedException ignored) {
            }
            return new Socket[]{socket1, socket2};
        }
    }

    SocketFactory factory = new SocketFactory(6666);

    @BeforeEach
    void before() {
        Socket[] sockets = factory.newSockets();
        clientSocket = sockets[0];
        serverSocket = sockets[1];
    }

    void setUpReader(BufferedReader reader, Client client) {
        this.controller = new ClientController(client) {
            @Override
            public void setScanner() {
                this.scanner = new Scanner(reader);
            }
            @Override
            public void start() {
                handleCommand(scanner.nextLine());
            }
        };

    }

    @Test
    void start() throws IOException {
        Client client = new Client(clientSocket);
        BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        setUpReader(new BufferedReader(new InputStreamReader(new ByteArrayInputStream("LIST\n".getBytes()))), client);
        controller.start();
        assertEquals(in.readLine(), "LIST");
    }
}