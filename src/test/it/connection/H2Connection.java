package connection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class H2Connection {

    private static Properties getH2DatabaseProperties() {
        try(InputStream is = H2Connection.class.getResourceAsStream("/h2.properties")) {
            Properties properties = new Properties();
            properties.load(is);

            return properties;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Connection getH2DatabaseConnection() {
        try {
            Properties properties = getH2DatabaseProperties();
            Connection conn = DriverManager.getConnection(properties.getProperty("url"), properties);

            return conn;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
