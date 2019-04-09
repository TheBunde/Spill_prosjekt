package GUI;

import database.Database;
import main.Main;
import game.Character;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class TeamMateListCell extends ListCell<Character> {

    @FXML
    private Label nameLabel, hpLabel, acLabel;
    @FXML
    private ImageView characterIV;
    @FXML
    private HBox hBox;
    private Database db = Main.db;

    @Override
    protected void updateItem(Character character, boolean empty){
        super.updateItem(character, empty);

        if (empty || character == null) {

            setText(null);
            setGraphic(null);

        } else {
            FXMLLoader mLLoader;
            mLLoader = new FXMLLoader(getClass().getResource("TeamMateCell.fxml"));
            mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            Image image = new Image("GUI/images/" + character.getImageUrl());
            characterIV.setImage(image);
            nameLabel.setText(db.fetchUsernameFromPlayerId(character.getPlayerId()));
            hpLabel.setText("HP: " + character.getHp() + " / " + character.getInitialHp());  //bytt ut med initialhp
            acLabel.setText("AC: " + character.getAc());
            setText(null);
            setGraphic(hBox);
        }
    }
}
