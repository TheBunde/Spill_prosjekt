package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class menuController {


    @FXML
    private Button startNewGame;

    public void buttonPressed1() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("createCarachter.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)startNewGame.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button joinLobby;

    public void buttonPressed2() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FindLobby.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)joinLobby.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button viewAccoun;

    public void buttonPressed3() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("AccountDetails.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)viewAccoun.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button settings;

    public void buttonPressed4() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)settings.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button help;

    public void buttonPressed5() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("https://gitlab.stud.iie.ntnu.no/heleneyj/game-development-project/wikis/System/User-manual"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)help.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button signOut;

    public void buttonPressed6() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("start.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)signOut.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
