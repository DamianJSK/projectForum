package com.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDB {

	private int user_id;
	private String user_name;
	private int persmissions_id;
	private Date lastLoginDate;
	private Date lastInvalidLoginDate;
	private int maxLoginAttempts;
	private int usedLoginAttempts;
    public static SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
	
	public UserDB() {
		super();
	}


	public UserDB(String user_id, String user_name, String persmissions_id, String dateValidLogin, String dateInvalidLogin, 
			String maxLoginAttempts, String usedLoginAttempts) {
		super();
		this.user_id = Integer.parseInt(user_id);
		this.user_name = user_name;
		this.persmissions_id =  Integer.parseInt(persmissions_id);
	    try {
			this.lastLoginDate = ft.parse(dateValidLogin);
			this.lastInvalidLoginDate = ft.parse(dateInvalidLogin);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    this.maxLoginAttempts = Integer.parseInt(maxLoginAttempts);
	    this.usedLoginAttempts = Integer.parseInt(usedLoginAttempts);

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


	public Date getLastInvalidLoginDate() {
		return lastInvalidLoginDate;
	}


	public void setLastInvalidLoginDate(Date lastInvalidLoginDate) {
		this.lastInvalidLoginDate = lastInvalidLoginDate;
	}


	public int getMaxLoginAttempts() {
		return maxLoginAttempts;
	}


	public void setMaxLoginAttempts(int maxLoginAttempts) {
		this.maxLoginAttempts = maxLoginAttempts;
	}


	public int getUsedLoginAttempts() {
		return usedLoginAttempts;
	}


	public void setUsedLoginAttempts(int usedLoginAttempts) {
		this.usedLoginAttempts = usedLoginAttempts;
	}
	
	
	
}
