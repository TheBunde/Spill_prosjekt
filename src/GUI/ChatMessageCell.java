package GUI;

import Database.Chat;
import Database.ChatMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


import java.io.IOException;


public class ChatMessageCell extends ListCell<ChatMessage> {

    @FXML
    private Label messageLabel;

    @FXML
    private HBox hbox;

    @Override
    protected void updateItem(ChatMessage chatmessage, boolean empty){
        super.updateItem(chatmessage, empty);
        if (empty || chatmessage == null){
            setText(null);
            setGraphic(null);
        }
        else{
            FXMLLoader loader;
            //As of now, the FXML for Event message will be used for both types of messages
            loader = new FXMLLoader(getClass().getResource("EventMessageListViewCell.fxml"));
            loader.setController(this);
            try {
                loader.load();
            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }


            if (chatmessage.isEvent()){
                messageLabel.setTextFill(Color.color(52.0/255.0, 152.0/255.0, 219.0/255.0));
                messageLabel.setText(chatmessage.getMessage() + " | " + chatmessage.getTimestamp());

            }
            else{
                setTextFill(Color.BLACK);
                messageLabel.setText(chatmessage.getUsername() + ": " + chatmessage.getMessage() + " | " + chatmessage.getTimestamp());
            }
            setText(null);
            setGraphic(hbox);
        }
    }
}
