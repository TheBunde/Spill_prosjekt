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
            boolean monster = true;
            while(dead < playable && monster == true){
                for(int index = 0; index < turn.size(); index++){
                    Creature playerNow = turn.get(index);
                    //figure out how to get just the i players turn
                    if(turn.get(index).getHp() <= 0){
                        break;
                    }
                    if(playerNow instanceof Character){
                        //player turn
                    }
                    if(playerNow instanceof Monster){
                        // monster turn
                    }
                }
            }
            if(dead == playable){
                System.out.println(story.get(story.size()-1));
                break;
            }
            gameRound++;
            //set new HP and everything
        }
        System.out.println(story.get(story.size()-2));
        //set every new value for users
        //game over
    }
}
