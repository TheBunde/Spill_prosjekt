package GUI;

import chat.ChatMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;


import java.io.IOException;


public class ChatMessageCell extends ListCell<ChatMessage> {

    @Override
    protected void updateItem(ChatMessage chatmessage, boolean empty){
        super.updateItem(chatmessage, empty);
        if (empty || chatmessage == null){
            setText(null);
            setGraphic(null);
        }
        else{
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("MessageListViewCell.fxml"));
            loader.setController(this);
            try {
                loader.load();
            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }


            if (chatmessage.isEvent()){
                setTextFill(Color.color(55.0/255.0, 126.0/255.0, 219.0/255.0));
                setText(chatmessage.getMessage());
                setStyle("-fx-font-weight: bold;");

            }
            else{
                setText(chatmessage.getUsername() + ": " + chatmessage.getMessage() + " | " + chatmessage.getTimestamp());
                setTextFill(Color.BLACK);
                setStyle("-fx-font-weight: normal;");
            }

            //setText(null);
            //setGraphic(messageLabel);
        }
    }
}
