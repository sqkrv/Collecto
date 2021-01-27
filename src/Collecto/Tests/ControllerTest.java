package Collecto.Tests;

import Collecto.Controller;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private static final String WRONG = "Wrong Data";
    private Controller controller;
    private InputStream input;
    private BufferedReader reader;
    private String answer;
    private String result;

    void setUpController() {
        controller = new Controller() {
            @Override
            public void setScanner() {
                this.scanner = new Scanner(input);
            }
        };
    }

    void setUpControllerReader() {
        controller = new Controller() {
            @Override
            public void setScanner() {
                this.scanner = new Scanner(reader);
            }
        };
    }

    void setUpInput(String newInput) {
        this.input = new ByteArrayInputStream(newInput.getBytes());
    }

    void setUpReader(BufferedReader newReader) {
        this.reader = newReader;
    }

    @Test
    void promptUser() {
        answer = "Data";
        setUpInput(answer);
        setUpController();
        result = controller.promptUser();
        assertEquals(answer, result);
        assertNotEquals(WRONG, result);
    }

    @Test
    void promptUserParam() {
        answer = "More Data";
        setUpInput(answer);
        setUpController();
        result = controller.promptUser("Question");
        assertEquals(answer, result);
        assertNotEquals(WRONG, result);
    }

    @Test
    void promptAddress() throws UnknownHostException {
        InetAddress address = InetAddress.getByName("127.0.0.1");
        InetAddress wrongAddress = InetAddress.getByName("130.89.1.0");
        setUpInput("localhost");
        setUpController();
        InetAddress resultAddress = controller.promptAddress();
        assertEquals(address, resultAddress);
        assertNotEquals(wrongAddress, resultAddress);
    }

    @Test
    void promptWrongAddress() {
        String wrongRight = "wrong \n more wrong";
        setUpReader(new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(wrongRight.getBytes()))));
        setUpControllerReader();
        assertThrows(NoSuchElementException.class, () -> controller.promptAddress());
    }

    @Test
    void promptWrongRightAddress() throws UnknownHostException {
        String wrongRightWrong = "wrong\nlocalhost\n130.89.1.0";
        setUpReader(new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(wrongRightWrong.getBytes()))));
        setUpControllerReader();
        InetAddress address = controller.promptAddress();
        assertEquals(address, InetAddress.getByName("127.0.0.1"));
    }

    @Test
    void promptPort() {
        int port = 4114;
        int wrongPort = 5555;
        setUpInput("4114");
        setUpController();
        int portResult = controller.promptPort();
        assertEquals(port, portResult);
        assertNotEquals(wrongPort, portResult);
    }

    @Test
    void promptWrongPort() {
        String wrongRight = "wrong \n more wrong";
        setUpReader(new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(wrongRight.getBytes()))));
        setUpControllerReader();
        assertThrows(NoSuchElementException.class, () -> controller.promptPort());
    }

    @Test
    void promptWrongRightPort() {
        String wrongRightWrong = "wrong \n 5555 \n 4114";
        setUpReader(new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(wrongRightWrong.getBytes()))));
        setUpControllerReader();
        Integer port = controller.promptPort();
        assertEquals(port, 5555);
    }
}