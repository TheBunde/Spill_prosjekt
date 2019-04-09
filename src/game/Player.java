package game;

import GUI.BattlefieldController;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player {
    private boolean attackPressed = false;
    private boolean movePressed = false;
    private boolean attackUsed = false;
    private boolean moveUsed = false;
    private int equippedWeapon = 0;
    private ImageView playerImage;

    public Player(ImageView playerImage){
        this.playerImage = playerImage;
    }

    public boolean isAttackPressed(){
        return attackPressed;
    }

    public boolean isMovePressed(){
        return movePressed;
    }

    public void setAttackPressed(boolean attackPressed) {
        this.attackPressed = attackPressed;
    }

    public void setMovePressed(boolean movePressed) {
        this.movePressed = movePressed;
    }

    public boolean isAttackUsed(){
        return attackUsed;
    }

    public boolean isMoveUsed(){
        return moveUsed;
    }

    public void setAttackUsed(boolean attackUsed){
        this.attackUsed = attackUsed;
    }

    public void setMoveUsed(boolean moveUsed){
        this.moveUsed = moveUsed;
    }

    public int getEquippedWeapon(){
        return equippedWeapon;
    }

    public void setEquippedWeapon(int equippedWeapon){
        this.equippedWeapon = equippedWeapon;
    }

    public boolean isAllActionsUsed(){
        return this.isAttackUsed() && this.isMoveUsed();
    }

    public void resetUsedActions(){
        this.setAttackUsed(false);
        this.setMoveUsed(false);
    }

    public void imageUpdate(){
        int chrID = BattlefieldController.game.playerCharacter.getCreatureId();
        int chrHP = BattlefieldController.game.playerCharacter.getHp();
        int chrInHP = BattlefieldController.game.playerCharacter.getInitialHp();



        int dmgOne = (chrInHP * 2)/3;
        int dmgTwo = chrInHP/3;

        //Warrior
        if(chrID == 1){
            if(chrHP > dmgOne){
                playerImage.setImage(new Image("GUI/images/" + BattlefieldController.game.playerCharacter.getImageUrl()));
            }
            else if(chrHP >= dmgTwo && chrHP <= dmgOne){
                playerImage.setImage(new Image("GUI/images/warriordamaged.jpg"));
            }
            else if(chrHP < dmgOne){
                playerImage.setImage(new Image("GUI/images/warriordamaged2.jpg"));
            }
        }
        //Rogue
        if(chrID == 2){
            if(chrHP > dmgOne){
                playerImage.setImage(new Image("GUI/images/" + BattlefieldController.game.playerCharacter.getImageUrl()));
            }
            else if(chrHP >= dmgTwo && chrHP <= dmgOne){
                playerImage.setImage(new Image("GUI/images/roguedamaged.jpg"));
            }
            else if(chrHP < dmgOne){
                playerImage.setImage(new Image("GUI/images/roguedamaged2.jpg"));
            }
        }
        //Wizard
        if(chrID == 3){
            if(chrHP > dmgOne){
                playerImage.setImage(new Image("GUI/images/" + BattlefieldController.game.playerCharacter.getImageUrl()));
            }
            else if(chrHP >= dmgTwo && chrHP <= dmgOne){
                playerImage.setImage(new Image("GUI/images/wizarddamaged.jpg"));
            }
            else if(chrHP < dmgOne){
                playerImage.setImage(new Image("GUI/images/wizarddamaged2.jpg"));
            }
        }
        //Ranger
        if(chrID == 4){
            if(chrHP > dmgOne){
                playerImage.setImage(new Image("GUI/images/" + BattlefieldController.game.playerCharacter.getImageUrl()));
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
