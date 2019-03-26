
package game;

import java.util.ArrayList;

public class Character extends Creature{

    private String backStory;


    public Character(int hp, int ac, String creatureName, int attacksPerTurn, int damageBonus, int xPos, int yPos, ArrayList weapons, String backStory, int playerId, int creatureId){
        super(hp, ac, creatureName, attacksPerTurn, damageBonus, xPos, yPos, weapons, backStory, playerId, creatureId);
    }


    public String getBackstory() {
        return this.backStory;
    }

    public String toString(){
        String res = super.toString();
        res += "\nBackstory:"+ this.getBackstory();
        return res;
    }
}
