package GUI;

import main.*;
import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller class to handle the Graphical User Interface for the find lobby-menu
 *
 * @author williad, shahedsa
 */
public class FindLobbyController {
    private SceneSwitcher sceneSwitcher;
    @FXML
    private Button joinLobbyButton, cancelButton;
    @FXML
    private TextField lobbyKeyInput;
    @FXML
    private Label errorLabel;

    /**
     * Constructor for FindLobbyController
     */
    public FindLobbyController() {
        sceneSwitcher = new SceneSwitcher();
    }

    /**
     * Connects the user to the lobby with the typed key
     * @throws Exception
     */
    public void joinLobbyButtonPressed() throws Exception {
        String key = lobbyKeyInput.getText();
        /* Checking if the input is valid */
        if (key.length() > 0){
            if (Main.db.connectUserToGameLobby(Integer.parseInt(key))){
                Main.db.addChatMessage(Main.user.getUsername() + " has joined the lobby as a guest", true);
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

    /**
     * Clears the error label
     */
    public void clearErrorLabel(){
        errorLabel.setText("");
    }

    /**
     * Switches back to main menu
     * @throws Exception
     */
    public void cancelButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(cancelButton, "MainMenu.fxml");
    }
}
