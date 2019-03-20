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
    public Creature creature;
    private ManageConnection manager;

    //Setup for database
    public Database(String url, String password) {
        this.con = null;
        this.url = url;
        this.password = password;
        this.manager = new ManageConnection();
        this.user = new User(3, "william", 2, "gmail@gmail.com");

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
    public ArrayList<String> getMessagesFromChat(Chatter chatter) {
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        ArrayList<String> messages = new ArrayList<String>();
        try {
            String prepString = "SELECT message_id, chat_message.chatter_id, message, name, time_stamp FROM chat_message LEFT OUTER JOIN chatter ON " +
                    "(chat_message.chatter_id = chatter.chatter_id) WHERE chat_message.chat_id = ? ORDER BY message_id DESC LIMIT 30";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, chatter.getChatID());
            res = prepStmt.executeQuery();
            while (res.next()) {
                messages.add(res.getString("name") + ": " + res.getString("message") + " | " + res.getString("time_stamp"));
            }
        } catch (SQLException sq) {
            manager.writeMessage(sq, "getMessagesFromChat");
            return null;
        } finally {
            if (res != null) {
                manager.closeRes(res);
            }
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
            return messages;
        }
    }

    //Sends new message to the chat that the user is connected to
    public boolean addChatMessage(Chatter chatter, String message) {
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
        } catch (SQLException sq) {
            manager.writeMessage(sq, "addChatMessage");
            return false;
        } finally {
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
            return true;
        }
    }

    public boolean chatExists(int chatID) {
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        //Boolean variable to keep track of the existence of the specified chat
        boolean chatExists = false;
        try {
            //Checks if chat with the specified chatID exists
            String prepString = "SELECT chat_id FROM chat WHERE chat_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, chatID);
            res = prepStmt.executeQuery();
            chatExists = res.next();

        } catch (SQLException sq) {
            manager.writeMessage(sq, "chatExists");
            return false;
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
            return chatExists;
        }
    }

    public boolean connectChatterToChat(Chatter chatter, int chatID) {
        PreparedStatement prepStmt = null;
        if (chatExists(chatID) && chatter.getChatterID() != -1) {
            this.openConnection();
            try {
                String prepString = "UPDATE chatter SET chat_id = ? WHERE chatter_id = ?";
                prepStmt = this.con.prepareStatement(prepString);
                prepStmt.setInt(1, chatID);
                prepStmt.setInt(2, chatter.getChatterID());
                prepStmt.executeUpdate();
                chatter.setChatID(chatID);
                return true;
            } catch (SQLException sq) {
                manager.writeMessage(sq, "connectChatterToChat");
                return false;
            } finally {
                manager.closePrepStmt(prepStmt);
                manager.closeConnection(this.con);
            }
        }
        return false;
    }

    public boolean addChatter(Chatter chatter) {
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        int chatterID = -1;
        try {
            String prepString = "INSERT INTO chatter VALUES(DEFAULT, DEFAULT, ?)";
            prepStmt = this.con.prepareStatement(prepString, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, chatter.getName());
            prepStmt.executeUpdate();
            res = prepStmt.getGeneratedKeys();
            res.next();
            chatterID = res.getInt(1);
            chatter.setChatterID(chatterID);
        } catch (SQLException sq) {
            manager.writeMessage(sq, "addChatter");
            return false;
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
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
            while (res.next()) {
                username += res.getString("username");
            }
        } catch (SQLException sq) {
            manager.writeMessage(sq, "fetchUsername");
        } finally {
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
            while (res.next()) {
                email += res.getString("email");
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
            while (res.next()) {
                rank += res.getInt("level");
            }
        } catch (SQLException sq) {
            manager.writeMessage(sq, "fetchRank");
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
        }
        return rank;
    }

    public boolean registerUser(User user) {
        if (userExist(user.getUsername()))
            return false;
        openConnection();
        PreparedStatement prepStmt = null;
        try {
            String prepString = "INSERT INTO usr VALUES(?, ?, DEFAULT, DEFAULT, ?, ?)";

            this.con.setAutoCommit(false);
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, user.getUser_id());
            prepStmt.setString(2, user.getUsername());
            prepStmt.setString(3, user.getEmail());
            prepStmt.setString(4, "test");
            prepStmt.executeUpdate();
            this.con.commit();

        } catch (SQLException sq) {
            manager.rollback(this.con);
            return false;
        } finally {
            manager.turnOnAutoCommit(this.con);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
            return true;
        }
    }

    public boolean userExist(String username) {
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean userExists = false;
        try {
            String prepString = "SELECT user_id FROM usr WHERE username = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setString(1, username);
            res = prepStmt.executeQuery();
            userExists = res.next();

        } catch (SQLException sq) {
            manager.writeMessage(sq, "userExist");
            return false;
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
            return userExists;
        }
    }


// the method will check the lobby_key to go to the game_lobby

    public boolean checkLobby_key(int lobby_key) {
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean ok = false;
        try {
            String prepString = "SELECT lobby_key FROM game_lobby WHERE lobby_key = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, lobby_key);
            res = prepStmt.executeQuery();
            ok = res.next();

        } catch (SQLException sq) {
            manager.writeMessage(sq, "checkLobby_key");
            return false;
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
            return ok;
        }
    }


    public boolean creatureExist(int creature_id) {
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean creatureExist = false;
        try {
            String prepString = "SELECT creture_id FROM creature WHERE creature_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, creature_id);
            res = prepStmt.executeQuery();
            creatureExist = res.next();

        } catch (SQLException sq) {
            manager.writeMessage(sq, "creatureExist");
            return false;
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
            return creatureExist;
        }
    }

    public int fetchHP() {
        int HP = 0;
        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {

            String prepString = "select HP from creature where creature_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, creature.getHp());
            res = prepStmt.executeQuery();
            while (res.next()) {
                HP += res.getInt("HP");
            }
        } catch (SQLException sq) {
            manager.writeMessage(sq, "fetchHP");
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
        }
        return HP;
    }


    public boolean setHP(int creature_id, int newHP) {
        PreparedStatement prepStmt = null;
        if (creatureExist(creature_id) && creature.getHp() != -1) {
            this.openConnection();
            try {
                String prepString = "UPDATE creature SET HP = ? WHERE creature_id = ?";
                this.con.setAutoCommit(false);
                prepStmt = this.con.prepareStatement(prepString);
                prepStmt.setInt(1, newHP);
                prepStmt.setInt(2, creature_id);
                prepStmt.executeUpdate();
                this.con.commit();
                creature.setHp(newHP);

            } catch (SQLException sq) {
                manager.rollback(this.con);
                return false;
            } finally {
                manager.turnOnAutoCommit(this.con);
                manager.closePrepStmt(prepStmt);
                manager.closeConnection(this.con);
                return true;
            }
        }
        return false;
    }


    public int fetchAC() {
        int AC = 0;
        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {

            String prepString = "select AC from creature where creature_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, creature.getAc());
            res = prepStmt.executeQuery();
            while (res.next()) {
                AC += res.getInt("AC");
            }
        } catch (SQLException sq) {
            manager.writeMessage(sq, "fetchAC");
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
        }
        return AC;
    }


    public boolean setAC(int creature_id, int newAC) {
        PreparedStatement prepStmt = null;
        if (creatureExist(creature_id) && creature.getAc() != -1) {
            this.openConnection();
            try {
                String prepString = "UPDATE creature SET AC = ? WHERE creature_id = ?";
                this.con.setAutoCommit(false);
                prepStmt = this.con.prepareStatement(prepString);
                prepStmt.setInt(1, newAC);
                prepStmt.setInt(2, creature_id);
                prepStmt.executeUpdate();
                this.con.commit();
                creature.setAc(newAC);
                return true;
            } catch (SQLException sq) {
                manager.rollback(this.con);
                return false;
            } finally {
                manager.turnOnAutoCommit(this.con);
                manager.closePrepStmt(prepStmt);
                manager.closeConnection(this.con);
            }
        }
        return false;
    }


    public int fetchLevel(int creature_id) {
        int level = 0;
        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {

            String prepString = "select lv from creature where creature_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, creature.getLevel());
            res = prepStmt.executeQuery();
            while (res.next()) {
                level += res.getInt("lv");
            }
        } catch (SQLException e) {
            manager.writeMessage(e, "fetchLevel");
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
        }
        return level;
    }


    public boolean setLevel(int creature_id, int newLevel) {
        PreparedStatement prepStmt = null;
        if (creatureExist(creature_id) && creature.getLevel() != -1) {
            this.openConnection();
            try {
                String prepString = "UPDATE creature SET lv = ? WHERE creature_id = ?";
                this.con.setAutoCommit(false);
                prepStmt = this.con.prepareStatement(prepString);
                prepStmt.setInt(1, newLevel);
                prepStmt.setInt(2, creature_id);
                prepStmt.executeUpdate();
                this.con.commit();
                creature.setLevel(newLevel);
                return true;
            } catch (SQLException sq) {
                manager.rollback(this.con);
                return false;
            } finally {
                manager.turnOnAutoCommit(this.con);
                manager.closePrepStmt(prepStmt);
                manager.closeConnection(this.con);
            }
        }
        return false;
    }


    public int fetchAttack_bonus(int creature_id) {
        int bonus = 0;
        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {

            String prepString = "select attack_bonus from creature where creature_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, creature.getAttackBonus());
            res = prepStmt.executeQuery();
            while (res.next()) {
                bonus += res.getInt("attack_bonus");
            }
        } catch (SQLException e) {
            manager.writeMessage(e, "fetchAttack_bonus");
        } finally {
            manager.closeRes(res);
            manager.closePrepStmt(prepStmt);
            manager.closeConnection(this.con);
        }
        return bonus;
    }


    public boolean setAttack_bonus(int creature_id, int newAttack_bonus) {
        PreparedStatement prepStmt = null;
        if (creatureExist(creature_id) && creature.getAttackBonus() != -1) {
            this.openConnection();
            try {
                String prepString = "UPDATE creature SET attack_bonus = ? WHERE creature_id = ?";
                this.con.setAutoCommit(false);
                prepStmt = this.con.prepareStatement(prepString);
                prepStmt.setInt(1, newAttack_bonus);
                prepStmt.setInt(2, creature_id);
                prepStmt.executeUpdate();
                this.con.commit();
                creature.setAttackBonus(newAttack_bonus);
                return true;
            } catch (SQLException sq) {
                manager.rollback(this.con);
                return false;
            } finally {
                manager.turnOnAutoCommit(this.con);
                manager.closePrepStmt(prepStmt);
                manager.closeConnection(this.con);
            }
        }
        return false;
    }

// The method checks the username and the password to log in

    public int checkLogin(String username, String password) {
        boolean con = openConnection();
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
            if (resultSet.next()) {
                return 0;
            }

        } catch (SQLException sq) {
            manager.writeMessage(sq, "checkLogin");
        } finally {
            manager.closePrepStmt(ps);
            manager.closeConnection(this.con);
        }
        //If made it to here return -1, login failed.
        return -1;
    }
}