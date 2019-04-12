package game;

import main.*;
import java.util.ArrayList;
import javafx.scene.layout.GridPane;

/**
 * Runs the game and maintains a connection to the database for pushing and updating game data.
 *
 * @author magnubau, williad, heleneyj
 */
public class Game {
    private ArrayList<Creature> creatures = new ArrayList<>();
    private Character playerCharacter;
    private int playerTurn = 0;
    private int amountOfLevels;
    private Level level;

    /**
     * A constructor for the Game class. It initializes the level.
     * If the user is a host, it adds the monsters to the lobby.
     * It adds all the creatures in te lobby to the ArrayList
     * creatures. It also sets the Creature with player id equal
     * to the users player id, to playerCharacter
     */
    public Game(){
        if(Main.db != null) {
            amountOfLevels = Main.db.fetchAmountOfLevels();
            /* Sets the level to level 1 */
            level = new Level(1);
            /* Host adds monsters to lobby and makes sure they do
            not stand on top of any other Creature
             */
            if (Main.user.isHost()) {
                this.addNewMonstersToLobby(this.level.getLevelId(), Main.db.fetchPlayerCount());
                this.assureNoOverlap();
                Main.db.setBattlefieldReady(Main.user.getLobbyKey());
                /* Waits for host to finish preparations */
            } else {
                while (!Main.db.fetchBattlefieldReady(Main.user.getLobbyKey())) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
            this.creatures = Main.db.fetchCreaturesFromLobby();

            /* Sets playerCharacter to correct Creature */
            for (int i = 0; i < this.creatures.size(); i++) {
                if (this.creatures.get(i).getPlayerId() == Main.user.getPlayerId()) {
                    playerCharacter = (game.Character) this.creatures.get(i);
                }
            }
        }
    }

    /**
     * Fetches and pushes data to the database.<br>
     * This method serves as one cycle of the game.
     */
    public void update(){
        if (this.isPlayerCharacterTurn()){
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

    /**
     * Resets the player turn on client and in the database.<br>
     * Used when changing to a new level.
     */
    public void resetPlayerTurn(){
        if (Main.user.isHost()){
            Main.db.incrementPlayerTurn(0);
        }
        this.setPlayerTurn(0);
    }

    /**
     * Pushes data about the next level to the database.<br>
     */
    public void pushNewLevel(){
        /* Adds new monsters based on amount of players */
        int playerAmount = this.getCharacters().size();
        addNewMonstersToLobby(this.level.getLevelId() + 1, playerAmount);
        Main.db.setLevelId(Main.user.getLobbyKey(), this.level.getLevelId() + 1);
        if (this.level.getLevelId() + 1 <= this.getAmountOfLevels()) {
            if (this.level.getLevelId() + 1 == this.getAmountOfLevels()){
                /*
                 * If next level is last level
                 * Manually sets positions for boss battle
                 */
                for (int i = 0; i < playerAmount; i++){
                    Main.db.setPos(7 - (int)Math.round(playerAmount/2) + 2*i, 13, this.getCharacters().get(i).getPlayerId());
                }
            }
            else {
                assureNoOverlap();
            }
            /* If next level is not last level */
            upgradePlayerStats();
        }
        this.resetPlayerTurn();
    }

    /**
     * Assures that all creatures are placed in unique cells
     */
    public void assureNoOverlap(){
        ArrayList<Creature> newCreatures = Main.db.fetchCreaturesFromLobby();
        for (int i = 0; i < newCreatures.size(); i++){
            for (int j = 0; j < newCreatures.size(); j++) {
                if (j != i) {
                    Creature c1 = newCreatures.get(i);
                    Creature c2 = newCreatures.get(j);
                    if (c1.getxPos() == c2.getxPos() && c1.getyPos() == c2.getyPos()) {
                        /* Finds new position */
                        int newX = (int) Math.floor(Math.random() * 16);
                        int newY = (int) Math.floor(Math.random() * 16);
                        boolean overlapsAgain = true;
                        while (overlapsAgain) {
                            /* Finds new position if new overlap */
                            overlapsAgain = false;
                            newX = (int) Math.floor(Math.random() * 16);
                            newY = (int) Math.floor(Math.random() * 16);
                            for (int k = 0; k < newCreatures.size(); k++) {
                                Creature c = newCreatures.get(k);
                                if (newX == c.getxPos() && newY == c.getyPos()) {
                                    overlapsAgain = true;
                                }
                            }
                        }
                        c1.setNewPos(newX, newY);
                    }
                }
            }
        }
        /* Pushes the new positions */
        for (Creature c : creatures){
            Main.db.setPos(c.getxPos(), c.getyPos(), c.getPlayerId());
        }
    }

    /**
     * Increases the hp and damagebonus for every Character
     */
    public void upgradePlayerStats(){
        ArrayList<Character> characters = this.getCharacters();
        for (Character c : characters){
            Main.db.setHp((int)(c.getInitialHp()*1.25), c.getPlayerId());
            Main.db.setDamageBonus((int)(c.getDamageBonus()*1.25), c.getPlayerId());
        }
        Main.db.addChatMessage("Your body is ready for the next fight!", true);
    }

    /**
     * Adds new monsters from a specific level to the game lobby
     *
     * @param levelId       level id
     * @param playerAmount  amount of player
     */
    public void addNewMonstersToLobby(int levelId, int playerAmount){
        /* Fetches the creature ids from the lobby */
        ArrayList<Integer> creatureIds = Main.db.fetchMonstersFromLevel(levelId, playerAmount);
        /* Sets random positions to the monsters and creates a player entity for these */
        for (int i = 0; i < creatureIds.size(); i++){
            int playerId = Main.db.createPlayer(false);
            int posX = (int)Math.floor(Math.random()*16);
            int posY = (int)Math.floor(Math.random()*16);
            /* If the creature is a dragon */
            if (creatureIds.get(i) >= 13){
                posX = 8;
                posY = 7;
            }
            Main.db.createCreature(playerId, creatureIds.get(i), posX, posY);
        }
    }

    /**
     * Method fetches data from the database and updates the client data using this
     */
    public void updateCreatureData(){
        this.updatePlayerTurn();
        /* Iterates through all creatures to update the data */
        for (int i = 0; i < creatures.size(); i++) {
            Creature c = creatures.get(i);
            int playerId = c.getPlayerId();
            ArrayList<Integer> newPos = Main.db.fetchPlayerPos(playerId);
            int newHp = Main.db.fetchPlayerHp(playerId);
            /*
             * Does not update the position for the creature the player is controlling
             * This is to avoid overwriting any moves the player does when it is its turn
             */
            if (playerId != Main.user.getPlayerId()) {
                c.setNewPos(newPos.get(0), newPos.get(1));
            }
            c.setHp(newHp);
        }
    }

    /**
     * Sends Creature data from klient to database.
     * Updates position and health points.
     */
    public void pushCreatureData(){
        for (Creature c : creatures) {
            int playerId = c.getPlayerId();
            int posX = c.getxPos();
            int posY = c.getyPos();
           Main.db.setPos(posX, posY, playerId);
            int hp = c.getHp();
           Main.db.setHp(hp, playerId);
        }
    }

    /**
     * sends message to chat that the user is dead, if playerCharacters
     * hp <= 0. Sets isDead = true for all creatures with hp <= 0.
     */
    public void handleCreatureData(){
        if (this.playerCharacter.getHp() <= 0 && !this.playerCharacter.isDead()){
            Main.db.addChatMessage(Main.user.getUsername() + " died", true);
        }
        for (Creature c : this.creatures) {
            c.updateDead();
        }
    }

    /**
     * Update the value for playerTurn from the database
     */
    public void updatePlayerTurn(){
        this.setPlayerTurn(Main.db.fetchPlayerTurn());
    }

    /**
     * @return an ArrayList of the creatures
     */
    public ArrayList<Creature> getCreatures(){
        return this.creatures;
    }

    /**
     * @return an ArrayList of the characters
     */
    public ArrayList<Character> getCharacters(){
        ArrayList<Character> characters= new ArrayList<>();
        for(Creature i: creatures){
            /* If the creature is a character */
            if(i instanceof Character){
                characters.add((Character) i);
            }
        }
        return characters;
    }

    /**
     * Checks if it is playerCharacters turn.
     *
     * @return      true if playerCharacters turn, false otherwise.
     */
    public boolean isPlayerCharacterTurn(){
        if(creatures.get(playerTurn % creatures.size()).getPlayerId() == Main.user.getPlayerId()){
            return true;
        }
        return false;
    }

    /**
     * Checks if it is a Monsters turn.
     *
     * @return      true if a Monsters turn, false otherwise.
     */
    public boolean isMonsterTurn(){
        if(creatures.get(playerTurn % creatures.size()) instanceof Monster){
            return true;
        }
        return false;
    }

    /**
     * Updates the data for players being ready to transition to the new level
     */
    public void updatePlayersReadyForNewLevel(){
        /* Fetches an ArrayList to update the data stored on the client */
        ArrayList<Boolean> playersReadyForNewLevel = Main.db.fetchPlayersReadyForLevel();
        for (int i = 0; i < playersReadyForNewLevel.size(); i++){
            /* Updates only the values for the other players to avoid overwriting the value for the player */
            if (this.getCharacters().get(i) != this.getPlayerCharacter()) {
                this.getCharacters().get(i).setReadyForNewLevel(playersReadyForNewLevel.get(i));
            }
        }
    }

    /**
     * Checks if all players are ready for new level.<br>
     * This variable is used to ensure that no player falls behind,
     * or that no players fetches creatures from the lobby
     * before the host has added these
     *
     * @return      true, if all players are ready for new level, false otherwise.
     */
    public boolean allPlayersReadyForNewLevel(){
        if(Main.db != null) {
            /* Makes sure all players are ready */
            this.updatePlayersReadyForNewLevel();
        }
        boolean ready = true;
        ArrayList<Character> characters = this.getCharacters();
        /* Checks if all players are ready */
        for (Character c : characters){
            if (!c.isReadyForNewLevel()){
                ready = false;
            }
        }
        return ready;
    }

    /**
     * Sets the value in both the client and the database
     *
     * @param ready boolean to set
     */
    public void setPlayerReadyForNewLevel(boolean ready){
        this.playerCharacter.setReadyForNewLevel(ready);
        Main.db.setReadyForNewLevel(this.playerCharacter.getPlayerId(), ready);
    }

    /**
     *
     * @return      ArrayList of all monsters in creatures.
     */
    public ArrayList<Monster> getMonsters(){
        ArrayList<Monster> monsters = new ArrayList<>();
        for (Creature c : this.creatures){
            if (c instanceof Monster){
                monsters.add((Monster)c);
            }
        }
        return monsters;
    }

    /**
     * Checks if all monsters are dead. If all monsters are dead
     * the level is cleared.
     *
     * @return  true if all monsters are dead, false otherwise
     */
    public boolean isLevelCleared(){
        boolean allMonstersDead = true;
        ArrayList<Monster> monsters = this.getMonsters();
        for (Monster m : monsters){
            if (!m.isDead()){
                allMonstersDead = false;
            }
        }
        return allMonstersDead;
    }

    /**
     * Checks if all characters are dead. If all characters are dead
     * it is game over.
     *
     * @return      true if all characters are dead, false otherwise.
     */
    public boolean isGameOver(){
        boolean gameOver = false;
        boolean allCharactersDead = true;
        ArrayList<Character> characters = this.getCharacters();
        /* Checks if all characters are dead */
        for (Character i: characters){
            if (!i.isDead()){
                allCharactersDead = false;
            }
        }
        gameOver = allCharactersDead;
        return gameOver;
    }

    /**
     * Fetches the next level from the database and changes to it
     */
    public void changeToNewLevel() {
        /* Resets the playerReadyForNewLevel value */
        this.setPlayerReadyForNewLevel(false);
        /* Updates to the new level */
        int LevelId = Main.db.fetchLevelId(Main.user.getLobbyKey());
        this.level.setLevelId(LevelId);
        this.level.updateLevel();
        /* Removes all pawns from the map */
        GridPane mapGrid = (GridPane) this.creatures.get(0).getPawn().getParent();
        for (Creature c : this.creatures) {
            mapGrid.getChildren().remove(c.getPawn());
        }
        /*
         * The values for player stats has been upgraded by the the host and must therefore be fetched again
         */
        this.creatures = Main.db.fetchCreaturesFromLobby();
        for (Creature c : this.creatures) {
            if (c.getPlayerId() == Main.user.getPlayerId()) {
                this.playerCharacter = (game.Character) c;
            }
            c.setPawnSize(mapGrid.getPrefWidth() / 16, mapGrid.getPrefHeight() / 16);
            mapGrid.add(c.getPawn(), c.getxPos(), c.getyPos());
        }
        /* Resets playerReadyForNewLevel for all characters */
        for (Character c : this.getCharacters()){
            c.setReadyForNewLevel(false);
        }
    }

    /**
     * Handels the turn of all monsters.
     * Moves, attacks and ends turn.
     */
    public void monsterAction() {
        if (isMonsterTurn()) {
            /* Creates a new thread so that the host can interact with
                the game, while it is running the monster logic.
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Monster monster = ((Monster) creatures.get(playerTurn % creatures.size()));
                    if (monster.isDead()) {
                        if(Main.db != null) {
                            endTurn();
                        }
                    } else {
                        monster.monsterMove(creatures);
                        monster.monsterAttack(creatures);
                        if(Main.db != null) {
                            pushCreatureData();
                            endTurn();
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * Increments the turn for both client and database.
     */
    public void endTurn(){
        this.incrementPlayerTurn();
        Main.db.incrementPlayerTurn(playerTurn);
    }

    /**
     *
     * @return      The level.
     */
    public Level getLevel(){
        return this.level;
    }

    /**
     *
     * @return      amountOfLevels
     */
    public int getAmountOfLevels() {
        return this.amountOfLevels;
    }

    /**
     * Checks if a Monster is within the correct range to attack it.
     * playerCharacter must be adjacent to a Monster to attack it with
     * melee weapons.
     * playerCharacter needs to be anywhere but adjacent to a Monster to
     * attack with ranged weapons.
     *
     * @param monster       The target Monster.
     * @param melee         boolean to tell if it is a check for melle or ranged attack.
     * @return              true if melee = true and within melee range,
     *                      true if melee = false and within ranged range, false otherwise.
     */
    public boolean attackRange(Monster monster, boolean melee){
        /* Checks if monster is within melee range */
        if(melee) {
            if ((Math.abs(playerCharacter.getxPos() - monster.getxPos()) <= 1) && (Math.abs(playerCharacter.getyPos() - monster.getyPos()) <= 1)) {
                return true;
            }
        }
        /* checks if monster is within ranged range */
        else if(!melee) {
            if ((Math.abs(playerCharacter.getxPos() - monster.getxPos()) > 1) || (Math.abs(playerCharacter.getyPos() - monster.getyPos())) > 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param creatureList ArrayList to be set
     */
    public void setCreatures(ArrayList<Creature> creatureList){
        creatures = creatureList;
    }

    /**
     * @param level level-object to be set
     */
    public void setLevel(Level level){
        this.level = level;
    }

    /**
     * Increments the playerTurn
     */
    public void incrementPlayerTurn(){
        this.setPlayerTurn(this.getPlayerTurn() + 1);
    }

    /**
     * @return player turn
     */
    public int getPlayerTurn(){
        return this.playerTurn;
    }

    /**
     * @param playerTurn turn to be set
     */
    public void setPlayerTurn(int playerTurn){
        this.playerTurn = playerTurn;
    }

    /**
     * @return the character the player is playing as
     */
    public Character getPlayerCharacter(){
        return this.playerCharacter;
    }

    /**
     * @param i the character to be set as the playerCharacter
     */
    public void setPlayerCharacter(Character i){
        playerCharacter = i;
    }

    /**
     * toString-method
     *
     * @return a string containing game status and information
     */
    public String toString(){
        StringBuilder string = new StringBuilder("");
        string.append("User host: " + Main.user.isHost() + "\n");
        string.append("Is playerCharacterTurn: " + this.isPlayerCharacterTurn() + "\n");
        string.append("Amount of creatures: " + this.creatures.size() + "\n");
        for (int i = 0; i < this.creatures.size(); i++){
            Creature c = this.creatures.get(i);
            string.append("Creature " + (i+1) + ": " + c.getCreatureName() + " HP: " + c.getHp() + "\n");
        }
        string.append("\n");
        return string.toString();
    }
}
