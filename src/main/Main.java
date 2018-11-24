package main;

import database.Database;
import menu.Menu;

public class Main {

	public static void main(String[] args) {

		Database individualDb = new Database();
		/*
		 * try { Class.forName("com.mysql.jdbc.Driver"); } catch (ClassNotFoundException
		 * e) { e.printStackTrace(); }
		 */
		individualDb.connect();
		Menu myMenu = new Menu(individualDb);
		myMenu.runMenu();
	}
}
