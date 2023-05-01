package com.travelbud.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Follow extends AbstractPersistentObject{
	private static final long serialVersionUID = 1L;

	private long userId;
	
	private long followedById;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getFollowedById() {
		return followedById;
	}

	public void setFollowedById(long followedById) {
		this.followedById = followedById;
	}

	public Follow(Long id, Date createdOn, Date lastModifiedOn, long userId, long follwedByUserId) {
		super(id, createdOn, lastModifiedOn);
		this.userId = userId;
		this.followedById = follwedByUserId;
	}

	public Follow() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Follow(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
	
	public Follow(long userId, long follwedByUserId) {
		super();
		this.userId = userId;
		this.followedById = follwedByUserId;
	}
}
