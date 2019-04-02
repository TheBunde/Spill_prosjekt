package game;

public class Player {
    private boolean attackPressed = false;
    private boolean movePressed = false;

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
}
