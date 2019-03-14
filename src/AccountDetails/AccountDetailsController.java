package AccountDetails;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AccountDetailsController {

    private Text username, email, level;
    private Button changeUsername, changePassword, back;

    private AccountDetailsDatabase db = AccountDetailsMain.db;
    private User user;
    private Player player;
    private Pane pane;


    public void setInfo() throws Exception{

        String un = db.fetchUsername();
        username.setText(un);

        String eml = db.fetchEmail();
        username.setText(eml);

        int lv = db.fetchLevel();
        level.setText(Integer.toString(lv));

        pane.getChildren().addAll(username, email, level);
    }


    public void changeUsername() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("changeUsername.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)changeUsername.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void changePassword() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("changePassword.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)changePassword.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void back() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)back.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
