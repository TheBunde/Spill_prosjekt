package game;
import game.Creature;

import java.util.ArrayList;

public class Monster extends Creature {

    public Monster(int hp, int ac, String characterName, int attacksPerTurn, int damageBonus, int xPos, int yPos, ArrayList weapons, int playerId){
        super(hp, ac, characterName, attacksPerTurn, damageBonus, xPos, yPos, weapons, playerId);
    }

    public String toString() {
        return super.toString();
    }
}
