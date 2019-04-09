package GUI;

import Main.Main;
import Database.Database;
import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * changeUsernameController.java
 * The program handles the components in changeUsername-scene.
 * @author shahedsa saramoh
 */
public class ChangeUsernameController {

    private SceneSwitcher sceneSwitcher;
    private Database db = Main.db;

    @FXML
    private TextField newUsername;

    @FXML
    private Button ok, cancel;

    public ChangeUsernameController(){
        sceneSwitcher = new SceneSwitcher();
    }

    /**
     * Sets new Username using the method setNewUsername() from DB
     * @throws Exception
     */
    public void setNewUsername()throws Exception{
        db.setNewUsername(newUsername.getText().trim());
    }

    /**
     * Confirms changing of the Username and goes back to the Account-scene.
     * @throws Exception
     */
    public void okButtonPressed() throws Exception{
        boolean enable;
        if(newUsername.getText().isEmpty()){
            enable = false;                       //It goes false if the new-username-input is empty.
        }else{
            setNewUsername();
            SFXPlayer.getInstance().setSFX(0);
            audio.MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(2);
            sceneSwitcher.switchScene(ok, "AccountDetails.fxml");
        }
    }

    /**
     * Switches the scene to the AccountDetails-scene.
     * @throws Exception
     */
    public void cancelButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(cancel, "AccountDetails.fxml");
    }
}
