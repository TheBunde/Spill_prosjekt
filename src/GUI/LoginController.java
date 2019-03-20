package GUI;

import Main.*;
import Database.Database;
import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginController {

    @FXML
    TextField username;
    @FXML
    TextField password;

    @FXML
    private Button cancelButton, loginButton;
    private SceneSwitcher sceneSwitcher;

    private Database db = Main.db;
    public LoginController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void cancel() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(0);
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }

    public void login() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(2);
        sceneSwitcher.switchScene(loginButton, "MainMenu.fxml");
    }

    public void onLogin(){
        System.out.println("onlogin");
       /* System.out.println("here");
        if(!username.getText().matches("[a-zA-Z0-9],{2,}")){
            return;

        }

        if(username.getText().isEmpty()){
            return;
        }*/

        String usernameInput = username.getText().trim().toLowerCase();
        String passwordInput = password.getText().trim();

        System.out.println("username " + usernameInput);
        System.out.println("password " + passwordInput);

        int status = db.checkLogin(usernameInput,passwordInput);
        System.out.println(status);
        switch (status){
            case 0:
                Stage stage = (Stage) username.getScene().getWindow();

                try {
                    Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
                    stage.setScene(new Scene(root));
                    JOptionPane.showMessageDialog(null, "supert! Du er logget inn");

                } catch (IOException ex) {
                    Logger.getLogger(GUI.LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case -1:
                JOptionPane.showMessageDialog(null, "Connection Failed");
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "Username or password wrong");
                break;
        }

    }
}

