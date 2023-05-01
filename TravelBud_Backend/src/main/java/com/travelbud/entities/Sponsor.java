package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Sponsor extends AbstractPersistentObject implements Serializable{
	private static final long serialVersionUID = 1L;

	private Double budget;
	
	@ManyToOne(optional = false)
    @JoinColumn(name="SPONSORED_BY_USER_ID")
	private User sponsoredBy;
	
	@ManyToOne(optional = false)
    @JoinColumn(name="SPONSORED_TO_USER_ID")
	private User sponsoredTo;
	
	private String details;

	public Double getBudget() {
		return budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public User getSponsoredBy() {
		return sponsoredBy;
	}

	public void setSponsoredBy(User sponsoredBy) {
		this.sponsoredBy = sponsoredBy;
	}

	public User getSponsoredTo() {
		return sponsoredTo;
	}

	public void setSponsoredTo(User sponsoredTo) {
		this.sponsoredTo = sponsoredTo;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Sponsor(Double budget, User sponsoredBy, User sponsoredTo, String details) {
		super();
		this.budget = budget;
		this.sponsoredBy = sponsoredBy;
		this.sponsoredTo = sponsoredTo;
		this.details = details;
	}

	public Sponsor() {
	}

	public Sponsor(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
	
	
	
}
