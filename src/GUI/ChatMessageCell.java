package GUI;

import Database.Chat;
import Database.ChatMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.io.IOException;


public class ChatMessageCell extends ListCell<ChatMessage> {

    @FXML
    private Label usernameCell;
    @FXML
    private Label messageCell;
    @FXML
    private Label timestampCell;
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
            if (chatmessage.isEvent()){
                loader = new FXMLLoader(getClass().getResource("EventMessageListViewCell.fxml"));
            }
            else {
                loader = new FXMLLoader(getClass().getResource("UserMessageListViewCell.fxml"));
            }
            loader.setController(this);
            try {
                loader.load();
            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }
            //prefWidthProperty().bind(chatController.list.widthProperty().subtract(2));
            if (chatmessage.isEvent()){
                messageCell.setText(chatmessage.getMessage());
                messageCell.setTextFill(Color.RED);
                timestampCell.setText(chatmessage.getTimestamp());
            }
            else{
                usernameCell.setText(chatmessage.getUsername() + ": ");
                messageCell.setText(chatmessage.getMessage());
                timestampCell.setText(chatmessage.getTimestamp());
            }
            setPrefWidth(1);
            setText(null);
            setGraphic(hbox);
        }
    }
}
