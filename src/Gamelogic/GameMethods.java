package Gamelogic;

import creature.Creature;
import creature.Monster;

import java.util.ArrayList;
import java.lang.Math.*;

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






    public void monsterMovement(Monster monster){

        ArrayList<Creature> creatures = new ArrayList<>();
        boolean first = true;
        Creature target = null;
        int xDistance = monster.getMovement();
        int yDistance = monster.getMovement();

        for (Creature i : creatures) {
            if ((i.getxcordinatte() >= (monster.getxcordinate() - monster.getMovement() - 1)) && (i.getxcordinate() <= (monster.getxcordinate() + monster.getMovement() + 1))
                    && (i.getycordinatte() >= (monster.getycordinate() - monster.getMovement() - 1)) && (i.getycordinate() <= (monster.getycordinate() + monster.getMovement() + 1)) && i != monster) {
                if (Math.abs(i.getycordinate() - monster.getycordinate()) <= xDistance && Math.abs(i.getycordinate() - monster.getycordinate()) <= yDistance) {
                    xDistance = i.getxcordinate() - monster.getxcordinate();
                    yDistance = i.getycordinate() - monster.getycordinate();
                    target = i;
                    if(xDistance < 0 && Math.abs(xDistance) != monster.getMovement()){xDistance++;}
                    else if(xDistance > 0 && Math.abs(xDistance) != monster.getMovement()){xDistance--;}
                    if(yDistance < 0 && Math.abs(yDistance) != monster.getMovement()){yDistance++;}
                    else if(yDistance > 0 && Math.abs(xDistance) != monster.getMovement()){yDistance++;}
                }
            }
        }
        if(target == null){
            for(Creature i: creatures){
                if(Math.abs(i.getxcordinate()- monster.getxcordinate()) < xDistance && Math.abs(i.getycordinate() - monster.getycordinate()) < yDistance || first){
                    if(i.getxcordinate() > monster.getxcordinate() && (i.getxcordinate() - monster.getxcordinate()) > monster.getMovement()){
                        xDistance = monster.getMovement();
                    }
                    else if(i.getxcordinate() > monster.getxcordinate()){
                        xDistance = i.getxcordinate() - monster.getxcordinate();
                    }
                    if(i.getxcordinate() < monster.getxcordinate() && (monster.getxcordinate() - i.getxcordinate()) > monster.getMovement()){
                        yDistance = -monster.getMovement();
                    }
                    else if(i.getxcordinate() < monster.getxcordinate()){
                        yDistance = -(monster.getycordinate() - i.getycordinate);
                    }
                    first = false;
                }
            }
            monster.setxcordinate(monster.getxcordinate + xDistance);
            monster.setycordinate(monster.getycordinate + yDistance);
        }
        else if(target != null){
            monster.setxcordinate(monster.getxcordinate + xDistance);
            monster.setycordinate(monster.getycordinate + yDistance);
            monster.monsterAttack(target);
        }
    }
}

