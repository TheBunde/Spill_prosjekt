package GUI;

import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.*;
import javafx.scene.text.Text;

/**
 * AccountDetailsController.java
 * The program handles the components in AccountDetails-scene.
 * @author saramoh
 */
public class AccountDetailsController {
    private SceneSwitcher sceneSwitcher;

    @FXML
    private Text usernameOutput, rankOutput;

    @FXML
    private Button changeUsernameButton, changePasswordButton, backButton;


    /**
     * Constructor for AccountDetailsController
     */
    public AccountDetailsController() {
        sceneSwitcher = new SceneSwitcher();
    }

    /**
     * Initializes information from the method getInfo()
     *
     * @throws Exception
     */
    public void initialize() throws Exception {
        getInfo();
    }

    /**
     * Sets the Username and the Rank which is fetched from DB as a fxml-text
     *
     * @throws Exception
     */
    public void getInfo() throws Exception {
        usernameOutput.setText(Main.db.fetchUsername());
        rankOutput.setText("Rank: " + Main.db.fetchRank(Main.user.getUser_id()));
    }

    /**
     * Switches the scene to the changeUsername-scene
     *
     * @throws Exception
     */
    public void changeUsernameButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(changeUsernameButton, "changeUsername.fxml");
    }

    /**
     * Switches the scene to the changePassword-scene
     *
     * @throws Exception
     */
    public void changePasswordButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(changePasswordButton, "changePassword.fxml");
    }

    /**
     * Switches the scene to the MainMenu-scene
     *
     * @throws Exception
     */
    public void backToMenuButtonPressed() throws Exception {
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(backButton, "MainMenu.fxml");
    }
}
