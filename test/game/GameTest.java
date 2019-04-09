package game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class GameTest {
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

        ArrayList<Creature> creatures = new ArrayList<>();
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
    }

    @Test
    void isPlayerTurn() {
    }

    @Test
    void isMonsterTurn() {
    }

    @Test
    void allPlayersReadyForNewLevel() {
    }

    @Test
    void isLevelCleared() {
    }

    @Test
    void isGameOver() {
    }

    @Test
    void monsterAction() {
    }

    @Test
    void attackRange() {
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
