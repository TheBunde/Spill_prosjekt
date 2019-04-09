package Database;
import Main.*;


/**
 * User.java
 * @author williad saramoh
 */
public class User {
    private Database db = Main.db;
    private int user_id = -1;
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
     * Gets id for the user
     * @return the user id
     */
    public int getUser_id(){
        return user_id;
    }

    /**
     * Sets a new id for the user
     * @param new_user_id The user's new id
     */
    public void setUser_id(int new_user_id){
        user_id = new_user_id;
    }

    /**
     * Gets the username for the user
     * @return the username
     */
    public String getUsername(){
        return username;
    }

    /**
     * Sets a new username for the user
     * @param new_username The user's new username
     */
    public void setUsername(String new_username){
        username = new_username;
    }

    /**
     * Gets the rank for the user
     * @return the user's rank
     */
    public int getRank(){
        return rank;
    }

    /**
     * Sets a new rank for the user
     * @param newRank The user's new rank
     */
    public void setRank(int newRank){
        rank = newRank;
    }

    /**
     * Gets the lobbyKey for the user
     * @return the user's lobbyKey
     */
    public int getLobbyKey(){
        return this.lobbyKey;
    }

    /**
     * Sets a lobbyKey for the user and join the user to the lobby
     * @param lobbyKey The user's lobbyKey
     */
    public void setLobbyKey(int lobbyKey){
        this.lobbyKey = lobbyKey;
        if (lobbyKey > 0){
            new Thread(new Runnable(){
                @Override public void run(){
                    db.addChatMessage(getUsername() + " has joined the lobby", true);
                }
            }).start();
        }
    }

    /**
     * Gets player_id to the user
     * @return the user's player_id
     */
    public int getPlayerId(){
        return this.playerId;
    }

    /**
     * Sets a new player_id for the user
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
     * Sets the user either as host or not
     * @param host
     */
    public void setHost(boolean host){
        this.host = host;
    }

    /**
     * @return info about the user
     */
    public String toString(){

        return "User info:\n" + user_id + "\n" + username + "\n" + rank;

    }
}
