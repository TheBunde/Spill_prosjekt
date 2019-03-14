package sample;

import javafx.fxml.FXML;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class loginController {
    private Connection con;
    private String username;
    private String password;
    private PreparedStatement ps;

    @FXML
    private TextField uname;
    private TextField email;
    private TextField pass;
    private TextField re_pass;


    // Method to check if the username exits allready in db

    public boolean checkUsername(String username){
        //this.openConnection();


        ResultSet rs;
        boolean checkUser = false;

        try{
            String query = "select * from user where username = ?";
            ps = this.con.prepareStatement(query);
            ps.setString(1,username);
            rs = ps.executeQuery();
            if(rs.next()){
                checkUser = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return checkUser;

    }

    public void Button_Register_ActionPerformed(){
        if(uname.equals("")){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Write the username");
            alert.showAndWait();

        }
        else if(email.equals("")){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Write your email");
            alert.showAndWait();

        }
        else if(pass.equals("")){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Write your password");
            alert.showAndWait();

        }
        else if(re_pass.equals("")){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Re-enter your password please");
            alert.showAndWait();

        }
        else if(checkUsername(username)){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("this username is already exist");
            alert.showAndWait();

        }



    }





}
