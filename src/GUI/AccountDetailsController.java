package GUI;

import Main.*;
import Database.*;

import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AccountDetailsController {

    @FXML
    private Text usernameOutput, emailOutput, rankOutput;

    private Database db = Main.db;
    private User user = Main.user;

    public void initialize() throws Exception{
        //getInfo();
    }

    public void getInfo()throws Exception{
        usernameOutput.setText(user.getUsername());
        emailOutput.setText(user.getEmail());
        rankOutput.setText(Integer.toString(user.getRank()));
    }

    @FXML
    private Button changeUsernameButton, changePasswordButton, backButton;

    public void changeUsername() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("changeUsername.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)changeUsernameButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void changePassword() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        Parent root = FXMLLoader.load(getClass().getResource("changePassword.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)changePasswordButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void menu() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)backButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
