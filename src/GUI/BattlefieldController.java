package GUI;


import Database.*;
import Main.*;
import audio.MusicPlayer;
import game.Creature;
import game.Game;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import javafx.event.EventHandler;
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
    private Database db = Main.db;
    private User user = Main.user;
    private SceneSwitcher sceneSwitcher = new SceneSwitcher();
    private double mouseX;
    private double mouseY;
    private double cellWidth;
    private double cellHeight;
    private ArrayList<ImageView> playerPawns = new ArrayList<>();
    private String[] imageUrls = {"GUI/images/warrior.jpg", "GUI/images/rogue.jpg", "GUI/images/wizard.jpg","ranger", "GUI/images/judge.jpg"};
    private Game game;
    private EventHandler<MouseEvent> mouseEventHandler = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent e){
            mouseX = e.getX();
            mouseY = e.getY();
            moveFinished();
        }
    };

    public static Timer timer = new Timer();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game = new Game();

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
            battlefieldUI.getChildren().add(iv);
            this.playerPawns.add(iv);
        }

        refreshGameFromClient();
        updateGameFromServer();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateGameFromServer();
                refreshGameFromClient();
                updateTurn();

                System.out.println("Hei");
            }
        },0 ,1500);
    }

    public BattlefieldController(){

    }

    public void attackButtonPressed(){
        System.out.println(Math.floor(mapGrid.getWidth() / 16));
        moveButton.setDisable(true);
        endTurnButton.setDisable(true);
        attackButton.getStyleClass().add("button-selected");
    }

    public void attackFinished(){
        moveButton.setDisable(false);
        endTurnButton.setDisable(false);
        attackButton.getStyleClass().clear();
        attackButton.getStyleClass().add("button");
    }

    public void moveButtonPressed(){
        attackButton.setDisable(true);
        endTurnButton.setDisable(true);
        moveButton.getStyleClass().add("button-selected");

        /*
        Pane movementPane = new Pane();
        double moveDistanceX = cellWidth*(2*game.playerCharacter.getMovement() + 1);
        double moveDistanceY = cellHeight*(2*game.playerCharacter.getMovement() + 1);
        movementPane.setPrefWidth(moveDistanceX);
        movementPane.setPrefHeight(moveDistanceY);
        movementPane.setStyle("-fx-background-color: rgb(26, 188, 156, 0.5)");
        movementPane.setLayoutX(cellWidth*((double)game.playerCharacter.getxPos() + 1.0/2.0));
        movementPane.setLayoutY(cellHeight*((double)game.playerCharacter.getxPos() + 1.0/2.0));
        battlefieldUI.getChildren().add(movementPane);
        */

        openMapGridEventHandler();
    }

    public void moveFinished(){
        game.playerCharacter.setNewPos(toGrid(mapGrid.getWidth(), mouseX), toGrid(mapGrid.getHeight(), mouseY));
        closeMapGridEventHandler();
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

    }

    public void exitButtonPressed() throws Exception {
        System.out.println(db.addChatMessage(user.getUsername() + " has left the lobby", true));
        db.disconnectUserFromGameLobby();
        chatController.timer.cancel();
        chatController.timer.purge();
        this.sceneSwitcher.switchScene(exitButton, "MainMenu.fxml");
    }


    private int toGrid(double pixels, double pos){
        double dPos = pos / (pixels / 16);
        int iPos = (int) (dPos);
        return iPos;
    }


    public void openMapGridEventHandler(){
        mapGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventHandler);
    }

    public void closeMapGridEventHandler(){
        mapGrid.removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventHandler);
    }

    public void updateGameFromServer(){
        game.update();
    }

    public void refreshGameFromClient(){
        for(int i = 0; i < playerPawns.size(); i++){
            ArrayList<Integer> pos = game.getPos(i);
            if (playerPawns.get(i).getParent() instanceof GridPane) {
                ((GridPane) playerPawns.get(i).getParent()).getChildren().remove(playerPawns.get(i));
                mapGrid.add(playerPawns.get(i), pos.get(0), pos.get(1));
            }
            else {
                mapGrid.add(playerPawns.get(i), pos.get(0), pos.get(1));
            }
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

    public void updateTurn(){
        if(!game.isYourTurn()){
            moveButton.setDisable(true);
            attackButton.setDisable(true);
            endTurnButton.setDisable(true);
        }
        else{
            moveButton.setDisable(false);
            attackButton.setDisable(false);
            endTurnButton.setDisable(false);
        }
    }
}
