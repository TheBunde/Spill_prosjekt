package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.xml.soap.Text;

public class GUI extends Application {
    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("sample.Chat");
        Label usernameLabel = new Label("Username");

        GridPane loginLayout = new GridPane();
        loginLayout.setAlignment(Pos.CENTER);


        primaryStage.show();
    }
}
