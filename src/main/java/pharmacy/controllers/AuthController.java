package pharmacy.controllers;

import java.sql.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pharmacy.connections.DatabaseConnection;
import pharmacy.daos.*;
import pharmacy.models.*;
import pharmacy.utils.PasswordUtil;

public class AuthController {
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultSet = null;
	private boolean isAuthenticated = false;

	public AuthController() throws SQLException {
		connection = DatabaseConnection.getConnection();
	}

	public boolean authenticateUser(String username, String password) {
		UserDAO userDAO = new UserDAO();
		UserModel user = userDAO.getUserByUsername(username);

		System.out.println("\n\nUser Found: " + user);

		if (user != null) {
			String storedPassword = user.getPassword();
			if (PasswordUtil.checkPassword(password, storedPassword)) {
				isAuthenticated = true;
			}
		}

		return isAuthenticated;
	}
}