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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrerController {

    Alert alert = new Alert(Alert.AlertType.WARNING);

    @FXML
    private TextField usernameInput, emailInput;

    @FXML
    private PasswordField passwordInput, rePasswordInput;

    @FXML
    private Button registerButton, cancelButton;
    private SceneSwitcher sceneSwitcher;

    private Database db = Main.db;
    private User user = Main.user;


    public RegistrerController() {
        sceneSwitcher = new SceneSwitcher();
    }


    public void register() throws Exception {
        boolean ok;

        if(!emailValidation()){
            alert.setTitle("Email validation");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter a Valid Email!");
            alert.showAndWait();
            //ok = false;
        }

        else if(usernameInput.getText().isEmpty() || passwordInput.getText().isEmpty() || passwordInput.getText().isEmpty() ||
                rePasswordInput.getText().isEmpty()) {
            alert.setTitle("Empty textfield");
            alert.setHeaderText(null);
            alert.setContentText("Field can not be empty.");
            alert.showAndWait();
            //ok = false;
        }

        else if (db.emailExist(emailInput.getText().trim())) {
            alert.setTitle("Check email");
            alert.setHeaderText(null);
            alert.setContentText("Email exists in database already!");
            alert.showAndWait();
            //ok = false;

        }else{
            db.registerUser(usernameInput.getText().trim(), emailInput.getText().trim(), passwordInput.getText().trim(), rePasswordInput.getText().trim());
            db.addPassword(passwordInput.getText().trim());
            SFXPlayer.getInstance().setSFX(0);
            audio.MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(2);
            sceneSwitcher.switchScene(registerButton, "MainMenu.fxml");
        }
    }

    public void cancel() throws Exception {
        SFXPlayer.getInstance().setSFX(0);

        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }

    // validate email
    public boolean emailValidation() {
        Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher m = p.matcher(emailInput.getText());
        if (m.find() && m.group().equals(emailInput.getText())) {
            return true;
        }
        return false;
    }

    /*
    public boolean emailExist() {
        if (db.emailExist(emailInput.getText().trim())) {
            alert.setTitle("Check email");
            alert.setHeaderText(null);
            alert.setContentText("Email exist in database already!");
            alert.showAndWait();
            return true;
        }
        return false;
    }


    public boolean fieldIsEmpty(){
        if (usernameInput.getText().isEmpty() || passwordInput.getText().isEmpty() || passwordInput.getText().isEmpty() ||
                rePasswordInput.getText().isEmpty()) {
            alert.setTitle("Empty textfield");
            alert.setHeaderText(null);
            alert.setContentText("Field can not be empty.");
            alert.showAndWait();
            return true;
        }
        return false;
    }
    */
}
