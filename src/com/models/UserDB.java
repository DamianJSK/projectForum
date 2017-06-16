package com.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDB {

	private int user_id;
	private String user_name;
	private int persmissions_id;
	private Date lastLoginDate;
    private SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
	
	public UserDB() {
		super();
	}


	public UserDB(String user_id, String user_name, String persmissions_id, String date) {
		super();
		this.user_id = Integer.parseInt(user_id);
		this.user_name = user_name;
		this.persmissions_id =  Integer.parseInt(persmissions_id);
	    try {
			this.lastLoginDate = ft.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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


	public Date getLastLoginDate() {
		return lastLoginDate;
	}


	public void setLastLoginDate(Date date) {
		this.lastLoginDate = date;
	}
	
	
	
}
