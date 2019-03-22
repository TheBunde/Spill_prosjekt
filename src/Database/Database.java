package Database;

import Main.*;
import javafx.scene.control.Alert;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private Connection con;
    private String url;
    private String password;
    private ManageConnection manager;
    private BasicDataSource bds;
    public Chat chat;

    //Setup for database
    public Database(String url, String password){
        this.con = null;
        this.url = url;
        this.password = password;
        this.manager = new ManageConnection();
        this.bds = DataSource.getInstance().getBds();
        this.chat = new Chat();
    }

    //Fetches messages from chat
    public void getMessagesFromChat(){

        Connection con1 = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try{
            con1 = this.bds.getConnection();
            String prepString = "SELECT chat_message.message_id, message, username, time_stamp FROM chat_message LEFT OUTER JOIN usr ON (chat_message.user_id = usr.user_id) WHERE chat_message.lobby_key = ? AND chat_message.message_id > ? ORDER BY message_id DESC LIMIT 30";
            prepStmt = con1.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            prepStmt.setInt(2, chat.getLastSeenMessageId());
            res = prepStmt.executeQuery();
            while (res.next()) {
                chat.addMessage(res.getString("username") + ": " + res.getString("message") + " | " + res.getString("time_stamp"));
                if (res.isFirst()){
                    chat.setLastSeenMessageId(res.getInt("chat_message.message_id"));
                }
            }
            System.out.println(chat.getLastSeenMessageId());
        }
        catch (SQLException sq){
            sq.printStackTrace();
        }
        finally {
            if (res != null) {
                this.manager.closeRes(res);
            }
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con1);
            this.manager.closeConnection(con1);
        }
    }

    //Sends new message to the chat that the user is connected to
    public boolean addChatMessage(String message){
        Connection con1 = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        int messageId = -1;
        try {
            con1 = this.bds.getConnection();
            con1.setAutoCommit(false);
            //Using a prepared statement to execute an insert into the chat_message entity
            String prepString = "INSERT INTO chat_message VALUES(?, DEFAULT, ?, ?, NOW())";
            prepStmt = con1.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            prepStmt.setInt(2, Main.user.getUser_id());
            prepStmt.setString(3, message);
            long time1 = System.currentTimeMillis();
            prepStmt.executeUpdate();
            con1.commit();
            time1 = System.currentTimeMillis() - time1;
            System.out.println(time1);
            res = prepStmt.getGeneratedKeys();
            res.next();
            messageId = res.getInt(1);

        }
        catch (Exception sq){
            this.manager.rollback(con1);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con1);
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con1);
            if (messageId <= 0){
                status = false;
            }
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
            //Checks if gamelobby with the specified lobbykey exists
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
        if (this.gameLobbyExists(lobbyKey) && Main.user.getUser_id() != -1){
            this.openConnection();
            try {
                this.con.setAutoCommit(false);
                String prepString = "UPDATE usr SET lobby_key = ? WHERE user_id = ?";
                prepStmt = this.con.prepareStatement(prepString);
                prepStmt.setInt(1, lobbyKey);
                prepStmt.setInt(2, Main.user.getUser_id());
                prepStmt.executeUpdate();
                this.con.commit();
                Main.user.setLobbyKey(lobbyKey);
            }
            catch (SQLException sq){
                this.manager.rollback(this.con);
                sq.printStackTrace();
                status = false;
            }
            finally {
                this.manager.turnOnAutoCommit(this.con);
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
            this.con.setAutoCommit(false);
            String prepString = "UPDATE usr SET lobby_key = NULL WHERE user_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getUser_id());
            prepStmt.executeUpdate();
            this.con.commit();
            Main.user.setLobbyKey(-1);
        }
        catch (SQLException sq){
            this.manager.rollback(this.con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(this.con);
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
            this.con.setAutoCommit(false);
            String prepString = "INSERT INTO usr VALUES(DEFAULT, ?, 0, ?, ?, DEFAULT)";
            prepStmt = this.con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, Main.user.getUsername());
            prepStmt.setString(2, Main.user.getEmail());
            prepStmt.setString(3, "hunter2");
            System.out.println("done");
            prepStmt.executeUpdate();
            res = prepStmt.getGeneratedKeys();
            res.next();
            user_id = res.getInt(1);
            Main.user.setUser_id(user_id);
            this.con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(this.con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(this.con);
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
        int lobbyKey = -1;
        boolean status = true;
        try{
            this.con.setAutoCommit(false);
            String prepString = "INSERT INTO game_lobby VALUES(DEFAULT, 0)";
            prepStmt = this.con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.executeUpdate();
            res = prepStmt.getGeneratedKeys();
            res.next();
            lobbyKey = res.getInt(1);
            this.con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(this.con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(this.con);
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(this.con);
            this.connectUserToGameLobby(lobbyKey);
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
            prepStmt.setInt(1, Main.user.getUser_id());
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
            prepStmt.setInt(1, Main.user.getUser_id());
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
            prepStmt.setInt(1, Main.user.getUser_id());
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
   // check if the user exits.
    public int checkLogin(String username, String password) {
        boolean con = openConnection();
        System.out.println(con);
        if (!con) {
            return -1;
        }
        PreparedStatement ps = null;
        try {
            String query = "SELECT * FROM usr WHERE username=? AND password =?";
            ps = this.con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();

            // if user found -> return 0 that indicates success login.
            if(resultSet.next()){
                return 0;
            }


        } catch (SQLException sq) {
            sq.printStackTrace();
        } finally {
            this.closePrepStmt(ps);
            this.closeConnection();
        }
        //If made it to here return -1, login failed.
        return -1;
    }


    public boolean emailExist(String email){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        //Boolean variable to keep track of the existence of the specified email
        boolean emailExists = false;
        try{
            //Checks if email with the specified user_id exists
            String prepString = "SELECT user_id FROM usr WHERE email =? ";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getUser_id());
            res = prepStmt.executeQuery();
            emailExists = res.next();

        }
        catch (SQLException sq){
            sq.printStackTrace();
            return false;
        }
        finally {
            this.closeRes(res);
            this.closePrepStmt(prepStmt);
            this.closeConnection();
            return emailExists;
        }
    }



    public int Button_Register_ActionPerformed(String username, String email, String password, String re_pass){
        try {
            if (!openConnection()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Connection failed");
                alert.showAndWait();
                return -1;

            } else if (username.equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Write the username");
                alert.showAndWait();
                return -1;

            } else if (email.equals("") ) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Write your email");
                alert.showAndWait();
                return -1;



            } else if (password.equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Write your password");
                alert.showAndWait();
                return -1;

            } else if (re_pass.equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Re-enter your password please");
                alert.showAndWait();
                return -1;

            } /*else if (fetchUsername().equals(user.getUsername())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("this username is already exist");
                alert.showAndWait();
                return -1;

            }*/
            else if(! emailExist(email)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("this email is already exist");
                alert.showAndWait();
                return -1;

            }
        }
        catch (NullPointerException np1){
            System.out.println(np1 +"np1");
        }

        PreparedStatement ps2 = null;
        //ResultSet rs2;
        String sql ="INSERT INTO usr(user_id, username, email, password) VALUES(?,?,?,?)";
        Main.user = new User(0, username+"", 1, email);
        try{
            ps2 = con.prepareStatement(sql);
            ps2.setInt(1, Main.user.getUser_id());
            ps2.setString(2, username);
            ps2.setString(3, email);
            ps2.setString(4, password);
            int added = ps2.executeUpdate();
            if (added == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("User has been added");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("adding failed");
            alert.showAndWait();
        }
        catch (NullPointerException nlp){
            System.out.println(nlp + "nlp");
        }
        finally {
            this.closePrepStmt(ps2);
            this.closeConnection();
        }

        return 1;


    }

}
