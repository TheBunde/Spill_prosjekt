package AccountDetails;

public class User {
    private int user_id;
    private String username;
    private int rank;
    private String email;
    private char password;


    public User(int user_id, String username, int rank, String email, char password){
        this.user_id = user_id;
        this.username = username;
        this.rank = rank;
        this.email = email;
        this.password = password;
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

    public char getPassword(){
        return password;
    }

    public void setPassword(char newPassword){
        password = newPassword;
    }

    public String toString(){
        return "User info:\n" + user_id + "\n" + username + "\n" + rank + "\n" + email + "\n" + password;
    }
}
