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
    private Database db = Main.db;

    @FXML
    private Text usernameOutput, rankOutput;


    public void initialize() throws Exception {
        getInfo();
    }

    public void getInfo() throws Exception {
        usernameOutput.setText(db.fetchUsername());
        rankOutput.setText(Integer.toString(db.fetchRank()));
    }

    @FXML
    private Button changeUsernameButton, changePasswordButton, backButton;

    public void changeUsername() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("changeUsername.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) changeUsernameButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void changePassword() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        Parent root = FXMLLoader.load(getClass().getResource("changePassword.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) changePasswordButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void menu() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
