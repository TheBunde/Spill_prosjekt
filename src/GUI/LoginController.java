package GUI;

import Main.*;
import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Database.*;
import login.*;


public class LoginController {

    Alert alert = new Alert(Alert.AlertType.WARNING);
    private SceneSwitcher sceneSwitcher;
    private Password pw = new Password();
    private Database db = Main.db;
    private User user = Main.user;

    @FXML
    TextField username, password;

    @FXML
    private Button cancelButton, loginButton;


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

    public boolean loginButtonPressed() throws Exception {

        if(username.getText().isEmpty() || password.getText().isEmpty()) {
            alert.setTitle("Empty Field");
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
            alert.setContentText("You input wrong password, try again!");
            alert.showAndWait();
        }else{
            Main.user = new User(db.fetchUser_id(username.getText().trim()), username.getText().trim(), db.fetchRank(db.fetchUser_id(username.getText().trim())));
            SFXPlayer.getInstance().setSFX(0);
            audio.MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(2);
            sceneSwitcher.switchScene(loginButton, "MainMenu.fxml");
        }
        return false;
    }


    public void cancelButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }
}


