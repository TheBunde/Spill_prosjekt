package Creature;
class Weapon {
    private String name;
    private int damageBonus;
    private int damageDice;
    private String description;

    public Weapon(String name, int damageBonus, int dammageDice, String description){
        this.name = name;
        this.damageBonus = damageBonus;
        this.damageDice = dammageDice;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getdamageBonus() {
        return damageBonus;
    }

    public int getDamageDice() {
        return damageDice;
    }

    public String getDescription() {
        return description;
    }
}
