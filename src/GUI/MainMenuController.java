package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {


    @FXML
    private Button startNewGameButton;

    public void startNewGameButtonPressed() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("createCharacter.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)startNewGameButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button joinLobbyButton;

    public void joinLobbyButtonPressed() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FindLobby.fxml"));
        Scene scene = new Scene(root, 800, 500);
        Stage stage = (Stage)joinLobbyButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
<<<<<<< HEAD
    private Button viewAccount;
=======
    private Button viewAccountButton;
>>>>>>> 26a297b9348301dc06af15853e1971fd7f954e10

    public void viewAccountButtonPressed() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("AccountDetails.fxml"));
        Scene scene = new Scene(root);
<<<<<<< HEAD
        Stage stage = (Stage)viewAccount.getScene().getWindow();
=======
        Stage stage = (Stage)viewAccountButton.getScene().getWindow();
>>>>>>> 26a297b9348301dc06af15853e1971fd7f954e10
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button settingsButton;

    public void settingsButtonPressed() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)settingsButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button helpButton;

<<<<<<< HEAD
    public void buttonPressed5() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("https://gitlab.stud.iie.ntnu.no/heleneyj/game-development-project/wikis/User%20manual"));
=======
    public void helpButtonPressed() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("https://gitlab.stud.iie.ntnu.no/heleneyj/game-development-project/wikis/System/User-manual"));
>>>>>>> 26a297b9348301dc06af15853e1971fd7f954e10
        Scene scene = new Scene(root);
        Stage stage = (Stage)helpButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button signOutButton;

    public void signOutButtonPressed() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("start.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)signOutButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
