package GUI;

import Database.Database;
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

    public LoginController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void cancel() throws Exception{
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }

    public void login() throws Exception{
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
        Database db = new Database("jdbc:mysql://mysql-ait.stud.idi.ntnu.no:3306/g_tdat1006_01?user=g_tdat1006_01&password=","q8CeXgyy");

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

