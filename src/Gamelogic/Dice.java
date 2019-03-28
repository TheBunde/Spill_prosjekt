package Gamelogic;

import java.util.Random;
class Dice {
    private int dice;
    private int amount;

    public Dice(){
        this.dice = dice;
    }

    public int roll(int dice, int amount){
        int number = 0;
        Random random = new Random();
        for(int i = 0; i < amount; i++ ) {
            number += random.nextInt(dice) + 1;
        }
        return number;
    }
}