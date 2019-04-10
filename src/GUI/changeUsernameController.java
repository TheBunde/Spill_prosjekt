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
 * @author saramoh, shahedsa
 */
public class changeUsernameController {

    private SceneSwitcher sceneSwitcher;

    @FXML
    private TextField newUsername;

    @FXML
    private Button ok, cancel;
  
    public changeUsernameController(){
        sceneSwitcher = new SceneSwitcher();
    }
    
     /**
     * Update the username
     * @throws Exception
     */

    public void setNewUsername()throws Exception{
        Main.db.setNewUsername(newUsername.getText().trim());
    }
    
    
    /**
     * This method performes when the ok button is pressed to change the username
     * @throws Exception
     */
    public void okButtonPressed() throws Exception{
        boolean enable;
        if(newUsername.getText().isEmpty()){
            enable = false;
        }else{
            setNewUsername();
            SFXPlayer.getInstance().setSFX(0);
            audio.MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(2);
            sceneSwitcher.switchScene(ok, "AccountDetails.fxml");
        }
    }
    
    /**
     * cancel changing and return to Account details page
     * @throws Exception
     */
    public void cancelButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(cancel, "AccountDetails.fxml");
    }
}
