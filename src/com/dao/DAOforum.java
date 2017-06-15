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

	public boolean authentication(String uName, String uPass) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		try {
			pS = (PreparedStatement) con.prepareStatement("select * from usersdb where user_name=? and password_hash=?");
			pS.setString(1, uName);
			pS.setString(2, uPass);
			rS = pS.executeQuery();

			if (rS.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Message> getAllMessages(){
		connect();
		ArrayList<Message> messages = new ArrayList<>();
		Statement st;
		try {
			st = (Statement) con.createStatement();

		ResultSet rS;
		rS = st.executeQuery("select * from message;");
		while(rS.next()){
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

}
