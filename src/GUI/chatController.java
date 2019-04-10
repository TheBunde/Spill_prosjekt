package GUI;

import main.*;
import chat.ChatMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller class to handle the Graphical User Interface for the Chat
 *
 * @author williad
 */
public class chatController{

    @FXML
    private ListView<ChatMessage> list;

    private ObservableList<ChatMessage> chatMessageObservableList;

    @FXML
    private Button sendMessageButton;

    @FXML
    private TextField messageInput;

    public static Timer timer = new Timer();

    /**
     * Constructor for chatController
     */
    public chatController(){
        chatMessageObservableList = Main.db.chat.getMessages();
    }


    public void initialize(){
        chatMessageObservableList.clear();
        chatMessageObservableList.add(new ChatMessage(0 ,"Event" ,"Welcome to the Chat! Here you can communicate with your teammates", "", true));
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

    public boolean sendMessage(){
        String text = messageInput.getText();
        if (text.length() == 0){
            return false;
        }
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
                list.scrollTo(list.getItems().size());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        messageInput.requestFocus();
                    }
                });
            }
        }).start();
        return true;
    }

    public void updateChat(){
        Main.db.getMessagesFromChat();
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
