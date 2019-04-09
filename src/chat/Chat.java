package chat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;

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

    public void addMessage(int messageId, String username, String message, String timestamp, boolean event){
        if (this.messages.size() > 30){
            this.messages.remove(0);
        }
        this.messages.add(new ChatMessage(messageId, username, message, timestamp, event));
        this.messages.sort(new Comparator<ChatMessage>() {
            @Override
            public int compare(ChatMessage o1, ChatMessage o2) {
                return o1.getMessageId() - o2.getMessageId();
            }
        });

    }

    public int getLastSeenMessageId() {
        return lastSeenMessageId;
    }
}
