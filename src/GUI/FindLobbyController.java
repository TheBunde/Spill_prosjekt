package GUI;

import Main.*;
import Database.*;
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

    private Database db = Main.db;


    public void joinLobbyButtonPressed() throws IOException {
        String key = lobbyKeyInput.getText();
        //Checking if the input is valid
        if (key.length() > 0){
            if (db.connectUserToGameLobby(Integer.parseInt(key))){
                //Loads new scene
                Parent root = FXMLLoader.load(getClass().getResource("createcharacter.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage)joinLobbyButton.getScene().getWindow();
                stage.setScene(scene);
            }
            else {
                errorLabel.setText("Not a valid lobby key");
            }

        }
        else{
            errorLabel.setText("Please enter a lobby key");
        }
    }

    public void clearErrorLabel(){
        errorLabel.setText("");
    }
}
