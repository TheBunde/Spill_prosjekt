package GUI;

import audio.MusicPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller class for handling the Graphical User Interface for the game lobby-menu
 *
 * @author williad, magnubau, henrikwt
 */
public class GameLobbyController {
    @FXML
    private Button readyButton;
    @FXML
    private Button backToMenuButton;
    @FXML
    private Label lobbyKeyLabel, readyCounter;

    private SceneSwitcher sceneSwitcher = new SceneSwitcher();

    private boolean ready = false;
    public static Timer playerReadyTimer = new Timer();
    public static Timer limitPlayerTimer = new Timer();
    private int playersReady;
    private int playerLimit = 4;
    private boolean joinable = true;

    /**
     * Method that runs when the corresponding FXML-file for this Controller is loaded
     */
    public void initialize(){
        lobbyKeyLabel.setText("" + Main.user.getLobbyKey());
        MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(10);
        /* Timer-loop for checking if all players are ready to travel to battlefield */
        playerReadyTimer = new Timer();
        playerReadyTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        playersReady();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        },0 ,1200);

        /* Timer-loop for limiting the amount of players part of the lobby */
        limitPlayerTimer = new Timer();
        /* Player limit is enforced by the host */
        if (Main.user.isHost()) {
            limitPlayerTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                limitPlayers();
                            }
                        }).start();
                    });
                }
            }, 0, 300);
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void readyButtonPressed() throws Exception{
        if(!ready){
            readyButton.setStyle("-fx-background-color: #2ecc71;");
            ready = true;
            Main.db.addChatMessage(Main.user.getUsername() + " is ready", true);
            Main.db.isReady(true);
        }else if(ready){
            readyButton.setStyle("-fx-background-color: #cccccc;");
            ready = false;
            Main.db.addChatMessage(Main.user.getUsername() + " is not ready", true);
            Main.db.isReady(false);
        }
    }

    public void backToMenuButtonPressed() throws Exception{
        if (Main.db.fetchPlayerCount() == 1){
            Main.db.setJoinable(false);
        }
        Main.db.addChatMessage(Main.user.getUsername() + " has left the lobby", true);
        Main.db.disconnectPlayerFromLobby(Main.db.fetchPlayerId());
        Main.db.disconnectUserFromGameLobby();
        Main.db.setHost(false);
        chatController.timer.cancel();
        chatController.timer.purge();
        playerReadyTimer.cancel();
        playerReadyTimer.purge();
        limitPlayerTimer.cancel();
        limitPlayerTimer.purge();
        MusicPlayer.getInstance().stopSong();
        this.sceneSwitcher.switchScene(backToMenuButton, "MainMenu.fxml");
    }

    public void playersReady() throws Exception{
        playersReady = 0;
        ArrayList<Boolean> players = Main.db.everyoneIsReady();
        for(int i = 0; i < players.size(); i++){
            if(players.get(i)){
               playersReady++;
            }
        }
        readyCounter.setText("Players Ready: " + playersReady + " / " + players.size());

        if(playersReady == players.size() && playersReady != 0){
            chatController.timer.cancel();
            chatController.timer.purge();
            playerReadyTimer.cancel();
            playerReadyTimer.purge();
            limitPlayerTimer.cancel();
            limitPlayerTimer.purge();
            Main.db.setJoinable(false);
            this.sceneSwitcher.switchScene(readyButton , "Battlefield.fxml");
        }
    }

    public void limitPlayers(){
        ArrayList<Boolean> players = Main.db.everyoneIsReady();
        if (players.size() >= this.playerLimit){
            Main.db.setJoinable(false);
            if (joinable) {
                joinable = false;
                Main.db.addChatMessage("Player limit reached", true);
            }
        }
        else if(playersReady != players.size()){
            Main.db.setJoinable(true);
            joinable = true;
        }
    }
}
