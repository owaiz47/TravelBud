package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class PostImage extends AbstractPersistentObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POST_ID")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Post post;
	
	@Column(nullable = false, length = 550, updatable = false)
	private String postImageDesc;
	
	private String postImageURL;
	
	private String mediaURL;

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public String getPostImageDesc() {
		return postImageDesc;
	}

	public void setPostImageDesc(String postImageDesc) {
		this.postImageDesc = postImageDesc;
	}

	public String getPostImageURL() {
		return postImageURL;
	}

	public void setPostImageURL(String postImageURL) {
		this.postImageURL = postImageURL;
	}

	public String getMediaURL() {
		return mediaURL;
	}

	public void setMediaURL(String mediaURL) {
		this.mediaURL = mediaURL;
	}

	public PostImage(Post post, String postImageDesc, String postImageURL, String mediaURL) {
		super();
		this.post = post;
		this.postImageDesc = postImageDesc;
		this.postImageURL = postImageURL;
		this.mediaURL = mediaURL;
	}

	public PostImage() {
	}

	public PostImage(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
	
	
}
