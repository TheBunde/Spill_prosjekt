
package creature;

public class Weapon {

    private String name;
    private int damageDice;
    private boolean ranged;
    private int diceAmount;

    public Weapon(String name, int dammageDice,  int diceAmount,boolean ranged){
        this.name = name;
        this.damageDice = dammageDice;
        this.ranged = ranged;
        this. diceAmount = diceAmount;
    }

    public String getName() {
        return name;
    }

    public int getDamageDice() {
        return damageDice;
    }

    public boolean getRanged(){
        return ranged;
    }

    public int getDiceAmount() {
        return diceAmount;
    }
}
