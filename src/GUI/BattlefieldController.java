package GUI;


import Database.*;
import Main.*;
import audio.MusicPlayer;
import game.Creature;
import game.Game;
import javafx.application.Platform;
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
    private boolean attackPressed = false;
    private boolean movePressed = false;
    private Pane movementPane;
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
            mapGrid.add(iv, creature.getxPos(), creature.getyPos());
            this.playerPawns.add(iv);
        }

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
        },0 ,1500);
    }

    public BattlefieldController(){

    }

    public void attackButtonPressed(){
        attackPressed = true;
        moveButton.setDisable(true);
        endTurnButton.setDisable(true);
        attackButton.getStyleClass().add("button-selected");
    }

    public void attackFinished(){
        attackPressed = false;
        moveButton.setDisable(false);
        endTurnButton.setDisable(false);
        attackButton.getStyleClass().clear();
        attackButton.getStyleClass().add("button");
    }

    public void moveButtonPressed(){
        movePressed = true;
        attackButton.setDisable(true);
        endTurnButton.setDisable(true);
        moveButton.getStyleClass().add("button-selected");


        movementPane = new Pane();
        double moveDistanceX = cellWidth*(2*game.playerCharacter.getMovement() + 1);
        double moveDistanceY = cellHeight*(2*game.playerCharacter.getMovement() + 1);
        movementPane.setMouseTransparent(true);
        movementPane.setPrefWidth(moveDistanceX);
        movementPane.setPrefHeight(moveDistanceY);
        movementPane.setStyle("-fx-background-color: rgb(26, 188, 156, 0.3)");
        movementPane.setLayoutX(cellWidth*((double)game.playerCharacter.getxPos()) - cellWidth*game.playerCharacter.getMovement());
        movementPane.setLayoutY(cellHeight*((double)game.playerCharacter.getyPos()) - cellHeight*game.playerCharacter.getMovement());

        battlefieldUI.getChildren().add(movementPane);



        openMapGridEventHandler();
    }

    public void moveFinished(){
        movePressed = false;
        battlefieldUI.getChildren().remove(movementPane);
        game.playerCharacter.moveCreature(toGrid(mapGrid.getWidth(), mouseX), toGrid(mapGrid.getHeight(), mouseY));
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
        timer.cancel();
        timer.purge();
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
            if (!movePressed || !attackPressed){
                endTurnButton.setDisable(false);
            }
        }
    }

    //private Pane createMovementPane
}
