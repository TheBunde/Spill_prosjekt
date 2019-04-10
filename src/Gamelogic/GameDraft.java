/**
 * Unused draft of the how the game would work
 * Later it was used as a draft for the final result
 * @author heleneyj
 */
/*
package Gamelogic;

import game.*;

import java.util.ArrayList;
class Game {
    public void entireGame(){
        // need to get all the data from the database in to the arrays bellow
        Dice dice = new Dice();
        GameMethods method = new GameMethods();
        ArrayList<Creature> players = new ArrayList<>(); // players in the game
        ArrayList<Creature> monsters =  new ArrayList<>(); // list of every monster in the game
        ArrayList<Creature> creatures = new ArrayList<>(); //  all creatures in the game
        ArrayList<String> story = new ArrayList<>(); // the story of the game
        boolean alive = true; // is only false if all the players are dead
        int gameRound = 0; // what level are they on
        while(alive && gameRound < monsters.size()){ // not dead and not won game
            //get the right monster
            story.get(gameRound); // prints out the right story part
            ArrayList<Creature> turn = method.initative(); // gets the initiative turn from randomizer
            for(int i = 0; i < turn.size(); i++){
                System.out.println();
            }
            int playable = players.size(); // players that are in the game
            int dead = 0; // amount of players that are dead
            boolean monsterAlive = true;
            while(dead < playable && monsterAlive == true){ // goes in to a new level
                for(int index = 0; index < turn.size(); index++){ // get the turn for one player
                    Creature playerNow = turn.get(index); // holds info about the current player
                    if(playerNow.getHp() <= 0){
                        break;
                    }
                    */
                    /* if the creature that have this turn is a player */
                    /*
                    if(playerNow instanceof game.Character){
                        int monsterI = 0;
                        for(int i = 0; i < creatures.size(); i++){
                            if(creatures.get(i) instanceof Monster){
                                monsterI = i;
                            }
                        }
                        Creature monster = creatures.get(monsterI);
                        boolean turnOver = false;
                        int hpForMonster = monster.getHp();
                        while(!turnOver){
                            ArrayList<Weapon> weaponsPlayer = new ArrayList<>();
                            Weapon melee = weaponsPlayer.get(0);
                            Weapon range = weaponsPlayer.get(1);
                            for(int i = 0; i < weaponsPlayer.size(); i++){
                                if(weaponsPlayer.get(i).getDescription().equalsIgnoreCase("ranged")){
                                    melee = weaponsPlayer.get(i);
                                }else{
                                    range = weaponsPlayer.get(i);
                                }
                            }
                            int attackCounter = 0;
                            int moveCounter = 0;
                            int realAttackCounter = playerNow.getAttacksPerTurn();
                            if (*//*Move button = pressed &&*//*moveCounter == 0){
                                boolean move = false;
                                while(!move){
                                    move = method.movePlayer(playerNow);
                                }
                                moveCounter++;
                            }
                            if(*//*Attack button = pressed &&*//*attackCounter < realAttackCounter){
                                if(method.nearMonster(playerNow, monster)){
                                    method.attack(playerNow, monster, melee);
                                }else{
                                    method.attack(playerNow, monster, range);
                                }
                                attackCounter++;
                            }
                            if(attackCounter == realAttackCounter && moveCounter == 1){
                                turnOver = true;
                            }
                            if(endTurn button = pressed){
                                  turnOver = true;
                            }
                        }
                        if(hpForMonster <= 0){ //monster has died
                            monsterAlive = false;
                        }
                    }
                    */
                    /* The creature this turn is a monster and this is hardcoded*/
                    /*
                    if(playerNow instanceof Monster){
                        int roll = dice.roll(players.size());
                        int playerHp = players.get(roll).getHp();
                        Creature monster = turn.get(index);
                        //int playerHP = method.monsterMovement(monster);
                        if(playerHp <= 0){ // player has died
                            dead++;
                        }
                    }
                }
            }
            if(dead == playable){ // all the players are dead
                System.out.println(story.get(story.size()-1));
                break;
            }
            gameRound++;
            */
            /*Set new player hp for next level */
            /*
            for(int i = 0; i < turn.size(); i++){
                if(turn.get(i) instanceof Character){
                    int newHp = 0;
                    if(turn.get(i).getCharacter().equalsIgnoreCase("Wizard")){
                        newHp = 22 + gameRound*4;
                    }
                    if(turn.get(i).getCharacter().equalsIgnoreCase("Rouge")){
                        newHp = 23 + gameRound*5;
                    }
                    if(turn.get(i).getCharacter().equalsIgnoreCase("Ranger")){
                        newHp = 32 + gameRound*8;
                    }
                    if(turn.get(i).getCharacter().equalsIgnoreCase("Fighter")){
                        newHp = 36 + gameRound*8;
                    }
                }
            }
        }
        System.out.println(story.get(story.size()-2));
        //set every new value for users
        //reset everything about the characters and monster for another game
        //game over
    }
}
*/

