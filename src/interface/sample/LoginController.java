package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class LoginController {

    @FXML
    private Button cancelButton, loginButton;
    private SceneSwitcher sceneSwitcher;

    public LoginController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void cancel() throws Exception{
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }

    public void login() throws Exception{
        sceneSwitcher.switchScene(loginButton, "MainMenu.fxml");
    }
}

