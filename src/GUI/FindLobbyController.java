package GUI;

import main.*;
import database.*;
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
    private SceneSwitcher sceneSwitcher;
    @FXML
    private Button joinLobbyButton, cancelButton;
    @FXML
    private TextField lobbyKeyInput;
    @FXML
    private Label errorLabel;

    public FindLobbyController() {
        sceneSwitcher = new SceneSwitcher();
    }
    
    public void joinLobbyButtonPressed() throws Exception {
        String key = lobbyKeyInput.getText();
        //Checking if the input is valid
        if (key.length() > 0){
            if (Main.db.connectUserToGameLobby(Integer.parseInt(key))){
                Main.db.addChatMessage(Main.user.getUsername() + " has joined the lobby as a guest", true);
                //Loads new scene
                SFXPlayer.getInstance().setSFX(7);
                this.sceneSwitcher.switchScene(joinLobbyButton, "createcharacter.fxml");
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

    public void cancelButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(cancelButton, "MainMenu.fxml");
    }
}
