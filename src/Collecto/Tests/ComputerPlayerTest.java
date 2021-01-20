package Collecto.Tests;

import Collecto.ComputerPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class ComputerPlayerTest {
    ComputerPlayer player;

    @BeforeEach
    void setUp() {
        player = new ComputerPlayer();
    }

    @Test
    void getLevel() {
        assertEquals(player.getLevel(), 1);
        assertThrows(AssertionError.class, () -> new ComputerPlayer(4));
        player = new ComputerPlayer(3);
        assertEquals(player.getLevel(), 3);
   }
}