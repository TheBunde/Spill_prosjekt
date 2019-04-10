package game;

import GUI.BattlefieldController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Keeps track of the actions done and the status for the player
 * This class is tied up with BattlefieldController
 *
 * @author henrikwt, williad
 */
public class PlayerActions {
    private boolean attackPressed = false;
    private boolean movePressed = false;
    private boolean attackUsed = false;
    private boolean moveUsed = false;
    private int equippedWeapon = 0;
    private ImageView playerImage;

    /**
     * Constructor for PlayerActions
     *
     * @param playerImage portrait image from BattlefieldController
     */
    public PlayerActions(ImageView playerImage){
        this.playerImage = playerImage;
    }

    /**
     *
     * @return true if pressed, false otherwise
     */
    public boolean isAttackPressed(){
        return attackPressed;
    }

    /**
     * @return true if pressed, false otherwise
     */
    public boolean isMovePressed(){
        return movePressed;
    }

    /**
     * @param attackPressed boolean to set
     */
    public void setAttackPressed(boolean attackPressed) {
        this.attackPressed = attackPressed;
    }

    /**
     * @param movePressed boolean to set
     */
    public void setMovePressed(boolean movePressed) {
        this.movePressed = movePressed;
    }

    /**
     * @return true if attack is used, false otherwise
     */
    public boolean isAttackUsed(){
        return attackUsed;
    }

    /**
     * @return true if move used, false otherwise
     */
    public boolean isMoveUsed(){
        return moveUsed;
    }

    /**
     * @param attackUsed boolean to set
     */
    public void setAttackUsed(boolean attackUsed){
        this.attackUsed = attackUsed;
    }

    /**
     * @param moveUsed boolean to set
     */
    public void setMoveUsed(boolean moveUsed){
        this.moveUsed = moveUsed;
    }

    /**
     * @return the index of the equipped weapon
     */
    public int getEquippedWeapon(){
        return equippedWeapon;
    }

    /**
     * @param equippedWeapon equippedweapon to set
     */
    public void setEquippedWeapon(int equippedWeapon){
        this.equippedWeapon = equippedWeapon;
    }

    /**
     * Checks if both move and attack has been used
     *
     * @return true if all actions are used, false otherwise
     */
    public boolean isAllActionsUsed(){
        return this.isAttackUsed() && this.isMoveUsed();
    }

    /**
     * Resets the used actions to not used
     */
    public void resetUsedActions(){
        this.setAttackUsed(false);
        this.setMoveUsed(false);
    }

    /**
     * Updates the portrait image in BattlefieldController based on the hp of the character the player is
     */
    public void imageUpdate(){
        /* The image changes for every third part of the initial hp the character hp gets below */
        int chrID = BattlefieldController.game.getPlayerCharacter().getCreatureId();
        int chrHP = BattlefieldController.game.getPlayerCharacter().getHp();
        int chrInHP = BattlefieldController.game.getPlayerCharacter().getInitialHp();

        int dmgOne = (chrInHP * 2)/3;
        int dmgTwo = chrInHP/3;

        /* Warrior */
        if(chrID == 1){
            if(chrHP > dmgOne){
                playerImage.setImage(new Image("GUI/images/" + BattlefieldController.game.getPlayerCharacter().getImageUrl()));
            }
            else if(chrHP >= dmgTwo && chrHP <= dmgOne){
                playerImage.setImage(new Image("GUI/images/warriordamaged.jpg"));
            }
            else if(chrHP < dmgOne){
                playerImage.setImage(new Image("GUI/images/warriordamaged2.jpg"));
            }
        }
        /* Rogue */
        if(chrID == 2){
            if(chrHP > dmgOne){
                playerImage.setImage(new Image("GUI/images/" + BattlefieldController.game.getPlayerCharacter().getImageUrl()));
            }
            else if(chrHP >= dmgTwo && chrHP <= dmgOne){
                playerImage.setImage(new Image("GUI/images/roguedamaged.jpg"));
            }
            else if(chrHP < dmgOne){
                playerImage.setImage(new Image("GUI/images/roguedamaged2.jpg"));
            }
        }
        /* Wizard */
        if(chrID == 3){
            if(chrHP > dmgOne){
                playerImage.setImage(new Image("GUI/images/" + BattlefieldController.game.getPlayerCharacter().getImageUrl()));
            }
            else if(chrHP >= dmgTwo && chrHP <= dmgOne){
                playerImage.setImage(new Image("GUI/images/wizarddamaged.jpg"));
            }
            else if(chrHP < dmgOne){
                playerImage.setImage(new Image("GUI/images/wizarddamaged2.jpg"));
            }
        }
        /* Ranger */
        if(chrID == 4){
            if(chrHP > dmgOne){
                playerImage.setImage(new Image("GUI/images/" + BattlefieldController.game.getPlayerCharacter().getImageUrl()));
            }
            else if(chrHP >= dmgTwo && chrHP <= dmgOne){
                playerImage.setImage(new Image("GUI/images/rangerdamaged.jpg"));
            }
            else if(chrHP < dmgOne){
                playerImage.setImage(new Image("GUI/images/rangerdamaged2.jpg"));
            }
        }
        if(chrHP <= 0){
            playerImage.setImage(new Image("GUI/images/dead.jpg"));
        }

    }

}
