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

        ArrayList<Weapon> m2weapons = new ArrayList<>();
        Weapon m2w1 = new Weapon("Claw", 6, false, 2, "");
        m2weapons.add(m2w1);
        Monster m2 = new Monster(3, 5, "Bear", 20, 10, 3, 6, 5, "", 8, 5, null, m2weapons);

        ArrayList<Weapon> m3weapons = new ArrayList<>();
        m3weapons.add(m1w2);
        Monster m3 = new Monster(3, 5, "Ushabti", 20, 10, 3, 6, 5, "", 8, 7, null, m3weapons);

        creatures = new ArrayList<>();
        creatures.add(c1);
        creatures.add(c2);
        creatures.add(m1);
        creatures.add(m2);
        creatures.add(m3);
    }

    @AfterEach
    void tearDown() {
        creatures = null;
    }

    @Test
    void toStringTest() {
        Monster m1 = (Monster) creatures.get(2);
        String expected1 = "Character: Ushabti\nHP: 20\nAC: 10\nMovement: 3\nWeapon: Dagger, Greatbow\nAttack bonus: 5\nBackstory: ";
        assertEquals(expected1, m1.toString(), "toString method did not output correct values");

        //Altering some values
        m1.setHp(30);
        m1.addNewWeapon(new Weapon("Javelin", 8, true, 1, ""));

        String expected2 = "Character: Ushabti\nHP: 30\nAC: 10\nMovement: 3\nWeapon: Dagger, Greatbow, Javelin\nAttack bonus: 5\nBackstory: ";
        //Checking for change in result
        assertEquals(expected2, m1.toString(), "toString method did not output correct values after change");
    }

    @Test
    void monsterMove() {
        Monster m1 = (Monster) creatures.get(2);
        Character c1 = (Character) creatures.get(0);
        Character c2 = (Character) creatures.get(1);

        // Checking if m1 moves to the closest Character
        m1.setNewPos(11, 11);
        c1.setNewPos(13, 11);
        c2.setNewPos(8, 11);
        m1.monsterMove(creatures);
        assertTrue(m1.getxPos() == 12 && m1.getyPos() == 11, "Did not move to closest Character");

        //Checking if m1 moves towards the closest Character when all Characters are out of range
        m1.setNewPos(10, 10);
        c1.setNewPos(15, 12);
        c2.setNewPos(3, 1);
        m1.monsterMove(creatures);
        assertTrue(m1.getxPos() == 13 && m1.getyPos() == 11, "Did not move toward closest creature");

        //Checking if m1 does not move when all its weapons are ranged
        Monster m3 = (Monster) creatures.get(4);
        m3.setNewPos(10, 10);
        m3.monsterMove(creatures);
        assertTrue(m3.getxPos() == 10 && m3.getyPos() == 10, "Monster moved");
    }

    @Test
    void monsterAttack() {
        Character c1 = (Character) creatures.get(0);
        Character c2 = (Character) creatures.get(1);
        Monster m1 = (Monster) creatures.get(2);
        Monster m2 = (Monster) creatures.get(3);
        Monster m3 = (Monster) creatures.get(4);

        // Checking if m1 attacks the closest Character
        m1.setNewPos(11, 11);
        c1.setNewPos(15, 11);
        c2.setNewPos(5, 5);
        for(int i = 0; i < 100; i++){
            m1.monsterAttack(creatures);
        }
        assertTrue(c1.getHp() < c1.getInitialHp(), "Did not attack closest creature");

        // Checking if m2 does not attack when out of range and all weapons are melee
        m2.setNewPos(3, 3);
        for(int i = 0; i < 100; i++){
            m2.monsterAttack(creatures);
        }
        assertTrue(c2.getHp() == c2.getInitialHp(), "Attacked a Character out of range");

        // Checking if m2 attack when in range and all weapons are melee
        m2.setNewPos(4, 4);
        for(int i = 0; i < 100; i++){
            m2.monsterAttack(creatures);
        }
        assertTrue(c2.getHp() < c2.getInitialHp(), "Did not attack Character within melee range");

        c1.setHp(c1.getInitialHp());
        c2.setHp(c2.getInitialHp());

        // Checking if m3 does not attack Character adjacent to it when all weapons are ranged,
        m3.setNewPos(6, 6);
        c2.setNewPos(6, 5);
        c1.setNewPos(13, 13);
        for(int i = 0; i < 100; i++){
            m3.monsterAttack(creatures);
        }
        assertTrue(c2.getHp() == c2.getInitialHp(), "Attacked adjacent Character when all weapons are ranged");
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
        Monster m1 = (Monster) creatures.get(2);
        Monster m2 = (Monster) creatures.get(3);
        Character c1 = (Character) creatures.get(0);

        // Checking if m1 moves towards c1
        m1.setNewPos(0, 15);
        c1.setNewPos(5, 10);
        m1.moveToward(c1, creatures);
        assertTrue(m1.getxPos() == 3 && m1.getyPos() == 12, "Did not move to the correct position");

        // Checking if m1 doeas not move on top of m2
        m1.setNewPos(0, 15);
        m2.setNewPos(3, 12);
        m1.moveToward(c1, creatures);
        assertTrue(m1.getxPos() == 3 && m1.getyPos() == 11, "Moved on top of m2");
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
        Monster m1 = (Monster) creatures.get(2);
        Character c1 = (Character) creatures.get(0);
        Monster m2 = (Monster) creatures.get(3);

        // Checking if m1 moves next to c1
        c1.setNewPos(12, 2);
        m1.setNewPos(10, 4);
        m1.moveTo(c1, creatures);
        assertTrue(m1.getxPos() == 11, "Did not move to right place");
        assertTrue(m1.getyPos() == 3, "Did not move to right place");

        // Checking if m1 does not move on top of m2
        m1.setNewPos(10, 4);
        m2.setNewPos(11, 3);
        m1.moveTo(c1, creatures);
        assertTrue(m1.getxPos() == 11 && m1.getyPos() == 2, "Did not move away from m2 in the correct way");

    }

    @Test
    void dontStepOnOthers() {
        Monster m1 = (Monster) creatures.get(2);
        Character c1 = (Character) creatures.get(0);

        // Checking if the monster moves one grid down
        c1.setNewPos(9, 9);
        m1.setNewPos(10, 9);
        ArrayList<Integer> pos = m1.dontStepOnOthers(m1.getxPos(), m1.getyPos(), c1);
        m1.setNewPos(pos.get(0), pos.get(1));
        assertTrue(m1.getxPos() == 10 && m1.getyPos() == 10, "Did not step in the right direction");

        // Checking if the monster does not try to move outside the map
        c1.setNewPos(0, 0);
        m1.setNewPos(0, 1);
        pos = m1.dontStepOnOthers(m1.getxPos(), m1.getyPos(), c1);
        m1.setNewPos(pos.get(0), pos.get(1));
        assertTrue(m1.getxPos() == 1 && m1.getyPos() == 0, "Did not step in the right direction");
    }

    @Test
    void direction() {
        Monster m1 = (Monster) creatures.get(2);
        int direction = m1.direction(10, 5);
        assertTrue(direction == -1, "Method gives wrong direction");
    }

    @Test
    void relativePos() {
        Monster m1 = (Monster) creatures.get(2);
        Character c1 = (Character) creatures.get(0);

        //Checking if relativePos() returns the correct values
        c1.setNewPos(5, 5);
        m1.setNewPos(5, 6);
        int relativeX = m1.relativePos(m1.getxPos(), c1.getxPos());
        int relativeY = m1.relativePos(m1.getyPos(), c1.getyPos());
        assertTrue(relativeX == 0, "Wrong relative xPos");
        assertTrue(relativeY == -1, "Wrong relative yPos");

        // Checking relativePos() again, but from a new possition
        m1.setNewPos(4, 5);
        relativeX = m1.relativePos(m1.getxPos(), c1.getxPos());
        relativeY = m1.relativePos(m1.getyPos(), c1.getyPos());
        assertTrue(relativeX == 1, "Wrong relative xPos");
        assertTrue(relativeY == 0, "Wrong relative yPos");
    }
}