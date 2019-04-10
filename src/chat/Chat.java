package chat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Comparator;

/**
 * This class contains an ObservableList with the messages shown in chatController.java
 *
 * @author williad
 */
public class Chat{
    private ObservableList<ChatMessage> messages;
    private int lastSeenMessageId;

    /**
     * Constructor for Chat
     */
    public Chat(){
        this.messages = FXCollections.observableArrayList();
        this.lastSeenMessageId = 0;
    }

    /**
     * Method that sets the lastSeenMessageId
     * @param lastSeenMessageId id for the last seen message
     */
    public void setLastSeenMessageId(int lastSeenMessageId){
        this.lastSeenMessageId = lastSeenMessageId;
    }

    /**
     * Adds a message to the ObservableList
     *
     * @param messageId the message id
     * @param username  username of the sender
     * @param message   the message
     * @param timestamp the time of sending
     * @param event     true if event, false otherwise
     */
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

    /**
     *
     * @return id of the last seen message
     */
    public int getLastSeenMessageId() {
        return lastSeenMessageId;
    }

    /**
     *
     * @return ObservableList consisting of chatmessages
     */
    public ObservableList<ChatMessage> getMessages(){
        return this.messages;
    }
}
