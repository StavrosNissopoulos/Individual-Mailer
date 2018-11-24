package menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;

import users.User;
import users.UserA;
import users.UserAdmin;
import users.UserB;
import users.UserC;

public class Menu {

	Database myDb;
	User activeUser;

	public Menu() {
	}

	public Menu(Database db) {
		myDb = db;
	}

	boolean exitWelcomeMenu = false;

	public void runMenu() {
		System.out.println("Hello and welcome to my Individual program");
		System.out.println("It is a project for the 6th AFDEMP Bootcamp held in 2018");
		System.out.println("My name is Stavros and we are gonna have a blast!");
		System.out.println("Through the program you can exchange messages and interact with other users");
		System.out.println("Depending on your privileges you can perform different tasks\n");

		while (!exitWelcomeMenu) {
			printWelcomeMenu();
			int choice = getInputMenu(2);
			welcomeMenu(choice);
		}
	}

	private void printWelcomeMenu() {
		System.out.println("*---------------------------------------*");
		System.out.println("|------------** Main Menu **------------|");
		System.out.println("|                                       |");
		System.out.println("|  Enter 1 to log in with your account  |");
		System.out.println("|  Enter 2 to register a new account    |");
		System.out.println("|  Enter 0 to exit the program          |");
		System.out.println("|                                       |");
		System.out.println("*---------------------------------------*");
	}

	private int getInputMenu(int range) {
		int choice = -1;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		do {
			try {
				choice = Integer.parseInt(br.readLine());
				if (choice < 0 || choice > range) {
					System.out.println("Please enter one number between 0 and " + range);
				}
			} catch (NumberFormatException | IOException e) {
				System.out.println("\nPlease enter a valid input. A number between 0 and " + range);
			}
		} while (choice < 0 || choice > range);
		return choice;
	}

	private void welcomeMenu(int choice) {
		switch (choice) {
		case 0:
			System.out.println("Program is being terminated");
			System.out.println("Thank you and have a good day!");
			exitWelcomeMenu = true;
			break;
		case 1:
			System.out.println("\nPlease enter your username and password to log into your account");
			int userType = loginLookUp();
			if (userType == -1) {
				System.out.println("\nFailed to log in");
				System.out.println("Please try again\n");
			} else {
				userMenu(userType);
			}
			break;
		case 2:
			System.out.println("\nYou can create a new account with level 1 privileges");
			System.out.println("You will be able to view your messages and send messages to other users");
			System.out.println("For more information please contact the admin at tzatziki@mail.com\n");
			registerUser();
			break;
		}
	}

	private void userAMenu() {
		int choice;
		do {
			System.out.println("\nPress 1 to send a new message");
			System.out.println("Press 2 to view your messages");
			System.out.println("Press 0 to exit to main menu");
			choice = getInputMenu(2);
			switch (choice) {
			case (1):
				activeUser.sendMessage();
				break;
			case (2):
				activeUser.readMessages();
				break;
			}
		} while (choice != 0);
		clearScreen();
	}

	private void userBMenu() {
		int choice;
		do {
			System.out.println("\nPress 1 to send a new message");
			System.out.println("Press 2 to view and edit your messages");
			System.out.println("Press 0 to exit to main menu");
			choice = getInputMenu(2);
			switch (choice) {
			case (1):
				activeUser.sendMessage();
				break;
			case (2):
				activeUser.readMessages();
				break;
			}
		} while (choice != 0);
		clearScreen();
	}

	private void userCMenu() {
		int choice;
		do {
			System.out.println("\nPress 1 to send a new message");
			System.out.println("Press 2 to view,edit or delete your messages");
			System.out.println("Press 0 to exit to main menu");
			choice = getInputMenu(2);
			switch (choice) {
			case (1):
				activeUser.sendMessage();
				break;
			case (2):
				activeUser.readMessages();
				break;
			}
		} while (choice != 0);
		clearScreen();
	}

	private void userAdminMenu() {
		int choice;
		do {
			System.out.println("\nPress 1 to send a new message");
			System.out.println("Press 2 to view all the messages in the system including the deleted ones");
			System.out.println("Press 3 to manage the users");
			System.out.println("Press 0 to exit to main menu");
			choice = getInputMenu(3);
			switch (choice) {
			case (1):
				activeUser.sendMessage();
				break;
			case (2):
				activeUser.readMessages();
				break;
			case (3):
				activeUser.editUsers();
				break;
			}
		} while (choice != 0);
		clearScreen();
	}

	private void userMenu(int userType) {
		switch (userType) {
		case (1):
			userAMenu();
			break;
		case (2):
			userBMenu();
			break;

		case (3):
			userCMenu();
			break;

		case (4):
			userAdminMenu();
			break;
		}
	}

	public int loginLookUp() {
		int userTypeReturned = -1;
		String lookupQuery = "Select * from individual.user;";
		String username = null;
		String password = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Enter your username");
			username = br.readLine();
			System.out.println("Enter your password");
			password = MaskedPassword.readAPassword();
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		try {
			myDb.stmt = myDb.connection.createStatement();
			ResultSet rs = myDb.stmt.executeQuery(lookupQuery);
			while (rs.next()) {
				String dbUsername = rs.getString("username");
				String dbPassword = rs.getString("password");
				int dbUserId = rs.getInt("user_id");
				int dbUserType = rs.getInt("user_type");
				if (username.equals(dbUsername) && password.equals(dbPassword)) {
					userTypeReturned = dbUserType;
					switch (dbUserType) {
					case (1):
						activeUser = new UserA(dbUserId, dbUsername, dbPassword, dbUserType, myDb);
						clearScreen();
						System.out.println("\nYou logged in as a user with level 1 privileges");
						break;
					case (2):
						activeUser = new UserB(dbUserId, dbUsername, dbPassword, dbUserType, myDb);
						clearScreen();
						System.out.println("\nYou logged in as a user with level 2 privileges");
						break;
					case (3):
						activeUser = new UserC(dbUserId, dbUsername, dbPassword, dbUserType, myDb);
						clearScreen();
						System.out.println("\nYou logged in as a user with level 3 privileges");
						break;
					case (4):
						activeUser = new UserAdmin(dbUserId, dbUsername, dbPassword, dbUserType, myDb);
						clearScreen();
						System.out.println("\nYou logged in as admin/superuser");
						break;
					}
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}
		return userTypeReturned;
	}

	private void registerUser() {
		System.out.println("\nThe username you will enter is gonna be your name in the program");
		System.out.println("You will be able to send messages to other users by knowing their name\n");
		String password1 = null;
		String password2 = null;
		String username = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("---------------------------------------------------");
		System.out.println("Username can contain any character except spaces");
		System.out.println("Username must start with a letter");
		System.out.println("Username must be at least 5 and up to 18 characters");
		System.out.println("---------------------------------------------------");
		System.out.println("Password can contain only letters and digits");
		System.out.println("Password must be at least 6 and up to 18 characters");
		System.out.println("Password must contain at least one digit");
		System.out.println("---------------------------------------------------\n");
		System.out.println("Please enter a username for your account");
		try {
			username = br.readLine();
			System.out.println("Now enter your password");
			password1 = MaskedPassword.readAPassword();
			System.out.println("Enter you password again");
			password2 = MaskedPassword.readAPassword();
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		if (password1.equals(password2)) {
			if (Menu.checkPassword(password1) && Menu.checkUsername(username)) {
				boolean enteredException = false;
				String query = "INSERT INTO individual.user (username,password,user_type)\r\n" + "values('" + username
						+ "','" + password1 + "',1);";
				try {
					myDb.stmt = myDb.connection.createStatement();
					myDb.stmt.executeUpdate(query);
				} catch (SQLException e) {
					enteredException = true;
					e.printStackTrace(System.out);
				}
				if (!enteredException) {
					System.out.println("\n@@------------------------------------------------------------------@@");
					System.out.println("|                     Account created succesfully                     |");
					System.out.println("|You can use the amazing features of the program with your account now|");
					System.out.println("@@------------------------------------------------------------------@@\n");
				}
			}
		} else {
			System.out.println("\nPasswords didn't match");
			System.out.println("Please try again\n");
		}
	}

	public static boolean checkPassword(String password) {
		boolean flag = true;
		int digitCounter = 0;
		if (password.length() < 6) {
			flag = false;
			System.out.println("\nPassword shouldn't have less than 6 characters");
		}
		if (password.length() > 18) {
			flag = false;
			System.out.println("\nPassword shouldn't have more than 18 characters");
		}
		char ch;
		for (int i = 0; i < password.length(); i++) {
			ch = password.charAt(i);
			if (ch == ' ' || Character.isWhitespace(ch)) {
				flag = false;
				System.out.println("\nPassword shouldn't include any spaces");
			}
			if (!Character.isLetterOrDigit(ch)) {
				flag = false;
				System.out.println("\nPassword should have only numbers and digits");
			}
			if (Character.isDigit(ch)) {
				digitCounter++;
			}
		}
		if (digitCounter == 0) {
			flag = false;
			System.out.println("\nPassword must have at least one number");
		}
		System.out.println("\n");
		return flag;
	}

	public static boolean checkUsername(String username) {
		boolean flag = true;
		if (username.length() < 5) {
			flag = false;
			System.out.println("\nUsername shouldn't have less than 5 characters");
		}
		if (username.length() > 18) {
			flag = false;
			System.out.println("\nUsername shouldn't have more than 18 characters");
		}
		if (!Character.isLetter(username.charAt(0))) {
			flag = false;
			System.out.println("\nUsername can only start with a letter");
		}
		for (int i = 0; i < username.length(); i++) {
			char ch = username.charAt(i);
			if (Character.isWhitespace(ch) || ch == ' ') {
				flag = false;
				System.out.println("\nUsername shouldn't contain any spaces");
			}
		}
		System.out.println("\n");
		return flag;
	}

	public static void clearScreen() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace(System.out);
		}
	}

}
