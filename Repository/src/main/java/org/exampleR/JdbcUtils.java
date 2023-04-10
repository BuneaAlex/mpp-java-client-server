package org.exampleR;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class JdbcUtils {

    private String url;

    public JdbcUtils() {
        loadDBCredentials();
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Error getting connection " + e);
        }

        return connection;
    }

    private void loadDBCredentials() {

        Properties properties = new Properties();

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);

            url = properties.getProperty("jdbc.url");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
