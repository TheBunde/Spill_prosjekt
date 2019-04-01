package Database;


import Main.*;
import com.mysql.cj.protocol.Resultset;
import game.Creature;
import game.Monster;
import game.Weapon;
import javafx.scene.control.Alert;
import login.Password;
import org.apache.commons.dbcp2.BasicDataSource;
//
//
// import org.apache.commons.dbcp2.BasicDataSource;

import javax.xml.bind.annotation.XmlType;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    private String url;
    private String password;
    private ManageConnection manager;
    private BasicDataSource bds;
    public Chat chat;
    private Alert alert;

    //Setup for database
    public Database(String url, String password){
        this.url = url;
        this.password = password;
        this.manager = new ManageConnection();
        this.bds = DataSource.getInstance().getBds();
        this.chat = new Chat();
    }

    //Fetches messages from chat
    public void getMessagesFromChat(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT chat_message.message_id, chat_message.user_id, message, username, time_stamp FROM chat_message LEFT OUTER JOIN usr ON (chat_message.user_id = usr.user_id) WHERE chat_message.lobby_key = ? AND chat_message.message_id > ? ORDER BY message_id DESC LIMIT 30";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            prepStmt.setInt(2, chat.getLastSeenMessageId());

            res = prepStmt.executeQuery();
            while (res.next()) {
                if (res.getInt("user_id") == 0){
                    chat.addMessage("Event", res.getString("message"), res.getString("time_stamp"), true);
                }
                else{
                    chat.addMessage(res.getString("username"), res.getString("message"), res.getString("time_stamp"), false);
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

    //Sends new message to the chat that the user is connected to
    public boolean addChatMessage(String message, boolean event){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        int messageId = -1;
        try {
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            //Using a prepared statement to execute an insert into the chat_message entity
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
            if (messageId <= 0){
                status = false;
            }
            return status;
        }
    }


    public boolean gameLobbyExists(int lobbyKey){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        //Boolean variable to keep track of the existence of the specified gamelobby
        boolean lobbyExists = false;
        try{
            //Checks if gamelobby with the specified lobbykey exists
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

    public boolean connectUserToGameLobby(int lobbyKey){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        if (this.gameLobbyExists(lobbyKey) && Main.user.getUser_id() != -1){
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

    public boolean addUser(User user){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int user_id = -1;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "INSERT INTO usr VALUES(DEFAULT, ?, 0, DEFAULT, DEFAULT)";
            prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, Main.user.getUsername());
            System.out.println("done");
            prepStmt.executeUpdate();
            res = prepStmt.getGeneratedKeys();
            res.next();
            user_id = res.getInt(1);
            Main.user.setUser_id(user_id);
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

    public boolean createNewLobby(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int lobbyKey = -1;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "INSERT INTO game_lobby VALUES(DEFAULT, 0, DEFAULT)";
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
            this.connectUserToGameLobby(lobbyKey);
            return status;
        }
    }

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

    public int fetchRank(int user_id){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int rank = 0;

        try {
            con = this.bds.getConnection();
            String prepString = "select rank from usr where user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, user_id);
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


    public boolean userExist(String username) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean userExists = false;
        try {
            con = this.bds.getConnection();
            String prepString = "SELECT user_id FROM usr WHERE username = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setString(1, username);
            res = prepStmt.executeQuery();
            userExists = res.next();

        } catch (SQLException sq) {
            manager.writeMessage(sq, "userExist");
            return false;
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(con);
            return userExists;
        }
    }

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
            Main.user = new User(user_id, un, 0);
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
                System.out.println("not found");
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

    public boolean setNewUsername(String newun){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE usr SET username = ? WHERE user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setString(1, newun);
            prepStmt.setInt(2, Main.user.getUser_id());
            prepStmt.executeUpdate();
            con.commit();
            Main.user.setUsername(newun);
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


    public boolean addPassword(String pw){

        Password pass = new Password();
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

    public boolean deleteOldPassword(String newPw){

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



    public boolean createPlayer(String character, boolean playable) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        int playerId = -1;
        try {
            System.out.println("uno");
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "INSERT INTO player VALUES(DEFAULT, ?, ?)";
            prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            if (playable) {
                prepStmt.setInt(2, Main.user.getUser_id());
            } else {
                prepStmt.setNull(2, java.sql.Types.INTEGER);
            }
            System.out.println(Main.user.getLobbyKey() + "\n" + Main.user.getUser_id());
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
            status = false;
        } finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            if (playerId < 0) {
                status = false;
            } else {
                createCreature(playerId, character);
            }
            return status;
        }
    }


    public boolean createCreature(int playerId, String character) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        try {
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            int creatureId = fetchCreatureId(character);
            String prepString = "INSERT INTO creature SELECT ?, ?, creature_name, hp, ac, movement, damage_bonus, attack_bonus, attacks_per_turn, backstory, ?, ? FROM creatureTemplate WHERE creature_id = ?";
            prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setInt(1, playerId);
            prepStmt.setInt(2, creatureId);
            prepStmt.setInt(3, (int) Math.floor(Math.random() * 16));
            prepStmt.setInt(4, (int) Math.floor(Math.random() * 16));
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

    public int fetchCreatureId(String character) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int characterId = -1;
        try {
            con = this.bds.getConnection();
            String prepString = "SELECT creatureTemplate.creature_id FROM creatureTemplate WHERE creature_name = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setString(1, character);
            res = prepStmt.executeQuery();
            while (res.next()) {
                characterId = res.getInt(1);
            }

        } catch (SQLException sq) {
            sq.printStackTrace();
            characterId = -1;
        } finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return characterId;
        }
    }

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
            res.next();
            id = res.getInt(1);

        }
        catch (SQLException sq){
            sq.printStackTrace();
            id = -1;
        }
        finally {
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return id;
        }
    }

    public int fetchPlayerCount(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int count = -1;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT COUNT(*) FROM player WHERE lobby_key = ?";
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

   /* public boolean setStartPos(int playerId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            //String prepString = "UPDATE creature INNER JOIN player ON(creature.player_id = player.player_id) SET pos_x = ?, pos_y = ? WHERE user_id = ?";
            String prepString = "UPDATE creature SET pos_x = ?, pos_y = ? WHERE player_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, 3 + fetchPlayerCount());
            prepStmt.setInt(2, 3 + fetchPlayerCount());
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
    }*/

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

    public ArrayList<Integer> fetchStartPos(boolean xpos){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        ArrayList<Integer> pos = new ArrayList<>();
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT pos_x, pos_y FROM creature WHERE lobby_key = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            res = prepStmt.executeQuery();
            while (res.next()){
                if(xpos) {
                    pos.add(res.getInt(1));
                }
                else{
                    pos.add(res.getInt(2));
                }
            }

        }
        catch (SQLException sq){
            sq.printStackTrace();
            status = false;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return pos;
        }
    }

    public ArrayList<Integer> fetchAllPlayerId(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<Integer> playerId = new ArrayList<>();
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT player_id FROM player WHERE lobby_key = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            res = prepStmt.executeQuery();
            while (res.next()){
                playerId.add(res.getInt(1));
            }

        }
        catch (SQLException sq){
            sq.printStackTrace();
            playerId = null;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return playerId;
        }
    }

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

    public int fetchPlayerCreatureId(int playerId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int characterId = 0;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT creature_id FROM creature WHERE player_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, playerId);
            res = prepStmt.executeQuery();
            res.next();
            characterId = res.getInt(1);
        }
        catch (SQLException sq){
            sq.printStackTrace();
            characterId = -1;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return characterId;
        }
    }

    public ArrayList<Creature> fetchCreaturesFromLobby(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<Creature> creatures = new ArrayList<>();
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT creature.*, player.user_id From creature, player WHERE player.lobby_key = ? AND player.player_id = creature.player_id ";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            res = prepStmt.executeQuery();
            while (res.next()){
                int creatureId = res.getInt("creature_id");
                ArrayList<Weapon> weapons = this.fetchWeaponsFromCreature(creatureId);
                if(res.getInt("player.user_id") <= 0) {
                    creatures.add(new Monster(res.getInt("player_id"), creatureId, res.getString("creature_name"), res.getInt("hp"), res.getInt("ac"), res.getInt("movement"), res.getInt("damage_bonus"), res.getInt("attack_bonus"), res.getInt("attacks_per_turn"), res.getString("backstory"), res.getInt("pos_x"), res.getInt("pos_y"), weapons));
                }
                else{
                    creatures.add(new game.Character(res.getInt("player_id"), creatureId, res.getString("creature_name"), res.getInt("hp"), res.getInt("ac"), res.getInt("movement"), res.getInt("damage_bonus"), res.getInt("attack_bonus"), res.getInt("attacks_per_turn"), res.getString("backstory"), res.getInt("pos_x"), res.getInt("pos_y"), weapons));
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

    public ArrayList<Weapon> fetchWeaponsFromCreature(int creatureId){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<Weapon> weapons = new ArrayList<>();
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT weapon_name, damage_dice, description, dice_amount FROM weapon INNER JOIN creature_weapon ON (weapon.weapon_id = creature_weapon.weapon_id) WHERE creature_weapon.creature_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, creatureId);
            res = prepStmt.executeQuery();
            while (res.next()){
                boolean ranged = false;
                if (res.getString("description").equals("Ranged")){
                    ranged = true;
                }
                weapons.add(new Weapon(res.getString("weapon_name"), res.getInt("damage_dice"), ranged, res.getInt("dice_amount")));
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


    public boolean isReady(boolean ready){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "UPDATE players SET ready = ? WHERE lobby_key = ? AND player_id = ?";
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
}
