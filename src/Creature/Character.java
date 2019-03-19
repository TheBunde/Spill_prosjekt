package Creature;

import java.util.ArrayList;

class Character extends Creature{

    private String backstory;

    public Character(int hp, int ac, int level, String character, String name, String backstory, ArrayList weapon){
        super(hp, ac, level, character, weapon);
        this.backstory = backstory;
    }

    public String getBackstory() {
        return backstory;
    }

    public String toString(){
        String res = super.toString();
        res += "\nBackstory:"+ backstory;
        return res;
    }
}
