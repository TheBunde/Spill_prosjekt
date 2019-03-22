package GUI;

import Main.*;
import Database.*;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;



public class chatController implements Initializable {
    @FXML
    private ListView<String> list;

    @FXML
    private Button sendMessageButton;

    @FXML
    private TextField messageInput;

    private Database db = Main.db;
    public static Timer timer = new Timer();
    private int count = 0;

    public void initialize(URL location, ResourceBundle resources){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //updateChat();
                //count++;
                //System.out.println(count);
            }
        },0 ,1500);
    }

    public void sendMessage(){
        String text = messageInput.getText();
        if(Main.db.addChatMessage(text)){
            System.out.println("Message sent");
        }
        messageInput.setText("");
        updateChat();
    }

    public void updateChat(){
        ArrayList<String> messages = this.db.getMessagesFromChat();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = messages.size() - 1; i >= 0; i--){
            items.add(messages.get(i));
        }
        list.setItems(items);
    }




}
