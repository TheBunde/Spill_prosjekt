package creature;

public class Weapon {
    private String name;
    private int damageDice;
    private String description;
    private int diceAmount;

    public Weapon(String name, int dammageDice, String description, int diceAmount){
        this.name = name;
        this.damageDice = dammageDice;
        this.description = description;
        this. diceAmount = diceAmount;
    }

    public String getName() {
        return name;
    }

    public int getDamageDice() {
        return damageDice;
    }

    public String getDescription() {
        return description;
    }

    public int getDiceAmount() {
        return diceAmount;
    }
}
