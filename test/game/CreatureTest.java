package game;

import Database.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CreatureTest {
    ArrayList<Creature> creatures;

    //Creating two characters and two monsters
    @BeforeEach
    void setUp() {
        ArrayList<Weapon> c1weapons = new ArrayList<>();
        Weapon c1w1 = new Weapon("Sword", 6, false, 2, "");
        c1weapons.add(c1w1);
        Character c1 = new Character(1, 1, "Warrior", 20, 10, 3, 6, 5, "", 2, 5, null, c1weapons);

        ArrayList<Weapon> c2weapons = new ArrayList<>();
        Weapon c2w1 = new Weapon("Bow", 6, true, 2, "");
        c1weapons.add(c1w1);
        Character c2 = new Character(2, 4, "Ranger", 20, 10, 3, 6, 5, "", 2, 5, null, c2weapons);

        ArrayList<Weapon> m1weapons = new ArrayList<>();
        Weapon m1w1 = new Weapon("Claw", 6, false, 2, "");
        m1weapons.add(m1w1);
        Monster m1 = new Monster(3, 5, "Bear", 20, 10, 3, 6, 5, "", 2, 5, null, m1weapons);

        ArrayList<Weapon> m2weapons = new ArrayList<>();
        Weapon m2w1 = new Weapon("Greatbow", 6, true, 2, "");
        m1weapons.add(m1w1);
        Monster m2 = new Monster(4, 8, "Ushabti", 20, 10, 3, 6, 5, "", 2, 5, null, m2weapons);

        creatures = new ArrayList<>();
        creatures.add(c1);
        creatures.add(c2);
        creatures.add(m1);
        creatures.add(m2);

    }

    @AfterEach
    void tearDown() {
        creatures = null;
    }

    @Test
    void attackCreature() {

    }

    @Test
    void hit() {
        Character c1 = (Character) creatures.get(0);
        for (int i = 0; i < 100; i++){
            int hit = c1.hit();
            int max = 20 + c1.getAttackBonus();
            int min = 1 + c1.getAttackBonus();
            assertTrue(hit <= max, "Hit too high");
            assertTrue(hit >= min, "Hit too low");
        }
    }

    @Test
    void hitSuccess() {
    }

    @Test
    void moveCreature() {
    }

    @Test
    void updateDead() {
    }

    @Test
    void isDead() {
    }
}