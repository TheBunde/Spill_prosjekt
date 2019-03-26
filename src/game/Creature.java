package game;

import java.util.ArrayList;
import Main.*;

public abstract class Creature {
    private int hp;
    private int ac;
    private String creatureName;
    private int attackBonus;
    private int movement;
    private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
    private int attacksPerTurn;
    private int damageBonus;
    private int xPos;
    private int yPos;
    private int playerId;
    private String backStory;


    public Creature(int hp, int ac, String characterName, int attacksPerTurn, int damageBonus, int xPos, int yPos, ArrayList weapons, String backStory, int playerId){
        this.hp = hp;
        this.ac = ac;
        this.creatureName = characterName;
        this.attackBonus = attackBonus;
        this.movement = movement;
        this.attacksPerTurn = attacksPerTurn;
        this.damageBonus = damageBonus;
        this.xPos = xPos;
        this.yPos = yPos;
        this.weapons = weapons;
        this.backStory = backStory;
        this.playerId = playerId;
    }

    public boolean attackCreature(Creature target, int weaponIndex){
        Weapon weapon = this.weapons.get(weaponIndex);
        if (!hitSuccess(target)){
            return false;
        }

        int damage = 0;
        for (int i = 0; i < weapon.getDiceAmount(); i++){
            damage += Dice.roll(weapon.getDamageDice());
        }
        damage += this.damageBonus;
        target.setHp(target.getHp() - damage);
        return true;
    }

    public boolean hitSuccess(Creature target){
        int hit = Dice.roll(20) + this.attackBonus;
        if (hit >= target.getAc()){
            return true;
        }
        return false;
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

    public String getCreatureName() {
        return creatureName;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public int getMovement() {
        return movement;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public int getAttacksPerTurn() {
        return attacksPerTurn;
    }

    public void setAttacksPerTurn(int attacksPerTurn) {
        this.attacksPerTurn = attacksPerTurn;
    }

    public int getDamageBonus() {
        return damageBonus;
    }

    public void setDamageBonus(int damageBonus) {
        this.damageBonus = damageBonus;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setNewPos(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public String toString() {
        String weaponNames = "";
        for(int i = 0; i < this.weapons.size(); i++){
            weaponNames = this.weapons.get(i).getName();
        }
        return "Character: " + this.getCreatureName() + "\nHP: " + this.getHp() + "\nAC: " + this.getAc() + "\nSpeed: " + this.getMovement() +
                "\nWeapon: " + weaponNames + "\nAttack bonus: " + this.getAttackBonus() +"\nAttacks per turn: " + this.getAttacksPerTurn();
    }
}
