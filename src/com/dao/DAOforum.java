package com.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.models.Message;
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

	public int authentication(String uName, String uPass) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		try {
			pS = (PreparedStatement) con
					.prepareStatement("select * from usersdb where user_name=? and password_hash=?");
			pS.setString(1, uName);
			pS.setString(2, uPass);
			rS = pS.executeQuery();

			if (rS.next()) {
				return Integer.parseInt(rS.getString("user_id"));
			} else {
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
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
				Message ms = new Message(rS.getString("message_id"), rS.getString("text"), rS.getString("created"));
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

	public boolean addMessage(String text, int user_id) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		int updated = 0;
		Message ms;
		try {
			String insert = "Insert into message (text, created) values(?, ?)";
			pS = (PreparedStatement) con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
			ms = new Message(text);
			pS.setString(1, ms.getText());
			pS.setString(2, ms.getCreatedFormated());
			updated = pS.executeUpdate();
			rS = pS.getGeneratedKeys();
			rS.next();  
			int message_id = rS.getInt(1);
			System.out.println("Added message with id: "+message_id);

			insert = "Insert into allowed_messages (user_id, message_id) values(?, ?)";
			pS = (PreparedStatement) con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
			pS.setString(1, Integer.toString(user_id));
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

}
