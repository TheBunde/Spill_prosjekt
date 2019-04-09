package game;

import java.util.ArrayList;

import database.*;
import javafx.scene.layout.GridPane;
import main.*;

public class Game {
    private ArrayList<Creature> creatures = new ArrayList<>();
    public game.Character playerCharacter;
    private Database db = Main.db;
    private int turn = 0;
    private int amountOfLevels = Main.db.fetchAmountOfLevels();
    public Level level;

    public Game(){
        level = new Level(1);
        if (Main.user.isHost()){
            this.addNewMonstersToLobby(1, Main.db.fetchPlayerCount());
            Main.db.setBattlefieldReady(Main.user.getLobbyKey());

        }
        else{
            while (!Main.db.fetchBattlefieldReady(Main.user.getLobbyKey())){
                try{
                    Thread.sleep(300);
                }
                catch (InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        }
        this.creatures = db.fetchCreaturesFromLobby();

        for (int i = 0; i < this.creatures.size(); i++){
            if (this.creatures.get(i).getPlayerId() == Main.user.getPlayerId()){
                playerCharacter = (game.Character) this.creatures.get(i);
            }
        }
    }

    public void update(){
        if (this.isPlayerTurn()){
            pushCreatureData();
        }
        else {
            updateCreatureData();
        }
        handleCreatureData();
        if (Main.user.isHost()) {
            monsterAction();
        }
    }

    public void resetTurn(){
        if (Main.user.isHost()){
            Main.db.incrementPlayerTurn(0);
        }
        turn = 0;
    }

    public void pushNewLevel(){
        int playerAmount = this.getCharacters().size();
        addNewMonstersToLobby(this.level.getLevelId() + 1, playerAmount);
        Main.db.setLevelId(Main.user.getLobbyKey(), this.level.getLevelId() + 1);
        if (this.level.getLevelId() + 1 <= this.getAmountOfLevels()) {
            if (this.level.getLevelId() + 1 == this.getAmountOfLevels()){
                for (int i = 0; i < playerAmount; i++){
                    Main.db.setPos(7 - (int)Math.round(playerAmount/2) + 2*i, 13, this.getCharacters().get(i).getPlayerId());
                }
            }
            upgradePlayerStats();
        }
        this.resetTurn();
    }

    public void upgradePlayerStats(){
        ArrayList<Character> characters = this.getCharacters();
        for (Character c : characters){
            Main.db.setHp((int)(c.getInitialHp()*1.25), c.getPlayerId());
            Main.db.setDamageBonus((int)(c.getDamageBonus()*1.25), c.getPlayerId());
        }
        Main.db.addChatMessage("You feel the energy from this battle, you are now more powerful!", true);
    }

    public void addNewMonstersToLobby(int levelId, int playerAmount){
        ArrayList<Integer> creatureIds = Main.db.fetchMonstersFromLevel(levelId, playerAmount);
        for (int i = 0; i < creatureIds.size(); i++){
            int playerId = Main.db.createPlayer(creatureIds.get(i), false);
            int posX = (int)Math.floor(Math.random()*16);
            int posY = (int)Math.floor(Math.random()*16);
            if (creatureIds.get(i) >= 13){
                posX = 8;
                posY = 7;
            }
            Main.db.createCreature(playerId, creatureIds.get(i), posX, posY);
        }
    }


    public void updateCreatureData(){
        this.updatePlayerTurn();
        for (int i = 0; i < creatures.size(); i++) {
            Creature c = creatures.get(i);
            int playerId = c.getPlayerId();
            ArrayList<Integer> newPos = db.fetchPlayerPos(playerId);
            int newHp = db.fetchPlayerHp(playerId);
            if (playerId != Main.user.getPlayerId()) {
                c.setNewPos(newPos.get(0), newPos.get(1));
            }
            c.setHp(newHp);
        }
    }

    public void pushCreatureData(){
        for (Creature c : creatures) {
            int playerId = c.getPlayerId();
            int posX = c.getxPos();
            int posY = c.getyPos();
            db.setPos(posX, posY, playerId);
            int hp = c.getHp();
            db.setHp(hp, playerId);
        }
    }

    public void handleCreatureData(){
        if (this.playerCharacter.getHp() <= 0 && !this.playerCharacter.isDead()){
            Main.db.addChatMessage(Main.user.getUsername() + " died", true);
        }
        for (Creature c : this.creatures) {
            c.updateDead();
        }
    }

    public void updatePlayerTurn(){
        this.turn = db.fetchPlayerTurn();
    }

    public ArrayList<Integer> getMonstersIndex(){
        ArrayList<Integer> monstersIndex = new ArrayList<>();
        for (int i = 0; i < this.creatures.size(); i++){
            if (this.creatures.get(i) instanceof Monster && !(this.creatures.get(i).isDead())){
                monstersIndex.add(i);
            }
        }
        return monstersIndex;
    }

    public Creature getCreature(int index){
        return this.creatures.get(index);
    }

    public Creature getYourCreature(){
        for(Creature i: creatures){
            if(i.getPlayerId() == Main.user.getPlayerId()){
                return i;
            }
        }
        return null;
    }

    public int getAmountOfCreatures(){
        return this.creatures.size();
    }

    public ArrayList<Creature> getCreatures(){
        return this.creatures;
    }

    public ArrayList<Character> getCharacters(){
        ArrayList<Character> characters= new ArrayList<>();
        for(Creature i: creatures){
            if(i instanceof Character){
                characters.add((Character) i);
            }
        }
        return characters;
    }

    public ArrayList<Integer> getPos(int index){
        ArrayList<Integer> pos = new ArrayList<>();
        pos.add(this.creatures.get(index).getxPos());
        pos.add(this.creatures.get(index).getyPos());
        return pos;
    }

    public boolean isPlayerTurn(){
        if(creatures.get(turn % creatures.size()).getPlayerId() == Main.user.getPlayerId()){
            return true;
        }
        return false;
    }

    public boolean isMonsterTurn(){
        if(creatures.get(turn % creatures.size()) instanceof Monster){
            return true;
        }
        return false;
    }

    public void updatePlayersReadyForNewLevel(){
        ArrayList<Boolean> playersReadyForNewLevel = Main.db.fetchPlayersReadyForLevel();
        for (int i = 0; i < playersReadyForNewLevel.size(); i++){
            if (this.getCharacters().get(i) != this.playerCharacter) {
                this.getCharacters().get(i).setReadyForNewLevel(playersReadyForNewLevel.get(i));
            }
        }
    }

    public boolean allPlayersReadyForNewLevel(){
        this.updatePlayersReadyForNewLevel();
        boolean ready = true;
        ArrayList<Character> characters = this.getCharacters();
        for (Character c : characters){
            if (!c.isReadyForNewLevel()){
                ready = false;
            }
        }
        return ready;
    }

    public void setPlayerReadyForNewLevel(boolean ready){
        this.playerCharacter.setReadyForNewLevel(ready);
        Main.db.setReadyForNewLevel(this.playerCharacter.getPlayerId(), ready);
    }

    public ArrayList<Monster> getMonsters(){
        ArrayList<Monster> monsters = new ArrayList<>();
        for (Creature c : this.creatures){
            if (c instanceof Monster){
                monsters.add((Monster)c);
            }
        }
        return monsters;
    }

    public boolean isLevelCleared(){
        boolean cleared = false;
        boolean allMonstersDead = true;
        ArrayList<Monster> monsters = this.getMonsters();
        for (Monster m : monsters){
            if (!m.isDead()){
                allMonstersDead = false;
            }
        }
        cleared = allMonstersDead;
        return cleared;
    }

    public boolean isGameOver(){
        boolean gameOver = false;
        boolean allCharactersDead = true;
        ArrayList<Character> characters = this.getCharacters();
        for (Character i: characters){
            if (!i.isDead()){
                allCharactersDead = false;
            }
        }
        gameOver = allCharactersDead;
        return gameOver;
    }

    public void changeToNewLevel() {
        this.setPlayerReadyForNewLevel(false);
        int dbLevelId = Main.db.fetchLevelId(Main.user.getLobbyKey());
        this.level.setLevelId(dbLevelId);
        this.level.updateLevel();
        GridPane mapGrid = (GridPane) this.creatures.get(0).getPawn().getParent();
        for (Creature c : this.creatures) {
            mapGrid.getChildren().remove(c.getPawn());
        }
        this.creatures = Main.db.fetchCreaturesFromLobby();
        for (Creature c : this.creatures) {
            if (c.getPlayerId() == Main.user.getPlayerId()) {
                this.playerCharacter = (game.Character) c;
            }
            c.setPawnSize(mapGrid.getPrefWidth() / 16, mapGrid.getPrefHeight() / 16);
            mapGrid.add(c.getPawn(), c.getxPos(), c.getyPos());
        }
        for (Character c : this.getCharacters()){
            c.setReadyForNewLevel(false);
        }
        if (this.level.getLevelId() == this.amountOfLevels){
            //Update rank for user
        }
    }

    public boolean containsMonster(){
        boolean containsmonster = false;
        for (Creature c : this.creatures){
            if (c instanceof Monster){
                containsmonster = true;
            }
        }
        return containsmonster;
    }

    public void monsterAction() {
        if (isMonsterTurn()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Monster monster = ((Monster) creatures.get(turn % creatures.size()));
                    if (monster.isDead()) {
                        endTurn();
                    } else {
                        monster.monsterMove(creatures);
                        monster.monsterAttack(creatures);
                        pushCreatureData();
                        endTurn();
                    }
                }
            }).start();
        }
    }

    public void endTurn(){
        turn++;
        db.incrementPlayerTurn(turn);
    }

    public Level getLevel(){
        return this.level;
    }

    public int getAmountOfLevels() {
        return this.amountOfLevels;
    }

    public boolean attackRange(Monster monster, boolean melee){
        /*if(melee && Math.abs(playerCharacter.getxPos() - monster.getxPos()) == 1 && Math.abs(playerCharacter.getyPos() - monster.getyPos()) == 1){
            return true;
        }else if(!melee && Math.abs(playerCharacter.getxPos() - monster.getxPos()) > 1 || Math.abs(playerCharacter.getyPos() - monster.getyPos()) > 1){
            return true;
        }else{
            return false;
        }*/
        if(melee) {
            if ((Math.abs(playerCharacter.getxPos() - monster.getxPos()) <= 1) && (Math.abs(playerCharacter.getyPos() - monster.getyPos()) <= 1)) {
                System.out.println("\nx distance: " + (Math.abs(playerCharacter.getxPos() - monster.getxPos()) <= 1) + "\n");
                System.out.println("\ny distance: " + (Math.abs(playerCharacter.getyPos() - monster.getyPos()) <= 1) + "\n");
                return true;
            }
        }
        else if(!melee) {
            if ((Math.abs(playerCharacter.getxPos() - monster.getxPos()) > 1) || (Math.abs(playerCharacter.getyPos() - monster.getyPos())) > 1) {
                System.out.println("holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                return true;
            }
        }
        return false;
    }

    public String toString(){
        StringBuilder string = new StringBuilder("");
        string.append("User host: " + Main.user.isHost() + "\n");
        string.append("Player turn: " + this.isPlayerTurn() + "\n");
        string.append("Amount of creatures: " + this.creatures.size() + "\n");
        for (int i = 0; i < this.creatures.size(); i++){
            Creature c = this.creatures.get(i);
            string.append("Creature " + (i+1) + ": " + c.getCreatureName() + " HP: " + c.getHp() + "\n");
        }
        string.append("\n");
        return string.toString();
    }
}
