package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    private Connection con;
    private String url;
    private String password;
    public User user;
    private ManageConnection manager;

    //Setup for database
    public Database(String url, String password){
        this.con = null;
        this.url = url;
        this.password = password;
        this.manager = new ManageConnection();
        this.user = new User(3, "william", 2, "gmail@gmail.com");

    }

    //Fetches messages from chat
    public ArrayList<String> getMessagesFromChat(Chatter chatter){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<String> messages = new ArrayList<String>();
        try{
            String prepString = "SELECT message_id, chat_message.chatter_id, message, name, time_stamp FROM chat_message LEFT OUTER JOIN chatter ON (chat_message.chatter_id = chatter.chatter_id) WHERE chat_message.chat_id = ? ORDER BY message_id DESC LIMIT 30";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, chatter.getChatID());
            res = prepStmt.executeQuery();
            while (res.next()) {
                messages.add(res.getString("name") + ": " + res.getString("message") + " | " + res.getString("time_stamp"));
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            return null;
        }
        finally {
            if (res != null) {
                this.closeRes(res);
            }
            this.closePrepStmt(prepStmt);
            this.closeConnection();
            return messages;
        }
    }

    //Sends new message to the chat that the user is connected to
    public boolean addChatMessage(Chatter chatter, String message){
        this.openConnection();
        PreparedStatement prepStmt = null;
        try {
            //Using a prepared statement to execute an insert into the chat_message entity
            String prepString = "INSERT INTO chat_message VALUES(?, DEFAULT, ?, ?, NOW())";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, chatter.getChatID());
            prepStmt.setInt(2, chatter.getChatterID());
            prepStmt.setString(3, message);
            prepStmt.executeUpdate();
        }
        catch (SQLException sq){
            sq.printStackTrace();
            return false;
        }
        finally {
            this.closePrepStmt(prepStmt);
            this.closeConnection();
            return true;
        }
    }

    public boolean chatExists(int chatID){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        //Boolean variable to keep track of the existence of the specified chat
        boolean chatExists = false;
        try{
            //Checks if chat with the specified chatID exists
            String prepString = "SELECT chat_id FROM chat WHERE chat_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, chatID);
            res = prepStmt.executeQuery();
            chatExists = res.next();

        }
        catch (SQLException sq){
            sq.printStackTrace();
            return false;
        }
        finally {
            this.closeRes(res);
            this.closePrepStmt(prepStmt);
            this.closeConnection();
            return chatExists;
        }
    }

    public boolean connectChatterToChat(Chatter chatter, int chatID){
        PreparedStatement prepStmt = null;
        if (chatExists(chatID) && chatter.getChatterID() != -1){
            this.openConnection();
            try {
                String prepString = "UPDATE chatter SET chat_id = ? WHERE chatter_id = ?";
                prepStmt = this.con.prepareStatement(prepString);
                prepStmt.setInt(1, chatID);
                prepStmt.setInt(2, chatter.getChatterID());
                prepStmt.executeUpdate();
                chatter.setChatID(chatID);
                return true;
            }
            catch (SQLException sq){
                sq.printStackTrace();
                return false;
            }
            finally {
                closePrepStmt(prepStmt);
                this.closeConnection();
            }
        }
        return false;
    }

    public boolean addChatter(Chatter chatter){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int chatterID = -1;
        try{
            String prepString = "INSERT INTO chatter VALUES(DEFAULT, DEFAULT, ?)";
            prepStmt = this.con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, chatter.getName());
            prepStmt.executeUpdate();
            res = prepStmt.getGeneratedKeys();
            res.next();
            chatterID = res.getInt(1);
            chatter.setChatterID(chatterID);
        }
        catch (SQLException sq){
            sq.printStackTrace();
            return false;
        }
        finally {
            this.closeRes(res);
            this.closePrepStmt(prepStmt);
            this.closeConnection();
            return true;
        }
    }

    public String fetchUsername() {
        String username = "";
        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {
            String prepString = "select distinct username from usr where user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, user.getUser_id());
            res = prepStmt.executeQuery();
            while(res.next()){
                username  += res.getString("username");
            }
        }
        catch (SQLException sq){
            manager.writeMessage(sq, "fetchUsername");
        }
        finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
        }
        return username;
    }

    public String fetchEmail() {
        String email = "";
        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {

            String prepString = "select distinct email from usr where user_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, user.getUser_id());
            res = prepStmt.executeQuery();
            while (res.next()){
                email  += res.getString("email");
            }
        } catch (SQLException sq) {
            manager.writeMessage(sq, "fetchEmail");
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
        }
        return email;
    }

    public int fetchRank() {
        int rank = 0;
        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {

            String prepString = "select distinct rank from usr where user_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, user.getUser_id());
            res = prepStmt.executeQuery();
            while(res.next()){
                rank += res.getInt("level");
            }
        } catch (SQLException e) {
            manager.writeMessage(e, "fetchLevel");
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(con);
        }
        return rank;
    }

    public boolean registerUser(User user) {
        if(userExist(user.getUsername()))
            return false;
        openConnection();
        PreparedStatement prepStmt = null;
        try {
            String prepString = "INSERT INTO usr VALUES(?, ?, DEFAULT, DEFAULT, ?, ?)";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, user.getUser_id());
            prepStmt.setString(2, user.getUsername());
            prepStmt.setString(3, user.getEmail());
            prepStmt.setString(4, "test");
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            manager.writeMessage(e, "registerUser");
            return false;
        } finally {
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(con);
            return true;
        }
    }

    public boolean userExist(String username){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean userExists = false;
        try{
            String prepString = "SELECT user_id FROM usr WHERE username = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setString(1, username);
            res = prepStmt.executeQuery();
            userExists = res.next();

        }
        catch (SQLException e){
            manager.writeMessage(e, "userExist");
            return false;
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(con);
            return userExists;
        }
    }



    public boolean closeRes(ResultSet res){
        try{
            res.close();
        }
        catch (SQLException sq){
            sq.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean closePrepStmt(PreparedStatement prepStmt){
        try{
            prepStmt.close();
        }
        catch (SQLException sq){
            sq.printStackTrace();
            return false;
        }
        return true;
    }

    //Safely opens connection between the application and the database
    public boolean openConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager.getConnection(this.url + this.password);
        }
        catch(SQLException sq){
            System.out.println("SQL-Exception: " + sq);
            return false;
        }
        catch (ClassNotFoundException e){
            System.out.println("Class-Exception: " + e);
            return false;
        }
        finally {
            return true;
        }
    }

    //Safely closes the connection between the application and the database
    public void closeConnection(){
        try {
            this.con.close();
        }
        catch(SQLException sq){
            System.out.println("SQL-feil: " + sq);
        }
    }
}
