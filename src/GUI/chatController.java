package GUI;

import Main.*;
import Database.*;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;



public class chatController implements Initializable {

    @FXML
    private ListView<ChatMessage> list;

    private ObservableList<ChatMessage> chatMessageObservableList;

    @FXML
    private Button sendMessageButton;

    @FXML
    private TextField messageInput;
    private AnchorPane anchorPane;

    private Database db = Main.db;
    private User user = Main.user;
    public static Timer timer = new Timer();

    public chatController(){
        chatMessageObservableList = db.chat.messages;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        list.setItems(chatMessageObservableList);
        list.setCellFactory(chatMessageObservableList -> {
            return new ChatMessageCell();
        });



        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    updateChat();
                });
            }
        },0 ,500);
    }

    @FXML
    private void handleButtonAction(KeyEvent key){
        if (key.getCode() == KeyCode.ENTER){
            this.sendMessage();
        }
    }

    public void sendMessage(){
        String text = messageInput.getText();
        new Thread(new Runnable(){
            @Override public void run(){
                disableChat();
                messageInput.setText("Message pending...");
                if(Main.db.addChatMessage(text, false)){
                    System.out.println("Message sent");
                }
                else {
                    System.out.println("Message was not sent");
                }
                messageInput.setText("");
                enableChat();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        messageInput.requestFocus();
                    }
                });
            }
        }).start();
    }

    public void updateChat(){
        this.db.getMessagesFromChat();
        list.scrollTo(list.getItems().size());
    }

    public void disableChat(){
        messageInput.setDisable(true);
        sendMessageButton.setDisable(true);
    }

    public void enableChat(){
        messageInput.setDisable(false);
        sendMessageButton.setDisable(false);
    }

}
