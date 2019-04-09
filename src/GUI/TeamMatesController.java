package GUI;

import database.Database;
import main.Main;
import game.Character;
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

    public static ObservableList<Character> characterObservableList;
    public static ArrayList<Character> charactersInListView;
    private Database db = Main.db;
    public static Game game = BattlefieldController.game;

    public TeamMatesController(){
        charactersInListView = BattlefieldController.game.getCharacters();
        int you = 0;
        for(Character i: charactersInListView){
            if(i == game.playerCharacter){
                you = charactersInListView.indexOf(i);
            }
        }
        charactersInListView.remove(you);
        characterObservableList = FXCollections.observableArrayList();
        for (Character c : charactersInListView){
            if (c != BattlefieldController.game.playerCharacter) {
                characterObservableList.add(c);
            }
        }

        /*for(Character i: characters){
            characterObservableList.add(i);
        }*/
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
       // characterObservableList.add(new Character(10000, 1, "Warrior", 20, 15, 3, 3, 4, 1, "yas", 3, 3, "warrior.jpg", null));

        listView.setItems(characterObservableList);
        listView.setCellFactory(characterListView -> {
            return new TeamMateListCell();
        });
    }

    public static void updateListView(){
        characterObservableList.removeAll(charactersInListView);
        charactersInListView = BattlefieldController.game.getCharacters();
        int you = 0;
        for(Character i: charactersInListView){
            if(i == game.playerCharacter){
                you = charactersInListView.indexOf(i);
            }
        }
        charactersInListView.remove(you);
        characterObservableList.addAll(charactersInListView);
    }

    /*public ArrayList<Character> getCharacters(){
        ArrayList<Creature> creatures = db.fetchCreaturesFromLobby();
        ArrayList<Character> characters = new ArrayList<>();
        for(int i = 0; i < creatures.size(); i++){
            if(creatures.get(i) instanceof Character){
                characters.add((Character) creatures.get(i));
            }
        }
        return characters;
    }*/


}
