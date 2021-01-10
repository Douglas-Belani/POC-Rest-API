package dao.connection.database;

import dao.connection.DatabaseConnection;
import org.apache.commons.dbcp2.BasicDataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLPoolConnection implements DatabaseConnection {

    private static BasicDataSource dataSource;

    public MySQLPoolConnection() {

    }

    public Connection getDatabaseConnection() {
       if (dataSource == null) {
           Properties properties = getMySQLDatabaseProperties();

           dataSource = new BasicDataSource();
           dataSource.setDriverClassName(properties.getProperty("driverClassName"));
           dataSource.setUrl(properties.getProperty("dburl"));
           dataSource.setUsername(properties.getProperty("username"));
           dataSource.setPassword(properties.getProperty("password"));
           dataSource.setMinIdle(8);
           dataSource.setMaxIdle(8);
           dataSource.setMaxTotal(8);
       }
       try {
           return dataSource.getConnection();

       } catch (SQLException e) {
           e.printStackTrace();
           return null;
       }
    }

    private static Properties getMySQLDatabaseProperties() {
        try {
            InputStream is = MySQLPoolConnection.class.getResourceAsStream("/MySQL.properties");
            Properties properties = new Properties();
            properties.load(is);

            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
