package com;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
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

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
