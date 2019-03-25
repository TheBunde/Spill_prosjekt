/*package Gamelogic;


<<<<<<< HEAD

import game.Creature;
import game.Weapon;
=======
import game.*;
>>>>>>> c125bee8d82a16dd5c1b30646b06e2680460f9db


import java.util.ArrayList;

public class GameMethods {
    private Dice dice;

    public GameMethods() {

    }

    public ArrayList initative() {
        ArrayList<Creature> creatures = new ArrayList<>();
        int[][] initiativeOrder = new int[creatures.size()][creatures.size()];
        ArrayList<Creature> turn = new ArrayList<>();
        for (int i = 0; i < creatures.size(); i++) {
            int roll = dice.roll(20);
            initiativeOrder[0][i] = i;
            initiativeOrder[1][i] = roll;
            //print witch user got witch value?
        }
        for (int start = 0; start < initiativeOrder[1].length; start++) {
            int highest = start;
            for (int j = start; j < initiativeOrder[1].length; j++) {
                if (initiativeOrder[1][j] > initiativeOrder[1][highest]) {
                    highest = j;
                }
            }
            int helperIndex = initiativeOrder[0][highest];
            int helperRoll = initiativeOrder[1][highest];
            initiativeOrder[0][highest] = initiativeOrder[0][start];
            initiativeOrder[1][highest] = initiativeOrder[1][start];
            initiativeOrder[0][start] = helperIndex;
            initiativeOrder[1][start] = helperRoll;
        }
        for (int i = 0; i < creatures.size(); i++) {
            int playerNr = initiativeOrder[0][i];
            Creature getFrom = creatures.get(playerNr);
            turn.add(getFrom);
        }
        return turn;
    }


    public void attack(Creature playerNow, Creature monster, Weapon weapon) {
        int roll = dice.roll(20) + playerNow.getAttackBonus();
        int acMonster = monster.getAc();
        if (roll >= acMonster) {
            int damage = 0;
            for(int i = 0; i < weapon.getDiceAmount(); i++){
                damage = damage + dice.roll(weapon.getDamageDice());
            }
            int monsterHP = monster.getHp() - damage;
            monster.setHp(monsterHP);
            //sending new monster hp to database 
            System.out.println("Hit");
        } else {
            System.out.println("Miss");
        }
    }

<<<<<<< HEAD
    public boolean movePlayer(Creature palyerNow) {
        int xCoordinate = palyerNow.getxPos();
        int yCoordinate = palyerNow.getyPos();
        int XMax = xCoordinate + palyerNow.getMovement();
        int XMix = xCoordinate - palyerNow.getMovement();
        int YMax = yCoordinate + palyerNow.getMovement();
        int YMix = yCoordinate - palyerNow.getMovement();
=======
    public boolean movePlayer(Creature playerNow) {
        int xCoordinate = playerNow.getxPos();
        int yCoordinate = playerNow.getyPos();
        int XMax = xCoordinate + playerNow.getMovement();
        int XMix = xCoordinate - playerNow.getMovement();
        int YMax = yCoordinate + playerNow.getMovement();
        int YMix = yCoordinate - playerNow.getMovement();
>>>>>>> c125bee8d82a16dd5c1b30646b06e2680460f9db
        int wantedX = 0;
        int wantedY = 0;
        boolean X = false;
        boolean Y = false;
        if(wantedX <= XMax && wantedX >= XMix){
            X = true;
        }
        if(wantedY <= YMax && wantedY >= YMix){
            Y = true;
        }
        if(X && Y){
            playerNow.setNewPos(wantedX, wantedY);
            System.out.println("You moved");
            //sending new x y to database
            return true;
        }else{
            System.out.println("Out of range. Try again");
            return false;
        }
    }


    public boolean nearMonster(Creature playerNow, Creature monster) {
        boolean near = false;
        int xPos = playerNow.getxPos();
        int yPos = playerNow.getxPos();
        int maxX = xPos + 1;
        int maxY = yPos + 1;
        int minX = xPos - 1;
        int minY = yPos - 1;
        int XForMonster = monster.getxPos();
        int YForMonster = monster.getyPos();
        boolean X = false;
        boolean Y = false;
        if (minX == XForMonster || xPos == XForMonster || maxX == XForMonster) {
            X = true;
        }
        if (minY == YForMonster || yPos == YForMonster || maxY == YForMonster) {
            Y = true;
        }
        if (X && Y) {
            near = true;
        }
        return near;
    }

    public int monsterAttack(Creature target, Creature monster) {
        ArrayList <Weapon> monsterWeapon = new ArrayList<>();
        int roll = dice.roll(20) + monster.getAttackBonus();
        int targetAC = target.getAc();
        int targetHP = target.getHp();
        if(roll >= targetAC){
            int weaponRoll = dice.roll(monsterWeapon.size());
            int damage = 0;
            for(int i = 0; i < monsterWeapon.get(weaponRoll).getDiceAmount(); i++){
                damage = damage + dice.roll(monsterWeapon.get(weaponRoll).getDamageDice());
            }
            targetHP = target.getHp() - damage;
            target.setHp(targetHP);
            //sending new target hp to database
            System.out.println("Hit");
        }else{
            System.out.println("Miss");
        }
        return targetHP;
    }

    /*
    public int monsterMovement (Creature monster){


        ArrayList<Creature> creatures = new ArrayList<>();
        boolean first = true;
        Creature target = null;
        int xDistance = monster.getMovement();
        int yDistance = monster.getMovement();
        int targetHP = 0;

        for (Creature i : creatures) {
            if ((i.getxCordinate() >= (monster.getxCordinate() - monster.getMovement() - 1)) && (i.getxCordinate() <= (monster.getxCordinate() + monster.getMovement() + 1))
                    && (i.getyCordinate() >= (monster.getyCordinate() - monster.getMovement() - 1)) && (i.getyCordinate() <= (monster.getyCordinate() + monster.getMovement() + 1)) && i != monster) {
                if (Math.abs(i.getyCordinate() - monster.getyCordinate()) <= xDistance && Math.abs(i.getyCordinate() - monster.getyCordinate()) <= yDistance) {
                    xDistance = i.getxCordinate() - monster.getxCordinate();
                    yDistance = i.getyCordinate() - monster.getyCordinate();
                    target = i;
                    if (xDistance < 0 && Math.abs(xDistance) != monster.getMovement()) {
                        xDistance++;
                    } else if (xDistance > 0 && Math.abs(xDistance) != monster.getMovement()) {
                        xDistance--;
                    }
                    if (yDistance < 0 && Math.abs(yDistance) != monster.getMovement()) {
                        yDistance++;
                    } else if (yDistance > 0 && Math.abs(xDistance) != monster.getMovement()) {
                        yDistance++;
                    }
                }
            }
        }
        if (target == null) {
            for (Creature i : creatures) {
                if (Math.abs(i.getxCordinate() - monster.getxCordinate()) < xDistance && Math.abs(i.getyCordinate() - monster.getyCordinate()) < yDistance || first) {
                    if (i.getxCordinate() > monster.getxCordinate() && (i.getxCordinate() - monster.getxCordinate()) > monster.getMovement()) {
                        xDistance = monster.getMovement();
                    } else if (i.getxCordinate() > monster.getxCordinate()) {
                        xDistance = i.getxCordinate() - monster.getxCordinate();
                    }
                    if (i.getxCordinate() < monster.getxCordinate() && (monster.getxCordinate() - i.getxCordinate()) > monster.getMovement()) {
                        yDistance = -monster.getMovement();
                    } else if (i.getxCordinate() < monster.getxCordinate()) {
                        yDistance = -(monster.getyCordinate() - i.getyCordinate());
                    }
                    first = false;
                }
            }
            monster.setxCordinate(monster.getxCordinate() + xDistance);
            monster.setyCordinate(monster.getyCordinate() + yDistance);
        } else if (target != null) {
            monster.setxCordinate(monster.getxCordinate() + xDistance);
            monster.setyCordinate(monster.getyCordinate() + yDistance);
            targetHP = monsterAttack(target, monster);
        }
        return targetHP;
    }*/

