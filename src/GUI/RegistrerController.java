package GUI;

import Main.*;
import Database.*;
import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrerController {



    @FXML
    private TextField usernameInput, emailInput;

    @FXML
    private PasswordField passwordInput, rePasswordInput;

    @FXML
    private Button registrerButton, cancelButton;
    private SceneSwitcher sceneSwitcher;

    private Database db = Main.db;
    private User user = Main.user;

    public RegistrerController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void registrer() throws Exception{
        Main.user = new User(-1, usernameInput.getText(), 0, emailInput.getText());
        db.addUser(Main.user);
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(2);
        sceneSwitcher.switchScene(registrerButton, "MainMenu.fxml");
    }


    public void cancel() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        audio.MusicPlayer.getInstance().stopSong();
        MusicPlayer.getInstance().changeSong(0);
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }

    // Validation of username, Email, password

    public boolean UsernameValidation(){
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(usernameInput.getText());
        if(m.find() && m.group().equals(usernameInput.getText())){
            return true;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Username validation");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid username");
            alert.showAndWait();

            return false;

        }
    }

    // validate email

    public boolean EmailValidation(){
        Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher m = p.matcher(emailInput.getText());
        if(m.find() && m.group().equals(emailInput.getText())){
            return true;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Email validation");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter a Valid Email");
            alert.showAndWait();

            return false;

        }
    }



    public void onRegister() throws Exception {


       // DBConnection db = new DBConnection("jdbc:mysql://mysql-ait.stud.idi.ntnu.no:3306/g_tdat1006_01?user=g_tdat1006_01&password=", "q8CeXgyy");
        if (UsernameValidation() && EmailValidation()) {
            int status = (int) db.Button_Register_ActionPerformed(usernameInput.getText().trim().toLowerCase(), emailInput.getText(), passwordInput.getText(), rePasswordInput.getText());
            if (status == 1) {

                SFXPlayer.getInstance().setSFX(0);
                audio.MusicPlayer.getInstance().stopSong();
                MusicPlayer.getInstance().changeSong(2);
                sceneSwitcher.switchScene(registrerButton, "MainMenu.fxml");

            } else {
                JOptionPane.showMessageDialog(null, "noe feil");

            }
        }
    }
}
