package GUI;

import main.*;
import chat.ChatMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller class to handle the Graphical User Interface for the chat
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

    /**
     * Method that executes when the corresponding FXML file to this controller is loaded
     */
    public void initialize(){
        chatMessageObservableList.clear();
        /* The elements in the chat's listview is set to be types of ChatMessageCell */
        chatMessageObservableList.add(new ChatMessage(0 ,"Event" ,"Welcome to the Chat! Here you can communicate with your teammates", "", true));
        list.setItems(chatMessageObservableList);
        list.setCellFactory(chatMessageObservableList -> {
            return new ChatMessageCell();
        });

        /* Timer-loop to update the chat */
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

    /**
     * Handles key event
     *
     * @param key KeyEvent to handle
     */
    @FXML
    private void handleButtonAction(KeyEvent key){
        if (key.getCode() == KeyCode.ENTER){
            this.sendMessage();
        }
    }

    /**
     * Sends a message to the gamelobby with the inputted text<br>
     * Executes when the sendMessageButton is pressed
     *
     * @return true if input field is not empty, false otherwise
     */
    public boolean sendMessage(){
        String text = messageInput.getText();
        if (text.length() == 0){
            return false;
        }
        /* Sends the message on a new thread to avoid the interface freezing */
        new Thread(new Runnable(){
            @Override public void run(){
                /* Disables inputs for chat while message is being sent */
                disableChat();
                messageInput.setText("Message pending...");
                Main.db.addChatMessage(text, false);
                messageInput.setText("");
                enableChat();
                /* Scrolls to bottom of the message list */
                list.scrollTo(list.getItems().size());
                /* Focuses on the input field after message has been sent */
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

    /**
     * Updates the chat's content
     */
    public void updateChat(){
        Main.db.getMessagesFromChat();
    }

    /**
     * Disables the input elements
     */
    public void disableChat(){
        messageInput.setDisable(true);
        sendMessageButton.setDisable(true);
    }

    /**
     * Enables the input elements
     */
    public void enableChat(){
        messageInput.setDisable(false);
        sendMessageButton.setDisable(false);
    }
}
