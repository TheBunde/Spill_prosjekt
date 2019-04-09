package GUI;

import database.*;

import main.*;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.text.Text;

public class AccountDetailsController {
    private Database db = Main.db;
    private SceneSwitcher sceneSwitcher;

    @FXML
    private Text usernameOutput, rankOutput;

    @FXML
    private Button changeUsernameButton, changePasswordButton, backButton;


    public AccountDetailsController() {
        sceneSwitcher = new SceneSwitcher();
    }

    public void initialize() throws Exception {
        getInfo();
    }

    public void getInfo() throws Exception {
        usernameOutput.setText(db.fetchUsername());
        rankOutput.setText(Integer.toString(db.fetchRank(Main.user.getUser_id())));
    }

    public void changeUsernameButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(changeUsernameButton, "changeUsername.fxml");
    }

    public void changePasswordButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(changePasswordButton, "changePassword.fxml");
    }

    public void backToMenuButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(backButton, "MainMenu.fxml");
    }
}
