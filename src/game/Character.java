
package game;

import java.util.ArrayList;

public class Character extends Creature{

    public Character(int playerId, int creatureId, String creatureName, int hp, int ac, int movement, int damageBonus, int attackBonus, int attacksPerTurn, String backstory, int xPos, int yPos, String imageUrl, ArrayList weapons){
        super(playerId, creatureId, creatureName, hp, ac, movement, damageBonus, attackBonus, attacksPerTurn, backstory, xPos, yPos, imageUrl, weapons);
    }


    public String toString(){
        String res = super.toString();
        return res;
    }
}
