class Weapon {
    private String name;
    private int attackbonus;
    private int damageDice;
    private String description;

    public Weapon(String name, int attackbonus, int dammageDice, String description){
        this.name = name;
        this.attackbonus = attackbonus;
        this.damageDice = dammageDice;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getAttackbonus() {
        return attackbonus;
    }

    public int getDamageDice() {
        return damageDice;
    }

    public String getDescription() {
        return description;
    }
}
