package GUI;

import Main.*;
import Database.*;
import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    Alert alert = new Alert(Alert.AlertType.WARNING);

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput, rePasswordInput;

    @FXML
    private Button registerButton, cancelButton;
    private SceneSwitcher sceneSwitcher;
    private Database db = Main.db;

    public RegisterController() {
        sceneSwitcher = new SceneSwitcher();
    }


    public void register() throws Exception {

        if(usernameInput.getText().isEmpty() || passwordInput.getText().isEmpty() || rePasswordInput.getText().isEmpty()) {
            alert.setTitle("Empty textfield");
            alert.setHeaderText(null);
            alert.setContentText("Field can not be empty.");
            alert.showAndWait();
        }
         else if (db.findUsername(usernameInput.getText().trim())) {
             alert.setTitle("Check username");
             alert.setHeaderText(null);
             alert.setContentText("Username exists in database already!");
             alert.showAndWait();

         }else{
            db.registerUser(usernameInput.getText().trim(), passwordInput.getText().trim(), rePasswordInput.getText().trim());
            db.addPassword(passwordInput.getText().trim());
            SFXPlayer.getInstance().setSFX(0);
            audio.MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(2);
            sceneSwitcher.switchScene(registerButton, "MainMenu.fxml");
        }
    }


    public void cancel() throws Exception {
        SFXPlayer.getInstance().setSFX(0);

        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }
}

