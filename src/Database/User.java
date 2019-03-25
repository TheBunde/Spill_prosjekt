package Database;
import Main.*;

public class User {
    private Database db = Main.db;
    private int user_id;
    private String username;
    private int rank;
    private String email;
    private int lobbyKey;


    public User(int user_id, String username, int rank, String email){
        this.user_id = user_id;
        this.username = username;
        this.rank = rank;
        this.email = email;

    }

    public int getUser_id(){
        return user_id;
    }

    public void setUser_id(int new_user_id){
        user_id = new_user_id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String new_username){
        username = new_username;
    }

    public int getRank(){
        return rank;
    }

    public void setRank(int newRank){
        rank = newRank;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String newEmail){
        email = newEmail;
    }

    public int getLobbyKey(){
        return this.lobbyKey;
    }

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

    public String toString(){
        return "User info:\n" + user_id + "\n" + username + "\n" + rank + "\n" + email;
    }
}
