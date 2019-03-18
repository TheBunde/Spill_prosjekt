package AccountDetails;
import java.sql.*;

public class AccountDetailsDatabase {

    private Connection con;
    private String url;
    private String password;

    private ManageConnection manager;
    private User user;


    public AccountDetailsDatabase(String url, String password) {
        this.con = null;
        this.url = url;
        this.password = password;
    }

    //Safely opens connection between the application and the database
    public boolean openConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager.getConnection(this.url + this.password);
        }
        catch(SQLException e){
            System.out.println("SQL-Exception: " + e);
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


    public String fetchUsername() {
        String username = "";
        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {
            String prepString = "select distinct username from usr where username = ?";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setString(1, user.getUsername());
            res = prepStmt.executeQuery();
            while(res.next()){
                username  += res.getString("username");
            }
        } catch (SQLException e) {
            manager.writeMessage(e, "fetchUsername");
        } finally {
            manager.closeResSet(res);
            manager.closeStatement(prepStmt);
            manager.closeConnection(con);
        }
        return username;
    }


    public String fetchEmail() {
        String email = "";
        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {

            String prepString = "select distinct email from usr where email = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setString(1, user.getEmail());
            res = prepStmt.executeQuery();
            while (res.next()){
                email  += res.getString("email");
            }
        } catch (SQLException e) {
            manager.writeMessage(e, "fetchEmail");
        } finally {
            manager.closeResSet(res);
            manager.closeStatement(prepStmt);
            manager.closeConnection(con);
        }
        return email;
    }

    public int fetchLevel() {
        int level = 0;
        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        try {

            String prepString = "select distinct [level] from usr where [level] = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, user.getLevel());
            res = prepStmt.executeQuery();
            while(res.next()){
                level  += res.getInt("level");
            }
        } catch (SQLException e) {
            manager.writeMessage(e, "fetchLevel");
        } finally {
            manager.closeResSet(res);
            manager.closeStatement(prepStmt);
            manager.closeConnection(con);
        }
        return level;
    }

    public boolean registerUser(User user) {
        if(userExist(user.getUser_id()))
            return false;
        openConnection();
        PreparedStatement prepStmt = null;
        try {
            String prepString = "INSERT INTO usr VALUES(?, ?, DEFAULT, DEFAULT, ?, ?)";
            prepStmt = con.prepareStatement(prepString);
            prepStmt.setInt(1, user.getUser_id());
            prepStmt.setString(2, user.getUsername());
            prepStmt.setString(3, user.getEmail());
            prepStmt.setString(4, String.valueOf(user.getPassword()));
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            manager.writeMessage(e, "registerUser");
            return false;
        } finally {
            manager.closeStatement(prepStmt);
            manager.closeConnection(con);
            return true;
        }
    }

    public boolean userExist(int userId){
        this.openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean userExists = false;
        try{
            String prepString = "SELECT user_id FROM usr WHERE user_id = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, userId);
            res = prepStmt.executeQuery();
            userExists = res.next();

        }
        catch (SQLException e){
            manager.writeMessage(e, "userExist");
            return false;
        } finally {
            manager.closeResSet(res);
            manager.closeStatement(prepStmt);
            manager.closeConnection(con);
            return userExists;
        }
    }
}
