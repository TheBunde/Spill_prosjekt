package user;

import main.*;
import database.Database;

/**
 * User.java
 * @author williad, saramoh
 */
public class User {
    private int user_id;
    private String username;
    private int rank;
    private int lobbyKey;
    private int playerId = -1;
    private boolean host = false;

    /**
     * A constructor for the class.
     * @param user_id       user id
     * @param username      username
     * @param rank          users rank
     */
    public User(int user_id, String username, int rank){
        this.user_id = user_id;
        this.username = username;
        this.rank = rank;

    }
    
   /**
    * @return   user id
    */
    public int getUser_id(){
        return user_id;
    }
    
    /**
     * @param new_user_id   The user's new id
     */
     public void setUser_id(int new_user_id){
        user_id = new_user_id;
    }
 
    /**
     * @return   username
     */
    public String getUsername(){
        return username;
    }

    /**
     * @param new_username   The user's new username
     */
    public void setUsername(String new_username){
        username = new_username;
    }

    /**
     * @return   rank
     */
    public int getRank(){
        return rank;
    }

    /**
     * @param newRank   The user's new rank
     */
    public void setRank(int newRank){
        rank = newRank;
    }
    
    /**
     * @return   lobbyKey
     */
    public int getLobbyKey(){
        return this.lobbyKey;
    }

    /**
     * @param lobbyKey   The user's lobbyKey
     */
    public void setLobbyKey(int lobbyKey){
        this.lobbyKey = lobbyKey;
    }
    
    /**
     * @return   player_id for the user
     */
    public int getPlayerId(){
        return this.playerId;
    }
    
    /**
     * @param playerId   The user's new player_id
     */
    public void setPlayerId(int playerId){
        this.playerId = playerId;
    }
    
    /**
     * @return   true if the user is host, false otherwise
     */
    public boolean isHost(){
        return this.host;
    }
    
    /**
     * @param host   Sets boolean host true if the user is host, false otherwise
     */
    public void setHost(boolean host){
        this.host = host;
    }
    
    /**
     * @return   info about the user
     */
    public String toString(){
        return "User info:\n" + user_id + "\n" + username + "\n" + rank ;
    }
}
