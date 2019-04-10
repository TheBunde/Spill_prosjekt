package chat;

/**
 * Contains information about a message shown in the chat
 *
 * @author williad
 */
public class ChatMessage {
    private int messageId;
    private String username;
    private String message;
    private String timestamp;
    private boolean event;

    /**
     * Constructor for ChatMessage
     *
     * @param messageId the message id
     * @param username  username of the sender
     * @param message   the message
     * @param timestamp the time of sending
     * @param event     boolean for if message is event message
     */
    public ChatMessage(int messageId, String username, String message, String timestamp, boolean event){
        this.messageId = messageId;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
        this.event = event;
    }

    /**
     *
     * @return the message id
     */
    public int getMessageId(){
        return messageId;
    }

    /**
     *
     * @return the message
     */
    public String getMessage(){
        return this.message;
    }

    /**
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @return true if event, false otherwise
     */
    public boolean isEvent() {
        return event;
    }

}
