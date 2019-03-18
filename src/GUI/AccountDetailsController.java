package GUI;

<<<<<<< HEAD

import AccountDetails.AccountDetailsDatabase;
import AccountDetails.AccountDetailsMain;
import AccountDetails.User;
=======
import Database.*;
>>>>>>> 02a18ff1149eccae70c466a10b316b1920464345
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
<<<<<<< HEAD
import javafx.scene.control.TextField;
=======
>>>>>>> 02a18ff1149eccae70c466a10b316b1920464345
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AccountDetailsController {
<<<<<<< HEAD
    private AccountDetailsDatabase db = AccountDetailsMain.db;


    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Text username, email, level;
=======
    @FXML
    private Text username, email, rank;

    private Database db = InterfaceMain.db;
>>>>>>> 02a18ff1149eccae70c466a10b316b1920464345

    public void initialize() throws Exception{
        getInfo();
    }

    public void getInfo()throws Exception{
<<<<<<< HEAD
        username.setText(db.fetchUsername());
        email.setText(db.fetchEmail());
        level.setText(Integer.toString(db.fetchLevel()));
        anchorPane.getChildren().addAll(username, email, level);
        Parent root = FXMLLoader.load(getClass().getResource("viewAccount.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)anchorPane.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
=======
        username.setText(db.user.getUsername());
        email.setText(db.user.getEmail());
        rank.setText(Integer.toString(db.user.getRank()));
>>>>>>> 02a18ff1149eccae70c466a10b316b1920464345
    }

    @FXML
    private Button changeUsername, changePassword, back;

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

    public void menu() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)back.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
