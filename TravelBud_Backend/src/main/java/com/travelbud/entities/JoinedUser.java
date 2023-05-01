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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "plan_joined_by")
public class JoinedUser extends AbstractPersistentObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)   
	@JoinColumn(name = "plan_id")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Plan plan;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	private boolean planConfirmed = false;

	private boolean planExitReq = false;

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

	public boolean isPlanConfirmed() {
		return planConfirmed;
	}

	public void setPlanConfirmed(boolean planConfirmed) {
		this.planConfirmed = planConfirmed;
	}

	public boolean isPlanExitReq() {
		return planExitReq;
	}

	public void setPlanExitReq(boolean planExitReq) {
		this.planExitReq = planExitReq;
	}

	public JoinedUser(Plan plan, User user, boolean planConfirmed, boolean planExitReq) {
		super();
		this.plan = plan;
		this.user = user;
		this.planConfirmed = planConfirmed;
		this.planExitReq = planExitReq;
	}

	public JoinedUser() {
	}

	public JoinedUser(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}

}
