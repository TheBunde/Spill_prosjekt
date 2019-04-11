package GUI;

import main.*;
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
 * @author shahedsa, saramoh
 */
public class LoginController {

    private Alert alert = new Alert(Alert.AlertType.WARNING);
    private SceneSwitcher sceneSwitcher;
    private Password pw = new Password();

    @FXML
    TextField username, password;

    @FXML
    private Button cancelButton, loginButton;


    /**
     * Constructor for LoginController
     */
    public LoginController() {
        sceneSwitcher = new SceneSwitcher();
    }

  /**
     * Checks if the username exists in database by calling the method findUsername() from database.java
     * 
     * @return   true if the username exists, false otherwise
     */
    public boolean checkUsername() {
        if (Main.db.findUsername(username.getText())) {
            return true;
        }
        return false;
    }

    
  /**
   * Checks if the password entered by user is identical with the one which is registered in database
   *
   * @return   true if the password is identical, false otherwise
   */
    public boolean checkPassword() {
        if (Main.db.fetchHash(username.getText()).equals(pw.getHash(password.getText(), Main.db.fetchSalt(username.getText())))) {
            return true;
        }
        return false;
    }

   /**
    * Logs in the user
    *
    * @return true if the user logs in, false otherwise
    * @throws Exception
    */
    public boolean loginButtonPressed() throws Exception {

        /*
        Checks if the username or the password field is empty
         */
        if(username.getText().isEmpty() || password.getText().isEmpty()) {
            alert.setTitle("Empty Field");
            alert.setHeaderText(null);
            alert.setContentText("Fields can not be empty.");
            alert.showAndWait();
        }

        /*
        Checks if the username exists in the DB
         */
        else if(!checkUsername()){
            alert.setTitle("Check username");
            alert.setHeaderText(null);
            alert.setContentText("Username is not valid, try again!");
            alert.showAndWait();
        }

        /*
        Checks if the password is correct
         */
        else if(!checkPassword()){
            alert.setTitle("Check password");
            alert.setHeaderText(null);
            alert.setContentText("Your password is wrong, try again!");
            alert.showAndWait();

            /*
            Sets the Main.user as a new instance of User, and switches to MainMenu-scene
             */
        }else{
            Main.user = new User(Main.db.fetchUser_id(username.getText().trim()), username.getText().trim(), Main.db.fetchRank(Main.db.fetchUser_id(username.getText().trim())));
            SFXPlayer.getInstance().setSFX(0);
            audio.MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(2);
            sceneSwitcher.switchScene(loginButton, "MainMenu.fxml");
            return true;
        }
        return false;
    }

     /**
      * Cancels the log in, and switches to the start-scene
      *
      * @throws Exception
      */
    public void cancelButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }
}


