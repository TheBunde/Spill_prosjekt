package game;

import java.util.ArrayList;
import Main.*;
import Database.*;

public class Game {
    private ArrayList<Creature> creatures = new ArrayList<>();
    public game.Character playerCharacter;
    private Database db = Main.db;
    private int turn = 1;

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
        pushCreatureData();
        updateCreatureData();
        if (isMonsterTurn()){
            monsterAction();
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
        for (Creature c : creatures){
            if (Main.user.isHost()){
                if (c instanceof Monster){
                    int playerId = c.getPlayerId();
                    int posX = c.getxPos();
                    int posY = c.getyPos();
                    db.setPos(posX, posY, playerId);
                    int hp = c.getHp();
                    db.setHp(hp, playerId);
                }
            }
            if (c == playerCharacter){
                int playerId = c.getPlayerId();
                int posX = c.getxPos();
                int posY = c.getyPos();
                db.setPos(posX, posY, playerId);
                int hp = c.getHp();
                db.setHp(hp, playerId);
            }
        }


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
        update();
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

    public void monsterAction(){
        ((Monster) creatures.get(turn % creatures.size())).monsterAction(creatures);
        endTurn();
    }

    public void endTurn(){
        turn++;
        db.incrementPlayerTurn(turn);
    }
}
