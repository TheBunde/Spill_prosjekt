package GUI;

import main.*;
import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * changeUsernameController.java
 * The program handles the components in changeUsername-scene.
 * @author shahedsa, saramoh
 */
public class changeUsernameController {

    private SceneSwitcher sceneSwitcher;

    @FXML
    private TextField newUsername;

    @FXML
    private Button ok, cancel;

    /**
     * Constructor for changeUsernameController
     */
    public changeUsernameController(){
        sceneSwitcher = new SceneSwitcher();
    }
    
     /**
      * Sets the new username calling the method setNewUsername from database.java
      *
      * @throws Exception
      */
    public void setNewUsername()throws Exception{
        Main.db.setNewUsername(newUsername.getText().trim());
    }

    /**
     * Confirms changing of the username, and switches the scene to the AccountDetails-scene
     *
     * @throws Exception
     */
    public void okButtonPressed() throws Exception{
        boolean enable;
        if(newUsername.getText().isEmpty()){
            enable = false;                      //If the newUsername-textfield is empty
        }else{
            setNewUsername();
            SFXPlayer.getInstance().setSFX(0);
            audio.MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(2);
            sceneSwitcher.switchScene(ok, "AccountDetails.fxml");
        }
    }
    
    /**
     * Cancels changing, and returns to the AccountDetails-scene
     *
     * @throws Exception
     */
    public void cancelButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(cancel, "AccountDetails.fxml");
    }
}
