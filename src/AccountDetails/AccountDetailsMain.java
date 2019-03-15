import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AccountDetailsMain extends Application {
    static AccountDetailsDatabase db;


    @Override
    public void start(Stage primaryStage) throws Exception{
        db = new AccountDetailsDatabase("jdbc:mysql://mysql-ait.stud.idi.ntnu.no:3306/g_tdat1006_01?user=g_tdat1006_01&password=", "q8CeXgyy");
        Parent root = FXMLLoader.load(getClass().getResource("AccountDetails.fxml"));
        primaryStage.setTitle("Account Details");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
