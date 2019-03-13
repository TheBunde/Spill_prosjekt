package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
