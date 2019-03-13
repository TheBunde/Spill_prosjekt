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

<<<<<<< HEAD:src/chat/Controller.java
    private Database db = ChatMain.db;
=======
    private Database db = InterfaceMain.db;
>>>>>>> 6b4e8038ffe98f0de9bbf60ba50086c6159e06d2:src/sample/Controller.java
    private Chatter chatter;

    public void joinChatPressed() throws Exception{
        //db = new Database("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/williad?user=williad&password=", "69bJDh4G");
<<<<<<< HEAD:src/chat/Controller.java
        ChatMain.chatter = new Chatter(usernameInput.getText());
        chatter = ChatMain.chatter;
=======
        InterfaceMain.chatter = new Chatter(usernameInput.getText());
        chatter = InterfaceMain.chatter;
>>>>>>> 6b4e8038ffe98f0de9bbf60ba50086c6159e06d2:src/sample/Controller.java
        db.addChatter(chatter);
        db.connectChatterToChat(chatter, chatter.getChatID());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage)usernameInput.getScene().getWindow();
        primaryStage.setScene(scene);

    }
}
