
package GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This class is used to more easily switch between scenes in the GUI.
 *
 * @author magnubau
 */
public class SceneSwitcher {

    /**
     * Switches to a desired scene.
     *
     * @param button    button from current scene.
     * @param page      the desired scene.
     * @throws Exception
     */
    public void switchScene(Button button, String page) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(page));
        Scene scene = new Scene(root);
        Stage stage = (Stage)button.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
