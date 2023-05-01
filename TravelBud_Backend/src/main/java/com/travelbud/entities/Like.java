package com.travelbud.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Like extends AbstractPersistentObject{
	private static final long serialVersionUID = 1L;
	
	private long userId;
	
	private long postId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	public Like(Long id, Date createdOn, Date lastModifiedOn, long userId, long postId) {
		super(id, createdOn, lastModifiedOn);
		this.userId = userId;
		this.postId = postId;
	}

	public Like() {
	}

	public Like(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
	
	
}
