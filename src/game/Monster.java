package game;

import javafx.scene.Cursor;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


import java.util.ArrayList;
import java.util.Random;

/**
 * This class inherits from Creature.java. The methods in
 * this class controll the AI of the monsters in the
 * game. The methods that controll the monsters are
 * only ran on the computer of the host.
 *
 * @author magnubau and heleneyj
 */
public class Monster extends Creature {

    private Pane attackPane;

    /**
     * A constructor for the class Monster
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
    public Monster(int playerId, int creatureId, String creatureName, int hp, int ac, int movement, int damageBonus, int attackBonus, String backstory, int xPos, int yPos, String imageUrl, ArrayList weapons){
        super(playerId, creatureId, creatureName, hp, ac, movement, damageBonus, attackBonus, backstory, xPos, yPos, imageUrl, weapons);
    }

    /**
     * Handels the movement of the Monster.
     * If the Monster has a melee weapon, it will move towards
     * the closest Character. If the Monster only has ranged
     * weapons, it does not move.
     *
     * @param creatures ArrayList of all creatures in game
     */
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

    /**
     * Handles the attack for the Monsters.
     * It finds the closest Character to attack. Then it
     * finds the weapons that can be used, either ranged or melee.
     * Finally, it picks a rendom usable weapon to attack with
     *
     * @param creatures ArrayList of all creatures in game
     */
    public void monsterAttack (ArrayList<Creature> creatures){
        ArrayList<Weapon> useableWeapons = new ArrayList<>();
        Creature target = getClosest(creatures);
        if(target != null) {
            if (meleeRange(target)) {
                for (Weapon i : getWeapons()) {
                    if (!i.isRanged()) {
                        useableWeapons.add(i);
                    }
                }
            } else {
                for (Weapon i : getWeapons()) {
                    if (i.isRanged()) {
                        useableWeapons.add(i);
                    }
                }
            }
            if(useableWeapons.size() != 0){
                Random r = new Random();
                attackCreature(target, r.nextInt((useableWeapons.size())));
            }
        }
    }

    /**
     * @param creatures     ArrayList of all creatures in game
     * @return      returns the Creature from the ArrayList closest
     *              to the Monster.
     */
    public Creature getClosest(ArrayList<Creature> creatures){
        Creature target = null;
        int xDistance = 16;
        int yDistance = 16;
        for (Creature i : creatures) {
            if(!i.isDead()) {
                if (i != this && i instanceof game.Character) {
                    /* The distance from the Monster to the Character i */
                    int xDistanceToI = Math.abs(i.getxPos() - this.getxPos());
                    int yDistanceToI = Math.abs(i.getyPos() - this.getyPos());
                    /* The distance to the Character currently closest */
                    int xDistanceToCurrent = Math.abs(xDistance);
                    int yDistanceToCurrent = Math.abs(yDistance );
                    /* Finds the hypotenuse of xDistanceToI and yDistanceToI
                       to find the distance between the Monster and the
                       Character, i, in a straight line.
                     */
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

    /**
     * This method finds the hypotenuse of two integers
     *
     * @param x     x cathetus
     * @param y     y cathetus
     * @return      returns a double that is the hypotenuse of
     *              the two cathetuses x and y.
     */
    public double getHypotenuse(int x, int y){
        double powX = Math.pow(x, 2);
        double powY = Math.pow(y, 2);
        return Math.sqrt(powX + powY);
    }

    /**
     * Moves the Monster towards a Creature when the creature is out of range.
     * Also makes sure Monster does not move on top of another Creature.
     *
     * @param target        the Creature to move towards
     * @param creatures     ArrayList of all creatures in game
     */
    public void moveToward(Creature target, ArrayList<Creature> creatures){
        int xPos = getxPos();
        int yPos = getyPos();
        /* Moves the Monster in the right direction and makes
            sure it does not move on top of the target.
         */
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
        /* Makes sure the Monster did not move on top of
            any other Creature.
         */
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

    /**
     * Checks if a Creature is within the movement range
     * of the Monster.
     *
     * @param target    The Creature to check if is within movement range.
     * @return          true if target is within the movement range
     *                  of the Monster, false otherwise.
     */
    public boolean inRange(Creature target){
        if(Math.abs(getxPos() - target.getxPos()) <= (getMovement() +1) && Math.abs(getyPos() - target.getyPos()) <= (getMovement() +1)){
            return true;
        }
        return false;
    }

    /**
     * Checks if a Ceature is in range to be attacked with
     * a melee weapon. A Creature must be adjacent to the
     * Monster to be within melee range.
     *
     * @param target    The Creature to check if is within melee range.
     * @return          true if target is within melee range, false otherwise.
     */
    public boolean meleeRange(Creature target){
        if(Math.abs(target.getxPos() - getxPos()) <= 1 && Math.abs(target.getyPos() - getyPos()) <= 1){
            return true;
        }
        return false;
    }

    /**
     * moves the Monster to a Creature when the Creature
     * is within the movement range of the Monster.Also
     * makes sure Monster does not move on top of another Creature.
     *
     * @param target        The Creature to move to
     * @param creatures     ArrayList of all creatures in game
     */
    public void moveTo(Creature target, ArrayList<Creature> creatures){
        int xPos = getxPos();
        int yPos = getyPos();
        /* Moves the Monster in the right direction and makes
            sure it does not move on top of the target.
         */
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
        /* Makes sure the Monster did not move on top of
            any other Creature.
         */
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

    /**
     * moves The monster clockwise around a Creature. Used
     * in moveToward() and moveTo() to prevent the Monster
     * to move on top of other creatures.
     * @param newX      x-position of Monster
     * @param newY      y-postion of Monster
     * @param target    the Creature to move around
     * @return          ArrayList with new x- and y-position
     */
    public ArrayList<Integer> dontStepOnOthers(int newX, int newY, Creature target){
        ArrayList<Integer> pos = new ArrayList<>();
        int xD = relativePos(newX, target.getxPos());
        int yD = relativePos(newY, target.getyPos());
        /* alters the position of the Monster depending on its
            position, ralative to target
         */
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

    /**
     * used to determin the direction the Monster must move.
     * to reach the postion of another Creature.
     *
     * @param pos           x- or y-position of Monster.
     * @param targetPos     x- or y-position of target Creature.
     * @return              -1 if target - pos < 0, 1 otherwise.
     */
    public int direction(int pos, int targetPos){
        if(targetPos - pos< 0){
            return -1;
        }
        return 1;
    }

    /**
     * finds if the Monster is standing behind, next to
     * or in front of a Creature on either the x- or y-axis.
     *
     * @param pos           x- or y-position of Monster.
     * @param targetPos     x- or y-position of target Creature.
     * @return              -1 if target - pos < 0, 0 if target - pos == 0,
     *                      1 otherwise.
     */
    public int relativePos(int pos, int targetPos){
        if(targetPos - pos< 0){
            return -1;
        }else if(targetPos - pos == 0){
            return 0;
        }
        return 1;
    }

    /**
     * Moves the attackPane to the position of the Monster.
     */
    public void updateAttackPane(){
        GridPane parent = (GridPane) this.attackPane.getParent();
        parent.getChildren().remove(this.attackPane);
        parent.add(this.attackPane, this.getxPos(), this.getyPos());
    }

    /**
     *
     * @return attackPane
     */
    public Pane getAttackPane(){
        return attackPane;
    }

    /**
     * Creates the attackPane of the Monster.
     * AttackPane is a transparent red filter over all monsters
     * that a user can click to attack it.
     * @param cellWidth     The width of the attackPane.
     * @param cellHeight    The height of the attackPane.
     */
    public void initAttackPane(double cellWidth, double cellHeight){
        this.attackPane = new Pane();
        this.attackPane.setPrefWidth(cellWidth);
        this.attackPane.setPrefHeight(cellHeight);
        this.attackPane.setStyle("-fx-background-color: rgb(252, 91, 55, 0.7)");
        this.attackPane.setVisible(false);
        this.attackPane.setCursor(Cursor.CROSSHAIR);
    }

    /**
     * Sets the attackPane to visible.
     */
    public void showAttackPane(){
        this.attackPane.setVisible(true);
    }

    /**
     * Sets the attackPane to invisible
     */
    public void hideAttackPane(){
        this.attackPane.setVisible(false);
    }

    /**
     *
     * @return      The toString() of Monsters super class,
     *              Creature.java.
     */
    public String toString() {
        return super.toString();
    }
}
