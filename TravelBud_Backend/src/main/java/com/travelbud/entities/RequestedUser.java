package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "plan_requested_by")
@Where(clause = "accepted=false and rejected=false")
public class RequestedUser extends AbstractPersistentObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false)   
	@JoinColumn(name = "plan_id")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Plan plan;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	private boolean accepted = false;
	
	private boolean rejected = false;

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	public RequestedUser(Plan  plan, User user, boolean accepted, boolean rejected) {
		super();
		this.plan = plan;
		this.user = user;
		this.accepted = accepted;
		this.rejected = rejected;
	}

	public RequestedUser() {
	}
	
	public RequestedUser(RequestedUser reqUser) {
		super(reqUser.getId(), reqUser.getCreatedOn(), reqUser.getLastModifiedOn());
		this.plan = reqUser.plan;
		this.user = reqUser.user;
		this.accepted = reqUser.accepted;
		this.rejected = reqUser.rejected;
	}

	public RequestedUser(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}

	
}
