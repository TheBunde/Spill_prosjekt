package game;

import java.util.ArrayList;

/**
 * This class inherits from Creature.java.
 * All players in one game will have their own Character to control.
 *
 * @author heleneyj, magnubau, williad
 */
public class Character extends Creature{
    private int initialHp;

    /**
     * A constructor for the class Character
     *
     * @param playerId      id of the player
     * @param creatureId    id of the creature
     * @param creatureName  name of the creature
     * @param hp            health points
     * @param ac            armor class
     * @param movement      movement range
     * @param damageBonus   damage bonus
     * @param attackBonus   attack bonus
     * @param backstory     backstory
     * @param xPos          x-coordinate
     * @param yPos          y-coordinate
     * @param imageUrl      image url
     * @param weapons       ArrayList of weapons
     */
    public Character(int playerId, int creatureId, String creatureName, int hp, int ac, int movement, int damageBonus, int attackBonus, String backstory, int xPos, int yPos, String imageUrl, ArrayList weapons){
        super(playerId, creatureId, creatureName, hp, ac, movement, damageBonus, attackBonus, backstory, xPos, yPos, imageUrl, weapons);
        this.initialHp = hp;
    }

    /**
     * get-method for the standard hp
     * @return initalHp   the standard hp for the character
     */
    public int getInitialHp() {
        return initialHp;
    }

    /**
     * toString method that returns information
     * @return res     a string of all information
     */
    public String toString(){
        String res = super.toString();
        return res;
    }
}
