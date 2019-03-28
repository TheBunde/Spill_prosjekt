package game;
import game.Creature;

import java.util.ArrayList;

public class Monster extends Creature {

    public Monster(int playerId, int creatureId, String creatureName, int hp, int ac, int movement, int damageBonus, int attackBonus, int attacksPerTurn, String backstory, int xPos, int yPos, ArrayList weapons){
        super(playerId, creatureId, creatureName, hp, ac, movement, damageBonus, attackBonus, attacksPerTurn, backstory, xPos, yPos, weapons);
    }

    public String toString() {
        return super.toString();
    }

    public void monsterAction (ArrayList<Creature> players){
        Creature target = getClosest(players);
        if(inRange(target)){
            moveTo(target);
            attackCreature(target, 0);
        }else{
            moveToward(target);
            attackCreature(target, 1);
        }
    }

    public Creature getClosest(ArrayList<Creature> players){
        ArrayList<Creature> creatures = players;
        Creature target = creatures.get(0);
        int xDistance = Math.abs(getxPos() - creatures.get(0).getxPos());
        int yDistance = Math.abs(getyPos() - creatures.get(0).getyPos());

        for (Creature i : creatures) {
           if(Math.abs(getxPos() - i.getxPos()) < xDistance && Math.abs(getyPos() - i.getyPos()) < yDistance){
               target = i;
           }
        }
        return target;
    }

    public void moveToward(Creature target){
        int xPos = getxPos();
        int yPos = getyPos();
        if(Math.abs(xPos -target.getxPos()) > getMovement()){
            xPos = xPos + getMovement() * direction(xPos, target.getxPos());
        }else if(Math.abs(xPos -target.getxPos()) < getMovement()){
            xPos = target.getxPos() - 1 * direction(xPos, target.getxPos());
        }
        if(Math.abs(yPos -target.getyPos()) > getMovement()){
            yPos = yPos + getMovement() * direction(yPos, target.getyPos());
        }else if(Math.abs(yPos -target.getyPos()) < getMovement()){
            yPos = target.getyPos() - 1 * direction(yPos, target.getyPos());
        }
        setNewPos(xPos, yPos);
    }

    public boolean inRange(Creature target){
        if(Math.abs(getxPos() - target.getxPos()) <= (getMovement() +1) && Math.abs(getyPos() - target.getyPos()) <= (getMovement() +1)){
            return true;
        }
        return false;
    }

    public void moveTo(Creature target){
        int xPos = getxPos();
        int yPos = getyPos();
        if(xPos < target.getxPos()){
            xPos = target.getxPos() -1;
        }else if(xPos > target.getxPos()){
            xPos = target.getxPos() + 1;
        }
        if(yPos < target.getyPos()){
            yPos = target.getyPos() -1;
        }else if(xPos > target.getyPos()){
            yPos = target.getyPos() + 1;
        }
        setNewPos(xPos, yPos);
    }

    public int direction(int pos, int targetPos){
        if(pos - targetPos < 0){
            return -1;
        }
        return 1;
    }
}
