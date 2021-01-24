package Collecto.Tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ClientMaliciousServerTest {

    @BeforeAll
    static void yes() throws IOException {
        ServerSocket serverSocket = new ServerSocket(4224, 0, InetAddress.getLocalHost());
        Socket socket = serverSocket.accept();
    }

    @BeforeEach
    void setUp() {
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
    void printBoard() {
    }

    @Test
    void printLogs() {
    }

    @Test
    void disconnect() {
    }

    @Test
    void printHelp() {
    }

    @Test
    void run() {
    }

    @Test
    void main() {
    }
}