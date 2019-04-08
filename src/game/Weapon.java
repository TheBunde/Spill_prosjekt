
package game;

public class Weapon {

    private String name;
    private int damageDice;
    private boolean ranged;
    private int diceAmount;
    private String imageUrl;


    public Weapon(String name, int damageDice, boolean ranged, int diceAmount, String imageUrl){
        this.name = name;
        this.damageDice = damageDice;
        this.ranged = ranged;
        this.diceAmount = diceAmount;
        this.imageUrl = imageUrl;
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

    public String getImageUrl(){
        return this.imageUrl;
    }

    @Override
    public String toString() {
        return "Name: " + this.getName() + "\nIs ranged: " + this.isRanged() + "\nDamageDice: " + this.getDamageDice() + "\nDiceAmount: " + this.getDiceAmount();
    }
}
