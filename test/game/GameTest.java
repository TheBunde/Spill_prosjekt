package game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class GameTest {
    ArrayList<Creature> creatures;
    Game game;
    @BeforeEach
    void setUp() {
        game = new Game();

        ArrayList<Weapon> c1weapons = new ArrayList<>();
        Weapon c1w1 = new Weapon("Sword", 6, false, 2, "");
        c1weapons.add(c1w1);
        Character c1 = new Character(1, 1, "Warrior", 20, 10, 3, 6, 5, "", 2, 5, null, c1weapons);

        ArrayList<Weapon> c2weapons = new ArrayList<>();
        Weapon c2w1 = new Weapon("Bow", 6, true, 2, "");
        c2weapons.add(c2w1);
        Character c2 = new Character(2, 4, "Ranger", 20, 10, 3, 6, 5, "", 2, 6, null, c2weapons);

        ArrayList<Weapon> m1weapons = new ArrayList<>();
        Weapon m1w1 = new Weapon("Claw", 6, false, 2, "");
        m1weapons.add(m1w1);
        Monster m1 = new Monster(3, 5, "Bear", 20, 10, 3, 6, 5, "", 8, 5, null, m1weapons);

        ArrayList<Weapon> m2weapons = new ArrayList<>();
        Weapon m2w1 = new Weapon("Greatbow", 6, true, 2, "");
        m2weapons.add(m2w1);
        Monster m2 = new Monster(4, 8, "Ushabti", 20, 10, 3, 6, 5, "", 8, 6, null, m2weapons);

        creatures = new ArrayList<>();
        creatures.add(c1);
        creatures.add(c2);
        creatures.add(m1);
        creatures.add(m2);

        game.setCreatures(creatures);

        Level level = new Level(1, 16, null);

        game.setLevel(level);

    }

    @AfterEach
    void tearDown() {
        game = null;
        creatures = null;
    }

    @Test
    void isPlayerTurn() {
    }

    @Test
    void isMonsterTurn() {
        game.incrementPlayerTurn();
        //Turn is 1
        assertFalse(game.isMonsterTurn(), "Error, not yet monsters turn");

        game.incrementPlayerTurn();
        //Turn is 2
        assertTrue(game.isMonsterTurn(), "Error, is monsters turn, but returned false");
    }

    @Test
    void allPlayersReadyForNewLevel() {
        Character c1 = (Character) creatures.get(0);
        Character c2 = (Character) creatures.get(1);
        Monster m1 = (Monster) creatures.get(2);

        //Checking if allPlayersReadyForNewLevel is true when all Creatures are ready
        c1.setReadyForNewLevel(true);
        c2.setReadyForNewLevel(true);
        m1.setReadyForNewLevel(true);
        boolean allReady = game.allPlayersReadyForNewLevel();
        assertTrue(allReady, "allReady not true when all Creatures are");

        //Checking if allPlayersReadyForNewLevel is false when not all Creatures are ready
        c2.setReadyForNewLevel(false);
        allReady = game.allPlayersReadyForNewLevel();
        assertFalse(allReady, "allReady is true when not all Creatures a");
    }

    @Test
    void isLevelCleared() {
        Character c1 = (Character) game.getCreatures().get(0);
        Character c2 = (Character) game.getCreatures().get(1);
        Monster m1 = (Monster) game.getCreatures().get(2);
        Monster m2 = (Monster) game.getCreatures().get(3);

        //Level is cleared when all monsters are dead
        m1.setHp(0);
        m1.updateDead();
        //One of two monsters are dead
        assertFalse(game.isLevelCleared(), "Method returns true when not all monsters are dead");

        m2.setHp(0);
        m2.updateDead();
        //Both monsters are dead
        assertTrue(game.isLevelCleared(), "Method returns false even though all monsters are dead");
    }

    @Test
    void isGameOver() {
        Character c1 = (Character) game.getCreatures().get(0);
        Character c2 = (Character) game.getCreatures().get(1);
        Monster m1 = (Monster) game.getCreatures().get(2);
        Monster m2 = (Monster) game.getCreatures().get(3);

        //Game over when all characters are dead
        c1.setHp(0);
        c1.updateDead();
        //One of two character are dead
        assertFalse(game.isGameOver(), "Method returns true when not all characters");

        c2.setHp(0);
        c2.updateDead();
        //Both characters are dead
        assertTrue(game.isGameOver(), "Method returns false even though all characters are dead");
    }

    @Test
    void monsterAction() {
        Character c1 = (Character) creatures.get(0);
        Monster m1 = (Monster) creatures.get(2);

        // Checking if m1 moves when it is its turn
        game.setPlayerTurn(2);
        m1.setNewPos(1, 1);
        c1.setNewPos(1,3);
        game.monsterAction();
        try {
            Thread.sleep(300); // Must use Thread.sleep() because monsterAction() starts a new thread
        }catch (InterruptedException ie){
            ie.printStackTrace();
        }
        assertTrue(m1.getxPos() == 1 && m1.getyPos() == 2, "m1 did not move correctly");

        // Checking if m1 attacks when it is its turn and Character is in range
        for(int i = 0; i < 20; i++){
            game.monsterAction();
            try {
                Thread.sleep(100);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
        assertTrue(c1.getHp() < c1.getInitialHp(), "m1 did not attack");
    }

    @Test
    void attackRange() {
        Character c1 = (Character) creatures.get(0);
        Character c2 = (Character) creatures.get(1);
        Monster m1 = (Monster) creatures.get(2);

        //Checking if attackRange() is true when m1 is adjacent to c1, and c1 has melee weapon
        m1.setNewPos(13, 13);
        c1.setNewPos(13, 14);
        game.setPlayerCharacter(c1);
        boolean inRange = game.attackRange(m1, true);
        assertTrue(inRange, "attackRange() false when in attack range");

        //Checking if attackRange() is false when m1 is not adjacent to c1, and c1 has melee weapon
        c1.setNewPos(13, 15);
        inRange = game.attackRange(m1, true);
        assertFalse(inRange, "attackRange() true when out of range");

        //Checking if attackRange() is true when m1 is not adjacent to c2, and c2 has ranged weapon
        c2.setNewPos(11, 13);
        m1.setNewPos(13, 13);
        game.setPlayerCharacter(c2);
        inRange = game.attackRange(m1, false);
        assertTrue(inRange, "attackRange() false when in attack range with ranged weapon");

        //Checking if attackRange() is false when m1 is adjacent to c2, and c2 has ranged weapon
        c2.setNewPos(12, 13);
        inRange = game.attackRange(m1, false);
        assertFalse(inRange, "attackRange() true when too close to target");
    }

    @Test
    void toStringTest() {
    }

    @Test
    void getCharacters() {
    }

    @Test
    void getMonsters() {
    }
}
