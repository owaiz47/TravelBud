package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Comment extends AbstractPersistentObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "POST_ID")
	private Post post;
	
	private String comment;
	
	private boolean deleted;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USER_ID")
	private User commentedBy;

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public User getCommentedBy() {
		return commentedBy;
	}

	public void setCommentedBy(User commentedBy) {
		this.commentedBy = commentedBy;
	}

	public Comment(Post post, String comment, boolean deleted, User commentedBy) {
		super();
		this.post = post;
		this.comment = comment;
		this.deleted = deleted;
		this.commentedBy = commentedBy;
	}

	public Comment() {
	}

	public Comment(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}
	
	
}
