package com.models;

public class UserDB {

	private int user_id;
	private String user_name;
	private int persmissions_id;
	private String password;
	
	
	public UserDB() {
		super();
	}


	public UserDB(int user_id, String user_name, int persmissions_id, String password) {
		super();
		this.user_id = user_id;
		this.user_name = user_name;
		this.persmissions_id = persmissions_id;
		this.password = password;
	}


	public int getUser_id() {
		return user_id;
	}


	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}


	public String getUser_name() {
		return user_name;
	}


	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}


	public int getPersmissions_id() {
		return persmissions_id;
	}


	public void setPersmissions_id(int persmissions_id) {
		this.persmissions_id = persmissions_id;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
