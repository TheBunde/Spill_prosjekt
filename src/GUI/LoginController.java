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
 // This method checking the username if it exists in database by call method findUsername() from database.java
    public boolean checkUsername() {
        if (db.findUsername(username.getText())) {
            return true;
        }
        return false;
    }

    
 // This method checking if the password entered by user is identical with the one which is registered in database
    public boolean checkPassword() {
        /* checking if the hashed password that associated with a specific username is equal with the hashed password and fetching
        the salted password that associated with the specific username
         */
        if (db.fetchHash(username.getText()).equals(pw.getHash(password.getText(), db.fetchSalt(username.getText())))) {
            return true;
        }
        return false;
    }
  // This method performes when login button is pressed
    public boolean loginButtonPressed() throws Exception {
       // checks if usernamer or password field is empty, and if it is display a warning message
        if(username.getText().isEmpty() || password.getText().isEmpty()) {
            alert.setTitle("Empty Field");
            alert.setHeaderText(null);
            alert.setContentText("Field can not be empty.");
            alert.showAndWait();
        }
         //check if username is valid(exist in database)
        else if(!checkUsername()){
            alert.setTitle("Check username");
            alert.setHeaderText(null);
            alert.setContentText("Username is not valid, try again!");
            alert.showAndWait();
        }
        // checks if password is identical with the password that associated with the specific username
        else if(!checkPassword()){
            alert.setTitle("Check password");
            alert.setHeaderText(null);
            alert.setContentText("You input wrong password, try again!");
            alert.showAndWait();
        }else{
            // if everything is ok, so switch the scene to main menu.
            Main.user = new User(db.fetchUser_id(username.getText().trim()), username.getText().trim(), db.fetchRank(db.fetchUser_id(username.getText().trim())));
            SFXPlayer.getInstance().setSFX(0);
            audio.MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(2);
            sceneSwitcher.switchScene(loginButton, "MainMenu.fxml");
        }
        return false;
    }

    // This method performs when the cancel button is pressed and switch the scene to start page of the application
    public void cancelButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }
}


