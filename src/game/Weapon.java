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

    /**
     * get-method that returns the name of the weapon
     * @return name       String for the name of the weapon
     */
    public String getName() {
        return name;
    }

    /**
     * get-method for how many sides the dice have/how high the random nr goes
     * @return damageDice       int value for how many sides the dice have
     */
    public int getDamageDice() {
        return damageDice;
    }

    /**
     * Method that sees if a weapon is ranged or melee
     * @return ranged       is true if the weapon is ranged, false otherwise
     */
    public boolean isRanged(){
        return this.ranged;
    }

    /**
     * get-methods that gets the amount of dice thrown
     * @return diceAmount     int for the amount of dice thrown
     */
    public int getDiceAmount() {
        return diceAmount;
    }

    /**
     * get-methods that returnsa string with the imageUrl
     * @return imageUrl        imageUrl for the image
     */
    public String getImageUrl(){
        return this.imageUrl;
    }


    /**
     * basic toString method
     * @return        returns String with everything in this class
     */
    @Override
    public String toString() {
        return "Name: " + this.getName() + "\nIs ranged: " + this.isRanged() + "\nDamageDice: " + this.getDamageDice() + "\nDiceAmount: " + this.getDiceAmount();
    }
}
