package GUI;

import Database.*;
import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrerController {

    @FXML
    private TextField usernameInput, emailInput;

    @FXML
    private PasswordField passwordInput, rePasswordInput;

    @FXML
    private Button registrerButton, cancelButton;
    private SceneSwitcher sceneSwitcher;

    private Database db = InterfaceMain.db;

    public RegistrerController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void registrer() throws Exception{
        db.user = new User(-1, usernameInput.getText(), 0, emailInput.getText());
        db.addUser(db.user);
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(2);
        sceneSwitcher.switchScene(registrerButton, "MainMenu.fxml");
    }


    public void cancel() throws Exception{
        SFXPlayer.getInstance().setSFX(0);

        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }
}
