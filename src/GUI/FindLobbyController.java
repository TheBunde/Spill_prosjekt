package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FindLobbyController {
    @FXML
    private Button joinLobbyButton;

    @FXML
    private TextField lobbyKeyInput;

    @FXML
    private Label errorLabel;

    public void joinLobby(){
        String key = lobbyKeyInput.getText();
        if (key.length() == 0){
            errorLabel.setText("Please enter a lobby key");
        }
    }

    public void clearErrorLabel(){
        errorLabel.setText("");
    }
}
