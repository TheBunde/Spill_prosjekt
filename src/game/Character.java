
package game;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Character extends Creature{
    private int initialHp = 0;

    public Character(int playerId, int creatureId, String creatureName, int hp, int ac, int movement, int damageBonus, int attackBonus, String backstory, int xPos, int yPos, String imageUrl, ArrayList weapons){
        super(playerId, creatureId, creatureName, hp, ac, movement, damageBonus, attackBonus, backstory, xPos, yPos, imageUrl, weapons);
        this.initialHp = hp;
    }

    public int getInitialHp() {
        return initialHp;
    }

    public String toString(){
        String res = super.toString();
        return res;
    }
}
