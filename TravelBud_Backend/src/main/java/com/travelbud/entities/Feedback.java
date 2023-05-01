package com.travelbud.entities;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Feedback extends AbstractPersistentObject{
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String name;
	private String message;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Feedback(Long id, Date createdOn, Date lastModifiedOn, String email, String name, String message) {
		super(id, createdOn, lastModifiedOn);
		this.email = email;
		this.name = name;
		this.message = message;
	}
	public Feedback() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Feedback(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
	
	

}
