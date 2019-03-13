package chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatMain extends Application {
    static Database db;
    static Chatter chatter;
    @Override
    public void start(Stage primaryStage) throws Exception{
        db = new Database("jdbc:mysql://mysql-ait.stud.idi.ntnu.no:3306/g_tdat1006_01?user=g_tdat1006_01&password=", "q8CeXgyy");
        //chatter = new Chatter(usernameInput.getText());
        //db.addChatter(chatter);
        //db.connectChatterToChat(chatter, chatter.getChatID());
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Chat");
        primaryStage.setScene(new Scene(root, 500, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
