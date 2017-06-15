package com.dao;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class DAOforum {

	Connection con = null;
	
	public void connect(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
