package GUI;

import Database.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Timer;

public class GameLobbyController {
    @FXML
    private Button travelButton;
    @FXML
    private Button backToMenuButton;
    @FXML
    private Label lobbyKeyLabel;

    private SceneSwitcher sceneSwitcher = new SceneSwitcher();

    private Database db = InterfaceMain.db;
    private Timer timer = chatController.timer;

    public void initialize(){
        lobbyKeyLabel.setText("" + db.user.getLobbyKey());
    }

    public void travelButtonPressed() throws Exception{
        this.sceneSwitcher.switchScene(travelButton, "battlefield.fxml");
    }

    public void backToMenuButtonPressed() throws Exception{
        db.disconnectUserFromGameLobby();
        this.timer = chatController.timer;
        this.timer.cancel();
        this.timer.purge();
        this.sceneSwitcher.switchScene(backToMenuButton, "MainMenu.fxml");
    }
}
