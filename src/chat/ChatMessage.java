package chat;

public class ChatMessage {
    private int messageId;
    private String username;
    private String message;
    private String timestamp;
    private boolean event;

    public ChatMessage(int messageId, String username, String message, String timestamp, boolean event){
        this.messageId = messageId;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
        this.event = event;
    }

    public int getMessageId(){
        return messageId;
    }

    public String getMessage(){
        return this.message;
    }

    public String getUsername() {
        return username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isEvent() {
        return event;
    }

}
