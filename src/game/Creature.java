
package game;

import java.util.ArrayList;

import main.*;
import audio.SFXPlayer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Creature is the superclass of Monster and Character
 * @author heleneyj, magnubau, williad
 */
public abstract class Creature {
    private int hp;
    private int ac;
    private String creatureName;
    private int attackBonus;
    private int movement;
    private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
    private int damageBonus;
    private int xPos;
    private int yPos;
    private int playerId;
    private int creatureId;
    private String backstory;
    private String imageUrl;
    private ImageView pawn;
    private boolean isDead;
    private boolean readyForNewLevel = false;

    /**
     * A constructor for the class Creature
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
    public Creature(int playerId, int creatureId, String creatureName, int hp, int ac, int movement, int damageBonus, int attackBonus, String backstory, int xPos, int yPos, String imageUrl, ArrayList weapons){
        this.playerId = playerId;
        this.creatureId = creatureId;
        this.creatureName = creatureName;
        this.hp = hp;
        this.ac = ac;
        this.movement = movement;
        this.attackBonus = attackBonus;
        this.damageBonus = damageBonus;
        this.backstory = backstory;
        this.xPos = xPos;
        this.yPos = yPos;
        this.imageUrl = imageUrl;
        this.weapons = weapons;
        this.isDead = false;

        if (imageUrl != null){
            Image image = new Image("GUI/images/" + this.imageUrl);
            this.pawn = new ImageView(image);
            this.pawn.setPreserveRatio(false);
        }
    }

    /**
     * Attacks a target-creature with the specified weapon
     *
     * @param target        Creature to attack
     * @param weaponIndex   weapon to be used
     * @return              true if hit success, false otherwise
     */
    public boolean attackCreature(Creature target, int weaponIndex){
        Weapon weapon = this.weapons.get(weaponIndex);
        /* Rolls a dice to determine successful hit */
        int roll = hit();
        if (!hitSuccess(roll, target)){
            /* Sends an event message based on if target is Character or not */
            String chatMessage = "";
            if (this instanceof Character){
                SFXPlayer.getInstance().setSFX(11);
                if (Main.db != null) {
                    chatMessage += Main.db.fetchUsernameFromPlayerId(this.getPlayerId()) + " rolled " + roll + " and missed " + target.getCreatureName();
                }
            }
            else{
                SFXPlayer.getInstance().setSFX(12);
                if (Main.db != null) {
                    chatMessage += this.getCreatureName() + " rolled " + roll + " and missed " + Main.db.fetchUsernameFromPlayerId(target.getPlayerId());
                }
            }
            if (Main.db != null) {
                Main.db.addChatMessage(chatMessage, true);
            }
            return false;
        }

        /* Rolls dice to determine damage */
        int damage = Dice.roll(weapon.getDamageDice(), weapon.getDiceAmount()) + this.damageBonus;
        target.setHp(target.getHp() - damage);

        /* Sends an event message based on if target is Character or not */
        String chatMessage = " rolled " + roll + " and dealt " + damage + " damage on ";
        if (this instanceof Character){
            SFXPlayer.getInstance().setSFX(10);
            if (Main.db != null) {
                chatMessage = Main.db.fetchUsernameFromPlayerId(this.getPlayerId()) + chatMessage + target.getCreatureName();
            }
        }
        else{
            SFXPlayer.getInstance().setSFX(10);
            if (Main.db != null) {
                chatMessage = this.getCreatureName() + chatMessage + Main.db.fetchUsernameFromPlayerId(target.getPlayerId());
            }
        }
        chatMessage += " with " + weapon.getName();
        if (Main.db != null) {
            Main.db.addChatMessage(chatMessage, true);
        }
        return true;
    }

    /**
     * Rolls 20 sided die to, adds attack bonus  of the Creature
     * to the dies value.
     *
     * @return      The value of the die ? attack bonus.
     */
    public int hit(){
        int hit = Dice.roll(20, 1) + this.attackBonus;
        return hit;
    }

    /**
     * Checks if the Creatures hit is higher than another
     * Creatures armour class.
     *
     * @param hit       hit.
     * @param target    A Creature.
     * @return          true if hit greater than or equal to target.getAc(), false otherwise.
     */
    public boolean hitSuccess(int hit, Creature target){
        if (hit >= target.getAc()){
            return true;
        }
        return false;
    }

    /**
     * Moves the Creature to a new position. Makes sure
     * the Creature do not move on top of another Creature,
     * and that it doeas not move outside of its movement range.
     * @param newX          new x-position.
     * @param newY          new y-position.
     * @param creatures     ArrayList of all creatures in game.
     * @return              true if the Creature moved to a valid location,
     *                      false otherwise.
     */
    public boolean moveCreature(int newX, int newY, ArrayList<Creature> creatures){
        for (Creature c : creatures){
            if (newX == c.getxPos() && newY == c.getyPos()){
                /* Monsters move to an invalid location before moving away from it */
                if(this instanceof Monster) {
                    this.setNewPos(newX, newY);
                }
                return false;
            }
        }
        /* Checks if position is within movement. If so, moves to the position */
        if ((Math.abs(newX - this.getxPos()) <= this.movement && Math.abs(newY - this.getyPos()) <= this.movement)){
            SFXPlayer.getInstance().setSFX(15);
            this.setNewPos(newX, newY);
            if (this instanceof Character){
                if(Main.db != null) {
                    Main.db.addChatMessage(Main.db.fetchUsernameFromPlayerId(this.getPlayerId()) + " moved to X: " + newX + " Y: " + newY, true);
                }
            }
            else{
                if(Main.db != null) {
                    Main.db.addChatMessage(this.getCreatureName() + " moved to X: " + newX + " Y: " + newY, true);
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * get-methods for seeing hp for the creature
     * @return hp       returns health points
     */
    public int getHp() {
        return hp;
    }

    /**
     * set-method for setting new hp after damage is dealt
     * @param hp      sets the new hp level
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * get-method for the armor class
     * @return ac       the value of the armor class
     */
    public int getAc() {
        return ac;
    }

    /**
     * get-method for the creatures name
     * @return creatureName        the name of the creature
     */
    public String getCreatureName() {
        return creatureName;
    }

    /**
     * get-method for the attack bonus
     * @return attackBonus     bonus added to attack dice
     */
    public int getAttackBonus() {
        return attackBonus;
    }

    /**
     * get-method for getting the movement range
     * @return movement      the range the creature can walk
     */
    public int getMovement() {
        return movement;
    }

    /**
     * get-method for ArrayList of weapons the player has available
     * @return weapons       the weapons available
     */
    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * get-method for the damage bonus
     * @return damageBonus      the bonus damage you deal if you hit
     */
    public int getDamageBonus() {
        return damageBonus;
    }

    /**
     * @return x-coordinate
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * @return y-coordinate
     */
    public int getyPos() {
        return yPos;
    }

    /**
     * @param xPos x-coordinate to set
     * @param yPos y-coordinate to set
     */
    public void setNewPos(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * @return player id
     */
    public int getPlayerId(){
        return this.playerId;
    }

    /**
     * @return creature id
     */
    public int getCreatureId(){
        return this.creatureId;
    }

    /**
     * @return backstory
     */
    public String getBackstory(){
        return this.backstory;
    }

    /**
     * @param weapon weapon to add
     */
    public void addNewWeapon(Weapon weapon){
        this.weapons.add(weapon);
    }

    /**
     * @return true if hp is less than zero, false otherwise
     */
    public boolean updateDead(){
        if(this.hp <= 0 && !isDead()){
            /* If player updated to be dead, the pawn-image is changed */
            SFXPlayer.getInstance().setSFX(0);
            this.setPawnImage("gravestone.png");
            isDead = true;
        }
        return isDead;
    }

    /**
     * @return true if dead, false otherwise
     */
    public boolean isDead(){
        return isDead;
    }

    /**
     * @return image url
     */
    public String getImageUrl(){
        return this.imageUrl;
    }

    /**
     * @return JavaFX-imageView for the pawn
     */
    public ImageView getPawn(){
        return this.pawn;
    }

    /**
     * @param imageUrl image url to be set
     */
    public void setPawnImage(String imageUrl){
        if (this.pawn != null) {
            this.pawn.setImage(new Image("GUI/images/" + imageUrl));
        }
    }

    /**
     * Changes the size of the pawn
     *
     * @param width     width to be set
     * @param height    height to be set
     */
    public void setPawnSize(double width, double height){
        this.pawn.setFitWidth(width);
        this.pawn.setFitHeight(height);
    }

    /**
     * @return true if ready for new level, false otherwise
     */
    public boolean isReadyForNewLevel(){
        return this.readyForNewLevel;
    }

    /**
     * @param readyForNewLevel ready for new level
     */
    public void setReadyForNewLevel(boolean readyForNewLevel){
        this.readyForNewLevel = readyForNewLevel;
    }

    /**
     * toString-method
     * @return String containing object-information
     */
    public String toString() {
        String weaponNames = "";
        for(int i = 0; i < this.weapons.size(); i++){
            weaponNames += this.weapons.get(i).getName() + ((i < this.weapons.size() - 1) ? ", " : "");
        }
        return "Character: " + this.getCreatureName() + "\nHP: " + this.getHp() + "\nAC: " + this.getAc() + "\nMovement: " + this.getMovement() +
                "\nWeapon: " + weaponNames + "\nAttack bonus: " + this.getAttackBonus() + "\nBackstory: " + this.getBackstory();
    }
}
