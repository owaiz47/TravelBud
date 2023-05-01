package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class ResetPassword extends AbstractPersistentObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private String code;
	private String email;
	@Transient
	private boolean isExpired = true;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isExpired() {
		if (new Date().getTime() - this.getCreatedOn().getTime() < 5 * 60 * 1000) {// 5Minutes
			return false;
		}
		return true;
	}

	public ResetPassword(Long id, Date createdOn, Date lastModifiedOn, String code, String email) {
		super(id, createdOn, lastModifiedOn);
		this.code = code;
		this.email = email;
	}

	public ResetPassword() {
		super();
	}

	public ResetPassword(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}

	public ResetPassword(String code, String email) {
		this.code = code;
		this.email = email;
	}

}
