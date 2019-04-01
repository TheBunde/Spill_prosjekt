package game;

import java.util.Random;
public class Dice {
    public static int roll(int dice, int amount){
        int number = 0;
        Random random = new Random();
        for(int i = 0; i < amount; i++ ) {
            number += random.nextInt(dice) + 1;
        }
        return number;
    }
}