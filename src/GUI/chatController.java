package GUI;
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

    private Database dbChat = new Database("jdbc:mysql://mysql-ait.stud.idi.ntnu.no:3306/g_tdat1006_01?user=g_tdat1006_01&password=", "q8CeXgyy");

    public static Timer timer = new Timer();
    private int count = 0;

    public void initialize(URL location, ResourceBundle resources){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateChat();
                count++;
                System.out.println(count);
            }
        },0 ,1500);
    }

    public void sendMessage(){
        String text = messageInput.getText();
        if(this.dbChat.addChatMessage(text)){
            System.out.println("Message sent");
        }
        messageInput.setText("");
        updateChat();
    }

    public void updateChat(){
        ArrayList<String> messages = this.dbChat.getMessagesFromChat();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = messages.size() - 1; i >= 0; i--){
            items.add(messages.get(i));
        }
        list.setItems(items);
    }




}
