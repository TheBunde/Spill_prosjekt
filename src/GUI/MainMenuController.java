package GUI;

import main.*;
import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * MainMenuController.Java
 * The porgram handles the components in the MainMenu-scene.
 * @author williad, saramoh
 */
public class MainMenuController {

    private SceneSwitcher sceneSwitcher;

    @FXML
    private Button startNewGameButton, joinLobbyButton, viewAccountButton, settingsButton, helpButton, signOutButton;


    /**
     * Constructor for MainMenuController
     */
    public MainMenuController(){sceneSwitcher = new SceneSwitcher();}
    
     /**
     * The user has no player_id, so, sets setHost() false
     */
     public void initialize(){
        MusicPlayer.getInstance().changeSong(2);
        MusicPlayer.getInstance().keepPlaying(2);
        Main.user.setPlayerId(-1);
        Main.db.setHost(false);
    }
    
    /**
     * Creates a new lobby using the method createNewLobby() from class database.java,
     * sets the player as host using the method setHost from DB, and switches the scene to the createcharacter-scene.
     *
     * @throws Exception
     */
    public void startNewGameButtonPressed() throws Exception{
        Main.db.createNewLobby();
        Main.db.setHost(true);
        Main.db.addChatMessage(Main.user.getUsername() + " has joined the lobby as the host", true);
        SFXPlayer.getInstance().setSFX(0);
        MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(3);
        sceneSwitcher.switchScene(startNewGameButton, "createcharacter.fxml");
    }
    
      /**
       * Switches the scene to the FindLobby-scene.
       *
       * @throws Exception
       */
    public void joinLobbyButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(joinLobbyButton, "FindLobby.fxml");
    }

    /**
     * Switches the scene to the AccountDetails-scene
     *
     * @throws Exception
     */
    public void viewAccountButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(viewAccountButton, "AccountDetails.fxml");
    }

    /**
     * Switches the scene to the settings-scene
     *
     * @throws Exception
     */
    public void settingsButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(settingsButton, "settings.fxml");
    }

    /**
     * Connects the User to the User-manual page in WIKI in GitLab
     *
     * @throws Exception
     */
    public void helpButtonPressed() throws Exception {
        new SFXPlayer("knockSFX").run();
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("https://gitlab.stud.iie.ntnu.no/heleneyj/game-development-project/blob/master/Documentation/User_manual.pdf"));
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

    /**
     * Switches the scene to the start-scene
     *
     * @throws Exception
     */
    public void signOutButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(10);
        sceneSwitcher.switchScene(signOutButton, "start.fxml");
    }
}
