package Database;
import Main.*;


/**
 * User.java
 * @author saramoh
 */
public class User {
    private Database db = Main.db;
    private int user_id = -1;
    private String username;
    private int rank;
    private int lobbyKey;
    private int playerId = -1;
    private boolean host = false;


    /**
     * @param user_id
     * @param username
     * @param rank
     */
    public User(int user_id, String username, int rank){
        this.user_id = user_id;
        this.username = username;
        this.rank = rank;
    }

    /**
     * @return
     */
    public int getUser_id(){
        return user_id;
    }

    /**
     * @param new_user_id
     */
    public void setUser_id(int new_user_id){
        user_id = new_user_id;
    }

    /**
     * @return
     */
    public String getUsername(){
        return username;
    }

    /**
     * @param new_username
     */
    public void setUsername(String new_username){
        username = new_username;
    }

    /**
     * @return
     */
    public int getRank(){
        return rank;
    }

    /**
     * @param newRank
     */
    public void setRank(int newRank){
        rank = newRank;
    }

    /**
     * @return
     */
    public int getLobbyKey(){
        return this.lobbyKey;
    }

    /**
     * @param lobbyKey
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
     * @return
     */
    public int getPlayerId(){
        return this.playerId;
    }

    /**
     * @param playerId
     */
    public void setPlayerId(int playerId){
        this.playerId = playerId;
    }

    /**
     * @return
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
     * @return
     */
    public String toString(){

        return "User info:\n" + user_id + "\n" + username + "\n" + rank;

    }
}
