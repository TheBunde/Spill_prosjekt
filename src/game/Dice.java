package game;

import java.util.Random;
class Dice {
    public static int roll(int dice){
        Random random = new Random();
        int number = random.nextInt(dice) + 1;
        return number;
    }
}