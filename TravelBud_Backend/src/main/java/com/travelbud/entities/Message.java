package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.travelbud.dto.MessageStatus;

@Entity
public class Message extends AbstractPersistentObject implements Serializable{
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "MESSAGE_FROM")
	private User messageFrom;
	
	@ManyToOne
	@JoinColumn(name = "MESSAGE_TO")
	private User messageTo;
	
	private String message;
	
	private boolean senderDeleted;
	
	private boolean receiverDeleted;
	
	@Transient
	private String tmpId;
	
	private MessageStatus msgStatus;

	public User getMessageFrom() {
		return messageFrom;
	}

	public void setMessageFrom(User messageFrom) {
		this.messageFrom = messageFrom;
	}

	public User getMessageTo() {
		return messageTo;
	}

	public void setMessageTo(User messageTo) {
		this.messageTo = messageTo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSenderDeleted() {
		return senderDeleted;
	}

	public void setSenderDeleted(boolean senderDeleted) {
		this.senderDeleted = senderDeleted;
	}

	public boolean isReceiverDeleted() {
		return receiverDeleted;
	}

	public void setReceiverDeleted(boolean receiverDeleted) {
		this.receiverDeleted = receiverDeleted;
	}
	
	public String getTmpId() {
		return tmpId;
	}

	public void setTmpId(String tmpId) {
		this.tmpId = tmpId;
	}

	public MessageStatus getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(MessageStatus msgStatus) {
		this.msgStatus = msgStatus;
	}

	public Message(User messageFrom, User messageTo, String message, boolean senderDeleted, boolean receiverDeleted) {
		super();
		this.messageFrom = messageFrom;
		this.messageTo = messageTo;
		this.message = message;
		this.senderDeleted = senderDeleted;
		this.receiverDeleted = receiverDeleted;
	}

	public Message() {
	}

	public Message(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
	
	
}
