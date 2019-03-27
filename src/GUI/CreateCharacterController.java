package GUI;

import Main.*;
import Database.*;
import audio.MusicPlayer;
import audio.SFXPlayer;
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

    private Database db = Main.db;

   // private String a = chooseClassDropdown.getValue();

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
    Image rogueImage = new Image("GUI/images/rogue.jpg");

    @FXML
    Image wizardImage = new Image("GUI/images/wizard.jpg");

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
            SFXPlayer.getInstance().setSFX(2);
            iv.setImage(warriorImage);
            text.setText("Warrior. This mighty motherfather is one of the best classes for new players.");
            MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(4);
        }
        if(a.equals("Rogue")) {
            iv.setImage(rogueImage);
            text.setText("Rogue.");
            SFXPlayer.getInstance().setSFX(3);
            MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(5);
        }
        if(a.equals("Wizard")) {
            iv.setImage(wizardImage);
            text.setText("Wizard is a class");
            SFXPlayer.getInstance().setSFX(4);
            MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(6);
        }
    }
    //method to create the character
    public boolean createCharacter() throws Exception{
        String a = chooseClassDropdown.getValue();
        if (a == null){
            return false;
        }
        SFXPlayer.getInstance().setSFX(0);
        if(db.createPlayer(a)){
            System.out.println("character created");
        }else{
            System.out.println("character not created");
        }
        sceneSwitcher.switchScene(createCharacterButton, "GameLobby.fxml");
        return true;
    }

}