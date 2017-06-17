package com.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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
			// jezeli login i haslo sie zgadzaja
			if (rS.next()) {

				user = refreshedLoggedUserByName(uName);
				System.out.println("Correctly logged user " + user.getUser_name());
				con.close();
				return user;

				// jezeli nie zgadza sie haslo
			} else {
				UserDB checkedUser = refreshedLoggedUserByName(uName);
				// jezeli nie przekroczono maksymalnej liczby blednych hasel
				if (checkedUser != null && checkedUser.getUsedLoginAttempts() < checkedUser.getMaxLoginAttempts()) {
					String update_used_login_attempts = "UPDATE usersdb SET used_login_attempts=used_login_attempts+1,"
							+ "last_invalid_login=? WHERE user_name=?";
					pS = (PreparedStatement) con.prepareStatement(update_used_login_attempts);
					pS.setString(1, UserDB.ft.format(new Date()));
					pS.setString(2, uName);
					pS.executeUpdate();
					System.out.println("Invalid attempt, login_attempts increased");
					// jezeli osiagnieto maksymalna liczbe nieudanych logowan
				} else if (checkedUser != null) {
					String last_invalid_login = "Select last_invalid_login from usersdb WHERE user_name=?";
					pS = (PreparedStatement) con.prepareStatement(last_invalid_login);
					pS.setString(1, uName);
					rS = pS.executeQuery();
					System.out.println("Overload invalid attempts");
				} else {
					System.out.println("Choosen user not exist");
				}

				con.close();

				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean logout(UserDB user, Date last_login) {
		connect();
		PreparedStatement pS;
		String update = "UPDATE usersdb SET used_login_attempts=0, last_login=? WHERE user_name=?";
		try {
			pS = (PreparedStatement) con.prepareStatement(update);
			pS.setString(1, UserDB.ft.format(last_login));
			pS.setString(2, user.getUser_name());
			pS.executeUpdate();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("Correctly loged out " + user.getUser_name() + ", used login attempts reset");
		return true;
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
			System.out.println("Added message with id: " + message_id);

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

	public boolean deleteMessage(String message_id) {
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

			System.out.println("Deleted message with id: " + message_id + " /tDeleted from message: " + delFromMessage
					+ "/tDeleted from alleowed: " + delFromAllowed);
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
		try {
			String update = "UPDATE message SET text=? WHERE message_id=?";
			pS = (PreparedStatement) con.prepareStatement(update);
			pS.setString(1, text);
			pS.setString(2, message_id);
			updateFromMessage = pS.executeUpdate();

			System.out.println("Updated message with id: " + message_id + " \tUpdated text message: " + text);
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean setMaxAttempts(int user_id, String max_attempts) {
		connect();
		PreparedStatement pS;

		try {
			String update = "UPDATE usersdb SET max_login_attempts=? WHERE user_id=?";
			pS = (PreparedStatement) con.prepareStatement(update);
			pS.setString(1, max_attempts);
			pS.setString(2, Integer.toString(user_id));
			pS.executeUpdate();

			System.out.println("Updated attempts to: " + max_attempts + " \tfor user with id: " + user_id);
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public UserDB refreshedLoggedUser(String user_id) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		UserDB user;
		try {
			pS = (PreparedStatement) con.prepareStatement("select * from usersdb where user_id=?");
			pS.setString(1, user_id);
			rS = pS.executeQuery();

			if (rS.next()) {
				String user_name = rS.getString("user_name");
				String permissions_id = rS.getString("permissions_id");
				String last_login = rS.getString("last_login");
				String last_invalid_login = rS.getString("last_invalid_login");
				String max_login_attempts = rS.getString("max_login_attempts");
				String used_login_attempts = rS.getString("used_login_attempts");

				user = new UserDB(user_id, user_name, permissions_id, last_login, last_invalid_login,
						max_login_attempts, used_login_attempts);
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

	public UserDB refreshedLoggedUserByName(String user_name) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		UserDB user;
		try {
			pS = (PreparedStatement) con.prepareStatement("select * from usersdb where user_name=?");
			pS.setString(1, user_name);
			rS = pS.executeQuery();

			if (rS.next()) {
				String user_id = rS.getString("user_id");
				user_name = rS.getString("user_name");
				String permissions_id = rS.getString("permissions_id");
				String last_login = rS.getString("last_login");
				String last_invalid_login = rS.getString("last_invalid_login");
				String max_login_attempts = rS.getString("max_login_attempts");
				String used_login_attempts = rS.getString("used_login_attempts");

				user = new UserDB(user_id, user_name, permissions_id, last_login, last_invalid_login,
						max_login_attempts, used_login_attempts);
				return user;
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
