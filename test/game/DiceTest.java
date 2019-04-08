package game;

import org.junit.jupiter.api.*;
import game.*;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @Test
    void roll() {
        int diceSize = 10;
        int diceAmount = 2;
        int max = diceSize * diceAmount;
        for (int i = 0; i < 100; i++) {
            int roll = Dice.roll(diceSize, diceAmount);
            assertTrue(roll >= diceAmount, "Dice roll too low");
            assertTrue(roll <= max, "Dice roll too high");
        }
    }
}