package user;

import main.*;
import database.Database;

/**
 * User.java
 * @author williad saramoh
 */


public class User {
    private int user_id;
    private String username;
    private int rank;
    private int lobbyKey;
    private int playerId = -1;
    private boolean host = false;


    public User(int user_id, String username, int rank){
        this.user_id = user_id;
        this.username = username;
        this.rank = rank;

    }
 /**
    * @return the user id
    */

    public int getUser_id(){
        return user_id;
    }
    
    /**
     * @param new_user_id The user's new id
     */
     public void setUser_id(int new_user_id){
        user_id = new_user_id;
    }
 
 /**
     * @return the username
     */

    public String getUsername(){
        return username;
    }
   /**
    * @param new_username The user's new username
     */

    public void setUsername(String new_username){
        username = new_username;
    }
 /**
    * @return the user's rank
    */

    public int getRank(){
        return rank;
    }
    /**
     * @param newRank The user's new rank
     */
    public void setRank(int newRank){
        rank = newRank;
    }
    
    /**
     * @return the user's lobbyKey
     */
    public int getLobbyKey(){
        return this.lobbyKey;
    }
    
     
    
    /**
     * @param lobbyKey The user's lobbyKey
     */


    public void setLobbyKey(int lobbyKey){
        this.lobbyKey = lobbyKey;
    }
    
    /**
     * @return the user's player_id
     */

    public int getPlayerId(){
        return this.playerId;
    }
    
    /**
     * @param playerId new user's player_id
     */

    public void setPlayerId(int playerId){
        this.playerId = playerId;
    }
    
    /**
     * @return true if the user is host otherwise returns false
     */


    public boolean isHost(){
        return this.host;
    }
    
    /**
     * @param host
     */


    public void setHost(boolean host){
        this.host = host;
    }
    
    /**
     * @return info about the user
     */


    public String toString(){
        return "User info:\n" + user_id + "\n" + username + "\n" + rank ;
    }
}
