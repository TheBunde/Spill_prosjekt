package AccountDetails;

import java.sql.*;

public class AccountDetailsDatabase {

    private Connection con;
    private String url;
    private String password;

    private ManageConnection manager;
    private User user;
    private Player player;


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

    public void closeConnection() {
        manager.closeConnection(con);
    }



    public String fetchUsername() {
        String username = "";

        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean ok = false;
        try {
            String prepString = "select distinct username from [user] where username = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setString(1, user.getUsername());
            res = prepStmt.executeQuery();

            username  = res.getString(username);
            ok = res.next();
            return username;

        } catch (SQLException sq) {
            sq.printStackTrace();
        } finally {
            manager.closeResSet(res);
            manager.closeStatement(prepStmt);
            manager.closeConnection(con);
        }
        return null;
    }


    public String fetchEmail() {
        String email = "";

        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean ok = false;
        try {

            String prepString = "select distinct email from [user] where email = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setString(1, user.getEmail());
            res = prepStmt.executeQuery();

            email  = res.getString(email);
            ok = res.next();
            return email;

        } catch (SQLException sq) {
            sq.printStackTrace();
        } finally {
            manager.closeResSet(res);
            manager.closeStatement(prepStmt);
            manager.closeConnection(con);
        }
        return null;
    }

    public int fetchLevel() {
        int level = 0;

        openConnection();
        PreparedStatement prepStmt = null;
        ResultSet res = null;
        boolean ok = false;
        try {

            String prepString = "select distinct [level] from player where [level] = ?";
            prepStmt = this.con.prepareStatement(prepString);
            prepStmt.setInt(1, player.getLevel());
            res = prepStmt.executeQuery();

            level  = res.getInt(level);
            ok = res.next();
            return level;

        } catch (SQLException sq) {
            sq.printStackTrace();
        } finally {
            manager.closeResSet(res);
            manager.closeStatement(prepStmt);
            manager.closeConnection(con);
        }
        return -1;
    }
}
