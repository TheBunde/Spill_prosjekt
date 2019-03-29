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

    public void initialize() throws Exception {
        getInfo();
    }

    public void getInfo() throws Exception {
        usernameOutput.setText(db.fetchUsername());
        rankOutput.setText(Integer.toString(db.fetchRank()));
    }

    public void changeUsernameButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(2);
        sceneSwitcher.switchScene(changeUsernameButton, "changeUsername.fxml");
    }

    public void changePasswordButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(2);
        sceneSwitcher.switchScene(changePasswordButton, "changePassword.fxml");
    }

    public void backToMenuButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(2);
        sceneSwitcher.switchScene(backButton, "MainMenu.fxml");
    }
}
