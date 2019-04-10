package GUI;

import main.*;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * LoginController.java
 * The program handles the components in Login-scene.
 * @author henrikwt, saramoh
 */
public class ChangePasswordController {

    private Alert alert = new Alert(Alert.AlertType.WARNING);
    private SceneSwitcher sceneSwitcher;

    @FXML
    private TextField oldPassword, newPassword, reNewPassword;

    @FXML
    private Button backToMenuButton, confirmButton;



    public ChangePasswordController(){
        sceneSwitcher = new SceneSwitcher();
    }
    /**
     *   This method performes when confirm button in changePassword page pressed
     * @throws Exception
     */

    public void confirmButtonPressed()throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        /**
         * check wether oldPassword-, newPassword or reNewPassword is empty
         */
        if(oldPassword.getText().isEmpty() || newPassword.getText().isEmpty() || reNewPassword.getText().isEmpty()) {
            alert.setTitle("Empty Field");
            alert.setHeaderText(null);
            alert.setContentText("Fill in all fields.");
            alert.showAndWait();
        }
         /**
         * check if the newPassword is equals to reNewPassword
         */

        else if(!newPassword.getText().equals(reNewPassword.getText())){
            alert.setTitle("Not match Password");
            alert.setHeaderText(null);
            alert.setContentText("The passwords do not match, try again!");
            alert.showAndWait();
        } else {
            /**
             * if everything is ok update the password
             */

            setNewPassword();
            alert.setTitle("Password changed");
            alert.setHeaderText(null);
            alert.setContentText("Password changed successfully.");
            alert.showAndWait();
            sceneSwitcher.switchScene(confirmButton, "AccountDetails.fxml");
        }
    }
    /**
     * updating the password by deleting the old one by calling the method deleteOldPassword() from database.java
     * and adding the new one by calling addPassword() method
     */

    public void setNewPassword(){
        Main.db.deleteOldPassword(oldPassword.getText().trim());
        Main.db.addPassword(newPassword.getText().trim());
    }
     /**
     * get back to Account Details page if you want to cancle changing the password
     * @throws Exception
     */

    public void backToMenu() throws Exception{
        SFXPlayer.getInstance().setSFX(0);
        sceneSwitcher.switchScene(backToMenuButton, "AccountDetails.fxml");
    }
}
