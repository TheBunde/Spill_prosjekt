package GUI;

import Main.*;
import Database.*;
import audio.MusicPlayer;
import audio.SFXPlayer;
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
                SFXPlayer.getInstance().setSFX(7);
                Parent root = FXMLLoader.load(getClass().getResource("createcharacter.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage)joinLobbyButton.getScene().getWindow();
                stage.setScene(scene);
                audio.MusicPlayer.getInstance().stopSong();
                MusicPlayer.getInstance().changeSong(8);
            }
            else {
                errorLabel.setText("Not a valid lobby key");
                SFXPlayer.getInstance().setSFX(6);
            }
        }
        else{
            errorLabel.setText("Please enter a lobby key");
            SFXPlayer.getInstance().setSFX(5);
        }


    }

    public void clearErrorLabel(){
        errorLabel.setText("");
    }
}
