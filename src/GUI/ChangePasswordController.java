package GUI;

import main.*;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * ChangePasswordController.java
 * The program handles the components in ChangePassword-scene.
 * @author henrikwt, saramoh
 */
public class ChangePasswordController {

    private Alert alert = new Alert(Alert.AlertType.WARNING);
    private SceneSwitcher sceneSwitcher;

    @FXML
    private TextField oldPassword, newPassword, reNewPassword;

    @FXML
    private Button backToMenuButton, confirmButton;


    /**
     * Constructor for ChangePasswordController
     */
    public ChangePasswordController(){
        sceneSwitcher = new SceneSwitcher();
    }

    /**
     * Confirms changing of the password
     *
     * @throws Exception
     */
    public void confirmButtonPressed()throws Exception{
        SFXPlayer.getInstance().setSFX(0);

        /*
         * Checks whether oldPassword, newPassword or reNewPassword is empty
         */
        if(oldPassword.getText().isEmpty() || newPassword.getText().isEmpty() || reNewPassword.getText().isEmpty()) {
            alert.setTitle("Empty Field");
            alert.setHeaderText(null);
            alert.setContentText("Fill in all fields.");
            alert.showAndWait();
        }

         /*
         * Checks if the newPassword is equals to reNewPassword
         */
        else if(!newPassword.getText().equals(reNewPassword.getText())){
            alert.setTitle("Not match Password");
            alert.setHeaderText(null);
            alert.setContentText("The passwords do not match, try again!");
            alert.showAndWait();
        } else {

            /*
             * Sets the new password, and switches to the AccountDetails-scene
             */
            if (setNewPassword()) {
                alert.setTitle("Password changed");
                alert.setHeaderText(null);
                alert.setContentText("Password changed successfully.");
                alert.showAndWait();
                sceneSwitcher.switchScene(confirmButton, "AccountDetails.fxml");
            }
            else{
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Old password was wrong, please try again.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Updates the password by deleting the old one by calling the method deleteOldPassword() from database.java
     * and adds the new one by calling addPassword()
     * @return  true if password is changed, false otherwise
     */
    public boolean setNewPassword(){
        if (Main.db.deleteOldPassword(oldPassword.getText().trim())) {
            Main.db.addPassword(newPassword.getText().trim());
            return true;
        }
        else{
            return false;
        }
    }

     /**
      * Switches to the AccountDetails-scene
      *
      * @throws Exception
      */
    public void backToMenu() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(backToMenuButton, "AccountDetails.fxml");
    }
}
