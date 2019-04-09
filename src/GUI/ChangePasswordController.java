package GUI;

import database.Database;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.*;

public class ChangePasswordController {

    Alert alert = new Alert(Alert.AlertType.WARNING);
    private SceneSwitcher sceneSwitcher;
    private Database db = Main.db;

    @FXML
    private TextField oldPassword, newPassword, reNewPassword;

    @FXML
    private Button backToMenuButton, confirmButton;



    public ChangePasswordController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void confirmButtonPressed()throws Exception{
        SFXPlayer.getInstance().setSFX(0);

        if(oldPassword.getText().isEmpty() || newPassword.getText().isEmpty() || reNewPassword.getText().isEmpty()) {
            alert.setTitle("Empty Field");
            alert.setHeaderText(null);
            alert.setContentText("Fill in all fields.");
            alert.showAndWait();
        }

        else if(!newPassword.getText().equals(reNewPassword.getText())){
            alert.setTitle("Not match Password");
            alert.setHeaderText(null);
            alert.setContentText("The passwords do not match, try again!");
            alert.showAndWait();
        } else {

            setNewPassword();
            alert.setTitle("Password changed");
            alert.setHeaderText(null);
            alert.setContentText("Password changed successfully.");
            alert.showAndWait();
            sceneSwitcher.switchScene(confirmButton, "AccountDetails.fxml");
        }
    }

    public void setNewPassword(){
        db.deleteOldPassword(oldPassword.getText().trim());
        db.addPassword(newPassword.getText().trim());
    }

    public void backToMenu() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(backToMenuButton, "AccountDetails.fxml");
    }
}
