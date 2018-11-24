package users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import database.Database;
import menu.Menu;

public class UserA extends User {

	public UserA() {
	}

	public UserA(int id, String usename, String passw, int type, Database myDb) {
		super(id, usename, passw, type, myDb);
	}

	public void readMessages() {
		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<String> messages = new ArrayList<String>();
		ArrayList<String> sender = new ArrayList<String>();
		ArrayList<Integer> messageId = new ArrayList<Integer>();
		ArrayList<Timestamp> timestamp = new ArrayList<Timestamp>();
		ArrayList<Integer> messageState = new ArrayList<Integer>();
		ArrayList<String> messageStateStringed = new ArrayList<String>();
		int tempSender;
		String query = "SELECT * FROM individual.message\r\n" + "where receiver like " + user_id
				+ " AND message_state NOT LIKE 0 order by message_id desc;";
		try {
			myDb.stmt = myDb.connection.createStatement();
			ResultSet rs = myDb.stmt.executeQuery(query);
			while (rs.next()) {
				titles.add(rs.getString("title"));
				messages.add(rs.getString("text"));
				tempSender = rs.getInt("sender");
				sender.add(myDb.userIdToUsername(tempSender));
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
				System.out.println((i + 1) + ") Title:{" + titles.get(i) + "} ||| Send by: " + sender.get(i) + " ||| "
						+ messageStateStringed.get(i));
			}
			System.out.println("\nPlease enter the number of the message you want to read or 0 to go back");
			messageSelected = getMessageInputZero(titles.size());
			if (messageSelected != 0) {
				System.out.println("\n----------Send By----------");
				System.out.println(sender.get((messageSelected - 1)));
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
					System.out.println("\nPress 1 to reply to this message");
					System.out.println("Press 0 to go to the previous menu\n");
					choice = getMessageInputZero(1);
					if (choice == 1) {
						replyToMessage(myDb.usernameToUserId(sender.get((messageSelected - 1))));
					}
				} while (choice != 0);
			}
		}
		Menu.clearScreen();
	}

}
