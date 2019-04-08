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
        Character c2 = (Character) creatures.get(1);
        Monster m2 = (Monster) creatures.get(2);
        int hit = c2.hit();
        boolean hitSuccess = c2.hitSuccess(hit, m2);
        if(hitSuccess){
            //Checking if hitSuccess is true when hit is higher than the targets ac
            assertTrue(hit >= m2.getAc(), "Hit on higher armor class");
        }
        else{
            //Checking if hitSuccess is false when hit is lower than the target ac
            assertTrue(hit < m2.getAc(), "Miss on lower armor class");
        }
    }

    @Test
    void moveCreature() {
        Character c1 = (Character) creatures.get(0);
        Character c2 = (Character) creatures.get(1);
        boolean moved = c1.moveCreature(2, 6, creatures);
        assertTrue(c1.getxPos() == 2 && c1.getyPos() == 5, "Cannot move on top of another creature");
        moved = c1.moveCreature(2, 7, creatures);
        assertTrue(c1.getxPos() == 2 && c1.getyPos() == 7, "Did not move to the valid space");
        moved = c1.moveCreature(15, 15, creatures);
        assertTrue(c1.getxPos() == 2 && c1.getyPos() == 7, "Moved out of range");
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

    @Test
    void setHp() {
        Character c1 = (Character) creatures.get(0);
        c1.setHp(100);
        // Checking if hp was updated to 100
        assertTrue(c1.getHp() == 100, "Did not update hp");
    }

    @Test
    void setNewPos() {
    }

    @Test
    void toStringTest() {
    }

    @Test
    void setReadyForNewLevel() {
    }
}