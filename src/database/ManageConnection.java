package database;
import java.sql.*;

/**
 * ManageConnection.java<br>
 * This program is inspired from the class Opprydder.java from the book "Programmering i Java", 4.version<br>
 * The program manage the use of database
 * @author saramoh
 */
public class ManageConnection {


    /**
     * Closes the ResultSet
     * @param res    an instance of ResultSet
     */
    public void closeRes(ResultSet res) {
        try {
            if (res != null) {
                res.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeResSet()");
        }
    }

    /**
     * Closes the PreparedStatement
     *
     * @param prepStmt    an instance of PreparedStatement
     */
    public void closePrepStmt(PreparedStatement prepStmt) {
        try {
            if (prepStmt != null) {
                prepStmt.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeStatement()");
        }
    }

    /**
     * Closes the Connection
     *
     * @param con    an instance of Connection
     */
    public void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeConnection()");
        }
    }
    
     /**
      * The method is an auxiliary method for other methods, it is used if the SQLException is thrown
      *
      * @param e          an instance of Exception
      * @param message    the head of the target method
      */
    public void writeMessage(Exception e, String message) {
        System.err.println("*** Fail happened: " + message + ". ***");
        e.printStackTrace(System.err);
    }

    /**
     * Turns on AutoCommit after finishing the transaction
     *
     * @param con    an instance of Connection
     */
    public void turnOnAutoCommit(Connection con){
        try{
            con.setAutoCommit(true);
        }
        catch(SQLException sq){
            sq.printStackTrace();
            System.out.println("SQL-exception: " + sq);
        }
    }

    /**
     * Rolls back the transaction when something goes wrong
     *
     * @param con    an instance of Connection
     */
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
