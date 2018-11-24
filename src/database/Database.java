package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	public Connection connection;
	public Statement stmt;
	public DriverManager dm;
	public PreparedStatement pstmt;

	/*State 0: deleted
	 *State 1 : unread
	 *State 2 : read */
	
	public java.sql.Connection connect() {
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/individual?autoReconnect=true&useSSL=false", "root", "sudo");
			//couldn't make it work with greek chars
			//tried in the connection jdbc:mysql://localhost:3306/individual?useUnicode=yes&characterEncoding=UTF-8" 
			//came to conclusion that is the console-cmd problem because everything is utf8.database+inputstream+connection
			stmt = connection.createStatement();
			stmt.executeQuery("SET NAMES 'UTF8'");
			stmt.executeQuery("SET CHARACTER SET 'UTF8'");
			return connection;
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			System.out.println("Problem conecting with the database");
			return null;
		}
	}

	public static void closeDbConnection(Connection conn) throws SQLException {
		conn.close();
	}

	public boolean foundReceiver(String username) {
		boolean foundReceiver = false;
		String query = "Select * From individual.user where username like '" + username + "';";
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String dbUsername = rs.getString("username");
				if (dbUsername.equals(username)) {
					foundReceiver = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}
		if (!foundReceiver) {
			System.out.println("\nPlease enter a name that exists");
		}
		return foundReceiver;
	}

	public void editMessage(int messageId) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String query = "UPDATE individual.message SET text = ? where message_id = ?";
		boolean flagException = false;
		try {
			String newText = br.readLine();
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, newText);
			pstmt.setInt(2, messageId);
			pstmt.executeUpdate();
		} catch (IOException | SQLException e) {
			e.printStackTrace(System.out);
			flagException = true;
		}
		if (!flagException) {
			System.out.println("\n||    Message updated succesfully    ||");
		}
	}
	public void changeMessageState(int messageId,int state) {
		String query = "UPDATE individual.message SET message_state = ? where message_id = ?";
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, state);
			pstmt.setInt(2, messageId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}
	}

	public int usernameToUserId(String username) {
		int userId = -1;
		String query = "Select * From individual.user where username like '" + username + "';";
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				userId = rs.getInt("user_id");
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}
		return userId;
	}

	public String userIdToUsername(int userId) {
		String username = null;
		String query = "Select * From individual.user where user_id like " + userId + ";";
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				username = rs.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}
		return username;
	}
}
