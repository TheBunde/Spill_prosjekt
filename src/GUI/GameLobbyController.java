package GUI;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class GameLobbyController {
    @FXML
    private Button travelButton;
    @FXML
    private Button backToMenuButton;

    private SceneSwitcher sceneSwitcher = new SceneSwitcher();

    public void travelButtonPressed() throws Exception{
        this.sceneSwitcher.switchScene(travelButton, "battlefield.fxml");
    }

    public void backToMenuButtonPressed() throws Exception{
        this.sceneSwitcher.switchScene(backToMenuButton, "MainMenu.fxml");
    }
}
