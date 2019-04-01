package GUI;

import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartController {

    @FXML
    private Button loginButton, registerButton;
    private SceneSwitcher sceneSwitcher;

    public StartController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void login() throws Exception{

        SFXPlayer.getInstance().setSFX(1);

        sceneSwitcher.switchScene(loginButton, "LoginScreen.fxml");
    }

    public void register() throws Exception{
        SFXPlayer.getInstance().setSFX(1);

        sceneSwitcher.switchScene(registerButton, "Register.fxml");
    }

}
