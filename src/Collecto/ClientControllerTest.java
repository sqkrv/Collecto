package Collecto;

import Collecto.Tests.SocketFactory;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientControllerTest {

    private static Socket[] sockets;
    private ClientController controller;
    private Client client;
    private Socket clientSocket;
    private BufferedReader in;

    @BeforeAll
    static void setUp() {
        sockets = new SocketFactory(6666).newSockets();
    }

    @BeforeEach
    void before() throws IOException {
        clientSocket = sockets[0];
        Socket serverSocket = sockets[1];
        client = new Client(clientSocket);
        in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
    }

    void setUpController(String input) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(input.getBytes())));
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream("input".getBytes())));
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
    @DisplayName("handleCommandTests")
    class HandleCommandTests {

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
                    game = new Game("", "");
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
                    game = new Game("", "");
                }
            };
            client.handleNewGame(null);
            setUpController("HINT");
            controller.start();
            assertEquals(in.readLine(), "Hinted");
        }

        @Test
        void hintTestNotInGame() {
            setUpController("HINT");
            controller.start();
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
                    game = new Game("", "");
                }
            };
            client.handleNewGame(null);
            setUpController("MOVE");
            controller.start();
            assertEquals(in.readLine(), "Moved");
        }

        @Test
        void moveTestNotInGame() {
            setUpController("MOVE");
            controller.start();
        }

        @Test
        void wrongCommandInGame() {
            client = new Client(clientSocket) {
                @Override
                public void handleNewGame(String[] params) {
                    game = new Game("", "");
                }
            };
            client.handleNewGame(null);
            setUpController("WRONG COMMAND");
            controller.start();
        }

        @Nested
        @DisplayName("chooseAITest")
        class ChoosingAITest {
            @Test
            void chooseAITest() throws IOException {
                client = new Client(clientSocket) {
                    @Override
                    public void chooseAI(String[] params) {
                        sendMessage(params[0]);
                    }
                };
                client.choosingAI = true;
                setUpController("CHOSEN");
                controller.start();
                assertEquals(in.readLine(), "CHOSEN");
            }

            @Test
            void useAITest() throws IOException {
                client = new Client(clientSocket) {
                    @Override
                    public void chooseDifficulty(String[] params) {
                        sendMessage(params[0]);
                    }
                };
                client.choosingAI = true;
                client.useAI = true;
                setUpController("USEDAI");
                controller.start();
                assertEquals(in.readLine(), "USEDAI");
            }
        }
    }
}