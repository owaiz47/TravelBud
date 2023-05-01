package com.travelbud.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Rating extends AbstractPersistentObject {
	private static final long serialVersionUID = 1L;

	private long userId;
	
	private long postId;

	private double stars;


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

	public double getStars() {
		return stars;
	}

	public void setStars(double stars) {
		this.stars = stars;
	}

	public Rating(Long id, Date createdOn, Date lastModifiedOn, long userId, long postId, double stars) {
		super(id, createdOn, lastModifiedOn);
		this.userId = userId;
		this.postId = postId;
		this.stars = stars;
	}

	public Rating() {
	}

	public Rating(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
	
	

}
