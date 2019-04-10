/**
 * The weapons and the data to the corresponding weapon
 * @author heleneyj
 */
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

    /**
     *
     * @return ranged       is true if the weapon is ranged, false otherwise
     */
    public boolean isRanged(){
        return this.ranged;
    }

    /**
     *
     * @return diceAmount     the amount of dice thrown
     */
    public int getDiceAmount() {
        return diceAmount;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }


    /**
     * basic toString method
     * @return        returns everything in this
     */
    @Override
    public String toString() {
        return "Name: " + this.getName() + "\nIs ranged: " + this.isRanged() + "\nDamageDice: " + this.getDamageDice() + "\nDiceAmount: " + this.getDiceAmount();
    }
}
