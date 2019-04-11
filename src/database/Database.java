package database;

import main.*;
import chat.Chat;
import game.Creature;
import game.Level;
import game.Monster;
import game.Weapon;
import javafx.scene.control.Alert;
import user.Password;
import org.apache.commons.dbcp2.BasicDataSource;
import user.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class contains methods to push and fetch data from the database.
 * @author magnubau, saramoh, shahedsa, williad
 */
public class Database {
    private ManageConnection manager;
    private BasicDataSource bds;
    public Chat chat;
    private Alert alert;

    /**
     * Constructor for Database
     */
    public Database(){
        this.manager = new ManageConnection();
        this.bds = DataSource.getInstance().getBds();
        this.chat = new Chat();
    }

    /**
     * Fetches messages from the gamelobby the user is part of<br>
     * Adds these messages to the ObservableList in the Chat class
     */
    public void getMessagesFromChat(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT chat_message.message_id, chat_message.user_id, message, username, time_stamp FROM chat_message LEFT OUTER JOIN usr ON (chat_message.user_id = usr.user_id) WHERE chat_message.lobby_key = ? AND chat_message.message_id > ? ORDER BY message_id DESC LIMIT 30";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            /* Only fetches messages that has a message id higher than the last seen message id */
            prepStmt.setInt(2, chat.getLastSeenMessageId());
            res = prepStmt.executeQuery();
            while (res.next()) {
                /* If message has no user_id, it is an event message */
                if (res.getInt("user_id") == 0){
                    chat.addMessage(res.getInt("message_id"), "Event", res.getString("message"), "", true);
                }
                else{
                    chat.addMessage(res.getInt("message_id"), res.getString("username"), res.getString("message"), res.getString("time_stamp"), false);
                }
                if (res.isFirst()){
                    chat.setLastSeenMessageId(res.getInt("chat_message.message_id"));
                }
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
            this.manager.closeConnection(con);
        }
    }

    /**
     * Adds a message to the gamelobby the user is part of
     *
     * @param message   the text to be added
     * @param event     boolean for eventmessage
     * @return          true if adding was successful, false otherwise
     */
    public boolean addChatMessage(String message, boolean event){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        int messageId = -1;
        try {
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            /*
            * Separates event and user messages
            * Event messages has user_id = NULL in the database
            */
            if (event){
                String prepString = "INSERT INTO chat_message VALUES(?, DEFAULT, NULL, ?, NOW())";
                prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
                prepStmt.setInt(1, Main.user.getLobbyKey());
                prepStmt.setString(2, message);
            }
            else{
                String prepString = "INSERT INTO chat_message VALUES(?, DEFAULT, ?, ?, NOW())";
                prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
                prepStmt.setInt(1, Main.user.getLobbyKey());
                prepStmt.setInt(2, Main.user.getUser_id());
                prepStmt.setString(3, message);
            }
            prepStmt.executeUpdate();
            con.commit();
            /* Returns the generated message id */
            res = prepStmt.getGeneratedKeys();
            res.next();
            messageId = res.getInt(1);
        }
        catch (Exception sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            /* checks for error */
            if (messageId <= 0){
                status = false;
            }
            return status;
        }
    }

    /**
     * Checks for existence of specific game lobby
     *
     * @param lobbyKey  the lobby key
     * @return          true if lobby exists, false otherwise
     */
    public boolean gameLobbyExists(int lobbyKey){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean lobbyExists = false;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT lobby_key FROM game_lobby WHERE lobby_key = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, lobbyKey);
            res = prepStmt.executeQuery();
            lobbyExists = res.next();
        }
        catch (SQLException sq){
            sq.printStackTrace();
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return lobbyExists;
        }
    }

    /**
     * Connects the user to a lobby with the specific lobby key
     *
     * @param lobbyKey  the lobby key
     * @return          true if user is connected, false otherwise
     */
    public boolean connectUserToGameLobby(int lobbyKey){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        System.out.println(this.isJoinable(lobbyKey));
        /*
         * The lobby must exist and be joinable
         * The user registered in the client must have a valid user id
         */
        if (this.gameLobbyExists(lobbyKey) && Main.user.getUser_id() != -1 && this.isJoinable(lobbyKey)){
            try {
                con = this.bds.getConnection();
                con.setAutoCommit(false);
                String prepString = "UPDATE usr SET lobby_key = ? WHERE user_id = ?";
                prepStmt = con.prepareStatement(prepString);
                prepStmt.setInt(1, lobbyKey);
                prepStmt.setInt(2, Main.user.getUser_id());
                prepStmt.executeUpdate();
                con.commit();
                Main.user.setLobbyKey(lobbyKey);
            }
            catch (SQLException sq){
                this.manager.rollback(con);
                sq.printStackTrace();
                status = false;
            }
            finally {
                this.manager.turnOnAutoCommit(con);
                this.manager.closePrepStmt(prepStmt);
                this.manager.closeConnection(con);
            }
        }
        else{
            status = false;
        }
        return status;
    }

    /**
     * Disconnects user from the lobby it is connected to
     *
     * @return  true if disconnected, false otherwise
     */
    public boolean disconnectUserFromGameLobby(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE usr SET lobby_key = NULL WHERE user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getUser_id());
            prepStmt.executeUpdate();
            con.commit();
            Main.user.setLobbyKey(-1);
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Creates a new lobby with a unique lobby key
     *
     * @return true if lobby is created, false otherwise
     */
    public boolean createNewLobby(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int lobbyKey = -1;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "INSERT INTO game_lobby VALUES(DEFAULT, 0, 1, DEFAULT, DEFAULT)";
            prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.executeUpdate();
            res = prepStmt.getGeneratedKeys();
            res.next();
            lobbyKey = res.getInt(1);
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            /* Connects the user to the new lobby */
            this.connectUserToGameLobby(lobbyKey);
            return status;
        }
    }

    /**
     * Sets the gamelobby ready to travel to the battlefield
     *
     * @param lobbyKey  the key for the lobby
     * @return          true if change is made, false otherwise
     */
    public boolean setBattlefieldReady(int lobbyKey){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE game_lobby SET battlefield_ready = TRUE WHERE lobby_key = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, lobbyKey);
            prepStmt.executeUpdate();
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Checks if the specific gamelobby is ready to travel to battlefield
     *
     * @param lobbyKey  the lobby key
     * @return          true if ready, false otherwise
     */
    public boolean fetchBattlefieldReady(int lobbyKey) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean ready = false;
        try {
            con = this.bds.getConnection();
            String prepString = "SELECT battlefield_ready FROM game_lobby WHERE lobby_key = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, lobbyKey);
            res = prepStmt.executeQuery();
            res.next();
            ready = res.getBoolean("battlefield_ready");
        } catch (SQLException sq) {
            sq.printStackTrace();
            ready = false;
        } finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return ready;
        }
    }

     /**
     * Fetches username from the DB
     *
     * @return username for the user
     */
    public String fetchUsername() {
        Connection con = null;
        String username = "";
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {
            con = this.bds.getConnection();
            String prepString = "select distinct username from usr where user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getUser_id());
            res = prepStmt.executeQuery();
            while (res.next()) {
                username += res.getString("username");
            }
        } catch (SQLException sq) {
            manager.writeMessage(sq, "fetchUsername");
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(con);
        }
        return username;
    }

    /**
     * Takes user_id as parameter and fetches rank from the DB
     *
     * @param userId  id for the user
     * @return        rank
     */
    public int fetchRank(int userId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int rank = 0;

        try {
            con = this.bds.getConnection();
            String prepString = "select rank from usr where user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, userId);
            res = prepStmt.executeQuery();
            while (res.next()) {
                rank = res.getInt("rank");
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            return -1;
        } finally {
            if(res != null){
                manager.closeRes(res);
            }
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(con);
        }
        return rank;
    }

    /**
     * Sets the rank a user to a given Integer
     * in the database.
     *
     * @param rank      the desired rank.
     * @return          true if the update was executed, false otherwise.
     */
    public boolean setRank(int rank){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE usr SET rank = ? WHERE user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, rank);
            prepStmt.setInt(2, Main.user.getUser_id());
            prepStmt.executeUpdate();
            con.commit();
            Main.user.setRank(rank + 1);
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

     /**
     *Register a new user in the DB, Takes username as parameter, an id for the user is generated automatically
     * Rank, lobbyKey and host in the user-table sets as default
     * 
     * @param un    username for the user
     * @return      1 if the user is registered successfully
     */
    public int registerUser(String un) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int user_id = -1;
        try {
            con = this.bds.getConnection();
            con.setAutoCommit(false);                    
            String prepString = "INSERT INTO usr VALUES(DEFAULT, ?, 0, DEFAULT, DEFAULT)";
            prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, un);
            int added = prepStmt.executeUpdate();
            res = prepStmt.getGeneratedKeys();
            res.next();
            user_id = res.getInt(1);
            Main.user = new User(user_id, un, 0);  //create an instance of User
            Main.user.setUser_id(user_id);              
            con.commit();                             

            if (added == 1) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("User has been added");
                alert.showAndWait();
            }

        } catch (SQLException sq) {
            this.manager.rollback(con);              
            sq.printStackTrace();
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("adding failed");
            alert.showAndWait();
        } finally {
            this.manager.turnOnAutoCommit(con);      
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return 1;
        }
    }

    /**
     * Checks if the username exists in the DB
     *
     * @param username    username for the user
     * @return            true if the username exists already in the DB, false otherwise
     */
    public boolean findUsername(String username){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean ok = false;
        try {
            con = this.bds.getConnection();
            String prepString = "SELECT * FROM usr WHERE username = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setString(1, username);
            res = prepStmt.executeQuery();
            if(res.next()){
                ok = true;
            }else{
                System.out.println("not found");   //If the username is not found in the DB
                return false;
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            return false;
        } finally {
            if(res != null){
                manager.closeRes(res);
            }
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(con);
            return ok;
        }
    }

    /**
     * Takes new username as parameter and sets it as username
     *
     * @param newUn     new username for the user
     * @return          true if the username is set, false otherwise
     */
    public boolean setNewUsername(String newUn){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);             
            String prepString = "UPDATE usr SET username = ? WHERE user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setString(1, newUn);
            prepStmt.setInt(2, Main.user.getUser_id());
            prepStmt.executeUpdate();
            con.commit();                        
            Main.user.setUsername(newUn);        //Sets new username for the Main.user
        }
        catch (SQLException sq){
            this.manager.rollback(con);         
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);   
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Takes password as parameter and creates a hashed and slated password
     *
     * @param pw     password for the user
     * @return       true if the password is created, false otherwise
     */
    public boolean addPassword(String pw){

        Password pass = new Password();      //create an instance of Password

        /*
        Slat is an array of the random bytes, it is generated using the method getSalt() from class Password.java
         */
        byte[] salt = pass.getSalt();

        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;

        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "INSERT INTO password VALUES(?, ?, ?)";
            prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setInt(1, Main.user.getUser_id());
            prepStmt.setBytes(2, salt);
            prepStmt.setString(3, pass.createPassword(pw, salt));
            prepStmt.executeUpdate();
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Takes the old password as parameter, and deletes it
     *
     * @param oldPw     old password for the user
     * @return          true if the password is deleted, false otherwise
     */
    public boolean deleteOldPassword(String oldPw){

        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "delete from password WHERE user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getUser_id());

            prepStmt.executeUpdate();
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Fetches salt from the DB
     *
     * @param un     username for the user
     * @return       the salted password, if SQLException is thrown returns null
     */
    public byte[] fetchSalt(String un){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        byte[] saltPass = null;

        try {
            con = this.bds.getConnection();
            String prepString = "SELECT  usr.username, password.salt_pass from usr left outer join password on(usr.user_id = password.user_id)";
            prepStmt = con.prepareStatement(prepString);
            res = prepStmt.executeQuery();
            String str;
            while (res.next()) {
                str = res.getString("username");
                if(str.equalsIgnoreCase(un)){
                    saltPass = res.getBytes("salt_pass");
                }
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            return null;
        } finally {
            if(res != null){
                manager.closeRes(res);
            }
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(con);
        }
        return saltPass;
    }

    /**
     * Fetches hash from the DB
     *
     * @param un     username for the user
     * @return       the hashed password, if SQLException is thrown returns null
     */
    public String fetchHash(String un){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        String hashpass = "";

        try {
            con = this.bds.getConnection();
            String prepString = "SELECT  usr.username, password.hash_pass from usr left outer join password on(usr.user_id = password.user_id)";
            prepStmt = con.prepareStatement(prepString);
            res = prepStmt.executeQuery();
            String str;
            while (res.next()) {
                str = res.getString("username");
                if(str.equalsIgnoreCase(un)){
                    hashpass = res.getString("hash_pass");
                }
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            return null;
        } finally {
            if(res != null){
                manager.closeRes(res);
            }
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(con);
        }
        return hashpass;
    }

    /**
     * Takes username as parameter, and fetches the user_id
     *
     * @param un    username for the user
     * @return      user_id, returns -1 if the SQLException is thrown
     */
    public int fetchUser_id(String un){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int user_id = 0;

        try {
            con = this.bds.getConnection();
            String prepString = "select user_id from usr where username = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setString(1, un);
            res = prepStmt.executeQuery();
            while (res.next()) {
                user_id = res.getInt("user_id");
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            return -1;
        } finally {
            if(res != null){
                manager.closeRes(res);
            }
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(con);
        }
        return user_id;
    }

    /**
     * Creates a player in the database
     * with the users userId and lobbyKey.
     *
     * @param playable      boolean that is true if the player
     *                      will be controlled by a user, and
     *                      false if it will be controlles by
     *                      the computer.
     * @return              true if update is executed, false otherwise.
     */
    public int createPlayer(boolean playable) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int playerId = -1;
        try {
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "INSERT INTO player VALUES(DEFAULT, ?, ?, DEFAULT, DEFAULT )";
            prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            if (playable) {
                prepStmt.setInt(2, Main.user.getUser_id());
            } else {
                prepStmt.setNull(2, java.sql.Types.INTEGER);
            }
            prepStmt.executeUpdate();
            con.commit();

            res = prepStmt.getGeneratedKeys();
            res.next();
            playerId = res.getInt(1);
            if (playable) {
                Main.user.setPlayerId(playerId);
            }


        } catch (SQLException sq) {
            this.manager.rollback(con);
            sq.printStackTrace();
            playerId = -1;
        } finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return playerId;
        }
    }

    /**
     * creates a creature in the database.
     * @param playerId      player id connected to creature.
     * @param creatureId    creature id.
     * @param posX          x-position.
     * @param posY          y-position.
     * @return              true if update is executed, false otherwise.
     */
    public boolean createCreature(int playerId, int creatureId, int posX, int posY) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        try {
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "INSERT INTO creature SELECT ?, ?, creature_name, hp, ac, movement, damage_bonus, attack_bonus, backstory, ?, ?, image_url FROM creatureTemplate WHERE creature_id = ?";
            prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setInt(1, playerId);
            prepStmt.setInt(2, creatureId);
            prepStmt.setInt(3, posX);
            prepStmt.setInt(4, posY);
            prepStmt.setInt(5, creatureId);
            prepStmt.executeUpdate();
            con.commit();
        } catch (SQLException sq) {
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        } finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * fetches username corresponding to a playerId,
     * from the database.
     * @param playerId      player id.
     * @return              The username.
     */
    public String fetchUsernameFromPlayerId(int playerId) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        String username = null;
        try {
            con = this.bds.getConnection();
            String prepString = "SELECT username FROM usr, player WHERE player.user_id = usr.user_id AND player_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, playerId);
            res = prepStmt.executeQuery();
            res.next();
            username = res.getString("username");

        } catch (SQLException sq) {
            sq.printStackTrace();
            username = null;
        } finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return username;
        }
    }

    /**
     * fethes a playerId corresponding to the users
     * user id and lobbyKey.
     *
     * @return      A player id.
     */
    public int fetchPlayerId(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        int id = -1;
        ResultSet res = null;
        try {
            con = this.bds.getConnection();
            String prepString = "SELECT player_id FROM player WHERE user_id = ? AND lobby_key = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getUser_id());
            prepStmt.setInt(2, Main.user.getLobbyKey());
            res = prepStmt.executeQuery();
            while(res.next()) {
                id = res.getInt(1);
                System.out.println(id);
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            id = -1;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return id;
        }
    }

    /**
     * fethes the number of players in a users lobby.
     *
     * @return      The number of players.
     */
    public int fetchPlayerCount(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int count = -1;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT COUNT(*) FROM player WHERE lobby_key = ? AND user_id IS NOT NULL";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            res = prepStmt.executeQuery();
            res.next();
            count = res.getInt(1);

        }
        catch (SQLException sq){
            sq.printStackTrace();
            count = -1;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return count;
        }
    }

    /**
     * Updates the position of a Creature with a
     * specific player id.
     *
     * @param xPos      new x-position.
     * @param yPos      new y-position.
     * @param playerId  player id
     * @return          true if update executed, false otherwise.
     */
    public boolean setPos(int xPos, int yPos, int playerId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE creature SET pos_x = ?, pos_y = ? WHERE player_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, xPos);
            prepStmt.setInt(2, yPos);
            prepStmt.setInt(3, playerId);
            prepStmt.executeUpdate();
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Makes the user a host.
     *
     * @param host  boolean host.
     * @return      true if update executed, false otherwise.
     */
    public boolean setHost(boolean host){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE usr SET host = ? WHERE user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setBoolean(1, host);
            prepStmt.setInt(2, Main.user.getUser_id());
            prepStmt.executeUpdate();
            con.commit();
            Main.user.setHost(host);
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Fetches the position of a Creature with a
     * specific playerId.
     *
     * @param playerId      player id.
     * @return              ArrayList of x- and y-position.
     */
    public ArrayList<Integer> fetchPlayerPos(int playerId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<Integer> pos = new ArrayList<>();
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT pos_x, pos_y FROM creature WHERE player_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, playerId);
            res = prepStmt.executeQuery();
            res.next();
            pos.add(res.getInt(1));
            pos.add(res.getInt(2));
        }
        catch (SQLException sq){
            sq.printStackTrace();
            pos = null;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return pos;
        }
    }

    /**
     * Fetches all creatures from the game lobby.
     * with the users lobbyKey.
     *
     * @return      ArrayList of creatures.
     */
    public ArrayList<Creature> fetchCreaturesFromLobby(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<Creature> creatures = new ArrayList<>();
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT creature.*, player.user_id From creature, player WHERE player.lobby_key = ? AND player.player_id = creature.player_id AND creature.hp > 0";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            res = prepStmt.executeQuery();
            while (res.next()){
                int creatureId = res.getInt("creature_id");
                System.out.println(creatureId);
                ArrayList<Weapon> weapons = this.fetchWeaponsFromCreature(creatureId);
                if(res.getInt("player.user_id") <= 0) {
                    creatures.add(new Monster(res.getInt("player_id"), creatureId, res.getString("creature_name"), res.getInt("hp"), res.getInt("ac"), res.getInt("movement"), res.getInt("damage_bonus"), res.getInt("attack_bonus"), res.getString("backstory"), res.getInt("pos_x"), res.getInt("pos_y"), res.getString("image_url"), weapons));
                }
                else{
                    creatures.add(new game.Character(res.getInt("player_id"), creatureId, res.getString("creature_name"), res.getInt("hp"), res.getInt("ac"), res.getInt("movement"), res.getInt("damage_bonus"), res.getInt("attack_bonus"), res.getString("backstory"), res.getInt("pos_x"), res.getInt("pos_y"), res.getString("image_url"), weapons));
                }
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            creatures = null;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return creatures;
        }
    }

    /**
     * Fetches all weapons from a Creature with a given creatureId.
     *
     * @param creatureId        creature id.
     * @return                  ArrayList of weapons.
     */
    public ArrayList<Weapon> fetchWeaponsFromCreature(int creatureId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<Weapon> weapons = new ArrayList<>();
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT weapon_name, damage_dice, description, dice_amount, image_url FROM weapon INNER JOIN creature_weapon ON (weapon.weapon_id = creature_weapon.weapon_id) WHERE creature_weapon.creature_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, creatureId);
            res = prepStmt.executeQuery();
            while (res.next()){
                boolean ranged = false;
                if (res.getString("description").equals("Ranged")){
                    ranged = true;
                }
                weapons.add(new Weapon(res.getString("weapon_name"), res.getInt("damage_dice"), ranged, res.getInt("dice_amount"), res.getString("image_url")));
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            weapons = null;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return weapons;
        }
    }

    /**
     * Fetches health points from a Creature with
     * a specific playerId.
     *
     * @param playerId      player id.
     * @return              health points.
     */
    public int fetchPlayerHp(int playerId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int hp = 0;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT hp FROM creature WHERE player_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, playerId);
            res = prepStmt.executeQuery();
            res.next();
            hp = res.getInt(1);
        }
        catch (SQLException sq){
            sq.printStackTrace();
            hp = -1;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return hp;
        }
    }

    /**
     * Sets the health points of creature, with a
     * specific playerId, to a given value.
     *
     * @param hp            health points.
     * @param playerId      player id.
     * @return              true if update executed, false otherwise.
     */
    public boolean setHp(int hp, int playerId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE creature SET hp = ? WHERE player_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, hp);
            prepStmt.setInt(2, playerId);
            prepStmt.executeUpdate();
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Sets the damage bonus of creature, with a
     * specific playerId, to a given value.
     *
     * @param damageBonus       damage bonus.
     * @param playerId          player id.
     * @return                  true if update executed, false otherwise.
     */
    public boolean setDamageBonus(int damageBonus, int playerId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE creature SET damage_bonus = ? WHERE player_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, damageBonus);
            prepStmt.setInt(2, playerId);
            prepStmt.executeUpdate();
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Increments the player turn in game lobby
     * where lobbyKey equals users lobbyKey
     *
     * @param turn      turn.
     * @return          true if update executed, false otherwise.
     */
    public boolean incrementPlayerTurn(int turn){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE game_lobby SET player_turn = ? WHERE lobby_key = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, turn);
            prepStmt.setInt(2, Main.user.getLobbyKey());
            prepStmt.executeUpdate();
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Fetches player turn from game lobby where lobbyKey
     * equals users lobbyKey.
     * @return      lobbyKey.
     */
    public int fetchPlayerTurn() {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int turn = 0;
        try {
            con = this.bds.getConnection();
            String prepString = "SELECT player_turn FROM game_lobby WHERE lobby_key = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            res = prepStmt.executeQuery();
            res.next();
            turn = res.getInt(1);
        } catch (SQLException sq) {
            sq.printStackTrace();
            turn = -1;
        } finally {
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return turn;
        }
    }

    /**
     * Sets a player ready where lobbyKey equals users lobbyKey
     * and playerId equals users playerId.
     *
     * @param ready         ready
     * @return              true if update executed, false otherwise.
     */
    public boolean isReady(boolean ready){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE player SET ready = ? WHERE lobby_key = ? AND player_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setBoolean(1, ready);
            prepStmt.setInt(2, Main.user.getLobbyKey());
            prepStmt.setInt(3, Main.user.getPlayerId());
            prepStmt.executeUpdate();
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Fetches an ArrayList of all players ready status
     * in the users game lobby.
     * @return      ArrayList of booleans.
     */
    public ArrayList<Boolean> everyoneIsReady(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res;
        ArrayList<Boolean> ready = new ArrayList<>();
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT ready FROM player WHERE lobby_key = ? AND user_id is not NULL";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            res = prepStmt.executeQuery();
            while (res.next()){
                ready.add(res.getBoolean(1));
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            return ready;
        }
        finally {
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return ready;
        }
    }

    /**
     * Sets the lobby the user is part of as joinable or not
     * @param joinable  joinable
     * @return          true if joinable attribute is changed, false otherwise
     */
    public boolean setJoinable(boolean joinable){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE game_lobby SET joinable = ? WHERE lobby_key = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setBoolean(1, joinable);
            prepStmt.setInt(2, Main.user.getLobbyKey());
            prepStmt.executeUpdate();
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Disconnects a player from the lobby is it part of
     *
     * @param playerId player id
     * @return         true if player is disconnected, false otherwise
     */
    public boolean disconnectPlayerFromLobby(int playerId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE player SET lobby_key = ? WHERE player_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, 1);
            prepStmt.setInt(2, playerId);
            prepStmt.executeUpdate();
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Checks if a specific lobby is joinable
     *
     * @param lobbyKey  lobby key
     * @return          true if joinable, false otherwise
     */
    public boolean isJoinable(int lobbyKey){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean joinable = false;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT joinable FROM game_lobby WHERE lobby_key = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, lobbyKey);
            res = prepStmt.executeQuery();
            res.next();
            joinable = res.getBoolean(1);
        }
        catch (SQLException sq){
            sq.printStackTrace();
            joinable = false;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return joinable;
        }
    }

    /**
     * Fetches data about a specific level to create the Level-object
     * @param levelId   level id
     * @return          Level-object
     */
    public Level fetchLevelObject(int levelId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        Level level = null;
        try {
            con = this.bds.getConnection();
            String prepString = "SELECT level.* FROM level WHERE level_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, levelId);
            res = prepStmt.executeQuery();
            if (res.next()) {
                level = new Level(res.getInt("level_id"), res.getInt("music"), res.getString("background_url"));
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return level;
        }
    }

    /**
     * Fetches the id of the level the lobby currently is at
     * @param lobbyKey  lobby key
     * @return          level id
     */
    public int fetchLevelId(int lobbyKey){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int levelId = 0;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT level_id FROM game_lobby WHERE lobby_key = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, lobbyKey);
            res = prepStmt.executeQuery();
            res.next();
            levelId = res.getInt(1);
        }
        catch (SQLException sq){
            sq.printStackTrace();
            levelId = 0;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return levelId;
        }
    }

    /**
     * Fetches the name of a specific level
     * @param levelId   level id
     * @return          level name
     */
    public String getLevelName(int levelId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        String levelName = null;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT background_url FROM level WHERE level_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, levelId);
            res = prepStmt.executeQuery();
            while(res.next()) {
                /* The name of the level is determined from the background-url */
                levelName = res.getString(1);
                levelName = levelName.split("-")[0];
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            levelName = null;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return levelName;
        }
    }

    /**
     * Fetches the amount of levels available
     * @return  amount of levels
     */
    public int fetchAmountOfLevels(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int levelCount = 0;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT COUNT(*) FROM level;";
            prepStmt = con.prepareStatement(prepString);
            res = prepStmt.executeQuery();
            res.next();
            levelCount = res.getInt(1);
        }
        catch (SQLException sq){
            sq.printStackTrace();
            levelCount = 0;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return levelCount;
        }
    }

    /**
     * Sets the current level id of a lobby
     *
     * @param lobbyKey lobbykey
     * @param levelId  level id
     * @return         true if level id is set, false otherwise
     */
    public boolean setLevelId(int lobbyKey, int levelId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = false;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE game_lobby SET level_id = ? WHERE lobby_key = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, levelId);
            prepStmt.setInt(2, lobbyKey);
            prepStmt.executeUpdate();
            status = true;
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

    /**
     * Fetches the monsters tied to a specific level based on the amount of players
     *
     * @param levelId       level id
     * @param playerAmount  amount of players
     * @return              ArrayList of creature ids
     */
    public ArrayList<Integer> fetchMonstersFromLevel(int levelId, int playerAmount){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<Integer> creatureIds = new ArrayList<>();
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT creature_id FROM level_monster WHERE level_id = ? AND player_amount = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, levelId);
            prepStmt.setInt(2, playerAmount);
            res = prepStmt.executeQuery();
            while(res.next()){
                creatureIds.add(res.getInt(1));
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            creatureIds = null;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return creatureIds;
        }
    }

    /**
     * Fetches an ArrayList containing a boolean for each player for if they are ready or not
     *
     * @return ArrayList with players ready for new level
     */
    public ArrayList<Boolean> fetchPlayersReadyForLevel(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<Boolean> playersReadyForLevel = new ArrayList<>();
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT ready_for_new_level FROM player WHERE lobby_key = ? AND user_id IS NOT NULL";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            res = prepStmt.executeQuery();
            while(res.next()){
                playersReadyForLevel.add(res.getBoolean(1));
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
            playersReadyForLevel = null;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return playersReadyForLevel;
        }
    }

    /**
     * Sets the player ready for new level
     *
     * @param playerId player id
     * @param ready    ready
     * @return         true if ready for new level is set, false otherwise
     */
    public boolean setReadyForNewLevel(int playerId, boolean ready){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = false;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE player SET ready_for_new_level = ? WHERE player_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setBoolean(1, ready);
            prepStmt.setInt(2, playerId);
            prepStmt.executeUpdate();
            status = true;
            con.commit();
        }
        catch (SQLException sq){
            this.manager.rollback(con);
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return status;
        }
    }

}
