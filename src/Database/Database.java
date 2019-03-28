package Database;

import Main.*;
import javafx.scene.control.Alert;
import login.Password;
import org.apache.commons.dbcp2.BasicDataSource;


import java.sql.*;
import java.util.ArrayList;

public class Database {
    private Connection con;
    private String url;
    private String password;
    private ManageConnection manager;
    private BasicDataSource bds;
    public User user = Main.user;
    public Chat chat;
    private Password pass = new Password();
    private Alert alert;

    //Setup for database
    public Database(String url, String password){
        this.con = null;
        this.url = url;
        this.password = password;
        this.manager = new ManageConnection();
        this.bds = DataSource.getInstance().getBds();
        this.chat = new Chat();
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
            this.openConnection();
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
            String prepString = "INSERT INTO game_lobby VALUES(DEFAULT, 0)";
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

    public boolean createCharacter(String character){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        try{
            String prepString = "INSERT INTO player VALUES(DEFAULT, ?, ?, ?)";
            prepStmt = this.con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setInt(1, user.getLobbyKey());
            prepStmt.setInt(2, fetchCharacterId(character));
            prepStmt.setInt(3, user.getUser_id());
            prepStmt.executeUpdate();
            res = prepStmt.getGeneratedKeys();
            res.next();
            createCreature(character);
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

    public boolean createCreature(String character){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        try{
            String prepString = "INSERT INTO creature VALUES(?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?)";
            prepStmt = this.con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setInt(2, fetchPlayerId());
            prepStmt.setInt(12, 0);
            if(fetchCharacterId(character) == 1) {
                prepStmt.setInt(1, fetchCharacterId(character));
                prepStmt.setInt(3, user.getLobbyKey());
                prepStmt.setInt(4, 36);
                prepStmt.setInt(5, 18);
                prepStmt.setInt(6, 0);
                prepStmt.setInt(7, 0);
                prepStmt.setInt(8, 3);
                prepStmt.setInt(9, 5);
                prepStmt.setInt(10, 8);
                prepStmt.setInt(11, 2);
            }
            else if(fetchCharacterId(character) == 2){
                prepStmt.setInt(1, fetchCharacterId(character));
                prepStmt.setInt(4, 23);
                prepStmt.setInt(3, user.getLobbyKey());
                prepStmt.setInt(5, 16);
                prepStmt.setInt(6, 0);
                prepStmt.setInt(7, 0);
                prepStmt.setInt(8, 3);
                prepStmt.setInt(9, 7);
                prepStmt.setInt(10, 7);
                prepStmt.setInt(11, 2);
            }
            else if(fetchCharacterId(character) == 3){
                prepStmt.setInt(1, fetchCharacterId(character));
                prepStmt.setInt(3, user.getLobbyKey());
                prepStmt.setInt(4, 22);
                prepStmt.setInt(5, 15);
                prepStmt.setInt(6, 0);
                prepStmt.setInt(7, 0);
                prepStmt.setInt(8, 3);
                prepStmt.setInt(9, 0);
                prepStmt.setInt(10, 8);
                prepStmt.setInt(11, 1);
            }
            prepStmt.executeUpdate();
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

    public int fetchCharacterId(String character){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int characterId = -1;
        try{
            String prepString = "SELECT chrctr.character_id FROM chrctr WHERE character_name = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setString(1, character);
            res = prepStmt.executeQuery();
            while (res.next()){
                characterId = res.getInt(1);
            }

        }
        catch (SQLException sq){
            sq.printStackTrace();
            characterId = -1;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(this.con);
            return characterId;
        }
    }

    public int fetchPlayerId(){
        this.openConnection();
        PreparedStatement prepStmt = null;
        int id = -1;
        ResultSet res = null;
        try {
            String prepString = "SELECT player_id FROM player WHERE user_id = ? AND lobby_key = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, user.getUser_id());
            prepStmt.setInt(2, user.getLobbyKey());
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
            this.manager.closeConnection(this.con);
            return id;
        }
    }

    public int fetchPlayerCount(){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int count = -1;
        try{
            String prepString = "SELECT COUNT(*) FROM player WHERE lobby_key = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, user.getLobbyKey());
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
            this.manager.closeConnection(this.con);
            return count;
        }
    }

    public boolean setStartPos(int playerId){
        this.openConnection();
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            //String prepString = "UPDATE creature INNER JOIN player ON(creature.player_id = player.player_id) SET pos_x = ?, pos_y = ? WHERE user_id = ?";
            String prepString = "UPDATE creature SET pos_x = ?, pos_y = ? WHERE player_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, 3 + fetchPlayerCount());
            prepStmt.setInt(2, 3 + fetchPlayerCount());
            prepStmt.setInt(3, playerId);
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

    public boolean movePos(int xPos, int yPos, int playerId){
        this.openConnection();
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            String prepString = "UPDATE creature SET pos_x = ?, pos_y = ? WHERE player_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, 8);
            prepStmt.setInt(2, 8);
            prepStmt.setInt(3, playerId);
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

    public boolean setHost(boolean host){
        this.openConnection();
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            String prepString = "UPDATE usr SET host = ? WHERE user_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setBoolean(1, host);
            prepStmt.setInt(2, user.getUser_id());
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

    public ArrayList<Integer> fetchStartPos(boolean xpos){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        ArrayList<Integer> pos = new ArrayList<>();
        try{
            String prepString = "SELECT pos_x, pos_y FROM creature WHERE lobby_key = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, user.getLobbyKey());
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
            this.manager.closeConnection(this.con);
            return pos;
        }
    }

    public ArrayList<Integer> fetchAllPlayerId(){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<Integer> playerId = new ArrayList<>();
        try{
            String prepString = "SELECT player_id FROM player WHERE lobby_key = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, user.getLobbyKey());
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
            this.manager.closeConnection(this.con);
            return playerId;
        }
    }

    public ArrayList<Integer> fetchPlayerPos(int playerId){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<Integer> pos = new ArrayList<>();
        try{
            String prepString = "SELECT pos_x, pos_y FROM creature WHERE player_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
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
            this.manager.closeConnection(this.con);
            return pos;
        }
    }

    public int fetchPlayerCharacterId(int playerId){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int characterId = 0;
        try{
            String prepString = "SELECT character_id FROM player WHERE player_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
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
            this.manager.closeConnection(this.con);
            return characterId;
        }
    }

    public int registerUser(String username, String password, String re_pass) {
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int user_id = -1;
        try {
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "INSERT INTO usr VALUES(DEFAULT, ?, 0, DEFAULT, DEFAULT)";
            Main.user = new User(username, 0);
            prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, Main.user.getUsername());
            int added = prepStmt.executeUpdate();
            res = prepStmt.getGeneratedKeys();
            res.next();
            user_id = res.getInt(1);
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


    public boolean addPassword(String pw){

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
            System.out.println("done");
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

    public boolean setNewUsername(String newusername){
        Connection con = null;
        PreparedStatement prepStmt = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            String prepString = "UPDATE usr SET username = ? WHERE user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setString(1, newusername);
            prepStmt.setInt(2, Main.user.getUser_id());
            prepStmt.executeUpdate();
            user.setUsername(newusername);
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

    public String fetchUsername(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        String str = "";

        try {
            con = this.bds.getConnection();
            String prepString = "select username from usr where user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getUser_id());
            res = prepStmt.executeQuery();
            while (res.next()) {
                str = res.getString("username");
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
        return str;
    }

    public int fetchRank(){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int rank = 0;

        try {
            con = this.bds.getConnection();
            String prepString = "select rank from usr where user_id = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getUser_id());
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
}


class test{
    public static void main(String[] args){
        Database db = new Database("jdbc:mysql://mysql-ait.stud.idi.ntnu.no:3306/g_tdat1006_01?user=g_tdat1006_01&password=", "q8CeXgyy");
    }

}


