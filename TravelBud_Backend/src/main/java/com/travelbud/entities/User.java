package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.travelbud.dto.Gender;

@Entity
@Table(name = "spy")
public class User extends AbstractPersistentObject implements Serializable{
	private static final long serialVersionUID = 1L;
	public static String CURRENT_USER = "CURRENT_USER_ID";

	@Column(unique = true, nullable = false, length = 50)
	private String username;

	@Column(unique = true, nullable = false, length = 225, updatable = false)
	private String email;

	@Column(nullable = false, length = 225)	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@Column(nullable = false, length = 225)
	private String fullname;

	@Column(nullable = false, length = 450)
	private String dp;

	@Column(length = 20)
	private String mobile;

	@Column(length = 5)
	private Gender gender;

	@Column(length = 5)
	private Boolean donation = false;

	@Column(length = 450)
	private String profileURL;

	@Transient
	private Double postRating;
	
	@Transient
	private Double planRating;
	
	@Transient
	private int followerCount;

	@Transient
	private int followingCount;
	
	@Transient
	private boolean isFollowingUser;

	@PostLoad
	public void fillTransients() {
		/*
		 * this.planRating = 5.0; this.postRating = 4.5; this.followerCount = 20;
		 * this.isFollowingUser = false;
		 */
	}

	/*
	 * @PrePersist
	 * 
	 * @PreUpdate void preInsert() { if (donation == null) donation = true; }
	 */

	public User(String username, String email, String password, String fullname, String dp, String mobile, Gender gender,
			Boolean donation, String profileURL, Double postRating, Double planRating, int followerCount,
			int followingCount, boolean isFollowingUser) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.fullname = fullname;
		this.dp = dp;
		this.mobile = mobile;
		this.gender = gender;
		this.donation = donation;
		this.profileURL = profileURL;
		this.postRating = postRating;
		this.planRating = planRating;
		this.followerCount = followerCount;
		this.followingCount = followingCount;
		this.isFollowingUser = isFollowingUser;
	}

	public User() {
	}

	public User(Long id, Date createdOn, Date lastModifiedOn) {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getDp() {
		return dp;
	}

	public void setDp(String dp) {
		this.dp = dp;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Boolean isDonation() {
		return donation;
	}

	public void setDonation(Boolean donation) {
		this.donation = donation;
	}

	public String getProfileURL() {
		return profileURL;
	}

	public void setProfileURL(String profileURL) {
		this.profileURL = profileURL;
	}

	public Double getPostRating() {
		return postRating;
	}

	public void setPostRating(Double postRating) {
		this.postRating = postRating;
	}

	public Double getPlanRating() {
		return planRating;
	}

	public void setPlanRating(Double planRating) {
		this.planRating = planRating;
	}

	public int getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}

	public int getFollowingCount() {
		return followingCount;
	}

	public void setFollowingCount(int followingCount) {
		this.followingCount = followingCount;
	}

	public boolean isFollowingUser() {
		return isFollowingUser;
	}

	public void setFollowingUser(boolean isFollowingUser) {
		this.isFollowingUser = isFollowingUser;
	}

}
