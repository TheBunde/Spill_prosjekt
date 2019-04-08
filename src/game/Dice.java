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

    public static void main(String[] args) {
        double sum = 0;
        double iter = 100000;
        for (int i = 0; i < iter; i++){
            int roll = Dice.roll(6, 3);
            System.out.println(roll);
            sum += roll;
        }
        System.out.println(sum/iter);
    }
}