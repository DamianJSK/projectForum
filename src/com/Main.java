package com;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class Main {

	public static void main(String[] args) {
		System.out.println("main class");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login",
					"root", "");

			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from usersdb");
			while(resultSet.next()){
				System.out.println(resultSet.getString(2)+"   "+resultSet.getString(4));
			}
			connection.close();
			
			PreparedStatement pS;
			ResultSet rS;
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "");
			pS = (PreparedStatement) con.prepareStatement("select * from usersdb where user_name=? and password_hash=?");
			pS.setString(1, "Test");
			pS.setString(2, "testtest");
			rS = pS.executeQuery();
			while(rS.next()){
				System.out.println(rS.getString("user_name")+"   "+rS.getString("password_hash"));
			}
			connection.close();

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
