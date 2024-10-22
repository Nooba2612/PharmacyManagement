package pharmacy.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_URL = dotenv.get("DB_URL");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");
    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Driver not found");
        }

        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
