package Database;


import Main.*;
import game.Creature;
import game.Monster;
import javafx.scene.control.Alert;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private String url;
    private String password;
    private ManageConnection manager;
    private BasicDataSource bds;
    public Chat chat;

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
            String prepString = "INSERT INTO usr VALUES(DEFAULT, ?, 0, ?, DEFAULT, DEFAULT)";
            prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, Main.user.getUsername());
            prepStmt.setString(2, Main.user.getEmail());
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
            manager.closeConnection(con);
        }
        return username;
    }

    public String fetchEmail() {
        Connection con = null;
        String email = "";
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {
            con = this.bds.getConnection();
            String prepString = "select distinct email from usr where user_id = ?";
            prepStmt = con.prepareStatement(prepString);
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
            manager.closeConnection(con);
        }
        return email;
    }

    public int fetchRank() {
        int rank = 0;
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {
            con = this.bds.getConnection();
            String prepString = "select distinct rank from usr where user_id = ?";
            prepStmt = con.prepareStatement(prepString);
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
        if(userExist(user.getUsername())){
            return false;
        }
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "INSERT INTO usr VALUES(?, ?, DEFAULT, DEFAULT, ?, ?)";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, user.getUser_id());
            prepStmt.setString(2, user.getUsername());
            prepStmt.setString(3, user.getEmail());
            prepStmt.setString(4, "test");
            prepStmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            this.manager.writeMessage(e, "registerUser");
            return false;
        } finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return true;
        }
    }

    public boolean userExist(String username){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean userExists = false;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT user_id FROM usr WHERE username = ?";
            prepStmt = con.prepareStatement(prepString);
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


   // check if the user exits.
    public int checkLogin(String username, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        int status = -1;
        try {
            con = this.bds.getConnection();
            String query = "SELECT * FROM usr WHERE username=? AND password =?";
            ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            res = ps.executeQuery();

            // if user found -> return 0 that indicates success login.
            if(res.next()){
                status = 0;
            }

        } catch (SQLException sq) {
            sq.printStackTrace();
        } finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(ps);
            this.manager.closeConnection(con);
        }
        //If made it to here return -1, login failed.
        return status;
    }


    public boolean emailExist(String email){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        //Boolean variable to keep track of the existence of the specified email
        boolean emailExists = false;
        try{
            con = this.bds.getConnection();
            //Checks if email with the specified user_id exists
            String prepString = "SELECT user_id FROM usr WHERE email =? ";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, Main.user.getUser_id());
            res = prepStmt.executeQuery();
            emailExists = res.next();

        }
        catch (SQLException sq){
            sq.printStackTrace();
            emailExists = false;
        }
        finally {
            this.manager.closeRes(res);
            this.manager.closePrepStmt(prepStmt);
            this.manager.closeConnection(con);
            return emailExists;
        }
    }



    public int Button_Register_ActionPerformed(String username, String email, String password, String re_pass){
        Connection con = null;
        try {
            con = this.bds.getConnection();
            if (con != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Connection failed");
                alert.showAndWait();
                return -1;

            }

            if(emailExist(email)) {
                System.out.println("here we are");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("this email is already exist");
                alert.showAndWait();
                return -1;
            }


            else if (password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Write your password");
                alert.showAndWait();
                return -1;

            }else if(re_pass.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Re-enter your password please");
                alert.showAndWait();
                return -1;
            }
        }
        catch (SQLException sq){
            sq.printStackTrace();
        }
        catch (NullPointerException np1){
            System.out.println(np1 +"np1");
        }



        PreparedStatement ps2 = null;
        //ResultSet rs2;
        String sql ="INSERT INTO usr(user_id, username, email, password) VALUES(?,?,?,?)";
        Main.user = new User(0, username+"", 1, email);
        try{
            con.setAutoCommit(false);
            ps2 = con.prepareStatement(sql);
            ps2.setInt(1, Main.user.getUser_id());
            ps2.setString(2, username);
            ps2.setString(3, email);
            ps2.setString(4, password);
            int added = ps2.executeUpdate();
            con.commit();
            if (added == 1){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("User has been added");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            this.manager.rollback(con);
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("adding failed");
            alert.showAndWait();
        }

        finally {
            this.manager.turnOnAutoCommit(con);
            this.manager.closePrepStmt(ps2);
            this.manager.closeConnection(con);
        }
        return 1;
    }

    public boolean createPlayer(String character){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        int playerId = -1;
        try{
            System.out.println("uno");
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            String prepString = "INSERT INTO player VALUES(DEFAULT, ?, ?)";
            prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setInt(1, Main.user.getLobbyKey());
            prepStmt.setInt(2, Main.user.getUser_id());
            System.out.println(Main.user.getLobbyKey() + "\n" + Main.user.getUser_id());
            prepStmt.executeUpdate();
            con.commit();
            res = prepStmt.getGeneratedKeys();
            res.next();
            playerId = res.getInt(1);

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
            if(playerId <= 0){
                status = false;
            }else{
                createCreature(playerId, character);
            }
            return status;
        }
    }

    public boolean createCreature(int playerId, String character){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean status = true;
        try{
            con = this.bds.getConnection();
            con.setAutoCommit(false);
            int creatureId = fetchCreatureId(character);
            String prepString = "INSERT INTO creature SELECT ?, ?, creature_name, hp, ac, movement, damage_bonus, attack_bonus, attacks_per_turn, ?, ? FROM creatureTemplate WHERE creature_id = ?";
            prepStmt = con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setInt(1, playerId);
            prepStmt.setInt(2, creatureId);
            prepStmt.setInt(3, (int)Math.floor(Math.random()*16 + 1));
            prepStmt.setInt(4, (int)Math.floor(Math.random()*16 + 1));
            prepStmt.setInt(5, creatureId);
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

    public int fetchCreatureId(String character){
        Connection con = null;
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int characterId = -1;
        try{
            con = this.bds.getConnection();
            String prepString = "SELECT creatureTemplate.creature_id FROM creatureTemplate WHERE creature_name = ?";
            prepStmt = con.prepareStatement(prepString);
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
                if(res.getInt("player.user_id") <= 0) {
                    creatures.add(new Monster(res.getInt("hp"), res.getInt("ac"), res.getString("creature_name"), res.getInt("attacks_per_turn"), res.getInt("damage_bonus"), res.getInt("pos_x"), res.getInt("pos_y"), null,res.getString("backstory"), res.getInt("player_id")));
                }
                else{
                    creatures.add(new Character(res.getInt("hp"), res.getInt("ac"), res.getString("creature_name"), res.getInt("attacks_per_turn"), res.getInt("damage_bonus"), res.getInt("pos_x"), res.getInt("pos_y"), null,res.getString("backstory"), res.getInt("player_id")));
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
}

