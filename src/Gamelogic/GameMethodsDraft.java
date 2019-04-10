/**
 * Unused first draft of methods
 * Later changed to fit the game
 * @ author magnubau & heleneyj
 */
/*
package Gamelogic;

import game.Creature;
import game.Weapon;
import java.util.ArrayList;

public class GameMethods {
    private Dice dice;

    public GameMethods() {

    }
*/

    /**
    * Method that randomizes turns for the creatures in the game
    * @return turn     ArrayList with sorted creature objects
    */
    /*
    public ArrayList initative() {
        ArrayList<Creature> creatures = new ArrayList<>();  // list with all creatures in the game
        int[][] initiativeOrder = new int[creatures.size()][creatures.size()]; // will save random nr and place in array
        ArrayList<Creature> turn = new ArrayList<>(); // new array that will be returned
        */
        /*
         * method that sets random values
         * then sorts them in order from highest to lowest
         */
        /*
        for (int i = 0; i < creatures.size(); i++) {
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
        for (int i = 0; i < creatures.size(); i++) { // converts from list to array
            int playerNr = initiativeOrder[0][i];
            Creature getFrom = creatures.get(playerNr);
            turn.add(getFrom);
        }
        return turn;
    }
    */

    /**
    * Method that let a player try to attack the monster
    * @param playerNow     a Creature parameter that is the player that attacks
    * @param monster       a Creature parameter of the monster that are attacked
    * @param weapon        The weapon that is used to attack
    */
    /*
    public void attack(Creature playerNow, Creature monster, Weapon weapon) {
        int roll = dice.roll(20) + playerNow.getAttackBonus();
        int acMonster = monster.getAc();
        if (roll >= acMonster) { // the player hits
            int damage = 0;
            for (int i = 0; i < weapon.getDiceAmount(); i++) {
                damage = damage + dice.roll(weapon.getDamageDice());
            }
            int monsterHP = monster.getHp() - damage;
            monster.setHp(monsterHP);
            //sending new monster hp to database 
            System.out.println("Hit");
        } else { // the player misses
            System.out.println("Miss");
        }
    }
    */

    /**
    * Method to move players in the grid
    * @param playerNow      a Creature parameter that is the player that attacks
    * @return boolean       returns true if move was legal. Return false if move was illegal
    */
    /*
    public boolean movePlayer(Creature playerNow) {
    */
        /* sets variables with valid position values */
    /*
        int xCoordinate = playerNow.getxPos();
        int yCoordinate = playerNow.getyPos();
        int XMax = xCoordinate + playerNow.getMovement();
        int XMix = xCoordinate - playerNow.getMovement();
        int YMax = yCoordinate + playerNow.getMovement();
        int YMix = yCoordinate - playerNow.getMovement();
        int wantedX = 0;
        int wantedY = 0;
        boolean X = false;
        boolean Y = false;
        if (wantedX <= XMax && wantedX >= XMix) {  // if x value inside range
            X = true;
        }
        if (wantedY <= YMax && wantedY >= YMix) {  // if y value inside range
            Y = true;
        }
        if (X && Y) {   // if both x and y inside range
            playerNow.setNewPos(wantedX, wantedY);
            System.out.println("You moved");
            //sending new x y to database
            return true;
        } else {    // if outside range
            System.out.println("Out of range. Try again");
            return false;
        }
    }
    */

    /**
     * Method that checks if player are stanging close to a monster
     * @param playerNow     a Creature parameter that is the player that attacks
     * @param monster       a Creature parameter of the monster that are attacked
     * @return near         near is true if a monster is standing next to the palyer
    */
    /*
    public boolean nearMonster(Creature playerNow, Creature monster) {
    */
    /* Sets variable values as positions around the player */
    /*
        boolean near = false;
        int xPos = playerNow.getxPos();
        int yPos = playerNow.getxPos();
        int maxX = xPos + 1;
        int maxY = yPos + 1;
        int minX = xPos - 1;
        int minY = yPos - 1;
        int XForMonster = monster.getxPos();
        int YForMonster = monster.getyPos();
        boolean X = false;
        boolean Y = false;
        if (minX == XForMonster || xPos == XForMonster || maxX == XForMonster) { // if x value is close
            X = true;
        }
        if (minY == YForMonster || yPos == YForMonster || maxY == YForMonster) { // if y value is close
            Y = true;
        }
        if (X && Y) { // if x and y value is close
            near = true;
        }
        return near;
    }
    */

    /**
     * Method that are the code of how a monster attacks
     * @param target       the player the monster attacks
     * @param monster      the monster that attacks
     * @return targetHp    the new hp the target has after the attack
     */
    /*
    public int monsterAttack(Creature target, Creature monster) {
        ArrayList<Weapon> monsterWeapon = new ArrayList<>();
        int roll = dice.roll(20) + monster.getAttackBonus();
        int targetAC = target.getAc();
        int targetHP = target.getHp();
        if (roll >= targetAC) { // if the monster hits
            int weaponRoll = dice.roll(monsterWeapon.size());
            int damage = 0;
            for (int i = 0; i < monsterWeapon.get(weaponRoll).getDiceAmount(); i++) {
                damage = damage + dice.roll(monsterWeapon.get(weaponRoll).getDamageDice());
            }
            targetHP = target.getHp() - damage;
            target.setHp(targetHP);
            //sending new target hp to database
            System.out.println("Hit");
        } else { // if the monster miss
            System.out.println("Miss");
        }
        return targetHP;
    }
}
*/


