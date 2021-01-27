package Collecto.Tests;

import Collecto.Client;
import Collecto.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class AIPlaysTest {
    private static void simulateInput(String message) {
        ByteArrayInputStream in = new ByteArrayInputStream(message.getBytes());
        System.setIn(in);
        System.setIn(System.in);
    }

//    @BeforeAll
//    static void setUpServer() throws IOException {
//        Server.main(new String[]{"1441"});
//
//        Client.main(new String[]{"localhost", "1441"});
//        simulateInput("client1");
//
//        Client.main(new String[]{"localhost", "1441"});
//        simulateInput("client2");
//
//
//    }

    public static void main(String[] args) throws IOException{
        Server.main(new String[]{"1441"});

//        Client client1 = new Client(new Socket(InetAddress.getLocalHost(), 1441));
//        Thread inputHandler1 = new Thread(client1);
//        inputHandler1.start();
//        client1.


//        Client.main(new String[]{"localhost", "1441"});
//        System.out.println("==========================");
//        simulateInput("client1");
//
//        Client.main(new String[]{"localhost", "1441"});
//        simulateInput("client2");
    }
}
