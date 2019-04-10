package database;
import java.sql.*;

public class ManageConnection {

    public void closeRes(ResultSet res) {
        try {
            if (res != null) {
                res.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeResSet()");
        }
    }

    public void closePrepStmt(PreparedStatement prepStmt) {
        try {
            if (prepStmt != null) {
                prepStmt.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeStatement()");
        }
    }

    public void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeConnection()");
        }
    }

    public void writeMessage(Exception e, String message) {
        System.err.println("*** Fail happened: " + message + ". ***");
        e.printStackTrace(System.err);
    }

    public void turnOnAutoCommit(Connection con){
        try{
            con.setAutoCommit(true);
        }
        catch(SQLException sq){
            sq.printStackTrace();
            System.out.println("SQL-exception: " + sq);
        }
    }

    public void rollback(Connection con){
        try{
            con.rollback();
        }
        catch(SQLException sq){
            sq.printStackTrace();
            System.out.println("SQL-exception: " + sq);
        }
    }
}

