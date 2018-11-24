package users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import database.Database;
import menu.MaskedPassword;
import menu.Menu;

public class UserAdmin extends User {

	public UserAdmin(int id, String usename, String passw, int type, Database myDb) {
		super(id, usename, passw, type, myDb);
	}

	public void readMessages() {
		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<String> messages = new ArrayList<String>();
		ArrayList<String> sender = new ArrayList<String>();
		ArrayList<String> receiver = new ArrayList<String>();
		ArrayList<Integer> messageId = new ArrayList<Integer>();
		ArrayList<Timestamp> timestamp = new ArrayList<Timestamp>();
		ArrayList<Integer> messageState = new ArrayList<Integer>();
		ArrayList<String> messageStateStringed = new ArrayList<String>();
		int tempSender;
		int tempReceiver;
		String query = "SELECT * FROM individual.message order by message_id desc;";

		try {
			myDb.stmt = myDb.connection.createStatement();
			ResultSet rs = myDb.stmt.executeQuery(query);
			while (rs.next()) {
				titles.add(rs.getString("title"));
				messages.add(rs.getString("text"));
				tempSender = rs.getInt("sender");
				sender.add(myDb.userIdToUsername(tempSender));
				tempReceiver = rs.getInt("receiver");
				receiver.add(myDb.userIdToUsername(tempReceiver));
				messageId.add(rs.getInt("message_id"));
				timestamp.add(rs.getTimestamp("message_time"));
				messageState.add(rs.getInt("message_state"));
				if (rs.getInt("message_state") == 0) {
					messageStateStringed.add("Deleted message");
				} else if (rs.getInt("message_state") == 1) {
					messageStateStringed.add("Unread message");
				} else if (rs.getInt("message_state") == 2) {
					messageStateStringed.add("Read message");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}
		System.out.println("\nYou got " + titles.size() + " messages\n");
		int messageSelected;
		if (titles.size() != 0) {
			for (int i = 0; i < titles.size(); i++) {
				System.out.println((i + 1) + ") Title:{" + titles.get(i) + "} ||| Send by: " + sender.get(i)
						+ " ||| Send to: " + receiver.get(i) + " ||| " + messageStateStringed.get(i));
			}
			System.out.println("\nPlease enter the number of the message you want to read,edit,delete or 0 to go back");
			messageSelected = getMessageInputZero(titles.size());
			if (messageSelected != 0) {
				System.out.println("\n----------Send By----------");
				System.out.println(sender.get((messageSelected - 1)));
				System.out.println("----------Send To----------");
				System.out.println(receiver.get((messageSelected - 1)));
				System.out.println("-------------On------------");
				// just removing the last two chars from the timestamp with substring
				System.out.println(timestamp.get((messageSelected - 1)).toString().substring(0,
						timestamp.get((messageSelected - 1)).toString().length() - 2));
				System.out.println("-------Message Title-------");
				System.out.println(titles.get((messageSelected - 1)));
				System.out.println("-------Message Text--------");
				System.out.println(messages.get((messageSelected - 1)));
				System.out.println("---------------------------");
				// changing the message's state to read
				myDb.changeMessageState(messageId.get(messageSelected - 1), 2);
				int choice;
				do {
					System.out.println("\nPress 1 to edit this message");
					System.out.println("Press 2 to delete this message");
					System.out.println("Press 0 to go to the previous menu\n");
					choice = getMessageInputZero(2);
					if (choice == 1) {
						System.out.println("Enter the new text of the message\n");
						myDb.editMessage(messageId.get(messageSelected - 1));
					} else if (choice == 2) {
						deleteInbox(messageId.get(messageSelected - 1));
						// Make choice 0 to avoid being able to delete the deleted message over and over
						choice = 0;
					}
				} while (choice != 0);
			}
		}
		Menu.clearScreen();
	}

	// admin executes a real delete and not just changing the state of the message
	private void deleteInbox(int messageId) {
		String query = "DELETE from individual.message WHERE message_id=" + messageId + ";";
		boolean deleteDone = true;
		try {
			myDb.stmt = myDb.connection.createStatement();
			myDb.stmt.executeUpdate(query);
		} catch (SQLException e) {
			deleteDone = false;
			e.printStackTrace(System.out);
		}
		if (!deleteDone) {
			System.out.println("\nA problem occurred while deleting the message");
		} else {
			System.out.println("\n---------------------------");
			System.out.println("Message deleted succesfully");
			System.out.println("---------------------------\n");
		}
	}

	public void editUsers() {
		System.out.println("\nPress 1 to create a new user");
		System.out.println("Press 2 to view,edit or delete users");
		System.out.println("Press 0 to go to previous menu");
		int choice = getMessageInputZero(2);
		switch (choice) {
		case (1):
			createUser();
			break;
		case (2):
			viewAllUsers();
			break;
		}
	}

	private void viewAllUsers() {
		ArrayList<Integer> userId = new ArrayList<Integer>();
		ArrayList<String> username = new ArrayList<String>();
		ArrayList<String> password = new ArrayList<String>();
		ArrayList<Integer> userType = new ArrayList<Integer>();
		String query = "SELECT * FROM individual.user;";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String newUsername = "";
		String newPassword1 = "";
		String newPassword2 = "";
		int newUserType;
		try {
			myDb.stmt = myDb.connection.createStatement();
			ResultSet rs = myDb.stmt.executeQuery(query);
			while (rs.next()) {
				userId.add(rs.getInt("user_id"));
				username.add(rs.getString("username"));
				password.add(rs.getString("password"));
				userType.add(rs.getInt("user_type"));
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}
		System.out.println("\nThere are: " + userId.size() + " users in the system\n");
		int userSelected;
		if (userId.size() != 0) {
			// adding spaces so the print looks nicer
			StringBuilder userPrint = new StringBuilder();
			for (int i = 0; i < userId.size(); i++) {
				userPrint.delete(0, 18);
				userPrint.append(username.get(i));
				userPrint.append("             ", 0, 18 - username.get(i).length());
				System.out.println((i + 1) + ") Privileges level:{ " + userType.get(i) + " } User:{ " + userPrint
						+ " } " + "|| Password:{ " + password.get(i) + " }");
			}
			System.out.println("\nPlease enter the number of the user you want to edit or 0 to go back");
			userSelected = getMessageInputZero(userId.size());
			if (userSelected != 0) {
				System.out.println("Press 1 to edit the user: " + username.get(userSelected - 1));
				System.out.println("Press 2 to delete the user: " + username.get(userSelected - 1)
						+ " and all his inbox messages");
				int choice = getMessageInputZero(2);
				if (choice == 1) {
					do {
						System.out.println("Enter the new username for the user or 0 to not change it");
						try {
							newUsername = br.readLine();
						} catch (IOException e) {
							e.printStackTrace(System.out);
						}
					} while (!Menu.checkUsername(newUsername) && !newUsername.equals("0"));
					if (newUsername.equals("0")) {
						newUsername = username.get(userSelected - 1);
					}
					boolean flagPassword;
					do {
						flagPassword = false;
						System.out.println("Enter a new password for the account or 0 to not change it");
						newPassword1 = MaskedPassword.readAPassword();
						if (!newPassword1.equals("0")) {
							System.out.println("Enter the password again");
							newPassword2 = MaskedPassword.readAPassword();
							if (newPassword1.equals(newPassword2) && Menu.checkPassword(newPassword1)) {
								flagPassword = true;
							}
							if (!newPassword1.equals(newPassword2)) {
								System.out.println("\nPasswords didn't match");
								System.out.println("Please try again\n");
							}
						} else {
							flagPassword = true;
						}
					} while (!flagPassword);
					if (newPassword1.equals("0")) {
						newPassword1 = password.get(userSelected - 1);
					}
					System.out.println(
							"\nEnter a number from 1 to 4 to change the level of privileges for the user or 0 to not change");
					newUserType = getMessageInputZero(4);
					if (newUserType == 0) {
						newUserType = userType.get(userSelected - 1);
					}
					String query2 = "UPDATE individual.user SET username = ?,password = ?,user_type = ? where user_id = ?";
					int userIdToChange = userId.get(userSelected - 1);
					boolean flagException = false;
					try {
						myDb.pstmt = myDb.connection.prepareStatement(query2);
						myDb.pstmt.setString(1, newUsername);
						myDb.pstmt.setString(2, newPassword1);
						myDb.pstmt.setInt(3, newUserType);
						myDb.pstmt.setInt(4, userIdToChange);
						myDb.pstmt.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace(System.out);
						flagException = true;
					}
					if (!flagException) {
						System.out.println("\n||    User updated succesfully    ||");
					}
				} else if (choice == 2) {
					boolean deleteIt = false;
					System.out.println("\nThe user and all his messages will be gone for ever and ever");
					System.out.println("Are you sure?");
					System.out.println("Enter Y/N");
					String areYouSure = null;
					try {
						areYouSure = br.readLine();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					if (areYouSure.equalsIgnoreCase("y")) {
						deleteIt = true;
					}else {
						System.out.println("\nUser managed to escape!");
						System.out.println("Try again to catch him");
					}
					if (deleteIt) {
						int userToDelete = userId.get(userSelected - 1);
						String query3 = "DELETE from individual.user WHERE user_id=" + userToDelete + ";";
						boolean deleteDone = true;
						try {
							myDb.stmt = myDb.connection.createStatement();
							myDb.stmt.executeUpdate(query3);
						} catch (SQLException e) {
							deleteDone = false;
							e.printStackTrace(System.out);
						}
						if (!deleteDone) {
							System.out.println("\nA problem occurred while deleting the user");
						} else {
							System.out.println("\n-----------------------------------------");
							System.out.println("User and his messages deleted permanently");
							System.out.println("-----------------------------------------\n");
						}
					}
				}
			}
		}
	}

	private void createUser() {
		String password1 = null;
		String password2 = null;
		String newUsername = null;
		int userType = -1;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nEnter a username for the new account");
		try {
			newUsername = br.readLine();
			System.out.println("\nNow enter a password for the account");
			password1 = MaskedPassword.readAPassword();
			System.out.println("\nEnter the password again");
			password2 = MaskedPassword.readAPassword();
			System.out.println("\nEnter the level of privileges the user should have");
			System.out.println("From 1 for the simplest user up to 4 for an admin user");
			userType = getMessageInput(4);
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		if (password1.equals(password2) && userType != -1) {
			if (Menu.checkPassword(password1) && Menu.checkUsername(newUsername)) {
				boolean enteredException = false;
				String query = "INSERT INTO individual.user (username,password,user_type)\r\n" + "values('"
						+ newUsername + "','" + password1 + "'," + userType + ");";
				try {
					myDb.stmt = myDb.connection.createStatement();
					myDb.stmt.executeUpdate(query);
				} catch (SQLException e) {
					enteredException = true;
					e.printStackTrace(System.out);
				}
				if (!enteredException) {
					System.out.println("@@------------------------------------------------------------------@@");
					System.out.println("|                     Account created succesfully                     |");
					System.out.println("@@------------------------------------------------------------------@@\n");
				}
			} else {
				System.out.println("Please enter valid username or password");
			}
		} else {
			System.out.println("\nPasswords didn't match");
			System.out.println("Please try again\n");
		}
	}
}
