package GUI;

import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RegistrerController {

    @FXML
    private Button registrerButton, cancelButton;
    private SceneSwitcher sceneSwitcher;

    public RegistrerController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void registrer() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(2);
        sceneSwitcher.switchScene(registrerButton, "MainMenu.fxml");
    }


    public void cancel() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(0);
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }
}
