
package game;

import java.util.ArrayList;

public class Character extends Creature{

    private String backStory;

    public Character(int hp, int ac, int level, String characterName, int attacksPerTurn, int damageBonus, int xPos, int yPos, ArrayList weapons, String backstory){
        super(hp, ac, characterName, attacksPerTurn, damageBonus, xPos, yPos, weapons);
        this.backStory = backstory;
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
