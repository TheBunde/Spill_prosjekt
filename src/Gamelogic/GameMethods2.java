/*
package Gamelogic;

import creature.Creature;

import java.util.ArrayList;

public class GameMethods2 {
    private Dice dice;


    public ArrayList initiativeSort(ArrayList initiative) {

        int[][] initiativeOrder = new int[initiative.size()][initiative.size()];
        ArrayList<Integer> sort = new ArrayList<>();
        for (int i = 0; i < initiative.size(); i++) {
            int roll = dice.roll(20);
            initiativeOrder[0][i] = i;
            initiativeOrder[1][i] = roll;
            //print witch user got witch value?
        }
        for (int start = 0; start < initiativeOrder[1].length; start++) {
            int highest = start;
            for (int j = start; j < initiativeOrder[1].length; j++) {
                if (initiativeOrder[1][j] > initiativeOrder[1][highest]) {
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
        for (int i = 0; i < initiative.size(); i++) {
            int playerNr = initiativeOrder[0][i];

            sort.add(playerNr);
        }
        return sort;
    }
}*/
