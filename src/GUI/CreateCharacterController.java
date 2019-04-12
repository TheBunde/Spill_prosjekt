package GUI;

import audio.MusicPlayer;
import audio.SFXPlayer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.*;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * Controller class for the character creation menu. Allows users to create a character.
 * @author henrikwt
 */
public class CreateCharacterController implements Initializable {

    @FXML
    private ComboBox<String> chooseClassDropdown;
    @FXML
    private Button createCharacterButton;
    @FXML
    private ImageView iv;
    @FXML
    private TextArea text;

    @FXML
    private SceneSwitcher sceneSwitcher;

  
    /**
     * Constructor.
     */
    public CreateCharacterController(){
        sceneSwitcher = new SceneSwitcher();
    }

    /*
     * Adding the images for the different characters.
     */
    @FXML
    Image RangerImage = new Image("GUI/images/ranger.jpg");
    @FXML
    Image warriorImage = new Image("GUI/images/warrior.jpg");
    @FXML
    Image rogueImage = new Image("GUI/images/rogue.jpg");
    @FXML
    Image wizardImage = new Image("GUI/images/wizard.jpg");
    @FXML
    Image defaultImage = new Image("GUI/images/Default.jpg");
    
    /**
     * Method that runs when the corresponding FXML loaded.
     * @param location URL location
     * @param resources Resource Bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chooseClassDropdown.getItems().addAll("Warrior","Rogue","Wizard","Ranger");
        iv.setImage(defaultImage);
        Main.db.createPlayer(true);
    }

    /**
     * Displays and allows for selection of a character.
     */
    public void displayCharacter(){

        String a = chooseClassDropdown.getValue();
        if(a.equals("Warrior")) {
            SFXPlayer.getInstance().setSFX(2);
            iv.setImage(warriorImage);
            text.setText("Warrior. This mighty motherfather is one of the best classes for new players." +
                    "\nYou like hitting things, and you like to hit it hard." +
                    "\nIf your enemy runs away you can always throw your javelin at it.");
            MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(4);
        }
        if(a.equals("Rogue")) {
            iv.setImage(rogueImage);
            text.setText("Rooge-y stab stab." +
                    "\nSneaking and stabbing without getting hit is your speciality." +
                    "\nOr assaulting it with your crossbow, your choice");
            SFXPlayer.getInstance().setSFX(3);
            MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(5);
        }
        if(a.equals("Wizard")) {
            iv.setImage(wizardImage);
            text.setText("It's LeviOsa, not LeviosA." +
                    "\nAs a Wizard you like to stay an arms distance away from you enemy," +
                    "\nwhile assaulting it with your array of spells.");
            SFXPlayer.getInstance().setSFX(4);
            MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(6);
        }
        if(a.equals("Ranger")) {
            iv.setImage(RangerImage);
            text.setText("Legolas got nothing on this fella." +
                    "\nRangers are one with nature." +
                    "\nWith their longbow and short sword they are good with both ranged and melee attacks." +
                    "\nA truly versatile character.");
            SFXPlayer.getInstance().setSFX(8);
            MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(13);
        }
    }
    /**
     * Creates the selected character for the user that chose it.
     * @return Boolean
     * @throws Exception
     */
    public boolean createCharacter() throws Exception{
        int a = chooseClassDropdown.getSelectionModel().getSelectedIndex() + 1;
        if (a == 0){
            return false;
        }
        SFXPlayer.getInstance().setSFX(0);
        if(Main.db.createCreature(Main.user.getPlayerId(), a, (int)Math.floor(Math.random()*16), (int)Math.floor(Math.random()*16))){
            Main.db.addChatMessage(Main.user.getUsername() + " will play as " + chooseClassDropdown.getValue(), true);
            System.out.println("character created");
        }else{
            System.out.println("character not created");
        }
        sceneSwitcher.switchScene(createCharacterButton, "GameLobby.fxml");
        return true;
    }

}