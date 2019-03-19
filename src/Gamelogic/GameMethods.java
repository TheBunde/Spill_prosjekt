package Gamelogic;

import creature.Creature;

import java.util.ArrayList;

public class GameMethods {
    private Dice dice;

    public GameMethods(){

    }

    public ArrayList initative(){
        ArrayList<Creature> creatures = new ArrayList<>();
        int[][] initiativeOrder = new int[creatures.size()][creatures.size()];
        ArrayList<Creature> turn = new ArrayList<>();
        for(int i = 0; i < creatures.size(); i++){
            int roll = dice.roll(20);
            initiativeOrder[0][i] = i;
            initiativeOrder[1][i] = roll;
            //print witch user got witch value?
        }
        for(int start = 0; start < initiativeOrder[1].length; start++){
            int highest = start;
            for(int j = start; j < initiativeOrder[1].length; j++){
                if(initiativeOrder[1][j] > initiativeOrder[1][highest]){
                    highest = j;
                }
            }
            int helperIndex = initiativeOrder[0][highest];
            int helperRoll = initiativeOrder[1][highest];
            initiativeOrder[0][highest] = initiativeOrder[0][start];
            initiativeOrder[1][highest] = initiativeOrder[1][start];
            initiativeOrder[0][start] = helperIndex;
            initiativeOrder[1][start] = helperRoll;
        }
        for(int i = 0; i < creatures.size(); i++){
            int playerNr = initiativeOrder[0][i];
            Creature getFrom = creatures.get(playerNr);
            turn.add(getFrom);
        }
        return turn;
    }

    public void rangedAttack(){}


}







