package com.travelbud.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class LastOnline {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", unique = true)
	private User user;

	private Date lastOnline;

	@Transient
	private boolean active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getLastOnline() {
		return lastOnline;
	}

	public void setLastOnline(Date lastOnline) {
		this.lastOnline = lastOnline;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LastOnline(boolean active, User user) {
		this.active = active;
		this.user = user;
	}

	public LastOnline(Long id, User user, Date lastOnline, boolean active) {
		super();
		this.id = id;
		this.user = user;
		this.lastOnline = lastOnline;
		this.active = active;
	}

	public LastOnline() {
	}

}
