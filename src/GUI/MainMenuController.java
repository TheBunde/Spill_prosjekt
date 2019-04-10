package GUI;

import main.*;
import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MainMenuController {


    @FXML
    private Button startNewGameButton;

    @FXML
    private Button joinLobbyButton;

    @FXML
    private Button viewAccountButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button signOutButton;

    private SceneSwitcher sceneSwitcher;

    public MainMenuController(){sceneSwitcher = new SceneSwitcher();}

    public void initialize(){

        MusicPlayer.getInstance().changeSong(2);
        MusicPlayer.getInstance().keepPlaying(2);
        Main.user.setPlayerId(-1);
        Main.db.setHost(false);
    }

    public void startNewGameButtonPressed() throws Exception{
        Main.db.createNewLobby();
        Main.db.setHost(true);
        Main.db.addChatMessage(Main.user.getUsername() + " has joined the lobby as the host", true);
        SFXPlayer.getInstance().setSFX(0);
        MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(3);
        sceneSwitcher.switchScene(startNewGameButton, "createcharacter.fxml");
    }

    public void joinLobbyButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(joinLobbyButton, "FindLobby.fxml");
    }

    public void viewAccountButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(viewAccountButton, "AccountDetails.fxml");
    }

    public void settingsButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(settingsButton, "settings.fxml");
    }

    public void helpButtonPressed() throws Exception {
        new SFXPlayer("knockSFX").run();
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("https://gitlab.stud.iie.ntnu.no/heleneyj/game-development-project/wikis/System%20Documentation"));
            }
            catch (IOException ioe) {
                System.out.println("Error with IO");
                ioe.printStackTrace();
            }
            catch (URISyntaxException e) {
                System.out.println("Error in URL");
                e.printStackTrace();
            }
        }
    }

    public void signOutButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(10);
        sceneSwitcher.switchScene(signOutButton, "start.fxml");
    }

}
