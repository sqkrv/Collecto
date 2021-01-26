package Collecto.Tests;

import Collecto.Global;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalTest {


    @BeforeEach
    void setUp() {
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
            14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27})
    void isPushValidTrue(int number) {
        assertTrue(Global.isPushValid(number));
    }

    @Test
    void isPushValid() {
        assertFalse(Global.isPushValid(-1));
        assertFalse(Global.isPushValid(28));
    }

    @Test
    void parsePush() {
        assertNull(Global.parsePush("-1"));
        assertNull(Global.parsePush("28"));
        assertNull(Global.parsePush(""));
        assertEquals(15, Global.parsePush("15"));
    }

    @Test
    void checkAddress() throws UnknownHostException {
        assertNull(Global.checkAddress("255.255.255.256"));
        assertNull(Global.checkAddress("lcoalhost"));

        InetAddress address = InetAddress.getByName("8.8.8.8");
        assertEquals(address, Global.checkAddress("8.8.8.8"));
    }

    @Test
    void checkPort() {
        assertNull(Global.checkPort("-1"));
        assertNull(Global.checkPort("port"));
        assertNull(Global.checkPort("10.24"));

        assertEquals(4114, Global.checkPort("4114"));
    }

//    @Test
//    void parseInt() {
//        assertNull(Global.parseInt("num"));
//        assertNull(Global.parseInt("10.24"));
//
//        assertEquals(-1, Global.parseInt("-1"));
//        assertEquals(1024, Global.parseInt("1024"));
//    }

//    @Test
//    void checkProtocolStrings() {
//        assertNotNull(Global.Protocol.class);
//        assertNotNull(Global.Protocol.Commands.class);
//        assertNotNull(Global.Protocol.Misc.class);
//        assertNotNull(Global.Protocol.Extensions.class);
//        assertNotNull(Global.Protocol.Win.class);
//    }
}