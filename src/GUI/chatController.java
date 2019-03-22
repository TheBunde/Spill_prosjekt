package GUI;

import Main.*;
import Database.*;

import audio.ThreadPool;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.xml.crypto.Data;
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
        ObservableList<String> items = FXCollections.observableArrayList();
        list.setItems(items);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    updateChat();
                });
            }
        },0 ,200);
    }

    public void sendMessage(){
        new Thread(new Runnable(){
            @Override public void run(){
                String text = messageInput.getText();
                if(Main.db.addChatMessage(text)){
                    System.out.println("Message sent");
                }
                else {
                    System.out.println("Message was not sent");
                }
                messageInput.setText("");
            }
        }).start();
    }

    public void updateChat(){
        this.db.getMessagesFromChat();
        list.getItems().setAll(this.db.chat.getMessages());
    }
}
