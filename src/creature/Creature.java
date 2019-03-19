
package creature;

import java.util.ArrayList;

public abstract class Creature {
    private int hp;
    private int ac;
    private int level;
    private String character;
    private int attackBonus;
    private int movement;
    private ArrayList<Weapon> weapon = new ArrayList<Weapon>();
    private int attackTurn; // attacks per turn
    private int damageBonus;
    private int xCordinate;
    private int yCordinate;


    public Creature(int hp, int ac, int level, String character, int attackTurn, int damageBonus, int xCordinate, int yCordinate, ArrayList weapon){
        this.hp = hp;
        this.ac = ac;
        this.level = level;
        this.character = character;
        this.attackBonus = attackBonus;
        this.movement = movement;
        this.attackTurn = attackTurn;
        this.damageBonus = damageBonus;
        this.xCordinate = xCordinate;
        this.yCordinate = yCordinate;
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

    public String getCharacter() {
        return character;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(int attackBonus) {
        this.attackBonus = attackBonus;
    }

    public int getMovement(){
        return movement;
    }

    public int getAttackTurn() {
        return attackTurn;
    }

    public int getDamageBonus(){
        return damageBonus;
    }

    public int getxCordinate() {
        return xCordinate;
    }

    public void setxCordinate(int xCordinate) {
        this.xCordinate = xCordinate;
    }

    public int getyCordinate() {
        return yCordinate;
    }

    public void setyCordinate(int yCordinate) {
        this.yCordinate = yCordinate;
    }

    public String toString() {
        String weapons = "";
        for(int i = 0; i < weapon.size(); i++){
            weapons = weapon.get(i).getName();
        }
        return "Character: " + getCharacter() + "\nHP: " + getHp() + "\nAC: " + getAc() + "\nSpeed: " + getMovement() +
                "\nLevel:" + getLevel() + "\nWeapon: " + weapons + "\nAttack bonus: " + getAttackBonus() +"\nAttacks per turn: " + getAttackTurn();
    }
}
