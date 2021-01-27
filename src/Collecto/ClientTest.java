package Collecto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    private static Client client;
    private static Server server;

    private OutputStream serverOut;
    private InputStream serverIn;

    private Semaphore lock = new Semaphore(0);

//    @BeforeAll
//    static void startServer() {
//        server = new Server(4200);
//        new Thread(server);
//    }

    @BeforeEach
    void setUp() throws IOException {
        client = new Client(new Socket(InetAddress.getLocalHost(), 4200));
    }

    @Test
    void main() {
        System.out.println("asd");
    }

    @Test
    void handleCommandIn() {

    }

    @Test
    void handleMove() {
    }

    @Test
    void chooseAI() {
    }

    @Test
    void chooseDifficulty() {
    }

    @Test
    void hint() {
    }

    @Test
    void sendMessage() {
    }

    @Test
    void status() {
    }

    @Test
    void disconnect() {
    }

    @Test
    void printBoard() {
    }

    @Test
    void printLogs() {
    }

    @Test
    void printHelp() {
    }

    @Test
    void inGame() {
    }

    @Test
    void run() {
    }
}