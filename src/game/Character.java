
package game;

import java.util.ArrayList;

public class Character extends Creature{

    public Character(int hp, int ac, String creatureName, int attacksPerTurn, int damageBonus, int xPos, int yPos, ArrayList weapons, String backstory, int playerId, int creatureId){
        super(hp, ac, creatureName, attacksPerTurn, damageBonus, xPos, yPos, weapons, backstory, playerId, creatureId);
    }


    public String toString(){
        String res = super.toString();
        return res;
    }
}
