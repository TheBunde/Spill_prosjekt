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
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import javafx.event.EventHandler;
import javafx.scene.text.Font;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
    private ImageView weaponOne, weaponTwo;
    @FXML
    public ImageView playerImage;
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
    private VBox transitionVbox;

    public static Game game;
    private Player player;
    private Lighting light = new Lighting();
    private InnerShadow shadow = new InnerShadow();


    public static Timer timer = new Timer();

    public BattlefieldController(){
        //New instance of game
        game = new Game();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        player = new Player(playerImage);
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

        acLabel.setText("AC: " + game.playerCharacter.getAc());

        //initLevelTransitionVBox();
        initMovementPane();

        refreshGameFromClient();
        updateGame();

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

        //showLevelTransitionVBox();
    }

    public void attackButtonPressed(){
        player.setAttackPressed(!player.isAttackPressed());
        if (player.isAttackPressed()) {
            moveButton.setDisable(true);
            endTurnButton.setDisable(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Monster m : game.getMonsters()) {
                        if (!m.isDead()) {
                            if(game.playerCharacter.getWeapons().get(player.getEquippedWeapon()).isRanged()){
                                if(game.attackRange(m, false)) {
                                    m.showAttackPane();
                                    m.attackPane.addEventFilter(MouseEvent.MOUSE_CLICKED, attackEventHandler);
                                }
                            }else if(!game.playerCharacter.getWeapons().get(player.getEquippedWeapon()).isRanged()){
                                if(game.attackRange(m, true)) {
                                    m.showAttackPane();
                                    m.attackPane.addEventFilter(MouseEvent.MOUSE_CLICKED, attackEventHandler);
                                }
                            }
                        }
                    }
                }
            }).start();
            attackButton.getStyleClass().add("attack-button-selected");
        }
        else{
            attackFinished();
        }
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
            player.setAttackUsed(true);
            attackFinished();
        }
    };

    public void attackFinished(){
        player.setAttackPressed(false);
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
        player.setMovePressed(!player.isMovePressed());
        if (player.isMovePressed()) {
            attackButton.setDisable(true);
            endTurnButton.setDisable(true);
            moveButton.getStyleClass().add("button-selected");
            showMovementPane();
            openMapMoveEventHandler();
        }
        else{
            moveFinished();
        }
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

    public void updateGame(){
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
        if (game.isLevelCleared()){

        }
        updateGame();
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
            if (player.isAttackUsed()){
                attackButton.setDisable(true);
            }
            if (player.isMoveUsed()){
                moveButton.setDisable(true);
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
    }


    public void showMovementPane(){
        int xpos = game.playerCharacter.getxPos();
        int ypos = game.playerCharacter.getyPos();
        int movement = game.playerCharacter.getMovement();
        mapGrid.getChildren().remove(movementPane);
        mapGrid.add(movementPane, xpos - movement + ((xpos < movement) ? movement-xpos : 0), ypos - movement + ((ypos < movement) ? movement-ypos : 0));
        movementPane.toBack();
        movementPane.setVisible(true);
        GridPane.setColumnSpan(movementPane, 2*(game.playerCharacter.getMovement()) + 1 - ((xpos < movement) ? movement-xpos : 0));
        GridPane.setRowSpan(movementPane, 2*(game.playerCharacter.getMovement()) + 1 - ((ypos < movement) ? movement-ypos : 0));
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

    public void updateImage() {
        player.imageUpdate();
    }

    public void initLevelTransitionVBox(){
        transitionVbox = new VBox();
        VBox vbox = transitionVbox;
        vbox.setPrefWidth(mapGrid.getPrefWidth());
        vbox.setPrefHeight(mapGrid.getPrefHeight());
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(40);
        vbox.setStyle("-fx-background-color:  #1c1c1c80");

        Label label1 = new Label("Level cleared!");
        label1.setTextFill(Color.web("f8d256"));
        label1.setFont(new Font(60));
        label1.setStyle("-fx-font-weight: bold");

        Label label2 = new Label();
        label2.setFont(new Font(30));
        label2.setTextFill(Color.WHITE);
        vbox.getChildren().addAll(label1, label2);
        vbox.setVisible(false);
        battlefieldUI.getChildren().add(vbox);
    }

    public void showLevelTransitionVBox(){
        ((Label)transitionVbox.getChildren().get(1)).setText("Travelling to " + game.getLevel().getLevelName());
        transitionVbox.setVisible(true);
    }

    public void hideLevelTransitionVbox(){
        transitionVbox.setVisible(false);
    }
}
