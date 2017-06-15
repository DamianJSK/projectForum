package com.models;

public class Message {

	private int message_id;
	private String text;
	
	public Message() {
		super();
	}
	
	public Message(int message_id, String text) {
		super();
		this.message_id = message_id;
		this.text = text;
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
	
	
	
}
