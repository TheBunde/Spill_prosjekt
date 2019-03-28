package game;
import game.Creature;

import java.util.ArrayList;

public class Monster extends Creature {

    public Monster(int playerId, int creatureId, String creatureName, int hp, int ac, int movement, int damageBonus, int attackBonus, int attacksPerTurn, String backstory, int xPos, int yPos, ArrayList weapons){
        super(playerId, creatureId, creatureName, hp, ac, movement, damageBonus, attackBonus, attacksPerTurn, backstory, xPos, yPos, weapons);
    }

    public String toString() {
        return super.toString();
    }
}
