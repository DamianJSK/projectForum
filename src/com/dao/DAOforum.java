package com.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.models.Message;
import com.models.UserDB;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class DAOforum {

	private static final DAOforum DAOFORUM = init();
	Connection con = null;

	private DAOforum() {

	}

	private static DAOforum init() {
		return new DAOforum();
	}

	public static DAOforum getDAOforum() {
		return DAOFORUM;
	}

	public void connect() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	public UserDB authentication(String uName, String uPass) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		UserDB user;
		try {
			pS = (PreparedStatement) con
					.prepareStatement("select * from usersdb where user_name=? and password_hash=?");
			pS.setString(1, uName);
			pS.setString(2, uPass);
			rS = pS.executeQuery();

			if (rS.next()) {
				String user_id = rS.getString("user_id");
				String user_name = rS.getString("user_name");
				String permissions_id = rS.getString("permissions_id");
				String last_login = rS.getString("last_login");
 
				user = new UserDB(user_id, user_name, permissions_id, last_login);
				return user;
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Message> getAllMessages() {
		connect();
		ArrayList<Message> messages = new ArrayList<>();
		Statement st;
		try {
			st = (Statement) con.createStatement();

			ResultSet rS;
			rS = st.executeQuery("select * from message;");
			while (rS.next()) {
				Message ms = new Message(rS.getString("message_id"), rS.getString("text"), rS.getString("created_by"), 
						rS.getString("created"), rS.getString("edited"));
				messages.add(ms);
				System.out.println(ms.toString());
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return messages;
	}

	public boolean addMessage(String text, UserDB logged_user) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		int updated = 0;
		Message ms;
		try {
			String insert = "Insert into message (text, created_by, created, edited) values(?, ?, ?, ?)";
			pS = (PreparedStatement) con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
			ms = new Message(text);
			pS.setString(1, ms.getText());
			pS.setString(2, Integer.toString(logged_user.getUser_id()));
			pS.setString(3, ms.getCreatedFormated());
			pS.setString(4, ms.getCreatedFormated());
			updated = pS.executeUpdate();
			rS = pS.getGeneratedKeys();
			rS.next();  
			int message_id = rS.getInt(1);
			System.out.println("Added message with id: "+message_id);

			insert = "Insert into allowed_messages (user_id, message_id) values(?, ?)";
			pS = (PreparedStatement) con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
			pS.setString(1, Integer.toString(logged_user.getUser_id()));
			pS.setString(2, Integer.toString(message_id));
			pS.executeUpdate();

			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		if (updated > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean deleteMessage(String message_id){
		connect();
		PreparedStatement pS;
		ResultSet rS;
		int delFromMessage, delFromAllowed;
		Message ms;
		try {
			String delete = "delete from message where message_id=?";
			pS = (PreparedStatement) con.prepareStatement(delete);
			pS.setString(1, message_id);
			delFromMessage = pS.executeUpdate();

			delete = "delete from allowed_messages where message_id=?";
			pS = (PreparedStatement) con.prepareStatement(delete);
			pS.setString(1, message_id);
			delFromAllowed = pS.executeUpdate();
			pS.executeUpdate();

			System.out.println("Deleted message with id: "+message_id+" /tDeleted from message: "+ delFromMessage+
					"/tDeleted from alleowed: "+ delFromAllowed);
			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean updateMessage(String text, String message_id) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		int updateFromMessage;
		Message ms;
		try {;
			String update = "UPDATE message SET text=? WHERE message_id=?";
			pS = (PreparedStatement) con.prepareStatement(update);
			pS.setString(1, text);
			pS.setString(2, message_id);
			updateFromMessage = pS.executeUpdate();


			System.out.println("Updated message with id: "+message_id+" \tUpdated text message: "+ text);
			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
