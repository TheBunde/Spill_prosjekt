import java.awt.event.*;
import java.sql.*;
public class Log_in {
    private Connection connection;
    private Statement st;
    private ResultSet res;

    private Log_in(){
        // her skal vi åpne kobling til database.


    }

    public void connect(){
        try{
            String driver ="mysql-ait.stud.idi.ntnu.no ";
            Class.forName(driver);

            String db ="g_tdat1006_01";
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}
