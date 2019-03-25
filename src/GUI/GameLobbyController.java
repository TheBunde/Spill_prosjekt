package GUI;

import Database.*;
import audio.MusicPlayer;
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
        db.setStartPos(db.fetchPlayerId());
        //db.movePos(8, 8, db.fetchPlayerId());
    }

    public void travelButtonPressed() throws Exception{
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(7);
        this.timer = chatController.timer;
        this.timer.cancel();
        this.timer.purge();
        this.sceneSwitcher.switchScene(travelButton, "Battlefield.fxml");

    }

    public void backToMenuButtonPressed() throws Exception{
        db.disconnectUserFromGameLobby();
        db.setHost(false);
        this.timer = chatController.timer;
        this.timer.cancel();
        this.timer.purge();
        this.sceneSwitcher.switchScene(backToMenuButton, "MainMenu.fxml");
    }
}
