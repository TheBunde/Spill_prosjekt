package GUI;
import Main.Main;
import Database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class changeUsernameController {

    private Database db = Main.db;

    @FXML
    private TextField newUsername;

    @FXML
    private Button ok, cancel;

    public void setNewUsername()throws Exception{
        db.setNewUsername(newUsername.getText().trim());
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
