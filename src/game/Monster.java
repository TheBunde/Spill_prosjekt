package game;
import game.Creature;

import java.util.ArrayList;

public class Monster extends Creature {

    public Monster(int hp, int ac, String creatureName, int attacksPerTurn, int damageBonus, int xPos, int yPos, ArrayList weapons, String backstory, int playerId, int creatureId){
        super(hp, ac, creatureName, attacksPerTurn, damageBonus, xPos, yPos, weapons, backstory, playerId, creatureId);
    }

    public String toString() {
        return super.toString();
    }

    public boolean monsterAction (ArrayList<Creature> players){


        ArrayList<Creature> creatures = players;
        boolean first = true;
        Creature target = null;
        int xDistance = this.getMovement();
        int yDistance = this.getMovement();

        for (Creature i : creatures) {
            if ((i.getxPos() >= (this.getxPos() - this.getMovement() - 1)) && (i.getxPos() <= (this.getxPos() + this.getMovement() + 1))
                    && (i.getyPos() >= (this.getyPos() - this.getMovement() - 1)) && (i.getyPos() <= (this.getyPos() + this.getMovement() + 1)) && i != this) {
                if (Math.abs(i.getxPos() - this.getxPos()) <= xDistance && Math.abs(i.getyPos() - this.getyPos()) <= yDistance) {
                    xDistance = i.getxPos() - this.getxPos();
                    yDistance = i.getyPos() - this.getyPos();
                    target = i;
                    if (xDistance < 0 && Math.abs(xDistance) != this.getMovement()) {
                        xDistance++;
                    } else if (xDistance > 0 && Math.abs(xDistance) != this.getMovement()) {
                        xDistance--;
                    }
                    if (yDistance < 0 && Math.abs(yDistance) != this.getMovement()) {
                        yDistance++;
                    } else if (yDistance > 0 && Math.abs(xDistance) != this.getMovement()) {
                        yDistance++;
                    }
                }
            }
        }
        if (target == null) {
            for (Creature i : creatures) {
                if (Math.abs(i.getxPos() - this.getxPos()) < xDistance && Math.abs(i.getyPos() - this.getyPos()) < yDistance || first) {
                    if (i.getxPos() > this.getxPos() && (i.getxPos() - this.getxPos()) > this.getMovement()) {
                        xDistance = this.getMovement();
                    } else if (i.getxPos() > this.getxPos()) {
                        xDistance = i.getxPos() - this.getxPos();
                    }
                    if (i.getxPos() < this.getxPos() && (this.getxPos() - i.getxPos()) > this.getMovement()) {
                        yDistance = -this.getMovement();
                    } else if (i.getxPos() < this.getxPos()) {
                        yDistance = -(this.getyPos() - i.getyPos());
                    }
                    first = false;
                }
            }
            setNewPos(this.getxPos() + xDistance, this.getyPos() + yDistance);
        } else if (target != null) {
            setNewPos(this.getxPos() + xDistance, this.getyPos() + yDistance);
            if(attackCreature(target, 0)){
                return true;
            }
        }
        return false;
    }
}
