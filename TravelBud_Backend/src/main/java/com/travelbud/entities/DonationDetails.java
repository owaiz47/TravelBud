package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.travelbud.dto.DontaionAccountType;

@Entity
public class DonationDetails extends AbstractPersistentObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false)
    @JoinColumn(name="USER_ID")
	@JsonProperty(access = Access.WRITE_ONLY)
	private User user;
	
	private DontaionAccountType dontaionAccountType;
	private String accountInfo;
	
	@Transient
	private String imageUrl;

	public DontaionAccountType getDontaionAccountType() {
		return dontaionAccountType;
	}

	public void setDontaionAccountType(DontaionAccountType dontaionAccountType) {
		this.dontaionAccountType = dontaionAccountType;
	}

	public String getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(String accountInfo) {
		this.accountInfo = accountInfo;
	}

	public String getImageUrl() {
		return dontaionAccountType.getImage();
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DonationDetails(User user, DontaionAccountType dontaionAccountType, String accountInfo, String imageUrl) {
		super();
		this.user = user;
		this.dontaionAccountType = dontaionAccountType;
		this.accountInfo = accountInfo;
		this.imageUrl = imageUrl;
	}

	public DonationDetails() {
	}

	public DonationDetails(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}

	
	
	
}
