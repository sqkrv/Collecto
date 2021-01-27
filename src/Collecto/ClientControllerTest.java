package Collecto;

import Collecto.Tests.SocketFactory;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ClientControllerTest {

    private ClientController controller;
    private Client client;
    private Socket clientSocket;
    private BufferedReader in;
    private static Socket[] sockets;

    @BeforeAll
    static void setUp() {
        sockets = new SocketFactory(6666).newSockets();
    }

    @BeforeEach
    void before() throws IOException {
        clientSocket = sockets[0];
        Socket serverSocket = sockets[1];
        client = new Client(clientSocket);
//        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
    }

    void setUpController(String input) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
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
    void start() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("input".getBytes())));
        controller = new ClientController(client) {
            @Override
            public void setScanner() {
                this.scanner = new Scanner(reader);
            }
        };
        assertThrows(NoSuchElementException.class, () -> controller.start());
    }

    @Test
    void startOverride() throws IOException {
        setUpController("LIST");
        controller.start();
        assertEquals(in.readLine(), "LIST");
    }

    @Test
    void startOverrideWrong() {
        setUpController("WRONG COMMAND");
        controller.start();
    }

    @Nested()
    @DisplayName("handleMove tests")
    class handleMoveTests {

        @Test
        void helpTest() throws IOException {
            client = new Client(clientSocket) {
                @Override
                public void printHelp() {
                    sendMessage("Helped");
                }
            };
            setUpController("HELP");
            controller.start();
            assertEquals(in.readLine(), "Helped");
        }

        @Test
        void queueTestInGame() {
            client = new Client(clientSocket) {
                @Override
                public void handleNewGame(String[] params) {
                    game = new Game("","");
                }
            };
            client.handleNewGame(null);
            setUpController("QUEUE");
            controller.start();
        }

        @Test
        void queueTestNotInGame() throws IOException {
            setUpController("QUEUE");
            controller.start();
            assertEquals(in.readLine(), "QUEUE");
        }

        @Test
        void listTest() throws IOException {
            setUpController("LIST");
            controller.start();
            assertEquals(in.readLine(), "LIST");
        }

        @Test
        void logsTest() throws IOException {
            client = new Client(clientSocket) {
                @Override
                public void printLogs() {
                    sendMessage("Logged");
                }
            };
            setUpController("LOGS");
            controller.start();
            assertEquals(in.readLine(), "Logged");
        }

        @Test
        void boardTest() throws IOException {
            client = new Client(clientSocket) {
                @Override
                public void printBoard() {
                    sendMessage("Board printed");
                }
            };
            setUpController("BOARD");
            controller.start();
            assertEquals(in.readLine(), "Board printed");
        }

        @Test
        void statusTest() throws IOException {
            client = new Client(clientSocket) {
                @Override
                public void status() {
                    sendMessage("Status printed");
                }
            };
            setUpController("STATUS");
            controller.start();
            assertEquals(in.readLine(), "Status printed");
        }

        @Test
        void disconnectTest() throws IOException {
            client = new Client(clientSocket) {
                @Override
                public void disconnect() {
                    sendMessage("Disconnected");
                }
            };
            setUpController("DISCONNECT");
            controller.start();
            assertEquals(in.readLine(), "Disconnected");
        }

        @Test
        void hintTest() throws IOException {
            client = new Client(clientSocket) {
                @Override
                public void hint() {
                    sendMessage("Hinted");

                    }
                @Override
                public void handleNewGame(String[] params) {
                    game = new Game("","");
                }
            };
            client.handleNewGame(null);
            setUpController("HINT");
            controller.start();
            assertEquals(in.readLine(), "Hinted");
        }

        @Test
        void moveTest() throws IOException {
            client = new Client(clientSocket) {
                @Override
                public void handleMove(String[] params) {
                    sendMessage("Moved");

                }
                @Override
                public void handleNewGame(String[] params) {
                    game = new Game("","");
                }
            };
            client.handleNewGame(null);
            setUpController("MOVE");
            controller.start();
            assertEquals(in.readLine(), "Moved");
        }
    }
}