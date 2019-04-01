package GUI;

import Main.*;
import Database.*;
import audio.MusicPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

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

    private Database db = Main.db;
    private User user = Main.user;
    private boolean ready = false;
    public static Timer timer = new Timer();
    private int playersReady;

    public void initialize(){
        //db.movePos(8, 8, db.fetchPlayerId());
        lobbyKeyLabel.setText("" + user.getLobbyKey());
        MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(10);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        playersReady();
                    }catch (Exception e){
                        System.out.println("travel to battlefield failed: " + e);
                    }
                });
            }
        },0 ,1200);
    }

    public void readyButtonPressed() throws Exception{

        if(!ready){
            readyButton.getStyleClass().add("button-selected");
            ready = true;
            db.isReady(true);
        }else if(ready){
            readyButton.getStyleClass().add("button-selected");
            ready = false;
            db.isReady(false);
        }

        /* audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(7);
        chatController.timer.cancel();
        chatController.timer.purge();
        this.sceneSwitcher.switchScene(travelButton, "Battlefield.fxml"); */

    }

    public void backToMenuButtonPressed() throws Exception{
        db.addChatMessage(user.getUsername() + " has left the lobby", true);
        db.disconnectUserFromGameLobby();
        db.setHost(false);
        chatController.timer.cancel();
        chatController.timer.purge();
        this.sceneSwitcher.switchScene(backToMenuButton, "MainMenu.fxml");
    }

    public void playersReady() throws Exception{
        playersReady = 0;
        ArrayList<Boolean> players = db.everyoneIsReady();
        for(int i = 0; i < players.size(); i++){
            if(players.get(i)){
               playersReady++;
            }
        }
        readyCounter.setText("Players Ready: " + playersReady + " / " + players.size());

        if(playersReady == players.size()){
            audio.MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(7);
            chatController.timer.cancel();
            chatController.timer.purge();
            this.sceneSwitcher.switchScene(readyButton , "Battlefield.fxml");
        }
    }
}
