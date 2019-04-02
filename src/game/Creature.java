package game;

import java.util.ArrayList;
import Main.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private int creatureId;
    private String backstory;
    private String imageUrl;
    private ImageView pawn;
    private boolean isDead;


    public Creature(int playerId, int creatureId, String creatureName, int hp, int ac, int movement, int damageBonus, int attackBonus, int attacksPerTurn, String backstory, int xPos, int yPos, String imageUrl, ArrayList weapons){
        this.playerId = playerId;
        this.creatureId = creatureId;
        this.creatureName = creatureName;
        this.hp = hp;
        this.ac = ac;
        this.movement = movement;
        this.attackBonus = attackBonus;
        this.attacksPerTurn = attacksPerTurn;
        this.damageBonus = damageBonus;
        this.backstory = backstory;
        this.xPos = xPos;
        this.yPos = yPos;
        this.imageUrl = imageUrl;
        this.weapons = weapons;
        this.isDead = false;

        Image image = new Image("GUI/images/" + this.imageUrl);
        this.pawn = new ImageView(image);
        this.pawn.setPreserveRatio(false);
    }

    public boolean attackCreature(Creature target, int weaponIndex){
        Weapon weapon = this.weapons.get(weaponIndex);
        int roll = hit();
        if (!hitSuccess(roll, target)){
            String chatMessage = "";
            if (this instanceof Character){
                chatMessage += Main.db.fetchUsernameFromPlayerId(this.getPlayerId()) + " rolled " + roll + " and missed " + target.getCreatureName();
            }
            else{
                chatMessage += this.getCreatureName() + " rolled " + roll + " and missed " + Main.db.fetchUsernameFromPlayerId(target.getPlayerId());
            }
            Main.db.addChatMessage(chatMessage, true);
            return false;
        }

        int damage = 0;
        damage += Dice.roll(weapon.getDamageDice(), weapon.getDiceAmount()) + this.damageBonus;

        target.setHp(target.getHp() - damage);
        String chatMessage = "";
        if (this instanceof Character){
            chatMessage += Main.db.fetchUsernameFromPlayerId(this.getPlayerId()) + " rolled " + roll + " and dealt " + damage + " to " + target.getCreatureName();
        }
        else{
            chatMessage += this.getCreatureName() + " rolled " + roll + " and dealt " + damage + " to " + Main.db.fetchUsernameFromPlayerId(target.getPlayerId());
        }
        Main.db.addChatMessage(chatMessage, true);
        return true;
    }

    public int hit(){
        int hit = Dice.roll(20, 1) + this.attackBonus;
        return hit;
    }

    public boolean hitSuccess(int hit, Creature target){
        if (hit >= target.getAc()){
            return true;
        }
        return false;
    }

    public boolean moveCreature(int newX, int newY){
        System.out.println("NewX: " + newX + "\nNewY: " + newY + "\nMovement: " + this.movement);
        System.out.println("Math.abs(newX - this.getxPos()): " + Math.abs(newX - this.getxPos()));
        System.out.println("Math.abs(newY - this.getyPos()): " + Math.abs(newY - this.getyPos()));
        if ((Math.abs(newX - this.getxPos()) <= this.movement && Math.abs(newY - this.getyPos()) <= this.movement)){
            this.setNewPos(newX, newY);
            System.out.println("Moved");
            if (this instanceof Character){
                Main.db.addChatMessage(Main.db.fetchUsernameFromPlayerId(this.getPlayerId()) + " moved to X: " + newX + " Y: " + newY, true);
            }
            else{
                Main.db.addChatMessage(this.getCreatureName() + " moved to X: " + newX + " Y: " + newY, true);
            }
            return true;
        }
        else{
            System.out.println("Did not move");
            return false;
        }
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

    public int getDamageBonus() {
        return damageBonus;
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

    public int getPlayerId(){
        return this.playerId;
    }

    public int getCreatureId(){
        return this.creatureId;
    }

    public String getBackstory(){
        return this.backstory;
    }

    public boolean updateDead(){
        isDead = (this.hp <= 0);
        return isDead;
    }

    public boolean isDead(){
        return isDead;
    }

    public String toString() {
        String weaponNames = "";
        for(int i = 0; i < this.weapons.size(); i++){
            weaponNames = this.weapons.get(i).getName();
        }
        return "Character: " + this.getCreatureName() + "\nHP: " + this.getHp() + "\nAC: " + this.getAc() + "\nSpeed: " + this.getMovement() +
                "\nWeapon: " + weaponNames + "\nAttack bonus: " + this.getAttackBonus() +"\nAttacks per turn: " + this.getAttacksPerTurn() + "\nBackstory: " + this.getBackstory();
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public ImageView getPawn(){
        return this.pawn;
    }

    public void setPawnImage(String imageUrl){
        this.pawn.setImage(new Image("GUI/images/" + imageUrl));
    }

    public void setPawnSize(double width, double height){
        this.pawn.setFitWidth(width);
        this.pawn.setFitHeight(height);
    }
}
