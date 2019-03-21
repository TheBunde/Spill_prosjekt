package GUI;


import Database.*;
import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {


    @FXML
    private Button startNewGameButton;


    private Database db = InterfaceMain.db;

    public void startNewGameButtonPressed() throws Exception{
        db.createNewLobby();
        new SFXPlayer("knockSFX").run();
        MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(3);
        Parent root = FXMLLoader.load(getClass().getResource("createcharacter.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)startNewGameButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button joinLobbyButton;

    public void joinLobbyButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        Parent root = FXMLLoader.load(getClass().getResource("FindLobby.fxml"));
        Scene scene = new Scene(root, 800, 500);
        Stage stage = (Stage)joinLobbyButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button viewAccountButton;

    public void viewAccountButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        Parent root = FXMLLoader.load(getClass().getResource("AccountDetails.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)viewAccountButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button settingsButton;

    public void settingsButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)settingsButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button helpButton;


    public void helpButtonPressed() throws Exception{
        new SFXPlayer("knockSFX").run();
        Parent root = FXMLLoader.load(getClass().getResource("https://gitlab.stud.iie.ntnu.no/heleneyj/game-development-project/wikis/System/User-manual"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)helpButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button signOutButton;

    public void signOutButtonPressed() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(0);
        Parent root = FXMLLoader.load(getClass().getResource("start.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)signOutButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
