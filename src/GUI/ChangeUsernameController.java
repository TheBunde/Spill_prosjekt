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
import static javax.swing.JOptionPane.*;

public class ChangeUsernameController {

    private Database db = InterfaceMain.db;

    @FXML
    private TextField newUsername;

    @FXML
    private Button ok, cancel;

    public void setNewUsername()throws Exception{
            db.user.setUsername(newUsername.getText().trim());
    }



    public void okButtonPressed() throws Exception{
        boolean enable;
        if(newUsername.getText().isEmpty()){
            enable = false;
        }else{
            setNewUsername();
            Parent root = FXMLLoader.load(getClass().getResource("AccountDetails.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage)ok.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    public void cancelButtonPressed() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("AccountDetails.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)cancel.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
