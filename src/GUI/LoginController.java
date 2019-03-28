package GUI;

import Main.*;
import Database.Database;
import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import login.Password;


public class LoginController {

    Alert alert = new Alert(Alert.AlertType.WARNING);
    private Password pw = new Password();


    @FXML
    TextField username;
    @FXML
    TextField password;

    @FXML
    private Button cancelButton, loginButton;
    private SceneSwitcher sceneSwitcher;

    private Database db = Main.db;

    public LoginController() {
        sceneSwitcher = new SceneSwitcher();
    }

    public boolean checkUsername() {
        if (db.findUsername(username.getText())) {
            return true;
        }
        return false;
    }


    public boolean checkPassword() {
        if (db.fetchHash(username.getText()).equals(pw.getHash(password.getText(), db.fetchSalt(username.getText())))) {
            return true;
        }
        return false;
    }

    public boolean login() throws Exception {
        boolean ok;

        if(username.getText().isEmpty() || password.getText().isEmpty()) {
            alert.setTitle("Empty textfield");
            alert.setHeaderText(null);
            alert.setContentText("Field can not be empty.");
            alert.showAndWait();
        }
        else if(!checkUsername()){
            alert.setTitle("Check username");
            alert.setHeaderText(null);
            alert.setContentText("Username is not valid, try again!");
            alert.showAndWait();
        }
        else if(!checkPassword()){
            alert.setTitle("Check password");
            alert.setHeaderText(null);
            alert.setContentText("You put wrong password, try again!");
            alert.showAndWait();
        }else{
            SFXPlayer.getInstance().setSFX(0);
            audio.MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(2);
            sceneSwitcher.switchScene(loginButton, "MainMenu.fxml");
        }
        return false;
    }


    public void cancel() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }
}

