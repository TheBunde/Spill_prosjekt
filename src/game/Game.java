package game;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import GUI.BattlefieldController;
import Main.*;
import Database.*;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;

public class Game {
    private ArrayList<Creature> creatures = new ArrayList<>();
    public game.Character playerCharacter;
    private Database db = Main.db;
    private int turn = 0;
    public Level level;

    public Game(){
        level = new Level(1, 16, "Forest-map.png");
        level.updateLevel();

        if (Main.user.isHost()){
            this.addNewMonstersToLobby(1);
        }
        creatures = db.fetchCreaturesFromLobby();
        for (int i = 0; i < this.creatures.size(); i++){
            if (this.creatures.get(i).getPlayerId() == Main.user.getPlayerId()){
                playerCharacter = (game.Character) this.creatures.get(i);
            }
        }
    }

    public void init(){

    }

    public void update(){
        if (this.isPlayerTurn()){
            pushCreatureData();
        }
        if(playerCharacter.getHp() <= 0 && !playerCharacter.isDead()){
            db.addChatMessage(Main.user.getUsername() + " died", true);
        }
        updateCreatureData();
        if (Main.user.isHost()){
            if (isMonsterTurn()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        monsterAction();
                    }
                }).start();
            }
            if (this.isLevelCleared()){
                Main.db.setLevel(Main.user.getLobbyKey(), this.level.getLevelId() + 1);
                addNewMonstersToLobby(this.level.getLevelId() + 1);
                changeToNewLevel();
            }
        }
        else{
            if (this.isLevelCleared()) {
                changeToNewLevel();
            }
        }
    }

    public void addNewMonstersToLobby(int levelId){
        ArrayList<Integer> creatureIds = Main.db.fetchMonstersFromLevel(levelId);
        for (int i = 0; i < creatureIds.size(); i++){
            Main.db.createPlayer(creatureIds.get(i), false);
        }
    }

    public void changeToNewLevel(){
        int newLevel_id = Main.db.fetchLevelId(Main.user.getLobbyKey());
        if (this.level.getLevelId() < newLevel_id){
            this.level.setLevelId(newLevel_id);
            this.level.updateLevel();
        }
        GridPane mapGrid = (GridPane)this.creatures.get(0).getPawn().getParent();
        for (Creature c : this.creatures){
            mapGrid.getChildren().remove(c.getPawn());
        }
        this.creatures = Main.db.fetchCreaturesFromLobby();
        for (Creature c : this.creatures){
            c.setPawnSize(mapGrid.getPrefWidth()/16, mapGrid.getPrefHeight()/16);
            mapGrid.add(c.getPawn(), c.getxPos(), c.getyPos());
        }
    }

    public void updateCreatureData(){
        turn = db.fetchPlayerTurn();
        for (int i = 0; i < creatures.size(); i++) {
            Creature c = creatures.get(i);
            int playerId = c.getPlayerId();
            ArrayList<Integer> newPos = db.fetchPlayerPos(playerId);
            int newHp = db.fetchPlayerHp(playerId);
            if (playerId != Main.user.getPlayerId()) {
                c.setNewPos(newPos.get(0), newPos.get(1));
            }
            c.setHp(newHp);
            c.updateDead();
            if (c.isDead()){
                c.setPawnImage("gravestone.png");
            }
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
            if(i instanceof Character && i != playerCharacter){
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

    public boolean isPositionAvailable(int x, int y){
        boolean available = true;
        for (Creature c : this.creatures){
            if (c.getxPos() == x && c.getyPos() == y){
                available = false;
            }
        }
        return available;
    }

    public Creature getCreatureFromPosition(int x, int y){
        for (Creature c : this.creatures){
            if (c.getxPos() == x && c.getyPos() == y){
                return c;
            }
        }
        return null;
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

    public void monsterAction(){
        Monster monster = ((Monster) creatures.get(turn % creatures.size()));
        if (monster.isDead()){
            endTurn();
        }
        else {
            monster.monsterMove(creatures);
            monster.monsterAttack(creatures);
            pushCreatureData();
            endTurn();
        }
    }

    public void endTurn(){
        turn++;
        db.incrementPlayerTurn(turn);
    }

    public Level getLevel(){
        return this.level;
    }
}
