package GUI;

import audio.MusicPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * This class starts the GUI. It loads the start menu.
 */
public class InterfaceMain extends Application implements Runnable {
    @Override
    public void run() {
        MusicPlayer.getInstance().changeSong(12);
        launch();
    }

    /**
     * creates stage and loads start menu.
     *
     * @param primaryStage      primaryStage.
     * @throws Exception
     */
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

    /**
     * Launches the GUI.
     *
     * @param args  args.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
