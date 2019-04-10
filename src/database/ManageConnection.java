package database;
import java.sql.*;

/**
 * ManageConnection.java
 * This program is inspired from the class Opprydder.java from the book "Programmering i Java", 4.version
 * The program manage the use of database
 * @author saramoh
 */


public class ManageConnection {
    
      /**
     * Close the ResultSet, the method writeMessage() is used if SQLException is thrown
     * @param res an instance of ResultSet
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
     * Close the PreparedStatement, the method writeMessage() is used if SQLException is thrown
     * @param prepStmt an instance of PreparedStatement
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
     * Close the Connection, the method writeMessage() is used if SQLException is thrown
     * @param con an instance of Connection
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
     * The method is an auxiliary method to other methods
     * @param e an instance of Exception
     * @param message is the head of the target method
     */


    public void writeMessage(Exception e, String message) {
        System.err.println("*** Fail happened: " + message + ". ***");
        e.printStackTrace(System.err);
    }
  /**
     * Turn on AutoCommit after finishing the transaction
     * @param con an instance of Connection
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
     * Roll back the transaction when something goes wrong
     * @param con an instance of Connection
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

