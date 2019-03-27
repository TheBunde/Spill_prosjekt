package GUI;


import Database.*;
import Main.*;
import audio.MusicPlayer;
import game.Creature;
import game.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
    private Button exitButton;
    @FXML
    private GridPane mapGrid;
    @FXML
    private AnchorPane battlefieldUI;
    @FXML
    private Button moveButton;

    private Database db = Main.db;
    private User user = Main.user;
    private SceneSwitcher sceneSwitcher = new SceneSwitcher();
    private double mouseX;
    private double mouseY;
    private ArrayList<ImageView> playerPawns = new ArrayList<>();
    private String[] imageUrls = {"GUI/images/warrior.jpg", "GUI/images/rogue.jpg", "GUI/images/wizard.jpg"};
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
        double width = 36.6875;
        double height = 23.8125;
        Image image = null;
        game = new Game();

        for(int i = 0; i < game.getAmountOfCreatures(); i++){
            Creature creature = game.getCreature(i);
            image = new Image(imageUrls[creature.getCreatureId() -1], width, height, false, false);
            ImageView iv = new ImageView(image);
            iv.setPreserveRatio(false);
            iv.setFitHeight(mapGrid.getHeight() / 16);
            iv.setFitWidth(mapGrid.getWidth() / 16);
            battlefieldUI.getChildren().add(iv);
            this.playerPawns.add(iv);
        }
        updateGame();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //db.movePos(10, 10, db.fetchPlayerId());
                updateGame();
                System.out.println("Hei");
            }
        },0 ,1500);
    }

    public void attackButtonPressed(){
        System.out.println(Math.floor(mapGrid.getWidth() / 16));
    }

    public void moveButtonPressed(){
        openMapGridEventHandler();
        /*System.out.println("X-pososjon: " + toGrid(431, mouseX) + "\nY-posisjon: " + toGrid(310, mouseY) + "\n");
        int x_pos = db.fetchPlayerPos(db.fetchPlayerId()).get(0);
        int y_pos = db.fetchPlayerPos(db.fetchPlayerId()).get(1);
        for(int i = 0; i < playerPawns.size(); i++){
            if(x_pos == toGrid(mapGrid.getWidth(), playerPawns.get(i).getX()) && y_pos == toGrid(mapGrid.getHeight(), playerPawns.get(i).getY())){
                db.setPos(toGrid(mapGrid.getWidth(), mouseX), toGrid(mapGrid.getHeight(), mouseY), db.fetchPlayerId());
                setPos(playerPawns.get(i), 0, 0);
            }

        }*/
    }
    public void moveFinished(){
        game.playerCharacter.setNewPos(toGrid(mapGrid.getWidth(), mouseX), toGrid(mapGrid.getHeight(), mouseY));
        updateGame();
        closeMapGridEventhandler();
    }

    public void endTurnButtonPressed(){

    }

    public void helpButtonPressed(){

    }

    public void exitButtonPressed(){
        
    }


    private int toGrid(double pixels, double pos){
        double dPos = pos / (pixels / 16);
        int iPos = (int) (dPos);
        return iPos;
    }

    public void exitButtonClicked() throws Exception{
        System.out.println(db.addChatMessage(user.getUsername() + " has left the lobby", true));
        db.disconnectUserFromGameLobby();
        chatController.timer.cancel();
        chatController.timer.purge();
        this.sceneSwitcher.switchScene(exitButton, "MainMenu.fxml");
    }

    public void openMapGridEventHandler(){
        mapGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventHandler);
    }

    public void closeMapGridEventhandler(){
        mapGrid.removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventHandler);
    }

    public void updateGame(){
        for(int i = 0; i < playerPawns.size(); i++){
            ArrayList<Integer> pos = game.getPos(i);
            playerPawns.get(i).setX((double) pos.get(0) * mapGrid.getWidth() / 16);
            playerPawns.get(i).setY((double) pos.get(1) * mapGrid.getHeight() / 16);
        }
        game.update();
    }
}
