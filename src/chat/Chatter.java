package chat;

public class Chatter {
    private String name;
    private int chatterID;
    private int chatID;

    public Chatter(String name){
        this.name = name;
        this.chatterID = -1;
        this.chatID = 1;
    }

    public String getName() {
        return name;
    }

    public int getChatterID() {
        return chatterID;
    }

    public void setChatterID(int chatterID){
        this.chatterID = chatterID;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID){
        this.chatID = chatID;
    }
}
