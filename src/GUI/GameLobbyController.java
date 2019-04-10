package GUI;

import audio.MusicPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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

    public void initialize(){
        lobbyKeyLabel.setText("" + main.user.getLobbyKey());
        MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(10);
        playerReadyTimer = new Timer();
        playerReadyTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        playersReady();
                    }catch (Exception e){
                        System.out.println("travel to battlefield failed: " + e);
                        e.printStackTrace();
                    }
                });
            }
        },0 ,1200);

        limitPlayerTimer = new Timer();
        if (main.user.isHost()) {
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

    public void readyButtonPressed() throws Exception{

        if(!ready){
            readyButton.setStyle("-fx-background-color: #2ecc71;");
            ready = true;
            main.db.addChatMessage(main.user.getUsername() + " is ready", true);
            main.db.isReady(true);
        }else if(ready){
            readyButton.setStyle("-fx-background-color: #cccccc;");
            ready = false;
            main.db.addChatMessage(main.user.getUsername() + " is not ready", true);
            main.db.isReady(false);
        }
    }

    public void backToMenuButtonPressed() throws Exception{
        if (main.db.fetchPlayerCount() == 1){
            main.db.setJoinable(false);
        }
        main.db.addChatMessage(main.user.getUsername() + " has left the lobby", true);
        main.db.disconnectPlayerFromLobby(main.db.fetchPlayerId());
        main.db.disconnectUserFromGameLobby();
        main.db.setHost(false);
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
        ArrayList<Boolean> players = main.db.everyoneIsReady();
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
            main.db.setJoinable(false);
            this.sceneSwitcher.switchScene(readyButton , "Battlefield.fxml");
        }
    }

    public void limitPlayers(){
        ArrayList<Boolean> players = main.db.everyoneIsReady();
        if (players.size() >= this.playerLimit){
            main.db.setJoinable(false);
            if (joinable) {
                joinable = false;
                main.db.addChatMessage("Player limit reached", true);
            }
        }
        else if(playersReady != players.size()){
            main.db.setJoinable(true);
            joinable = true;
        }
    }
}
