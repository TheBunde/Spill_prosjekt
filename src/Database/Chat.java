package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Chat{
    private ArrayList<String> messages = new ArrayList<String>();
    private int lastSeenMessageId = 0;

    public Chat(){
        this.lastSeenMessageId = 0;
        messages.add("Welcome to the Chat! Here you can communicate with your teammates");
    }

    public void setLastSeenMessageId(int lastSeenMessageId){
        this.lastSeenMessageId = lastSeenMessageId;
    }

    public void addMessage(String message){
        if (this.messages.size() > 30){
            this.messages.remove(0);
        }
        this.messages.add(message);
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public int getLastSeenMessageId() {
        return lastSeenMessageId;
    }
}
