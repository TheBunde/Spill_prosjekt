package AccountDetails;
import java.sql.*;

public class ManageConnection {

    public static void closeResSet(ResultSet res) {
        try {
            if (res != null) {
                res.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeResSet()");
        }
    }

    public static void closeStatement(Statement stm) {
        try {
            if (stm != null) {
                stm.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeStatement()");
        }
    }

    public static void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            writeMessage(e, "closeConnection()");
        }
    }

    public static void writeMessage(Exception e, String message) {
        System.err.println("*** Fail happened: " + message + ". ***");
        e.printStackTrace(System.err);
    }
}

