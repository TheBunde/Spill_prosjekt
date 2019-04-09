package GUI;
import Database.*;

import audio.MusicPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;



public class InterfaceMain extends Application implements Runnable {
    @Override
    public void run() {
        MusicPlayer.getInstance().changeSong(12);
        launch();
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("start.fxml"));
        primaryStage.setTitle("Combat");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.getIcons().add(new Image("GUI/images/icontransparent.png"));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event ->{
            MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(1);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
