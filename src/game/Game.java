package game;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Main.*;
import Database.*;
import javafx.application.Platform;

public class Game {
    private ArrayList<Creature> creatures = new ArrayList<>();
    public game.Character playerCharacter;
    private Database db = Main.db;
    private int turn = 0;

    public Game(){
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
        updateCreatureData();
        if (isMonsterTurn() && Main.user.isHost()){
            new Thread(new Runnable(){
                @Override public void run(){
                    monsterAction();
                }
            }).start();
        }
    }

    public void updateCreatureData(){
        turn = db.fetchPlayerTurn();
        for (int i = 0; i < creatures.size(); i++){
            int playerId = creatures.get(i).getPlayerId();
            ArrayList<Integer> newPos = db.fetchPlayerPos(playerId);
            int newHp = db.fetchPlayerHp(playerId);
            if (playerId != Main.user.getPlayerId()){
                creatures.get(i).setNewPos(newPos.get(0), newPos.get(1));
            }
            creatures.get(i).setHp(newHp);
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
            if (this.creatures.get(i) instanceof Monster){
                monstersIndex.add(i);
            }
        }
        return monstersIndex;
    }

    public Creature getCreature(int index){
        return this.creatures.get(index);
    }

    public int getAmountOfCreatures(){
        return this.creatures.size();
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

    public void monsterAction(){
        Monster monster = ((Monster) creatures.get(turn % creatures.size()));
        monster.monsterMove(creatures);
        monster.monsterAttack(creatures);
        pushCreatureData();
        endTurn();
    }

    public void endTurn(){
        turn++;
        db.incrementPlayerTurn(turn);
    }
}
