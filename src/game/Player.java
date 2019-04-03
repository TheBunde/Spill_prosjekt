package game;

import javafx.scene.image.ImageView;

public class Player {
    private boolean attackPressed = false;
    private boolean movePressed = false;
    private boolean attackUsed = false;
    private boolean moveUsed = false;
    private int equippedWeapon = 0;
    private ImageView playerImage;

    public Player(){

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
}
