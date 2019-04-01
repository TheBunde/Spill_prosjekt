package GUI;


import Database.*;
import Main.*;
import audio.MusicPlayer;
import game.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.*;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import javafx.event.EventHandler;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;



public class BattlefieldController implements Initializable {

    // https://stackoverflow.com/questions/41081905/javafx-getting-the-location-of-a-click-on-a-gridpane
    @FXML
    private Button exitButton, moveButton, attackButton, endTurnButton;
    @FXML
    private GridPane mapGrid;
    @FXML
    private AnchorPane battlefieldUI;
    @FXML
    private ImageView weaponOne, weaponTwo;
    private Database db = Main.db;
    private User user = Main.user;
    private SceneSwitcher sceneSwitcher = new SceneSwitcher();
    private double mouseX;
    private double mouseY;
    private double cellWidth;
    private double cellHeight;
    private boolean attackPressed = false;
    private boolean movePressed = false;
    private int equipedWeapon = 0;
    private Pane movementPane;
    private ArrayList<Pane> attackPanes = new ArrayList<>();
    private ArrayList<ImageView> playerPawns = new ArrayList<>();
    private String[] imageUrls = {"GUI/images/warrior.jpg", "GUI/images/rogue.jpg", "GUI/images/wizard.jpg","ranger", "GUI/images/judge.jpg"};
    private Game game;
    private Lighting light = new Lighting();
    private InnerShadow shadow = new InnerShadow();


    public static Timer timer = new Timer();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        game = new Game();
        weaponOne.setEffect(light);
        weaponTwo.setEffect(shadow);
        Image image = null;
        cellWidth = mapGrid.getPrefWidth()/(16.0);
        cellHeight = mapGrid.getPrefHeight()/(16.0);
        System.out.println(mapGrid.getWidth());

        for(int i = 0; i < game.getAmountOfCreatures(); i++){
            Creature creature = game.getCreature(i);
            image = new Image(imageUrls[creature.getCreatureId() -1], cellWidth, cellHeight, false, false);
            ImageView iv = new ImageView(image);
            iv.setPreserveRatio(false);
            iv.setFitHeight(mapGrid.getHeight() / 16);
            iv.setFitWidth(mapGrid.getWidth() / 16);
            mapGrid.add(iv, creature.getxPos(), creature.getyPos());
            this.playerPawns.add(iv);
        }

        initMovementPane();
        initAttackPanes();

        refreshGameFromClient();
        updateGameFromServer();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    update();
                });
            }
        },0 ,1200);
    }

    public BattlefieldController(){

    }

    public void attackButtonPressed(){
        attackPressed = true;
        moveButton.setDisable(true);
        endTurnButton.setDisable(true);
        new Thread(new Runnable(){
            @Override public void run(){
                showAttackPanes();
                ArrayList<Integer> monstersIndex = game.getMonstersIndex();
                for (int i = 0; i < monstersIndex.size(); i++){
                    ImageView image = playerPawns.get(monstersIndex.get(i));
                    image.addEventFilter(MouseEvent.MOUSE_CLICKED, attackEventHandler);
                    image.setCursor(Cursor.CROSSHAIR);
                }
            }
        }).start();
        attackButton.getStyleClass().add("attack-button-selected");
    }

    private EventHandler<MouseEvent> attackEventHandler = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent e){
            int clickedMonsterIndex = 0;
            for (int i = 0; i < game.getAmountOfCreatures(); i++){
                if (e.getSource() == playerPawns.get(i)){
                    clickedMonsterIndex = i;
                }
            }
            game.playerCharacter.attackCreature(game.getCreature(clickedMonsterIndex), equipedWeapon);
            attackFinished();
        }
    };

    public void attackFinished(){
        attackPressed = false;
        moveButton.setDisable(false);
        endTurnButton.setDisable(false);
        attackButton.getStyleClass().clear();
        attackButton.getStyleClass().add("button");
        hideAttackPanes();
        ArrayList<Integer> monstersIndex = game.getMonstersIndex();
        for (int i = 0; i < monstersIndex.size(); i++){
            ImageView image = playerPawns.get(monstersIndex.get(i));
            image.removeEventFilter(MouseEvent.MOUSE_CLICKED, attackEventHandler);
            image.setCursor(Cursor.DEFAULT);
        }
    }

    public void moveButtonPressed(){
        movePressed = true;
        attackButton.setDisable(true);
        endTurnButton.setDisable(true);
        moveButton.getStyleClass().add("button-selected");
        showMovementPane();
        openMapMoveEventHandler();
    }

    private EventHandler<MouseEvent> mapMoveEventHandler = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent e){
            mouseX = e.getX();
            mouseY = e.getY();
            moveFinished();
        }
    };

    public void moveFinished(){
        movePressed = false;
        hideMovementPane();
        game.playerCharacter.moveCreature(toGrid(mapGrid.getWidth(), mouseX), toGrid(mapGrid.getHeight(), mouseY));
        closeMapMoveEventHandler();
        attackButton.setDisable(false);
        endTurnButton.setDisable(false);
        moveButton.getStyleClass().clear();
        moveButton.getStyleClass().add("button");
        refreshGameFromClient();

    }

    public void endTurnButtonPressed(){
        game.endTurn();
    }

    public void helpButtonPressed(){
        if (Desktop.isDesktopSupported()){
            try {
                Desktop.getDesktop().browse(new URI("https://gitlab.stud.iie.ntnu.no/heleneyj/game-development-project/wikis/System%20Documentation"));
            }
            catch (IOException ioe){
                System.out.println("Error with IO");
                ioe.printStackTrace();
            }
            catch (URISyntaxException e){
                System.out.println("Error in URL");
                e.printStackTrace();
            }
        }
    }

    public void exitButtonPressed() throws Exception {
        System.out.println(db.addChatMessage(user.getUsername() + " has left the lobby", true));
        db.disconnectUserFromGameLobby();
        chatController.timer.cancel();
        chatController.timer.purge();
        timer.cancel();
        timer.purge();
        this.sceneSwitcher.switchScene(exitButton, "MainMenu.fxml");
    }


    private int toGrid(double pixels, double pos){
        double dPos = pos / (pixels / 16);
        int iPos = (int) (dPos);
        return iPos;
    }


    public void openMapMoveEventHandler(){
        mapGrid.setCursor(Cursor.HAND);
        mapGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, mapMoveEventHandler);
    }

    public void closeMapMoveEventHandler(){
        mapGrid.setCursor(Cursor.DEFAULT);
        mapGrid.removeEventFilter(MouseEvent.MOUSE_CLICKED, mapMoveEventHandler);
    }

    public void updateGameFromServer(){
        game.update();
    }

    public void refreshGameFromClient(){
        for(int i = 0; i < playerPawns.size(); i++){
            ArrayList<Integer> pos = game.getPos(i);
            ((GridPane) playerPawns.get(i).getParent()).getChildren().remove(playerPawns.get(i));
            mapGrid.add(playerPawns.get(i), pos.get(0), pos.get(1));
        }
    }


    public Node getNodeFromGrid(int x, int y){
        Node result = null;
        ObservableList<Node> nodes = mapGrid.getChildren();
        for (Node node : nodes){
            if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) ==  y){
                result = node;
            }
        }
        return result;
    }

    public void update(){
        updateGameFromServer();
        refreshGameFromClient();
        checkForPlayerTurn();
        System.out.println("Hei");
        for (int i = 0; i < game.getAmountOfCreatures(); i++){
            System.out.println(game.getCreature(i).getCreatureName() + ": " + game.getCreature(i).getHp());
        }
        System.out.println(movePressed + " " + attackPressed);
        System.out.println("Player turn: " + game.isPlayerTurn());
    }

    public void checkForPlayerTurn(){
        if(!game.isPlayerTurn()){
            moveButton.setDisable(true);
            attackButton.setDisable(true);
            endTurnButton.setDisable(true);
        }
        else{
            if (!movePressed){
                attackButton.setDisable(false);
            }
            if (!attackPressed){
                moveButton.setDisable(false);
            }
            if (!movePressed && !attackPressed){
                endTurnButton.setDisable(false);
            }
        }
    }

    public void initMovementPane(){
        movementPane = new Pane();
        double moveDistanceX = cellWidth*(2*game.playerCharacter.getMovement() + 1);
        double moveDistanceY = cellHeight*(2*game.playerCharacter.getMovement() + 1);
        movementPane.setPrefWidth(moveDistanceX);
        movementPane.setPrefHeight(moveDistanceY);
        movementPane.setMouseTransparent(true);
        movementPane.setStyle("-fx-background-color: rgb(26, 188, 156, 0.3)");
        movementPane.setVisible(false);
        battlefieldUI.getChildren().add(movementPane);
    }


    public void showMovementPane(){
        double moveDistanceX = cellWidth*(2*game.playerCharacter.getMovement() + 1);
        double moveDistanceY = cellHeight*(2*game.playerCharacter.getMovement() + 1);
        movementPane.setPrefWidth(moveDistanceX);
        movementPane.setPrefHeight(moveDistanceY);
        movementPane.setLayoutX(cellWidth*((double)game.playerCharacter.getxPos()) - cellWidth*game.playerCharacter.getMovement());
        movementPane.setLayoutY(cellHeight*((double)game.playerCharacter.getyPos()) - cellHeight*game.playerCharacter.getMovement());
        movementPane.setVisible(true);
    }

    public void hideMovementPane(){
        movementPane.setVisible(false);
    }

    public void initAttackPanes(){
        ArrayList<Integer> monstersIndex = game.getMonstersIndex();
        for (int i = 0; i < monstersIndex.size(); i++){
            Pane pane = new Pane();
            pane.setPrefWidth(cellWidth);
            pane.setPrefHeight(cellHeight);
            pane.setStyle("-fx-background-color: rgb(252, 91, 55, 0.7)");
            pane.setMouseTransparent(true);
            pane.setVisible(false);
            attackPanes.add(pane);
            battlefieldUI.getChildren().add(pane);
        }

    }

    public void showAttackPanes(){
        ArrayList<Integer> monstersIndex = game.getMonstersIndex();
        for (int i = 0; i < attackPanes.size(); i++){
            Pane attackpane = attackPanes.get(i);
            attackpane.setVisible(true);
            attackpane.setLayoutX(cellWidth*(double)game.getCreature(monstersIndex.get(i)).getxPos());
            attackpane.setLayoutY(cellHeight*(double)game.getCreature(monstersIndex.get(i)).getyPos());
        }
    }

    public void hideAttackPanes(){
        for (int i = 0; i < attackPanes.size(); i++){
            attackPanes.get(i).setVisible(false);
        }
    }

    public void weaponOneSelected(){
        weaponOne.setEffect(light);
        weaponTwo.setEffect(shadow);
        equipedWeapon = 0;
    }

    public void weaponTwoSelected(){
        weaponOne.setEffect(shadow);
        weaponTwo.setEffect(light);
        equipedWeapon = 1;
    }

}
