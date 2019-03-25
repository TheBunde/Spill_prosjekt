package GUI;

import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartController {

    @FXML
    private Button loginButton, registrerButton;
    private SceneSwitcher sceneSwitcher;

    public StartController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void login() throws Exception{

        SFXPlayer.getInstance().setSFX(1);

        sceneSwitcher.switchScene(loginButton, "LoginScreen.fxml");
    }

    public void registrer() throws Exception{
        SFXPlayer.getInstance().setSFX(1);

        sceneSwitcher.switchScene(registrerButton, "Registrer.fxml");
    }

}
