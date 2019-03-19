package Database;

import sun.plugin2.message.ProxyReplyMessage;

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
    }

    //Fetches messages from chat
    public ArrayList<String> getMessagesFromChat(){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<String> messages = new ArrayList<String>();
        try{
            String prepString = "SELECT message_id, chat_message.user_id, message, username, time_stamp FROM chat_message LEFT OUTER JOIN usr ON (chat_message.user_id = usr.user_id) WHERE chat_message.lobby_key = ? ORDER BY message_id DESC LIMIT 30";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, this.user.getLobbyKey());
            res = prepStmt.executeQuery();
            while (res.next()) {
                messages.add(res.getString("username") + ": " + res.getString("message") + " | " + res.getString("time_stamp"));
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
        }
        finally {
            if (res != null) {
                this.manager.closeRes(res);
            }
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(this.con);
            return messages;
        }
    }

    //Sends new message to the chat that the user is connected to
    public boolean addChatMessage(String message){
        this.openConnection();
        PreparedStatement prepStmt = null;
        boolean status = true;
        try {
            //Using a prepared statement to execute an insert into the chat_message entity
            String prepString = "INSERT INTO chat_message VALUES(?, DEFAULT, ?, ?, NOW())";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, this.user.getLobbyKey());
            prepStmt.setInt(2, this.user.getUser_id());
            prepStmt.setString(3, message);
            prepStmt.executeUpdate();
        }
        catch (SQLException sq){
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(this.con);
            return status;
        }
    }

    public boolean gameLobbyExists(int lobbyKey){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        //Boolean variable to keep track of the existence of the specified gamelobby
        boolean chatExists = false;
        try{
            //Checks if chat with the specified lobbykey exists
            String prepString = "SELECT lobby_key FROM game_lobby WHERE lobby_key = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, lobbyKey);
            res = prepStmt.executeQuery();
            chatExists = res.next();

        }
        catch (SQLException sq){
            sq.printStackTrace();
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(this.con);
            return chatExists;
        }
    }

    public boolean connectUserToGameLobby(int lobbyKey){
        PreparedStatement prepStmt = null;
        boolean status = true;
        if (this.gameLobbyExists(lobbyKey) && this.user.getUser_id() != -1){
            this.openConnection();
            try {
                String prepString = "UPDATE usr SET lobby_key = ? WHERE user_id = ?";
                prepStmt = this.con.prepareStatement(prepString);
                prepStmt.setInt(1, lobbyKey);
                prepStmt.setInt(2, this.user.getUser_id());
                prepStmt.executeUpdate();
                this.user.setLobbyKey(lobbyKey);
            }
            catch (SQLException sq){
                sq.printStackTrace();
                status = false;
            }
            finally {
                this.manager.closePrepStmt(prepStmt);
                this.manager.closeConnection(this.con);
            }
        }
        else{
            status = false;
        }
        return status;
    }

    public boolean disconnectUserFromGameLobby(){
        this.openConnection();
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            String prepString = "UPDATE usr SET lobby_key = NULL WHERE user_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, this.user.getUser_id());
            prepStmt.executeUpdate();
            this.user.setLobbyKey(-1);
        }
        catch (SQLException sq){
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(this.con);
            return status;
        }
    }

    public boolean addUser(User user){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int user_id = -1;
        boolean status = true;
        try{
            String prepString = "INSERT INTO usr VALUES(DEFAULT, ?, 0, ?, ?, DEFAULT)";
            prepStmt = this.con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, this.user.getUsername());
            prepStmt.setString(2, this.user.getEmail());
            prepStmt.setString(3, "hunter2");
            prepStmt.executeUpdate();
            res = prepStmt.getGeneratedKeys();
            res.next();
            user_id = res.getInt(1);
            this.user.setUser_id(user_id);
        }
        catch (SQLException sq){
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(this.con);
            return status;
        }
    }

    public boolean createNewLobby(){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int lobbyKey;
        boolean status = true;
        try{
            String prepString = "INSERT INTO game_lobby VALUES(DEFAULT, 0)";
            prepStmt = this.con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.executeUpdate();
            res = prepStmt.getGeneratedKeys();
            res.next();
            lobbyKey = res.getInt(1);
            this.connectUserToGameLobby(lobbyKey);
        }
        catch (SQLException sq){
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(this.con);
            return status;
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
