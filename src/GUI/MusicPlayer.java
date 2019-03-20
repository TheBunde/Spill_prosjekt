package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;

import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class MusicPlayer extends Application {


    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("hey");

        Media sound = new Media(new File("C:/Users/henri/OneDrive/Documents/Dataingeniør/gitworkshop/Ny Mappe/Game-development-project/src/GUI/audio/testintro.wav").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }
}
