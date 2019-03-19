package Gamelogic;

import java.util.Random;
class Dice {
    private int dice;

    public Dice(){
        this.dice = dice;
    }

    public int roll(int dice){
        Random random = new Random();
        int number = random.nextInt(dice) + 1;
        return number;
    }
}