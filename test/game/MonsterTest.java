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

        creatures = new ArrayList<>();
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
        Character c1 = (Character) creatures.get(0);
        Character c2 = (Character) creatures.get(1);
        Monster m1 = (Monster) creatures.get(2);
        //Setting positions for creatures
        c1.setNewPos(5, 5);
        c2.setNewPos(11, 5);
        m1.setNewPos(11, 7);
        assertEquals(c2, m1.getClosest(creatures), "Closest character was not returned");

        //Positions for creatures such that the distance to both characters are the same
        c2.setNewPos(11, 5);
        m1.setNewPos(8, 7);
        assertEquals(c1, m1.getClosest(creatures), "getClosest did not return the last checked character");

        //The closest character is dead
        c1.setNewPos(5, 5);
        c2.setNewPos(11, 5);
        c2.setHp(0);
        c2.updateDead();
        m1.setNewPos(11, 7);
        assertEquals(c1, m1.getClosest(creatures), "Returned dead character");

        //No characters, only monsters
        ArrayList<Creature> monsters = new ArrayList<>();
        Monster m2 = new Monster(3, 5, "Bear", 20, 10, 3, 6, 5, "", 8, 5, null, null);
        monsters.add(m1);
        monsters.add(m2);
        assertEquals(null, m1.getClosest(monsters), "Returned a monster instead of null");
    }

    @Test
    void getHypotenuse() {
        Monster m1 = (Monster) creatures.get(2);
        int k1 = 4;
        int k2 = 3;
        assertEquals(5.0, m1.getHypotenuse(4, 3), "Calculation of hypotenuse was wrong");
    }

    @Test
    void moveToward() {
    }

    @Test
    void inRange() {
        Monster m1 = (Monster) creatures.get(2);
        Character c1 = (Character) creatures.get(0);
        //Character not in range
        m1.setNewPos(5, 5);
        c1.setNewPos(10, 5);
        assertFalse(m1.inRange(c1), "Method returns true when character is not in range");

        //Character in range
        m1.setNewPos(5, 5);
        c1.setNewPos(7, 5);
        assertTrue(m1.inRange(c1), "Method returns false when character is in range");

    }

    @Test
    void meleeRange() {
        Monster m1 = (Monster) creatures.get(2);
        Character c1 = (Character) creatures.get(0);
        m1.setNewPos(5, 5);
        c1.setNewPos(7, 5);
        //Not in range for melee
        assertFalse(m1.meleeRange(c1), "Method returns true when character not in range for melee");

        m1.setNewPos(5, 5);
        c1.setNewPos(6, 5);
        //In range for melee
        assertTrue(m1.meleeRange(c1), "Method returns false when character in range for melee");
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