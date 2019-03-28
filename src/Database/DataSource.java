package Database;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;


public class DataSource {
    private BasicDataSource bds = new BasicDataSource();

    private DataSource(){
        bds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        bds.setUrl("jdbc:mysql://mysql-ait.stud.idi.ntnu.no:3306/g_tdat1006_01");
        bds.setUsername("g_tdat1006_01");
        bds.setPassword("q8CeXgyy");
        bds.setMinIdle(5);
        bds.setMaxIdle(10);
        bds.setMaxOpenPreparedStatements(100);
        bds.setInitialSize(10);
    }

    private static class DataSourceHolder{
        private static final DataSource INSTANCE = new DataSource();
    }

    public static DataSource getInstance(){
        return DataSourceHolder.INSTANCE;
    }

    public BasicDataSource getBds(){
        return bds;
    }

    public Connection getConnection() throws Exception {
        return getBds().getConnection();
    }
}
