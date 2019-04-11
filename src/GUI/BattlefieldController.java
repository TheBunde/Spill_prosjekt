package GUI;


import main.*;
import audio.MusicPlayer;
import audio.SFXPlayer;
import game.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
import java.util.Timer;
import java.util.TimerTask;


public class BattlefieldController{

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
    private ImageView playerImage;
    @FXML
    private Label hpLabel, acLabel, weaponOneLabel, weaponTwoLabel;

    private SceneSwitcher sceneSwitcher = new SceneSwitcher();

    private double mouseX;
    private double mouseY;
    public double cellWidth;
    public double cellHeight;
    private boolean transitioningToNewLevel;

    private Pane movementPane;
    private VBox transitionVbox;

    public static Game game;
    private PlayerActions player;
    private Lighting light = new Lighting();
    private InnerShadow shadow = new InnerShadow();


    public static Timer timer = new Timer();

    /**
     * A constructor for the BattlefieldController.
     * Creates an instance of the game class.
     */
    public BattlefieldController(){
        //New instance of Game

        game = new Game();

    }

    public void initialize() {
        player = new PlayerActions(playerImage);
        cellWidth = mapGrid.getPrefWidth()/(16.0);
        cellHeight = mapGrid.getPrefHeight()/(16.0);

        for (Creature c : game.getCreatures()){
            c.setPawnSize(cellWidth, cellHeight);
            mapGrid.add(c.getPawn(), c.getxPos(), c.getyPos());
            if (c instanceof Monster){
                ((Monster) c).initAttackPane(cellWidth, cellHeight);
                mapGrid.add(((Monster) c).getAttackPane(), c.getxPos(), c.getyPos());
            }
        }

        weaponOne.setImage(new Image("GUI/images/" + game.getPlayerCharacter().getWeapons().get(0).getImageUrl()));
        weaponTwo.setImage(new Image("GUI/images/" + game.getPlayerCharacter().getWeapons().get(1).getImageUrl()));
        weaponOne.setEffect(light);
        weaponTwo.setEffect(shadow);
        player.setEquippedWeapon(0);
        Weapon weapon1 = game.getPlayerCharacter().getWeapons().get(0);
        Weapon weapon2 = game.getPlayerCharacter().getWeapons().get(1);
        weaponOneLabel.setText(weapon1.getName() + "\n" + "Avg. damage: " + (((((double)weapon1.getDamageDice()/2)+0.5)*weapon1.getDiceAmount()) + game.getPlayerCharacter().getDamageBonus()) + "\n" + (weapon1.isRanged() ? "Ranged" : "Melee"));
        weaponTwoLabel.setText(weapon2.getName() + "\n" + "Avg. damage: " + (((((double)weapon2.getDamageDice()/2)+0.5)*weapon2.getDiceAmount()) + game.getPlayerCharacter().getDamageBonus()) + "\n" + (weapon2.isRanged() ? "Ranged" : "Melee"));

        mapContainer.getChildren().add(game.getLevel().backgroundImage);
        game.getLevel().backgroundImage.toBack();

        acLabel.setText("AC: " + game.getPlayerCharacter().getAc());

        initLevelTransitionVbox();
        initMovementPane();

        refreshViewFromGame();
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
        },0 ,2200);
    }

    /**
     * handles the attackButton. When attackButton is pressed,
     * all other buttons are diabled. All attackPanes for monsters
     * that can be attacked are set to visible. Pressing attackButton
     * again sets all attackPanes invisible and all other Buttons are
     * enabled.
     */
    public void attackButtonPressed(){
        /* Toggles isAttackPressed*/
        player.setAttackPressed(!player.isAttackPressed());
        /* Disables all other buttons */
        if (player.isAttackPressed()) {
            weaponOne.setDisable(true);
            weaponTwo.setDisable(true);
            moveButton.setDisable(true);
            endTurnButton.setDisable(true);
            /* Starts a new thread so that the user
               can interact with the game while the
               attackPanes are created.
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Monster m : game.getMonsters()) {
                        if (!m.isDead()) {
                            if (m.getAttackPane() == null){
                                m.initAttackPane(cellWidth, cellHeight);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapGrid.add(m.getAttackPane(), m.getxPos(), m.getyPos());
                                    }
                                });
                            }
                            /* Shows attackPanes for all monsters within ranged range */
                            if(game.getPlayerCharacter().getWeapons().get(player.getEquippedWeapon()).isRanged()){
                                if(game.attackRange(m, false)) {
                                    m.showAttackPane();
                                    m.getAttackPane().addEventFilter(MouseEvent.MOUSE_CLICKED, attackEventHandler);
                                }
                            }
                            /* Shows attackPanes for all monsters within melee range */
                            else if(!game.getPlayerCharacter().getWeapons().get(player.getEquippedWeapon()).isRanged()){
                                if(game.attackRange(m, true)) {
                                    m.showAttackPane();
                                    m.getAttackPane().addEventFilter(MouseEvent.MOUSE_CLICKED, attackEventHandler);
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
                if (e.getSource() == m.getAttackPane()){
                    clickedMonster = m;
                }
            }
            game.getPlayerCharacter().attackCreature(clickedMonster, player.getEquippedWeapon());
            player.setAttackUsed(true);
            attackFinished();
        }
    };

    public void attackFinished(){
        updateGame();
        player.setAttackPressed(false);
        moveButton.setDisable(false);
        endTurnButton.setDisable(false);
        attackButton.getStyleClass().clear();
        attackButton.getStyleClass().add("button");
        weaponOne.setDisable(false);
        weaponTwo.setDisable(false);

        for (Monster m : game.getMonsters()){
            m.hideAttackPane();
            m.getAttackPane().removeEventFilter(MouseEvent.MOUSE_CLICKED, attackEventHandler);
        }
        refreshViewFromGame();
        checkForPlayerTurn();
    }

    /**
     * Handles moveButton. When moveButton is pressed,
     * all other buttons are disabled. the movementPane
     * is set to visible. If the moveButton is pressed again,
     * the movementPane is set to disabled and all other buttons
     * are enabled.
     */
    public void moveButtonPressed(){
        /* toggles isMovePressed */
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
        for(Creature i: game.getCreatures()){
            System.out.println("\n" + i.getCreatureName() + "\nxpos: " + i.getxPos() + "\nypos: " + i.getyPos() + "\n");
        }
        if (game.getPlayerCharacter().moveCreature(toGrid(mapGrid.getWidth(), mouseX), toGrid(mapGrid.getHeight(), mouseY), game.getCreatures())){
            player.setMoveUsed(true);
        }
        updateGame();
        player.setMovePressed(false);
        hideMovementPane();
        closeMapMoveEventHandler();
        attackButton.setDisable(false);
        endTurnButton.setDisable(false);
        moveButton.getStyleClass().clear();
        moveButton.getStyleClass().add("button");
        refreshViewFromGame();
        checkForPlayerTurn();
    }

    /**
     * Ends the turn. All buttons are disabled.
     */
    public void endTurnButtonPressed(){
        game.endTurn();
        /* Runs checkForPlayerTurn to reinforce responsiveness of the application */
        checkForPlayerTurn();
        player.resetUsedActions();
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
        System.out.println(Main.db.addChatMessage(Main.user.getUsername() + " has left the lobby", true));
        Main.db.disconnectUserFromGameLobby();
        chatController.timer.cancel();
        chatController.timer.purge();
        timer.cancel();
        timer.purge();
        MusicPlayer.getInstance().stopSong();
        this.sceneSwitcher.switchScene(exitButton, "MainMenu.fxml");
    }

    private int toGrid(double pixels, double pos){
        double dPos = pos / (pixels / 16);
        int iPos = (int) (dPos);
        return iPos;
    }

    /**
     * enables mapMoveEventHandler so that a user
     * can move its Character.
     */
    public void openMapMoveEventHandler(){
        mapGrid.setCursor(Cursor.HAND);
        mapGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, mapMoveEventHandler);
    }

    /**
     * disables mapMoveEventHandler so that a user
     * cannot move its Character when it is not supposed to.
     */
    public void closeMapMoveEventHandler(){
        mapGrid.setCursor(Cursor.DEFAULT);
        mapGrid.removeEventFilter(MouseEvent.MOUSE_CLICKED, mapMoveEventHandler);
    }

    /**
     * Updates data from game.
     */
    public void updateGame(){
        game.update();
    }

    /**
     * Updates the position of a pawn if it is not equal
     * to the position to the corresponding Creature.
     * It also updates the weapons and health points from game.
     */
    public void refreshViewFromGame(){
        /* Updates pawn */
        for(int i = 0; i < game.getCreatures().size(); i++){
            Creature c = game.getCreatures().get(i);
            if (GridPane.getColumnIndex(c.getPawn()) != c.getxPos() || GridPane.getRowIndex(c.getPawn()) != c.getyPos()) {
                mapGrid.getChildren().remove(c.getPawn());
                mapGrid.add(c.getPawn(), c.getxPos(), c.getyPos());
                /* Updates attackPane of Monsters */
                if (c instanceof Monster){
                    ((Monster) c).updateAttackPane();
                }
            }
        }
        /* Updates weapons */
        Weapon weapon1 = game.getPlayerCharacter().getWeapons().get(0);
        Weapon weapon2 = game.getPlayerCharacter().getWeapons().get(1);
        weaponOneLabel.setText(weapon1.getName() + "\n" + "Avg. damage: " + (((((double)weapon1.getDamageDice()/2)+0.5)*weapon1.getDiceAmount()) + game.getPlayerCharacter().getDamageBonus()) + "\n" + (weapon1.isRanged() ? "Ranged" : "Melee"));
        weaponTwoLabel.setText(weapon2.getName() + "\n" + "Avg. damage: " + (((((double)weapon2.getDamageDice()/2)+0.5)*weapon2.getDiceAmount()) + game.getPlayerCharacter().getDamageBonus()) + "\n" + (weapon2.isRanged() ? "Ranged" : "Melee"));

        /* Updates health points */
        hpLabel.setText("HP: " + Math.max(0, game.getPlayerCharacter().getHp()) + "/" + game.getPlayerCharacter().getInitialHp());
        updateImage();
    }


    public boolean update(){
        if (game.isLevelCleared()) {
            if (!game.getPlayerCharacter().isReadyForNewLevel()){
                if (Main.user.isHost()){
                    game.pushNewLevel();
                }
                game.setPlayerReadyForNewLevel(true);
            }
            while(!game.allPlayersReadyForNewLevel()){
                System.out.println("Hello");
                try{
                    Thread.sleep(200);
                }
                catch(InterruptedException ie){
                    ie.printStackTrace();
                }
            }
            nextLevelTransition();
            return false;
        }
        else if(game.isGameOver()){
            gameOverTransition();
            return false;
        }
        updateGame();
        refreshViewFromGame();
        checkForPlayerTurn();
        System.out.println(game.toString());
        return true;
    }

    /**
     * Changes to new level.
     * Resets usead actions.
     */
    public void newLevel(){
        game.changeToNewLevel();
        player.resetUsedActions();
    }

    /**
     * Checks if it is game.playerCharacters turn.
     * Disables all buttons if not game.playerCharacteers turn.
     * Enables all buttons if it is. Also disables buttons when its
     * action is used.
     * @return
     */
    public boolean checkForPlayerTurn(){
        if(!game.isPlayerCharacterTurn()){
            moveButton.setDisable(true);
            attackButton.setDisable(true);
            endTurnButton.setDisable(true);
            return false;
        }
        else{
            /* Skips turn if dead */
            if (game.getPlayerCharacter().isDead() || player.isAllActionsUsed()){
                game.endTurn();
                player.resetUsedActions();
                return false;
            }
            /* Reenables buttons after another action is used */
            if (!player.isMovePressed()){
                attackButton.setDisable(false);
            }
            if (!player.isAttackPressed()){
                    moveButton.setDisable(false);
            }
            if (!player.isMovePressed() && !player.isAttackPressed()){
                endTurnButton.setDisable(false);
            }
            /* Disables button if its action is used */
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
        double moveDistanceX = cellWidth*(2*game.getPlayerCharacter().getMovement() + 1);
        double moveDistanceY = cellHeight*(2*game.getPlayerCharacter().getMovement() + 1);
        movementPane.setPrefWidth(moveDistanceX);
        movementPane.setPrefHeight(moveDistanceY);
        movementPane.setMouseTransparent(true);
        movementPane.setStyle("-fx-background-color: rgb(26, 188, 156, 0.3)");
        movementPane.setVisible(false);
    }


    public void showMovementPane(){
        int xpos = game.getPlayerCharacter().getxPos();
        int ypos = game.getPlayerCharacter().getyPos();
        int movement = game.getPlayerCharacter().getMovement();
        mapGrid.getChildren().remove(movementPane);
        mapGrid.add(movementPane, xpos - movement + ((xpos < movement) ? movement-xpos : 0), ypos - movement + ((ypos < movement) ? movement-ypos : 0));
        movementPane.toBack();
        movementPane.setVisible(true);
        GridPane.setColumnSpan(movementPane, 2*(game.getPlayerCharacter().getMovement()) + 1 - ((xpos < movement) ? movement-xpos : 0));
        GridPane.setRowSpan(movementPane, 2*(game.getPlayerCharacter().getMovement()) + 1 - ((ypos < movement) ? movement-ypos : 0));
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

    public void initLevelTransitionVbox(){
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

    /**
     * Showns transition screen after levels or when
     * the game is lost
     */
    public void showLevelTransitionVbox(){
        String nextLevelName = Main.db.getLevelName(game.getLevel().getLevelId() + 1);
        /* Shows transition between levels */
        if (nextLevelName != null && !game.isGameOver()){
            SFXPlayer.getInstance().setSFX(13);
            ((Label)transitionVbox.getChildren().get(1)).setText("Travelling to " + nextLevelName + "world");
        }
        /* Shows defeat screen */
        else if(game.isGameOver()){
            SFXPlayer.getInstance().setSFX(14);
            ((Label)transitionVbox.getChildren().get(0)).setText("Defeat");
            ((Label)transitionVbox.getChildren().get(1)).setText("Better luck next time");
        }
        /* Shows victory screen */
        else{
            SFXPlayer.getInstance().setSFX(16);
            ((Label)transitionVbox.getChildren().get(0)).setText("Victory!");
            ((Label)transitionVbox.getChildren().get(1)).setAlignment(Pos.CENTER);
            ((Label)transitionVbox.getChildren().get(1)).setText("Made by:\nTeam 3");
            Main.db.setRank(Main.db.fetchRank(Main.user.getUser_id()) + 1);
        }
        transitionVbox.setVisible(true);
        mapGrid.setGridLinesVisible(false);
        this.disableAllButtons();
    }

    public void hideLevelTransitionVbox(){
        transitionVbox.setVisible(false);
        mapGrid.setGridLinesVisible(true);
        this.enableAllButtons();
    }

    public void enableAllButtons(){
        attackButton.setDisable(false);
        moveButton.setDisable(false);
        endTurnButton.setDisable(false);
        exitButton.setDisable(false);
    }

    /**
     * Disable all buttons
     */
    public void disableAllButtons(){
        attackButton.setDisable(true);
        moveButton.setDisable(true);
        endTurnButton.setDisable(true);
        //exitButton.setDisable(true);
    }

    public void nextLevelTransition() {
            if (!transitioningToNewLevel) {
                transitioningToNewLevel = true;
                MusicPlayer.getInstance().stopSong();
                MusicPlayer.getInstance().changeSong(1);
                showLevelTransitionVbox();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(6000);
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                newLevel();
                                game.updatePlayerTurn();
                                checkForPlayerTurn();
                                if (game.getLevel().getLevelId() <= game.getAmountOfLevels()) {
                                    hideLevelTransitionVbox();
                                    transitioningToNewLevel = false;
                                }
                                else{
                                    try {
                                        Thread.sleep(7000);
                                        MusicPlayer.getInstance().stopSong();
                                        MusicPlayer.getInstance().changeSong(2);
                                        sceneSwitcher.switchScene(exitButton, "MainMenu.fxml");
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    chatController.timer.cancel();
                                    chatController.timer.purge();
                                    timer.cancel();
                                    timer.purge();
                                    Main.user.setHost(false);
                                }
                            }
                        });
                    }
                }).start();
            }
    }

    /**
     * Shows defeat screen, closes all timers
     * and goes to Main Menu.
     */
    public void gameOverTransition(){
        if (!transitioningToNewLevel) {
            transitioningToNewLevel = true;
            SFXPlayer.getInstance().setSFX(3);
            MusicPlayer.getInstance().stopSong();
            MusicPlayer.getInstance().changeSong(1);
            showLevelTransitionVbox();
            /* Creates thread so that user may interact with game
               during transition.
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    chatController.timer.cancel();
                    chatController.timer.purge();
                    timer.cancel();
                    timer.purge();
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MusicPlayer.getInstance().changeSong(2);
                                sceneSwitcher.switchScene(exitButton, "MainMenu.fxml");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                                hideLevelTransitionVbox();
                                transitioningToNewLevel = false;
                        }
                    });
                }
            }).start();
        }
    }
}
