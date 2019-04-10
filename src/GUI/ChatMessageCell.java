package GUI;

import chat.ChatMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;


import java.io.IOException;

/**
 * Handles the content of a chat message before being displayed in the chat GUI<br>
 *
 * Inspired by the blog post <i>Custom ListCell in a JavaFX ListView</i> by johannes
 * @see <a href="https://www.turais.de/how-to-custom-listview-cell-in-javafx/">Custom ListCell in a JavaFX ListView</a>
 *
 * @author williad
 */
public class ChatMessageCell extends ListCell<ChatMessage> {

    /**
     * Updates a specific cell in the ListView based on a specific ChatMessage-object
     *
     * @param chatmessage   the chat message to be displayed
     * @param empty         true if empty cell, false otherwise
     */
    @Override
    protected void updateItem(ChatMessage chatmessage, boolean empty){
        super.updateItem(chatmessage, empty);
        if (empty || chatmessage == null){
            setText(null);
            setGraphic(null);
        }
        else{
            /* Loads the FXML-file for a ChatMessageCell */
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("MessageListViewCell.fxml"));
            loader.setController(this);
            try {
                loader.load();
            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }

            /* Cells are styled based on if the chat message is event or not */
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
        }
    }
}
