package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.travelbud.dto.ReportType;

@Entity
public class Report extends AbstractPersistentObject implements Serializable{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
    @JoinColumn(name="REPORTED_BY_USER_ID")
	private User reportedBy;
	
	private ReportType type;
	
	private long referenceId;
	
	private String description;

	public User getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(User reportedBy) {
		this.reportedBy = reportedBy;
	}

	public ReportType getType() {
		return type;
	}

	public void setType(ReportType type) {
		this.type = type;
	}

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Report(User reportedBy, ReportType type, long referenceId, String description) {
		super();
		this.reportedBy = reportedBy;
		this.type = type;
		this.referenceId = referenceId;
		this.description = description;
	}

	public Report() {
	}

	public Report(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
	
	
}
