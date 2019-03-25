package GUI;

import Database.Database;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ChangePasswordController {

    @FXML
    private TextField oldPassword;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField reNewPassword;
    @FXML
    private Label statusLabel;
    @FXML
    private Button backToMenuButton;
    @FXML
    private Button confirmButton;


    private SceneSwitcher sceneSwitcher;

    private Database db = InterfaceMain.db;

    public ChangePasswordController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void confirmButtonPressed(){
        SFXPlayer.getInstance().setSFX(0);

        if(!newPassword.getText().equals(reNewPassword.getText())){
            statusLabel.setText("The passwords do not match.");
        }
        else{
            if(newPassword.getText().equals("") || reNewPassword.getText().equals("") || oldPassword.getText().equals("")){
                statusLabel.setText("Fill in all fields.");
            }
            else{
                SFXPlayer.getInstance().setSFX(0);
                statusLabel.setText("Password change successful.");
            }
        }
    }

    public void backToMenu() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(backToMenuButton, "AccountDetails.fxml");
    }
}
