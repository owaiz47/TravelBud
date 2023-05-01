package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE plan SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Plan extends AbstractPersistentObject implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Date startDate;
	
	private Double budget;
	
	@Column(nullable = false, length = 2555, updatable = false)
	private String details;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USER_ID")
	private User planBy;
	
	@OneToMany(mappedBy="plan", fetch = FetchType.EAGER)
	private Set<JoinedUser> joinedBy;
	
	@OneToMany(mappedBy="plan", fetch = FetchType.EAGER)
	private Set<RequestedUser> joinRequests;
	
	@OneToMany(mappedBy="plan", fetch = FetchType.EAGER)
	private List<Place> visitingPlaces;
	
	private String planURL;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Double getBudget() {
		return budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public User getPlanBy() {
		return planBy;
	}

	public void setPlanBy(User planBy) {
		this.planBy = planBy;
	}

	

	public Set<JoinedUser> getJoinedBy() {
		return joinedBy;
	}

	public void setJoinedBy(Set<JoinedUser> joinedBy) {
		this.joinedBy = joinedBy;
	}

	public Set<RequestedUser> getJoinRequests() {
		return joinRequests;
	}

	public void setJoinRequests(Set<RequestedUser> joinRequests) {
		this.joinRequests = joinRequests;
	}

	

	public List<Place> getVisitingPlaces() {
		return visitingPlaces;
	}

	public void setVisitingPlaces(List<Place> visitingPlaces) {
		this.visitingPlaces = visitingPlaces;
	}

	public String getPlanURL() {
		return planURL;
	}

	public void setPlanURL(String planURL) {
		this.planURL = planURL;
	}

	public Plan(Date startDate, Double budget, String details, User planBy, Set<JoinedUser> joinedBy,
			Set<RequestedUser> joinRequests, List<Place> visitingPlaces, String planURL) {
		super();
		this.startDate = startDate;
		this.budget = budget;
		this.details = details;
		this.planBy = planBy;
		this.joinedBy = joinedBy;
		this.joinRequests = joinRequests;
		this.visitingPlaces = visitingPlaces;
		this.planURL = planURL;
	}

	public Plan() {
	}
	
	public Plan(long id) {
		this.setId(id);
	}

	public Plan(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
	

	
}
