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
    private SceneSwitcher sceneSwitcher;
    private Database db = Main.db;

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput, rePasswordInput;

    @FXML
    private Button registerButton, cancelButton;


    public RegisterController() {
        sceneSwitcher = new SceneSwitcher();
    }


    public void register() throws Exception {

        if(usernameInput.getText().isEmpty() || passwordInput.getText().isEmpty() || rePasswordInput.getText().isEmpty()) {
            alert.setTitle("Empty Field");
            alert.setHeaderText(null);
            alert.setContentText("Field can not be empty.");
            alert.showAndWait();
        }
        else if (db.findUsername(usernameInput.getText().trim())) {
            alert.setTitle("Check Username");
            alert.setHeaderText(null);
            alert.setContentText("Username exists in database already!");
            alert.showAndWait();

        }
        else if(!passwordInput.getText().trim().equals(rePasswordInput.getText().trim())){
            alert.setTitle("Not match Password");
            alert.setHeaderText(null);
            alert.setContentText("You input different password, try again!");
            alert.showAndWait();

        }else{
            db.registerUser(usernameInput.getText().trim());
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

