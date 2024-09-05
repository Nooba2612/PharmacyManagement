package pharmacy.daos;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import pharmacy.connections.DatabaseConnection;
import pharmacy.models.UserModel;

public class UserDAO {

	// create new user
	public boolean createUser(UserModel user) {
		String query = "INSERT INTO Users (user_id, username, password, full_name, role, email, phone_number, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, user.getUserId());
			statement.setString(2, user.getUsername());
			statement.setString(3, user.getPassword());
			statement.setString(4, user.getFullName());
			statement.setString(5, user.getRole());
			statement.setString(6, user.getEmail());
			statement.setString(7, user.getPhoneNumber());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// retrieve a user by username
	public UserModel getUserByUsername(String username) {
	    String query = "SELECT * FROM Users WHERE username = ?";

	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {

	        statement.setString(1, username);

	        ResultSet rs = statement.executeQuery();
	        if (rs.next()) {
	            // Handle null values for created_at and updated_at safely
	            LocalDateTime createdAt = rs.getTimestamp("created_at") != null 
	                ? rs.getTimestamp("created_at").toLocalDateTime() 
	                : null;
	            LocalDateTime updatedAt = rs.getTimestamp("updated_at") != null 
	                ? rs.getTimestamp("updated_at").toLocalDateTime() 
	                : null;

	            return new UserModel(
	                rs.getString("user_id"), 
	                rs.getString("username"), 
	                rs.getString("password"),
	                rs.getString("full_name"), 
	                rs.getString("role"), 
	                rs.getString("email"),
	                rs.getString("phone_number"), 
	                createdAt, 
	                updatedAt
	            );
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	// update a user's information
	public boolean updateUser(UserModel user) {
		String query = "UPDATE Users SET full_name = ?, email = ?, phone_number = ?, updated_at = GETDATE() WHERE user_id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(query)) {

			statement.setString(1, user.getFullName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPhoneNumber());
			statement.setString(4, user.getUserId());

			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Method to delete a user by user ID
	public boolean deleteUser(String userId) {
		String query = "DELETE FROM Users WHERE user_id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(query)) {

			statement.setString(1, userId);
			int result = statement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Method to get a list of all users
	public List<UserModel> getAllUsers() {
		List<UserModel> users = new ArrayList<>();
		String query = "SELECT * FROM Users";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(query)) {

			while (rs.next()) {
				UserModel user = new UserModel(rs.getString("user_id"), rs.getString("username"),
						rs.getString("password_hash"), rs.getString("full_name"), rs.getString("role"),
						rs.getString("email"), rs.getString("phone_number"),
						rs.getTimestamp("created_at").toLocalDateTime(),
						rs.getTimestamp("updated_at").toLocalDateTime());
				users.add(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
}
