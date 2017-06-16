package com.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable{

	private int message_id;
	private String text;
	private Date created;
    private SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
	
	public Message() {
		super();
	}
	
	
	public Message(String text) {
		super();
		this.text=text;
		this.created = new Date();
	}
	
	public Message(String message_id, String text, String created) {
		super();
		this.message_id = Integer.parseInt(message_id);
		this.text = text;
		try {
			this.created = ft.parse(created);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getMessage_id() {
		return message_id;
	}

	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreated() {
		return created;
	}
	
	public String getCreatedFormated() {
		return ft.format(created);
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "Message [message_id=" + message_id + ", text=" + text + ", created=" + ft.format(created) + "]";
	}
	
	
	
}
