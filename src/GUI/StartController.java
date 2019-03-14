package GUI;

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
        sceneSwitcher.switchScene(loginButton, "LoginScreen.fxml");
    }

    public void registrer() throws Exception{
        sceneSwitcher.switchScene(registrerButton, "Registrer.fxml");
    }

}
