package peaksoft.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConfig {
    private static final String url = "jdbc:postgresql://localhost:5432/jdbcc";
    private static final String username = "postgres";
    private static final String password = "1234";
    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url,username,password);
//            System.out.println("Application is successfully ta database!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } return connection;
    }

}
