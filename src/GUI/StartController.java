package GUI;

import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController {

    @FXML
    private Button loginButton, registrerButton;
    private SceneSwitcher sceneSwitcher;

    public StartController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void initialize(){
        loginButton.getStyleClass().add("GUI/images/startButton.png");
    }

    public void login() throws Exception{

        SFXPlayer.getInstance().setSFX(1);

        sceneSwitcher.switchScene(loginButton, "LoginScreen.fxml");
    }

    public void registrer() throws Exception{
        SFXPlayer.getInstance().setSFX(1);

        sceneSwitcher.switchScene(registrerButton, "Register.fxml");
    }

}
