package GUI;

import Database.*;
import Main.*;
import audio.MusicPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.xml.crypto.Data;


public class BattlefieldController {

    // https://stackoverflow.com/questions/41081905/javafx-getting-the-location-of-a-click-on-a-gridpane
    @FXML
    private Button exitButton;
    @FXML
    private GridPane mapGrid;
    @FXML
    private ImageView yobama, green;
    @FXML
    private Button moveButton;

    private Database db = Main.db;
    private User user = Main.user;
    private SceneSwitcher sceneSwitcher = new SceneSwitcher();
    private double xPos;
    private double yPos;
    private boolean yobClicked;

    public void move() throws Exception{
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
    }

    public void yobSelected(){
        selected(yobama);
        yobClicked = true;
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

    public void exitButtonClicked() throws Exception{
        System.out.println(db.addChatMessage(user.getUsername() + " has left the lobby", true));
        db.disconnectUserFromGameLobby();
        chatController.timer.cancel();
        chatController.timer.purge();
        this.sceneSwitcher.switchScene(exitButton, "MainMenu.fxml");
    }
}
