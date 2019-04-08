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
        Character c2 = new Character(2, 4, "Ranger", 20, 10, 3, 6, 5, "", 2, 6, null, c2weapons);

        ArrayList<Weapon> m1weapons = new ArrayList<>();
        Weapon m1w1 = new Weapon("Claw", 6, false, 2, "");
        m1weapons.add(m1w1);
        Monster m1 = new Monster(3, 5, "Bear", 20, 10, 3, 6, 5, "", 8, 5, null, m1weapons);

        ArrayList<Weapon> m2weapons = new ArrayList<>();
        Weapon m2w1 = new Weapon("Greatbow", 6, true, 2, "");
        m1weapons.add(m1w1);
        Monster m2 = new Monster(4, 8, "Ushabti", 20, 10, 3, 6, 5, "", 8, 6, null, m2weapons);

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
        Character c1 = (Character) creatures.get(0);
        Monster m1 = (Monster) creatures.get(2);
        int m1InitialHp = m1.getHp();
        //Checking if damage was dealt against the monster depending on hit or miss
        if (c1.attackCreature(m1, 0)){
            //Hit successful
            assertTrue(m1.getHp() < m1InitialHp, "Successful attack did not deal damage");
        }
        else{
            //Hit not successful
            assertTrue(m1.getHp() == m1InitialHp, "Unsuccessful attack dealt damage");
        }

    }

    @Test
    void hit() {
        Character c1 = (Character) creatures.get(0);
        for (int i = 0; i < 100; i++){
            int hit = c1.hit();
            int max = 20 + c1.getAttackBonus();
            int min = 1 + c1.getAttackBonus();
            //Checking if hit value is between minimum and maximum
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
        Character c1 = (Character) creatures.get(0);
        c1.updateDead();
        //Checking if character still is not dead
        assertTrue(c1.getHp() > 0 && !c1.isDead(), "Character is announced dead with full hp");

        //Killing character
        c1.setHp(0);
        c1.updateDead();
        //Checking if isDead variable has been updated
        assertTrue(c1.isDead(), "Character not announced dead even though hp is less than 0");
    }
}