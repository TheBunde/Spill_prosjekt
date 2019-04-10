package database;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Uses the Apache Commons library to create a connection pool<br>
 * @see <a href="http://commons.apache.org/">Apache Commons</a>
 *
 * @author williad
 */
public class DataSource {
    private BasicDataSource bds = new BasicDataSource();

    /**
     * Private constructor to assure no instantiation
     */
    private DataSource(){
        bds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        bds.setUrl("jdbc:mysql://mysql-ait.stud.idi.ntnu.no:3306/g_tdat1006_01");
        bds.setUsername("g_tdat1006_01");
        bds.setPassword("q8CeXgyy");
        bds.setMinIdle(5);
        bds.setMaxIdle(15);
        bds.setMaxOpenPreparedStatements(100);
        bds.setInitialSize(15);
    }

    private static class DataSourceInstance{
        private static final DataSource INSTANCE = new DataSource();
    }

    /**
     *
     * @return an instance of the DataSource
     */
    public static DataSource getInstance(){
        return DataSourceInstance.INSTANCE;
    }

    /**
     *
     * @return the BasicDataSource object
     */
    public BasicDataSource getBds(){
        return bds;
    }

}
