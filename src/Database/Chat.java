package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Chat{
    public ObservableList<ChatMessage> messages;
    private int lastSeenMessageId = 0;

    public Chat(){
        this.messages = FXCollections.observableArrayList();
        this.lastSeenMessageId = 0;

    }

    public void setLastSeenMessageId(int lastSeenMessageId){
        this.lastSeenMessageId = lastSeenMessageId;
    }

    public void addMessage(String username, String message, String timestamp, boolean event){
        if (this.messages.size() > 30){
            this.messages.remove(0);
        }
        this.messages.add(new ChatMessage(username, message, timestamp, event));

    }

    public int getLastSeenMessageId() {
        return lastSeenMessageId;
    }
}
