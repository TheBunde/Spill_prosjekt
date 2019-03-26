package game;
import game.Creature;

import java.util.ArrayList;

public class Monster extends Creature {

    public Monster(int hp, int ac, String creatureName, int attacksPerTurn, int damageBonus, int xPos, int yPos, ArrayList weapons, String backStory, int playerId){
        super(hp, ac, creatureName, attacksPerTurn, damageBonus, xPos, yPos, weapons, backStory, playerId);
    }

    public String toString() {
        return super.toString();
    }
}
