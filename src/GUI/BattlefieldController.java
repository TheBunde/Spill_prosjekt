package GUI;

import Database.Database;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class BattlefieldController implements Initializable {

    // https://stackoverflow.com/questions/41081905/javafx-getting-the-location-of-a-click-on-a-gridpane

    @FXML
    private GridPane mapGrid;
    @FXML
    private AnchorPane battlefieldUI;
    @FXML
    private ImageView yobama, green;
    @FXML
    private Button moveButton;
    private double xPos = 100;
    private double yPos = 100;
    private boolean yobClicked;
    private ArrayList<ImageView> playerPawns = new ArrayList<>();
    private ArrayList<Integer> players = new ArrayList<>();

    private Database db = InterfaceMain.db;
    public static Timer timer = new Timer();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            mapGrid.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                this.xPos = e.getX();
                this.yPos = e.getY();
            }); }catch (NullPointerException e){
            System.out.println(e);
        }
        double width = 36.6875;
        double height = 23.8125;
        ArrayList<Integer> x_pos = new ArrayList<>();
        ArrayList<Integer> y_pos = new ArrayList<>();
        x_pos = db.fetchStartPos(true);
        y_pos = db.fetchStartPos(false);
        players = db.fetchAllPlayerId();
        Image image = null;
        for(int i = 0; i < db.fetchPlayerCount(); i++){
            for(int j = 0; i < db.fetchPlayerCount(); i++){
                ArrayList<Integer> pos = db.fetchPlayerPos(players.get(j));
                if(pos.get(0) == x_pos.get(i) && pos.get(1) == y_pos.get(i)){
                    if(db.fetchPlayerCharacterId(players.get(j)) == 1){
                        image = new Image("GUI/images/warrior.jpg", width, height, false, false);
                    }
                    else if(db.fetchPlayerCharacterId(players.get(j)) == 2){
                        image = new Image("GUI/images/rogue.jpg", width, height, false, false);
                    }
                    else if(db.fetchPlayerCharacterId(players.get(j)) == 3){
                        image = new Image("GUI/images/wizard.jpg", width, height, false, false);
                    }
                    ImageView iv = new ImageView(image);
                    iv.setPreserveRatio(false);
                    iv.setFitHeight(mapGrid.getHeight() / 16);
                    iv.setFitWidth(mapGrid.getWidth() / 16);
                    battlefieldUI.getChildren().add(iv);
                    this.playerPawns.add(iv);
                }
            }
        }
        if(db.movePos(10, 10, db.fetchPlayerId())){
            System.out.println("si");
        }
        else{
            System.out.println("no");
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                db.movePos(10, 10, db.fetchPlayerId());
                updatePos();
                System.out.println("Hei");
            }
        },0 ,1500);
    }

    /*public void move() throws Exception{   //legg til move på gridpane mapgrid i scene builder
        try{
            mapGrid.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                this.xPos = e.getX();
                this.yPos = e.getY();
                green.setOpacity(0);
            }); }catch (NullPointerException e){
            System.out.println(e);
        }
        if(yobClicked) {
            setPos(yobama, 0, 0);
            setPos(green, 2, 2);
            yobClicked = false;
        }
        System.out.println("X-pososjon: " + toGrid(431, xPos) + "\nY-posisjon: " + toGrid(310, yPos) + "\n");
    }*/

    public void yobSelected(){
        selected(yobama);
        yobClicked = true;
    }

    public void attackButtonPressed(){
        System.out.println(Math.floor(mapGrid.getWidth() / 16));
    }

    public void moveButtonPressed(){
        System.out.println("X-pososjon: " + toGrid(431, xPos) + "\nY-posisjon: " + toGrid(310, yPos) + "\n");
        int x_pos = db.fetchPlayerPos(db.fetchPlayerId()).get(0);
        int y_pos = db.fetchPlayerPos(db.fetchPlayerId()).get(1);
        for(int i = 0; i < playerPawns.size(); i++){
            if(x_pos == toGrid(mapGrid.getWidth(), playerPawns.get(i).getX()) && y_pos == toGrid(mapGrid.getHeight(), playerPawns.get(i).getY())){
                db.movePos(toGrid(mapGrid.getWidth(), xPos), toGrid(mapGrid.getHeight(), yPos), db.fetchPlayerId());
                setPos(playerPawns.get(i), 0, 0);
            }

        }


        /*double gridWidth = mapGrid.getWidth() / 16;
        double gridHeight = mapGrid.getHeight() / 16;
        System.out.println(gridWidth);
        playerPawns.get(0).setX(gridWidth);
        playerPawns.get(0).setY(gridHeight);*/
    }
    public void endTurnButtonPressed(){

    }

    public void helpButtonPressed(){

    }

    public void exitButtonPressed(){
        
    }

    private void selected(ImageView image){
        try{
            image.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                green.setOpacity(1);
            });}catch (NullPointerException e){
            System.out.println(e);
        }
    }

    private int toGrid(double pixels, double pos){
        double dPos = pos / (pixels / 16);
        int iPos = (int) (dPos);
        return iPos;
    }

    private void setPos(ImageView image, double xOffset, double yOffset){
        double gridWidth = mapGrid.getWidth();
        double gridHeight = mapGrid.getHeight();
        double xGrid = (double) toGrid(gridWidth, xPos);
        double yGrid = (double) toGrid(gridHeight, yPos);
        image.setX((xGrid - xOffset) * gridWidth / 16);
        image.setY((yGrid - yOffset) * gridHeight / 16);
    }

    private void setStartPos(ImageView image, int player_id){
        double gridWidth = mapGrid.getWidth();
        double gridHeight = mapGrid.getHeight();
        image.setX(4 * gridWidth);
        image.setY(db.fetchPlayerPos(player_id).get(1) * gridHeight);
        System.out.println(db.fetchPlayerPos(player_id).get(0));
    }

    private void updatePos(){
        for(int i = 0; i < players.size(); i++){
            int x_pos = db.fetchPlayerPos(players.get(i)).get(0);
            int y_pos =  db.fetchPlayerPos(players.get(i)).get(1);
            if(x_pos != toGrid(mapGrid.getWidth() ,playerPawns.get(i).getX()) || y_pos != toGrid(mapGrid.getHeight() ,playerPawns.get(i).getY())){
                playerPawns.get(i).setX((double) x_pos * mapGrid.getWidth() / 16);
                playerPawns.get(i).setY((double) y_pos * mapGrid.getHeight() / 16);
            }
        }
    }
}
