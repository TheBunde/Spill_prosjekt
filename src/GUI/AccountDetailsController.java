package GUI;

import Main.*;
import Database.*;

import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * AccountDetailsController.java
 * The program handles the components in AccountDetails-scene.
 * @author saramoh
 */
public class AccountDetailsController {
    private Database db = Main.db;
    private SceneSwitcher sceneSwitcher;

    @FXML
    private Text usernameOutput, rankOutput;

    @FXML
    private Button changeUsernameButton, changePasswordButton, backButton;


    public AccountDetailsController() {
        sceneSwitcher = new SceneSwitcher();
    }

    /**
     * Initializes information from the method getInfo()
     * @throws Exception
     */
    public void initialize() throws Exception {
        getInfo();
    }

    /**
     * Sets the Username and the Rank which is fetched from DB as a fxml-text
     * @throws Exception
     */
    public void getInfo() throws Exception {
        usernameOutput.setText(db.fetchUsername());
        rankOutput.setText(Integer.toString(db.fetchRank(Main.user.getUser_id())));
    }

    /**
     * Switches the scene to the ChangeUsername-scene.
     * @throws Exception
     */
    public void changeUsernameButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(2);
        sceneSwitcher.switchScene(changeUsernameButton, "changeUsername.fxml");
    }

    /**
     * Switches the scene to the ChangePassword-scene.
     * @throws Exception
     */
    public void changePasswordButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(2);
        sceneSwitcher.switchScene(changePasswordButton, "changePassword.fxml");
    }

    /**
     * Switches the scene to the MainMenu-scene.
     * @throws Exception
     */
    public void backToMenuButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(2);
        sceneSwitcher.switchScene(backButton, "MainMenu.fxml");
    }
}
