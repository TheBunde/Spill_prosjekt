package chat;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class ChatController{
    @FXML
    private ListView<String> list;

    @FXML
    private Button sendMessageButton;

    @FXML
    private TextField messageInput;

    private Database db = ChatMain.db;
    private Chatter chatter = ChatMain.chatter;

    public void initialize(){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateChat();
            }
        },0 ,1500);
    }

    public void sendMessage(){
        String text = messageInput.getText();
        if(db.addChatMessage(chatter, text)){
            System.out.println("Message sent");
        }
        messageInput.setText("");
        updateChat();
    }

    public void updateChat(){
        ArrayList<String> messages = db.getMessagesFromChat(chatter);
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = messages.size() - 1; i >= 0; i--){
            items.add(messages.get(i));
        }
        list.setItems(items);
    }


}
