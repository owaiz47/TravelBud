package com.travelbud.dto;

import java.util.Date;
import java.util.List;

import com.travelbud.entities.Message;
import com.travelbud.entities.User;

public class MessageInfo {
	private User user;
	private List<Message> messages;
	private String lastMsg;
	private Date lastMsgOn;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	public String getLastMsg() {
		return lastMsg;
	}
	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}
	public Date getLastMsgOn() {
		return lastMsgOn;
	}
	public void setLastMsgOn(Date lastMsgOn) {
		this.lastMsgOn = lastMsgOn;
	}
	public MessageInfo(User user, List<Message> messages, String lastMsg, Date lastMsgOn) {
		super();
		this.user = user;
		this.messages = messages;
		this.lastMsg = lastMsg;
		this.lastMsgOn = lastMsgOn;
	}
	public MessageInfo() {
	}
	
	
}
