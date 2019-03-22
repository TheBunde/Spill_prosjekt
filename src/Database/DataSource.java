package Database;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class DataSource {
    private static BasicDataSource ds;

    static{
        ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://mysql-ait.stud.idi.ntnu.no:3306/g_tdat1006_01");
        ds.setUsername("g_tdat1006_01");
        ds.setPassword("q8CeXgyy");
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    public static Connection getConnection(){
        Connection con = null;
        try {
            con = ds.getConnection();
            System.out.println("connection done");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return con;
        }
    }

    private DataSource(){}
}
