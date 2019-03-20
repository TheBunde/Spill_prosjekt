package GUI;

import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class LoginController {

    @FXML
    private Button cancelButton, loginButton;
    private SceneSwitcher sceneSwitcher;

    public LoginController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void cancel() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(0);
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }

    public void login() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(2);
        sceneSwitcher.switchScene(loginButton, "MainMenu.fxml");
    }
}

