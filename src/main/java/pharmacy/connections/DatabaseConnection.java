package pharmacy.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
	private static final Dotenv dotenv = Dotenv.load();
	private static final String dbUrl = dotenv.get("DB_URL");
	private static final String dbUser = dotenv.get("DB_USER");
	private static final String dbPassoword = dotenv.get("DB_PASSWORD");

	public static Connection getConnection() throws SQLException {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SQLException("Driver not found");
		}

		return DriverManager.getConnection(dbUrl, dbUser, dbPassoword);
	}
}
