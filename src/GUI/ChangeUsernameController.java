package GUI;

import Database.Database;
import GUI.InterfaceMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChangeUsernameController {

    @FXML
    private TextField newUsername;
    private Database db = InterfaceMain.db;


    public void setNewUsername()throws Exception{
        db.user.setUsername(newUsername.getText());
    }

    @FXML
    private Button ok, cancel;

    public void okButtonPressed() throws Exception{
        setNewUsername();
        Parent root = FXMLLoader.load(getClass().getResource("AccountDetails.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)ok.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void cancelButtonPressed() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("AccountDetails.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)cancel.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
