
package game;

public class Weapon {

    private String name;
    private int damageDice;
    private boolean ranged;
    private int diceAmount;
    private int imageUrl;

    public Weapon(String name, int damageDice, boolean ranged, int diceAmount){
        this.name = name;
        this.damageDice = damageDice;
        this.ranged = ranged;
        this.diceAmount = diceAmount;
    }

    public String getName() {
        return name;
    }

    public int getDamageDice() {
        return damageDice;
    }

    public boolean isRanged(){
        return this.ranged;
    }

    public int getDiceAmount() {
        return diceAmount;
    }
}
