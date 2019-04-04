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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


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
    private Pane mapContainer;
    @FXML
    private ImageView weaponOne, weaponTwo, playerImage;
    @FXML
    private Label hpLabel, acLabel;

    private Database db = Main.db;
    private User user = Main.user;
    private SceneSwitcher sceneSwitcher = new SceneSwitcher();

    private double mouseX;
    private double mouseY;
    public double cellWidth;
    public double cellHeight;

    private Pane movementPane;

    public static Game game;
    private Player player;
    private Lighting light = new Lighting();
    private InnerShadow shadow = new InnerShadow();


    public static Timer timer = new Timer();

    public BattlefieldController(){
        game = new Game();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //New instance of game
        //game = new Game();
        player = new Player();

        cellWidth = mapGrid.getPrefWidth()/(16.0);
        cellHeight = mapGrid.getPrefHeight()/(16.0);

        for (Creature c : game.getCreatures()){
            if (c instanceof Monster){
                ((Monster) c).initAttackPane(cellWidth, cellHeight);
                mapGrid.add(((Monster) c).attackPane, c.getxPos(), c.getyPos());
            }
            c.setPawnSize(cellWidth, cellHeight);
            mapGrid.add(c.getPawn(), c.getxPos(), c.getyPos());
        }

        weaponOne.setImage(new Image("GUI/images/" + game.playerCharacter.getWeapons().get(0).getImageUrl()));
        weaponTwo.setImage(new Image("GUI/images/" + game.playerCharacter.getWeapons().get(1).getImageUrl()));

        weaponOne.setEffect(light);
        weaponTwo.setEffect(shadow);

        mapContainer.getChildren().add(game.getLevel().backgroundImage);
        game.getLevel().backgroundImage.toBack();

        playerImage.setImage(new Image("GUI/images/" + game.playerCharacter.getImageUrl()));
        acLabel.setText("AC: " + game.playerCharacter.getAc());

        initMovementPane();

        refreshGameFromClient();
        updateGameFromServer();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    update();
                    TeamMatesController.updateListView();
                });
            }
        },0 ,1200);
    }

    public void attackButtonPressed(){
        player.setAttackPressed(true);
        moveButton.setDisable(true);
        endTurnButton.setDisable(true);
        new Thread(new Runnable(){
            @Override public void run(){
                for (Monster m : game.getMonsters()){
                    if (!m.isDead()) {
                        if(!game.attackRange(m, game.playerCharacter.getWeapons().get(player.getEquippedWeapon()).isRanged())){
                            m.showAttackPane();
                            m.attackPane.addEventFilter(MouseEvent.MOUSE_CLICKED, attackEventHandler);
                        }
                    }
                }
            }
        }).start();
        attackButton.getStyleClass().add("attack-button-selected");
    }

    private EventHandler<MouseEvent> attackEventHandler = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent e){
            Monster clickedMonster = null;
            for (Monster m : game.getMonsters()){
                if (e.getSource() == m.attackPane){
                    clickedMonster = m;
                }
            }
            game.playerCharacter.attackCreature(clickedMonster, player.getEquippedWeapon());
            attackFinished();
        }
    };

    public void attackFinished(){
        player.setAttackPressed(false);
        player.setAttackUsed(true);
        moveButton.setDisable(false);
        endTurnButton.setDisable(false);
        attackButton.getStyleClass().clear();
        attackButton.getStyleClass().add("button");

        for (Monster m : game.getMonsters()){
            m.hideAttackPane();
            m.attackPane.removeEventFilter(MouseEvent.MOUSE_CLICKED, attackEventHandler);
        }
        checkForPlayerTurn();
        refreshGameFromClient();
    }

    public void moveButtonPressed(){
        player.setMovePressed(true);
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
        player.setMovePressed(false);
        hideMovementPane();
        if (game.playerCharacter.moveCreature(toGrid(mapGrid.getWidth(), mouseX), toGrid(mapGrid.getHeight(), mouseY), game.getCreatures())){
            player.setMoveUsed(true);
        };
        closeMapMoveEventHandler();
        attackButton.setDisable(false);
        endTurnButton.setDisable(false);
        moveButton.getStyleClass().clear();
        moveButton.getStyleClass().add("button");
        refreshGameFromClient();
        checkForPlayerTurn();

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
        for(int i = 0; i < game.getCreatures().size(); i++){
            Creature c = game.getCreatures().get(i);
            if (c.getPawn().getX() != c.getxPos() || c.getPawn().getY() != c.getyPos()) {
                mapGrid.getChildren().remove(c.getPawn());
                mapGrid.add(c.getPawn(), c.getxPos(), c.getyPos());
                if (c instanceof Monster){
                    ((Monster) c).updateAttackPane();
                }
            }
        }
        hpLabel.setText("HP: " + Math.max(0, game.playerCharacter.getHp()) + "/" + game.playerCharacter.getInitialHp());
        updateImage();
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
        System.out.println(player.isMovePressed() + " " + player.isAttackPressed());
        System.out.println("Player turn: " + game.isPlayerTurn());

    }

    public boolean checkForPlayerTurn(){
        if(!game.isPlayerTurn()){
            moveButton.setDisable(true);
            attackButton.setDisable(true);
            endTurnButton.setDisable(true);
            return false;
        }
        else{
            if (game.playerCharacter.isDead() || player.isAllActionsUsed()){
                game.endTurn();
                player.resetUsedActions();
                return false;
            }
            if (!player.isMovePressed()){
                attackButton.setDisable(false);
            }
            if (!player.isAttackPressed()){
                    moveButton.setDisable(false);
            }
            if (!player.isMovePressed() && !player.isAttackPressed()){
                endTurnButton.setDisable(false);
            }
            return true;
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
        mapGrid.add(movementPane, game.playerCharacter.getxPos(), game.playerCharacter.getyPos());
        GridPane.setColumnSpan(movementPane, 2*(game.playerCharacter.getMovement()) + 1);
        GridPane.setRowSpan(movementPane, 2*(game.playerCharacter.getMovement()) + 1);
        movementPane.setTranslateX(-game.playerCharacter.getMovement()*cellWidth);
        movementPane.setTranslateY(-game.playerCharacter.getMovement()*cellHeight);
    }


    public void showMovementPane(){
        mapGrid.getChildren().remove(movementPane);
        mapGrid.add(movementPane, game.playerCharacter.getxPos(), game.playerCharacter.getyPos());
        movementPane.toBack();
        movementPane.setVisible(true);
    }

    public void hideMovementPane(){
        movementPane.setVisible(false);
    }

    public void weaponOneSelected(){
        weaponOne.setEffect(light);
        weaponTwo.setEffect(shadow);
        player.setEquippedWeapon(0);
    }

    public void weaponTwoSelected(){
        weaponOne.setEffect(shadow);
        weaponTwo.setEffect(light);
        player.setEquippedWeapon(1);
    }

    public void updateImage(){
        int chrID = game.playerCharacter.getCreatureId();
        int chrHP = game.playerCharacter.getHp();
        int chrInHP = game.playerCharacter.getInitialHp();

        int dmgOne = (chrInHP * 2)/3;
        int dmgTwo = chrInHP/3;

        //Warrior
        if(chrID == 1){
            if(chrHP > dmgOne){
                playerImage.setImage(new Image("GUI/images/" + game.playerCharacter.getImageUrl()));
            }
            else if(chrHP >= dmgTwo && chrHP <= dmgOne){
                playerImage.setImage(new Image("GUI/images/warriordamaged.jpg"));
            }
            else if(chrHP < dmgOne){
                playerImage.setImage(new Image("GUI/images/warriordamaged2.jpg"));
            }
        }
        //Rogue
        if(chrID == 2){
            if(chrHP > dmgOne){
                playerImage.setImage(new Image("GUI/images/" + game.playerCharacter.getImageUrl()));
            }
            else if(chrHP >= dmgTwo && chrHP <= dmgOne){
                playerImage.setImage(new Image("GUI/images/roguedamaged.jpg"));
            }
            else if(chrHP < dmgOne){
                playerImage.setImage(new Image("GUI/images/roguedamaged2.jpg"));
            }
        }
        //Wizard
        if(chrID == 3){
            if(chrHP > dmgOne){
                playerImage.setImage(new Image("GUI/images/" + game.playerCharacter.getImageUrl()));
            }
            else if(chrHP >= dmgTwo && chrHP <= dmgOne){
                playerImage.setImage(new Image("GUI/images/wizarddamaged.jpg"));
            }
            else if(chrHP < dmgOne){
                playerImage.setImage(new Image("GUI/images/wizarddamaged2.jpg"));
            }
        }
        //Ranger
        if(chrID == 4){
            if(chrHP > dmgOne){
                playerImage.setImage(new Image("GUI/images/" + game.playerCharacter.getImageUrl()));
            }
            else if(chrHP >= dmgTwo && chrHP <= dmgOne){
                playerImage.setImage(new Image("GUI/images/ranger.jpg"));
            }
            else if(chrHP < dmgOne){
                playerImage.setImage(new Image("GUI/images/ranger.jpg"));
            }
        }
        if(chrHP <= 0){
            playerImage.setImage(new Image("GUI/images/dead.jpg"));
        }

    }

}
