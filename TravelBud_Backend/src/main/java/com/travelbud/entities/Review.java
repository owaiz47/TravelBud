package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE post SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Review extends AbstractPersistentObject implements Serializable{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "plan_id", referencedColumnName = "id")
	private Plan plan;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User reviewedBy;
	
	private Double stars;
	
	private String review;

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public User getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(User reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	public Double getStars() {
		return stars;
	}

	public void setStars(Double stars) {
		this.stars = stars;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public Review(Plan plan, User reviewedBy, Double stars, String review) {
		super();
		this.plan = plan;
		this.reviewedBy = reviewedBy;
		this.stars = stars;
		this.review = review;
	}

	public Review() {
	}

	public Review(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
	
	
	
}
