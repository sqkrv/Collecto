package Collecto.Tests;

import Collecto.HumanPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HumanPlayerTest {

    HumanPlayer player;

    @BeforeEach
    void setUp() {
//        player = new HumanPlayer("Patricia");
    }

    @Test
    void getName() {
        assertEquals(player.getName(), "Patricia");
        assertNotEquals(player.getName(), "Jack Sparrow");
    }
}