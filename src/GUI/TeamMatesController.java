package GUI;

import Database.Database;
import Main.Main;
import game.Character;
import game.Creature;
import game.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class TeamMatesController implements Initializable {

    @FXML
    private ListView<Character> listView;

    private ObservableList<Character> characterObservableList;
    private ArrayList<Character> characters;
    private Database db = Main.db;

    public TeamMatesController(){
        characters = getCharacters();
        characterObservableList = FXCollections.observableArrayList();
        for(Character i: characters){
            characterObservableList.add(i);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        listView.setItems(characterObservableList);
        listView.setCellFactory(characterListView -> new TeamMateCellController());
    }

    public ArrayList<Character> getCharacters(){
        ArrayList<Creature> creatures = db.fetchCreaturesFromLobby();
        ArrayList<Character> characters = new ArrayList<>();
        for(int i = 0; i < creatures.size(); i++){
            if(creatures.get(i) instanceof Character){
                characters.add((Character) creatures.get(i));
            }
        }
        return characters;
    }


}
