package GUI;


import game.Character;
import game.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.util.ArrayList;

/**
 * A controller class that handles the graphical user interface of the
 * teamMateList.
 *
 * @author magnubau
 */
public class TeamMatesController {

    @FXML
    private ListView<Character> listView;

    public static ObservableList<Character> characterObservableList;
    public static ArrayList<Character> charactersInListView;
    public static Game game = BattlefieldController.game;

    /**
     * Constructor for the TeamMatesController class.
     * It adds all all Characters to the Observable list,
     * then it removes the users Character.
     */
    public TeamMatesController(){
        /* Adds characters */
        charactersInListView = BattlefieldController.game.getCharacters();
        int you = 0;
        for(Character i: charactersInListView){
            if(i == game.getPlayerCharacter()){
                you = charactersInListView.indexOf(i);
            }
        }
        /* Removes users Character */
        charactersInListView.remove(you);
        characterObservableList = FXCollections.observableArrayList();
        for (Character c : charactersInListView){
            if (c != BattlefieldController.game.getPlayerCharacter()) {
                characterObservableList.add(c);
            }
        }
    }

    /**
     * executes when the corresponding FXML file to this controller is loaded.
     */
    public void initialize(){
        /* Sets items in listView to TeamMateListCell */
        listView.setItems(characterObservableList);
        listView.setCellFactory(characterListView -> {
            return new TeamMateListCell();
        });
    }

    /**
     * Updates the listView.
     */
    public static void updateListView(){
        /* Adds all Characters */
        characterObservableList.removeAll(charactersInListView);
        charactersInListView = BattlefieldController.game.getCharacters();
        int you = 0;
        /* Removes users Character */
        for(Character i: charactersInListView){
            if(i == game.getPlayerCharacter()){
                you = charactersInListView.indexOf(i);
            }
        }
        charactersInListView.remove(you);
        characterObservableList.addAll(charactersInListView);
    }
}
