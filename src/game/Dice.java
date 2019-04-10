/**
 * Simulates the throwing of dice in a game
 * @author heleneyj
 */
package game;

import java.util.Random;
public class Dice {
    /**
     * Get a random number that simulates dice roll
     * @ param dice     the amount of sides the dice have
     * @ param amount   times the dice are thrown / number generated
     * @ return         random number generated / numbers rolled by dice
     */
    public static int roll(int dice, int amount){
        int number = 0;
        Random random = new Random();
        for(int i = 0; i < amount; i++ ) {
            number += random.nextInt(dice) + 1;
        }
        return number;
    }
}