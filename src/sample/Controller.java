package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    @FXML
    private TextField usernameInput;

    @FXML
    private Button signInBtn;

    private Database db = Main.db;
    private Chatter chatter;

    public void joinChatPressed() throws Exception{
        //db = new Database("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/williad?user=williad&password=", "69bJDh4G");
        Main.chatter = new Chatter(usernameInput.getText());
        chatter = Main.chatter;
        db.addChatter(chatter);
        db.connectChatterToChat(chatter, chatter.getChatID());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage)usernameInput.getScene().getWindow();
        primaryStage.setScene(scene);

    }
}
