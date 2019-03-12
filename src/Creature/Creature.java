import java.util.ArrayList;

abstract class Creature {
    private int hp;
    private int ac;
    private int level;
    private String character;
    private ArrayList<Weapon> weapon = new ArrayList<Weapon>();

    public Creature(int hp, int ac, int level, String character, ArrayList weapon){
        this.hp = hp;
        this.ac = ac;
        this.level = level;
        this.character = character;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAc() {
        return ac;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<Weapon> getWeapon() {
        return weapon;
    }

    public String toString() {
        String weapons = "";
        for(int i = 0; i < weapon.size(); i++){
            weapons = weapon.get(i).getName();
        }
        return "HP: " + getHp() + "\nAC: " + getAc() + "\nLevel:" + getLevel() + "\nWeapon: " + weapons;
    }
}
