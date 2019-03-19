package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class FindLobbyController {
    @FXML
    private Button joinLobbyButton;

    @FXML
    private TextField lobbyKeyInput;

    @FXML
    private Label errorLabel;


    public void joinLobbyButtonPressed() throws IOException {
        String key = lobbyKeyInput.getText();
        //Checking if the input is valid
        if (key.length() > 0){
            //Loads new scene
            Parent root = FXMLLoader.load(getClass().getResource("GameLobby.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage)joinLobbyButton.getScene().getWindow();
            stage.setScene(scene);
        }
        else{
            errorLabel.setText("Please enter a lobby key");
        }
    }

    public void clearErrorLabel(){
        errorLabel.setText("");
    }
}
