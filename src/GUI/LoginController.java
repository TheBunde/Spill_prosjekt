package GUI;

import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import user.*;

/**
 * LoginController.java
 * The program handles the components in Login-scene.
 * @author saramoh, shahedsa
 */

public class LoginController {

    private Alert alert = new Alert(Alert.AlertType.WARNING);
    private SceneSwitcher sceneSwitcher;
    private Password pw = new Password();

    @FXML
    TextField username, password;

    @FXML
    private Button cancelButton, loginButton;


    public LoginController() {
        sceneSwitcher = new SceneSwitcher();
    }
  /**
     * This method checking the username if it exists in database by call method findUsername() from database.java
     * @return true if the username exist
     */
    public boolean checkUsername() {
        if (main.db.findUsername(username.getText())) {
            return true;
        }
        return false;
    }

    
  /**
     * This method checking if the password entered by user is identical with the one which is registered in database
     * @return true if the password identical
     */
    public boolean checkPassword() {
        if (main.db.fetchHash(username.getText()).equals(pw.getHash(password.getText(), main.db.fetchSalt(username.getText())))) {
            return true;
        }
        return false;
    }
   /**
     * This method performes when login button is pressed
     * @return false if there is something went wrong
     * @throws Exception 
     */
    public boolean loginButtonPressed() throws Exception {
       // checks if usernamer or password field is empty, and if it is display a warning message
        if(username.getText().isEmpty() || password.getText().isEmpty()) {
            alert.setTitle("Empty Field");
            alert.setHeaderText(null);
            alert.setContentText("Fields can not be empty.");
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
            alert.setContentText("Your password is wrong, try again!");
            alert.showAndWait();
        }else{
            main.user = new User(main.db.fetchUser_id(username.getText().trim()), username.getText().trim(), main.db.fetchRank(main.db.fetchUser_id(username.getText().trim())));
            SFXPlayer.getInstance().setSFX(0);
            audio.MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(2);
            sceneSwitcher.switchScene(loginButton, "MainMenu.fxml");
        }
        return false;
    }

     /**
     * This method performs when the cancel button is pressed and switch the scene to start page of the application
     * @throws Exception
     */
    public void cancelButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }
}


