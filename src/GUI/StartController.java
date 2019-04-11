package GUI;

import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller class for handling the Graphical User Interface for the game start menu.
 *
 * @author magnubau
 */
public class StartController {

    @FXML
    private Button loginButton, registrerButton;
    private SceneSwitcher sceneSwitcher;

    /**
     * Constructor for the StartController class.
     */
    public StartController(){
        sceneSwitcher = new SceneSwitcher();
    }

    /**
     * Switches scene to login screen when loginButton is pressed.
     * @throws Exception
     */
    public void login() throws Exception{

        SFXPlayer.getInstance().setSFX(1);

        sceneSwitcher.switchScene(loginButton, "LoginScreen.fxml");
    }

    /**
     * Switches scene to register when registerButton is pressed.
     * @throws Exception
     */
    public void registrer() throws Exception{
        SFXPlayer.getInstance().setSFX(1);

        sceneSwitcher.switchScene(registrerButton, "Register.fxml");
    }

}
