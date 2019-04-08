package game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTest{
    ArrayList<Creature> creatures;

    @BeforeEach
    void setUp() {
        ArrayList<Weapon> c1weapons = new ArrayList<>();
        Weapon c1w1 = new Weapon("Sword", 6, false, 2, "");
        c1weapons.add(c1w1);
        Character c1 = new Character(1, 1, "Warrior", 20, 10, 3, 6, 5, "", 2, 5, null, c1weapons);

        ArrayList<Weapon> c2weapons = new ArrayList<>();
        Weapon c2w1 = new Weapon("Bow", 6, true, 2, "");
        c2weapons.add(c2w1);
        Character c2 = new Character(2, 4, "Ranger", 20, 10, 3, 6, 5, "", 2, 6, null, c2weapons);

        ArrayList<Weapon> m1weapons = new ArrayList<>();
        Weapon m1w1 = new Weapon("Dagger", 6, false, 2, "");
        Weapon m1w2 = new Weapon("Greatbow", 6, true, 2, "");
        m1weapons.add(m1w1);
        m1weapons.add(m1w2);
        Monster m1 = new Monster(4, 8, "Ushabti", 20, 10, 3, 6, 5, "", 8, 6, null, m1weapons);

        creatures.add(c1);
        creatures.add(c2);
        creatures.add(m1);
    }

    @AfterEach
    void tearDown() {
        creatures = null;
    }

    @Test
    void toStringTest() {
    }

    @Test
    void monsterMove() {
    }

    @Test
    void monsterAttack() {
    }

    @Test
    void getClosest() {
    }

    @Test
    void pytagoras() {
    }

    @Test
    void moveToward() {
    }

    @Test
    void inRange() {
    }

    @Test
    void meleeRange() {
    }

    @Test
    void moveTo() {
    }

    @Test
    void dontStepOnOthers() {
    }

    @Test
    void direction() {
    }

    @Test
    void relativePos() {
    }
}