package GUI;

import Database.Database;
import Main.Main;
import game.Character;
import game.Creature;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class TeamMateCellController extends ListCell<Character> {

    @FXML
    private Label nameLabel, hpLabel, acLabel;
    @FXML
    private ImageView characterIV;
    @FXML
    private HBox hBox;
    private FXMLLoader mLLoader;
    private Database db = Main.db;

    @Override
    protected void updateItem(Character character, boolean empty) {
        super.updateItem(character, empty);

        if (empty || character == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/TeamMateCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            characterIV.setImage(new Image("GUI/images/" + character.getImageUrl()));
            nameLabel.setText(db.fetchUsernameFromPlayerId(character.getPlayerId()));
            hpLabel.setText(character.getHp() + " / " + character.getHp());  //bytt ut med initialhp
            acLabel.setText("" + character.getAc());
            setText(null);
            setGraphic(hBox);
        }
    }
}
