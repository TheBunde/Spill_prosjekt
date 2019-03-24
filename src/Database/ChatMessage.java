package Database;

public class ChatMessage {
    private String username;
    private String message;
    private String timestamp;
    private boolean event;

    public ChatMessage(String username, String message, String timestamp, boolean event){
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
        this.event = event;
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
