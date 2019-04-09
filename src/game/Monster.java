package game;
//import com.sun.java.util.jar.pack.Instruction;
import game.Creature;
import javafx.scene.Cursor;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


import java.util.ArrayList;
import java.util.Random;

public class Monster extends Creature {

    private Pane attackPane;
    public Monster(int playerId, int creatureId, String creatureName, int hp, int ac, int movement, int damageBonus, int attackBonus, String backstory, int xPos, int yPos, String imageUrl, ArrayList weapons){
        super(playerId, creatureId, creatureName, hp, ac, movement, damageBonus, attackBonus, backstory, xPos, yPos, imageUrl, weapons);
    }

    public void monsterAction (ArrayList<Creature> creatures){
        Creature target = getClosest(creatures);
        if(inRange(target)){
            moveTo(target, creatures);
            attackCreature(target, 0);
        }else{
            moveToward(target, creatures);
            attackCreature(target, 1);
        }
    }

    //Splitted version of monsterAction for movement
    public void monsterMove (ArrayList<Creature> creatures){
        Creature target = getClosest(creatures);
        boolean melee = false;
        for(Weapon i: getWeapons()){
            if(!i.isRanged()){
                melee = true;
            }
        }
        if(target != null) {
            if (inRange(target) && melee) {
                moveTo(target, creatures);
                System.out.println("moveto");
            } else if (melee) {
                moveToward(target, creatures);
                System.out.println("movetoward");
            }
        }
    }

    //Splitted version of monsterAction for attack
    public void monsterAttack (ArrayList<Creature> players){
        ArrayList<Weapon> useableWeapons = new ArrayList<>();
        Creature target = getClosest(players);
        if(target != null) {
            if (meleeRange(target)) {
                for (Weapon i : getWeapons()) {
                    if (!i.isRanged()) {
                        //attackCreature(target, getWeapons().indexOf(i));
                        useableWeapons.add(i);
                    }
                }
            } else {
                for (Weapon i : getWeapons()) {
                    if (i.isRanged()) {
                        useableWeapons.add(i);
                        //attackCreature(target, getWeapons().indexOf(i));
                    }
                }
            }
            if(useableWeapons.size() != 0){
                Random r = new Random();
                attackCreature(target, r.nextInt((useableWeapons.size())));
            }
        }
    }

    public Creature getClosest(ArrayList<Creature> players){
        ArrayList<Creature> creatures = players;
        Creature target = null;
        int xDistance = 16;
        int yDistance = 16;
        for (Creature i : creatures) {
            if(!i.isDead()) {
                if (i != this && i instanceof game.Character) {
                    int xDistanceToI = Math.abs(i.getxPos() - this.getxPos());
                    int yDistanceToI = Math.abs(i.getyPos() - this.getyPos());
                    int xDistanceToCurrent = Math.abs(xDistance);
                    int yDistanceToCurrent = Math.abs(yDistance );
                    if (getHypotenuse(xDistanceToI, yDistanceToI) < getHypotenuse(xDistanceToCurrent, yDistanceToCurrent)) {
                        xDistance = xDistanceToI;
                        yDistance = yDistanceToI;
                        target = i;
                    }
                }
            }
        }
        return target;
    }

    public double getHypotenuse(int x, int y){
        double powX = Math.pow(x, 2);
        double powY = Math.pow(y, 2);
        return Math.sqrt(powX + powY);
    }

    public void moveToward(Creature target, ArrayList<Creature> creatures){
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
        boolean validPos = false;
        while(!validPos){
            if (!moveCreature(xPos, yPos, creatures)){
                ArrayList<Integer> newPos = dontStepOnOthers(xPos, yPos, target);
                xPos = newPos.get(0);
                yPos = newPos.get(1);
            }else{
                validPos = true;
            }
        }
    }

    public boolean inRange(Creature target){
        if(Math.abs(getxPos() - target.getxPos()) <= (getMovement() +1) && Math.abs(getyPos() - target.getyPos()) <= (getMovement() +1)){
            return true;
        }
        return false;
    }

    public boolean meleeRange(Creature target){
        if(Math.abs(target.getxPos() - getxPos()) <= 1 && Math.abs(target.getyPos() - getyPos()) <= 1){
            return true;
        }
        return false;
    }

    public void moveTo(Creature target, ArrayList<Creature> creatures){
        int xPos = getxPos();
        int yPos = getyPos();
        if(xPos < target.getxPos()){
            xPos = target.getxPos() -1;
        }else if(xPos > target.getxPos()){
            xPos = target.getxPos() + 1;
        }
        if(yPos < target.getyPos()){
            yPos = target.getyPos() -1;
        }else if(yPos > target.getyPos()){
            yPos = target.getyPos() + 1;
        }
        boolean validPos = false;
        while(!validPos){
            if (!moveCreature(xPos, yPos, creatures)){
                ArrayList<Integer> newPos = dontStepOnOthers(xPos, yPos, target);
                xPos = newPos.get(0);
                yPos = newPos.get(1);
            }else{
                validPos = true;
            }
        }
    }

    public ArrayList<Integer> dontStepOnOthers(int newX, int newY, Creature target){
        ArrayList<Integer> pos = new ArrayList<>();
        int xD = relativePos(newX, target.getxPos());
        int yD = relativePos(newY, target.getyPos());
        if(xD == 1 && yD == 1){
            pos.add(newX +1);
            pos.add(newY);
        }else if(xD == 0 && yD == 1){
            if(getxPos() == 15){
                pos.add(newX -1);
                pos.add(newY + 1);
            }else {
                pos.add(newX + 1);
                pos.add(newY);
            }
        }else if(xD == -1 && yD == 1){
            pos.add(newX);
            pos.add(newY + 1);
        }else if(xD == -1 && yD == 0){
            if(getyPos() == 15){
                pos.add(newX - 1);
                pos.add(newY - 1);
            }else {
                pos.add(newX);
                pos.add(newY + 1);
            }
        }else if(xD == -1 && yD == -1){
            pos.add(newX - 1);
            pos.add(newY);
        }else if(xD == 0 && yD == -1){
            if(getxPos() == 0){
                pos.add(newX + 1);
                pos.add(newY - 1);
            }else {
                pos.add(newX - 1);
                pos.add(newY);
            }
        }else if(xD == 1 && yD == -1){
            pos.add(newX);
            pos.add(newY - 1);
        }else if(xD == 1 && yD == 0){
            if(getyPos() == 0){
                pos.add(newX + 1);
                pos.add(newY + 1);
            }
            pos.add(newX);
            pos.add(newY - 1);
        }
        return pos;
    }

    public int direction(int pos, int targetPos){
        if(targetPos - pos< 0){
            return -1;
        }
        return 1;
    }

    public int relativePos(int pos, int targetPos){
        if(targetPos - pos< 0){
            return -1;
        }else if(targetPos - pos == 0){
            return 0;
        }
        return 1;
    }

    public void updateAttackPane(){
        GridPane parent = (GridPane) this.attackPane.getParent();
        parent.getChildren().remove(this.attackPane);
        parent.add(this.attackPane, this.getxPos(), this.getyPos());
    }

    public Pane getAttackPane(){
        return attackPane;
    }

    public void initAttackPane(double cellWidth, double cellHeight){
        this.attackPane = new Pane();
        this.attackPane.setPrefWidth(cellWidth);
        this.attackPane.setPrefHeight(cellHeight);
        this.attackPane.setStyle("-fx-background-color: rgb(252, 91, 55, 0.7)");
        this.attackPane.setVisible(false);
        this.attackPane.setCursor(Cursor.CROSSHAIR);
    }

    public void showAttackPane(){
        this.attackPane.setVisible(true);
    }

    public void hideAttackPane(){
        this.attackPane.setVisible(false);
    }

    public String toString() {
        return super.toString();
    }
}
