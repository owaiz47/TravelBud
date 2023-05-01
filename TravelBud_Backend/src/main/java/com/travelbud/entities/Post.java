package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Where(clause = "deleted=false")
public class Post extends AbstractPersistentObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User postedBy;
    
	@Column(nullable = false, length = 225, updatable = false)
    private String postTitle;
    
	@Column(nullable = false, length = 225, updatable = false)
    private String postDesc;
    
    private String coverImageURL;
    
    @Transient
    private boolean userLiked;
    
    @OneToMany(mappedBy="post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PostImage> postImages;
    
    @Transient 
    public List<DonationDetails> donationDetails;
    
    @Transient
    private Double userRating;
    
    private Long planId;
    
    private String mediaURL;

	public User getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(User postedBy) {
		this.postedBy = postedBy;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getPostDesc() {
		return postDesc;
	}

	public void setPostDesc(String postDesc) {
		this.postDesc = postDesc;
	}

	public String getCoverImageURL() {
		return coverImageURL;
	}

	public void setCoverImageURL(String coverImageURL) {
		this.coverImageURL = coverImageURL;
	}

	public boolean isUserLiked() {
		return userLiked;
	}

	public void setUserLiked(boolean userLiked) {
		this.userLiked = userLiked;
	}

	public List<PostImage> getPostImages() {
		return postImages;
	}

	public void setPostImages(List<PostImage> postImages) {
		this.postImages = postImages;
	}

	public List<DonationDetails> getDonationDetails() {
		return donationDetails;
	}

	public void setDonationDetails(List<DonationDetails> donationDetails) {
		this.donationDetails = donationDetails;
	}

	public Double getUserRating() {
		return userRating;
	}

	public void setUserRating(Double userRating) {
		this.userRating = userRating;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlan(Long planId) {
		this.planId = planId;
	}

	public String getMediaURL() {
		return mediaURL;
	}

	public void setMediaURL(String mediaURL) {
		this.mediaURL = mediaURL;
	}

	public Post(User postedBy, String postTitle, String postDesc, String coverImageURL, boolean userLiked,
			List<PostImage> postImages, List<DonationDetails> donationDetails, Double userRating, Long planId,
			String mediaURL) {
		super();
		this.postedBy = postedBy;
		this.postTitle = postTitle;
		this.postDesc = postDesc;
		this.coverImageURL = coverImageURL;
		this.userLiked = userLiked;
		this.postImages = postImages;
		this.donationDetails = donationDetails;
		this.userRating = userRating;
		this.planId = planId;
		this.mediaURL = mediaURL;
	}

	public Post() {
	}

	public Post(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
    
	public void addPhoneToImages() {
		if(this.postImages == null)return;
		for(PostImage pi : this.postImages) {
			pi.setPost(this);
		}
	}
    
    
}
