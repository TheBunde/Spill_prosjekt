package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RegistrerController {

    @FXML
    private Button registrerButton, cancelButton;
    private SceneSwitcher sceneSwitcher;

    public RegistrerController(){
        sceneSwitcher = new SceneSwitcher();
    }

    public void registrer() throws Exception{
        sceneSwitcher.switchScene(registrerButton, "MainMenu.fxml");
    }


    public void cancel() throws Exception{
        sceneSwitcher.switchScene(cancelButton, "start.fxml");
    }
}
