package chat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private TextField usernameInput;

    @FXML
    private Button signInBtn;

    private Database db = ChatMain.db;
    private Chatter chatter;

    public void joinChatPressed() throws Exception{
        //db = new Database("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/williad?user=williad&password=", "69bJDh4G");
        ChatMain.chatter = new Chatter(usernameInput.getText());
        chatter = ChatMain.chatter;
        db.addChatter(chatter);
        db.connectChatterToChat(chatter, chatter.getChatID());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage)usernameInput.getScene().getWindow();
        primaryStage.setScene(scene);

    }
}
