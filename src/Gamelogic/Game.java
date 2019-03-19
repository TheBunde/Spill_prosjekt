package Gamelogic;
import creature.*;
import creature.Character;

import java.util.ArrayList;
class Game {
    public static void main(String[] args){
        Dice dice = new Dice();
        GameMethods method = new GameMethods();
        ArrayList<Creature> players = new ArrayList<>(); // players in the game
        ArrayList<Creature> monsters =  new ArrayList<>(); // list of every monster in the game
        ArrayList<Creature> creatures = new ArrayList<>(); //  all creatures in the game
        ArrayList<String> story = new ArrayList<>();
        boolean alive = true;
        int gameRound = 0;
        while(alive && gameRound < monsters.size()){
            //get the right monster
            story.get(gameRound);
            ArrayList<Creature> turn = method.initative();
            int playable = players.size();
            int dead = 0;
            boolean monsterAlive = true;
            while(dead < playable && monsterAlive == true){
                for(int index = 0; index < turn.size(); index++){
                    Creature playerNow = turn.get(index);
                    //figure out how to get just the i players turn
                    if(turn.get(index).getHp() <= 0){
                        break;
                    }
                    if(playerNow instanceof Character){
                        boolean turnOver = false;
                        int hpForMonster = monsters.get(gameRound).getHp();
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
                            int realAttackCounter = turn.get(index).getAttackTurn();
                            if (/*Move button = pressed &&*/ moveCounter == 0){
                                method.movePlayer();
                                moveCounter++;
                            }
                            if(/*Attack button = pressed &&*/ attackCounter < realAttackCounter){
                                if(method.nearMonster(index, gameRound, turn, monsters)){
                                    method.attack(index, gameRound, turn, monsters, melee);
                                }else{
                                    method.attack(index, gameRound, turn, monsters, range);
                                }
                                attackCounter++;
                            }
                            if(attackCounter == realAttackCounter && moveCounter == 1){
                                turnOver = true;
                            }
                            /* if(endTurn button = pressed){
                                  turnOver = true;
                                }
                             */
                        }
                        if(hpForMonster <= 0){
                            monsterAlive = false;
                        }
                    }
                    if(playerNow instanceof Monster){
                        int roll = dice.roll(players.size());
                        int playerHp = players.get(roll).getHp();
                        Creature monster = turn.get(index);
                        method.monsterMovement(monster);
                        //attack if close
                        //move if not close, then attack
                        if(playerHp <= 0){
                            dead++;
                        }

                        // monster turn
                    }
                }
            }
            if(dead == playable){
                System.out.println(story.get(story.size()-1));
                break;
            }
            gameRound++;
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
        //game over
    }
}
