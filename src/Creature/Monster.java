package Creature;
import java.util.ArrayList;

class Monster extends Creature {

    public Monster(int hp, int ac, int level, String character, int attackTurn, int damageBonus, ArrayList weapon){
        super(hp, ac, level, character, attackTurn, damageBonus, weapon);
    }

    public String toString() {
        return super.toString();
    }
}
