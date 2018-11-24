package users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import database.Database;

public abstract class User {

	int user_id;
	String username;
	String password;
	int user_type;
	Database myDb;

	public User() {
	}

	public User(int user_id, String username, String password, int user_type, Database myDb) {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.user_type = user_type;
		this.myDb = myDb;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUser_type() {
		return user_type;
	}

	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}

	public Database getMyDb() {
		return myDb;
	}

	public void setMyDb(Database myDb) {
		this.myDb = myDb;
	}

	protected int getMessageInput(int range) {
		int choice = -1;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (choice < 1 || choice > range) {
			try {
				choice = Integer.parseInt(br.readLine());
				if (choice < 1 || choice > range) {
					System.out.println("Please enter one number between 1 and " + range);
				}
			} catch (NumberFormatException | IOException e) {
				System.out.println("Please enter a valid input. A number between 1 and " + range);
			}
		}
		return choice;
	}

	protected int getMessageInputZero(int range) {
		int choice = -1;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (choice < 0 || choice > range) {
			try {
				choice = Integer.parseInt(br.readLine());
				if (choice < 0 || choice > range) {
					System.out.println("Please enter one number between 0 and " + range);
				}
			} catch (NumberFormatException | IOException e) {
				System.out.println("Please enter a valid input. A number between 0 and " + range);
			}
		}
		return choice;
	}

	public void replyToMessage(int receiverId) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String title = "";
		String message = "";
		String query;
		System.out.println("\nPlease enter a title for your message");
		System.out.println("Title should contain up to 25 characters");
		do {
			try {
				title = br.readLine();
			} catch (IOException e) {
				e.printStackTrace(System.out);
			}
			if (title.length() > 25) {
				System.out.println("Please enter a title that doesn't exceed 25 characters in total");
			}
		} while (title.length() > 25);
		System.out.println("\nEnter a message that has up to 250 characters");
		do {
			try {
				message = br.readLine();
			} catch (IOException e) {
				e.printStackTrace(System.out);
			}
			if (message.length() > 250) {
				System.out.println("Your message was bigger than 250 characters.Please rewrite a smaller message");
			}
		} while (message.length() > 250);
		boolean messageSent = true;
		query = "insert into individual.message (sender,receiver,title,text)\r\n" + "values(" + user_id + ","
				+ receiverId + ",?,?);";
		try {
			myDb.pstmt = myDb.connection.prepareStatement(query);
			myDb.pstmt.setString(1, title);
			myDb.pstmt.setString(2, message);
			myDb.pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(System.out);
			messageSent = false;
		}
		if (messageSent) {
			System.out.println("\nMessage was sent succesfully\n");
		} else {
			System.out.println("\nThere was a problem while sending the message.Please try again\n");
		}
	}

	public void sendMessage() {
		BufferedReader br=null;
		try {
			br = new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String title = "";
		String receiver = null;
		String message = "";
		String query;
		System.out.println("\nEnter the name of the user who is gonna receive your message");
		do {
			try {
				receiver = br.readLine();
			} catch (IOException e) {
				//e.printStackTrace(System.out);
			}
		} while (!myDb.foundReceiver(receiver) && !receiver.equals("0"));
		if (!receiver.equals("0")) {
			System.out.println("\nPlease enter a title for your message");
			System.out.println("Title should contain up to 25 characters");
			do {
				try {
					title = br.readLine();
				} catch (IOException e) {
					e.printStackTrace(System.out);
				}
				if (title.length() > 25) {
					System.out.println("Please enter a title that doesn't exceed 25 characters in total");
				}
			} while (title.length() > 25);
			System.out.println("\nEnter a message that has up to 250 characters");
			do {
				try {
					message = br.readLine();
				} catch (IOException e) {
					e.printStackTrace(System.out);
				}
				if (message.length() > 250) {
					System.out.println("Your message was bigger than 250 characters.Please rewrite a smaller message");
				}
			} while (message.length() > 250);
			boolean messageSent = true;
			query = "INSERT INTO individual.message (sender,receiver,title,text)\r\n" + "values(" + user_id + ","
					+ myDb.usernameToUserId(receiver) + ",?,?);";
			try {
				myDb.pstmt = myDb.connection.prepareStatement(query);
				myDb.pstmt.setString(1, title);
				myDb.pstmt.setString(2, message);
				myDb.pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace(System.out);
				messageSent = false;
			}
			if (messageSent) {
				System.out.println("\n!!!--------------------------------------------------------!!!");
				System.out.println("         Your message was sent to " + receiver + " succesfully");
				System.out.println("!!!--------------------------------------------------------!!!\n");
				//write to logfile
				writeMessageToFile(title, message, receiver);
			} else {
				System.out.println("\nThere was a problem sending the message.Please try again");
			}
		}
	}

	// overriden by admin user
	public void editUsers() {}

	public abstract void readMessages();

	private void writeMessageToFile(String title,String message,String receiver) {
		FileWriter fileWriter=null;
		BufferedWriter bfWriter=null;
		PrintWriter writer=null;
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String sender = userIdToUsername(user_id);
        try {
        	fileWriter = new FileWriter("../Logs/messageFile.txt",true);
        	bfWriter = new BufferedWriter(fileWriter);
			writer = new PrintWriter(bfWriter);
			writer.println("Datetime: {"+timeStamp+"} || Sender: {"+sender+"} || Receiver: {"+receiver+"} || Title: {"+title+"} || Message: {"+message+"}");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
        finally {
        	try{
                if( fileWriter != null ){
                   fileWriter.close();
                }else if( bfWriter != null ){
                   bfWriter.close();
                }
                else if( writer != null ){
                  writer.close();
                }
             }
             catch( IOException e ){
            	 e.printStackTrace(System.out);
             }
        }
	}
	
	public String userIdToUsername(int userId) {
		String username = null;
		String query = "Select * From individual.user where user_id like " + userId + ";";
		try {
			myDb.stmt = myDb.connection.createStatement();
			ResultSet rs = myDb.stmt.executeQuery(query);
			while (rs.next()) {
				username = rs.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}
		return username;
	}
}
