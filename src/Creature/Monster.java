package creature;
import java.util.ArrayList;

public class Monster extends Creature {

    public Monster(int hp, int ac, int level, String character, int attackTurn, int damageBonus, int xCordinate, int yCordinate, ArrayList weapon){
        super( hp, ac, level, character, attackTurn, damageBonus, xCordinate, yCordinate, weapon);
    }

    public String toString() {
        return super.toString();
    }
}
