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


/**
 * RegisterController.java
 * The program will register a new user.
 * The username must not be registered in the DB before.
 * The password will be hashed and salted.
 * @author shahedsa saramoh
 */
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

    /**
     * Register the user
     * @throws Exception
     */
    public void register() throws Exception {

        /*
        Checks if any of the fields is empty
         */
        if(usernameInput.getText().isEmpty() || passwordInput.getText().isEmpty() || rePasswordInput.getText().isEmpty()) {
            alert.setTitle("Empty Field");
            alert.setHeaderText(null);
            alert.setContentText("Field can not be empty.");
            alert.showAndWait();
        }

        /*
        Checks the input username if it exists in DB already.
         */
        else if (db.findUsername(usernameInput.getText().trim())) {
            alert.setTitle("Check Username");
            alert.setHeaderText(null);
            alert.setContentText("Username exists in database already!");
            alert.showAndWait();

        }

        /*
        Checks the passwords if they do not match.
         */
        else if(!passwordInput.getText().trim().equals(rePasswordInput.getText().trim())){
            alert.setTitle("Not match Password");
            alert.setHeaderText(null);
            alert.setContentText("You input different password, try again!");
            alert.showAndWait();

            /*
            The user will be registered using the method registerUser() from the DB.
            The input password will be hashed and salted and saved in DB using the method addPassword().
            The scene will switch to the MainMenu-scene
             */
        }else{
            db.registerUser(usernameInput.getText().trim());
            db.addPassword(passwordInput.getText().trim());
            SFXPlayer.getInstance().setSFX(0);
            audio.MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(2);
            sceneSwitcher.switchScene(registerButton, "MainMenu.fxml");
        }
    }

    /**
     * Switches the scene to the start-scene.
     * @throws Exception
     */
    public void cancel() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }
}

