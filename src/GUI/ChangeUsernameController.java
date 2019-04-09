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
 * The program handles the components in AccountDetails scene.
 * @author saramoh
 */
public class changeUsernameController {

    private SceneSwitcher sceneSwitcher;
    private Database db = Main.db;

    @FXML
    private TextField newUsername;

    @FXML
    private Button ok, cancel;

    public changeUsernameController(){
        sceneSwitcher = new SceneSwitcher();
    }

    /**
     * The method sets new Username using another method which is connected to DB and sets new Username.
     * @throws Exception
     */
    public void setNewUsername()throws Exception{
        db.setNewUsername(newUsername.getText().trim());
    }

    /**
     * The method confirm changing of the Username and goes back to the Account-scene.
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
     * The method switches the scene to the AccountDetails-scene.
     * @throws Exception
     */
    public void cancelButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(cancel, "AccountDetails.fxml");
    }
}
