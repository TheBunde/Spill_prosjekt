package AccountDetails;

public class Player extends User {

    private int level;

    public Player(int user_id, String username, int rank, String email, char password, int level){
        super(user_id, username, rank, email, password);
        this.level = level;
    }

    public int getLevel(){
        return level;
    }

    public void setLevel(int newLevel){
        level = newLevel;
    }

    public String toString(){
        return super.toString() + "\n" + level;
    }
}
