package GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateCharacterController implements Initializable {

    @FXML
    private ComboBox<String> chooseClassDropdown;
    @FXML
    private Button createCharacterButton;
    @FXML
    private ImageView iv;
    @FXML
    private Image img;
    @FXML
    private TextArea text;

    @FXML
    private SceneSwitcher sceneSwitcher;

    public CreateCharacterController(){
        sceneSwitcher = new SceneSwitcher();
    }

    //To add more classes add jpg file to images package, make new Image object,
    //add class to chooseClassDropdown.getItems().addAll(... , "class")
    //add class to displayCharacter() method
    //images to display selected character

    @FXML
    Image warriorImage = new Image("GUI/images/warrior.jpg");

    @FXML
    Image rogueImage = new Image("GUI/images/Default.jpg");

    @FXML
    Image wizardImage = new Image("GUI/images/Default.jpg");

    @FXML
    Image defaultImage = new Image("GUI/images/Default.jpg");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("View is now loaded!");
        chooseClassDropdown.getItems().addAll("Warrior","Rogue","Wizard");
        iv.setImage(defaultImage);
    }

    //method to display selected character
    public void displayCharacter(){
        String a = chooseClassDropdown.getValue();

        if(a.equals("Warrior")) {
            iv.setImage(warriorImage);
            text.setText("Warrior.");
        }
        if(a.equals("Rogue")) {
            iv.setImage(rogueImage);
            text.setText("Rogue.");
        }
        if(a.equals("Wizard")) {
            iv.setImage(wizardImage);
            text.setText("Wizard is a class");
        }
    }
    //method to create the character
    public void createCharacter() throws Exception{
        sceneSwitcher.switchScene(createCharacterButton, "GameLobby.fxml");

    }

}