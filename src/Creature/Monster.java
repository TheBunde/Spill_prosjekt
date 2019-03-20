package Creature;
import java.util.ArrayList;

class Monster extends Creature{

    public Monster(int hp, int ac, int level, String character, ArrayList weapon){
        super(hp, ac, level, character, weapon);
    }

    public String toString() {
        return super.toString();
    }
}
