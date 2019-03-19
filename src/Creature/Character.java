package creature;
import java.util.ArrayList;

public class Character extends Creature{

    private String backstory;

    public Character(int hp, int ac, int level, String character, int attackTurn, int damageBonus, ArrayList weapon, String backstory){
        super(hp, ac, level, character, attackTurn, damageBonus, weapon);
        this.backstory = backstory;
    }

    public String getBackstory() {
        return backstory;
    }

    public String toString(){
        String res = super.toString();
        res += "\nBackstory:"+ getBackstory();
        return res;
    }
}
