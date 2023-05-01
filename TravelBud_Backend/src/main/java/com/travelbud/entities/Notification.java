package com.travelbud.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travelbud.dto.NotificationType;
import com.travelbud.messages.NotificationMessage;

@Entity
public class Notification extends AbstractPersistentObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.REMOVE, targetEntity = User.class, optional = false)
	@JoinColumn(name = "notification_from", referencedColumnName = "id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User notificationFrom;

	@ManyToOne(cascade = CascadeType.REMOVE, targetEntity = User.class, optional = false)
	@JoinColumn(name = "notification_to", referencedColumnName = "id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User notificationTo;

	private NotificationType notificationType;

	private long referenceId;

	@Transient
	private String message;
	
	@Transient @JsonIgnore
	private String heading;
	
	@Transient @JsonIgnore
	private String picture;

	public User getNotificationFrom() {
		return notificationFrom;
	}

	public void setNotificationFrom(User notificationFrom) {
		this.notificationFrom = notificationFrom;
	}

	public User getNotificationTo() {
		return notificationTo;
	}

	public void setNotificationTo(User notificationTo) {
		this.notificationTo = notificationTo;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

	public String getMessage() {
		if(message != null)return message;
		
		if (this.notificationType == NotificationType.FOLLOW) {
			return NotificationMessage.FOLLOWING_MSG;
		} else if (this.notificationType == NotificationType.COMMENT) {
			return NotificationMessage.COMMENT_MSG;
		} else if (this.notificationType == NotificationType.LIKE) {
			return NotificationMessage.LIKE_MSG;
		} else if (this.notificationType == NotificationType.DONATE) {
			return NotificationMessage.DONATION_MSG;
		} else if (this.notificationType == NotificationType.RATING) {
			return NotificationMessage.RATING_MSG;
		} else if (this.notificationType == NotificationType.MESSAGE) {
			return NotificationMessage.MSG_MSG;
		} else if (this.notificationType == NotificationType.SPONSOR) {
			return NotificationMessage.SPONSOR_MSG;
		} else {
			return planMsg();
		}
	}

	private String planMsg() {
		if (this.notificationType == NotificationType.PLAN_COWAN_REQ) {
			return NotificationMessage.PLAN_COWAN_REQ;
		}else if(this.notificationType == NotificationType.PLAN_ACCEPT_COWNA) {
			return NotificationMessage.PLAN_COWAN_ACPT;
		}else if(this.notificationType == NotificationType.PLAN_REJECT_COWNA) {
			return NotificationMessage.PLAN_COWNAN_RJCT;
		}else if(this.notificationType == NotificationType.PLAN_JOIN_REQ) {
			return NotificationMessage.PLAN_JOIN_REQ;
		}else if(this.notificationType == NotificationType.PLAN_JOIN_ACPT) {
			return NotificationMessage.PLAN_JOIN_ACPT;
		}else if(this.notificationType == NotificationType.PLAN_JOIN_RJCT) {
			return NotificationMessage.PLAN_JOIN_RJCT;
		}else if(this.notificationType == NotificationType.PLAN_EXIT_REQ) {
			return NotificationMessage.PLAN_EXIT_REQ;
		}else if(this.notificationType == NotificationType.PLAN_EXIT_ACPT) {
			return NotificationMessage.PLAN_EXIT_ACPT;
		}else if(this.notificationType == NotificationType.PLAN_EXIT_RJCT) {
			return NotificationMessage.PLAN_EXIT_RJCT;
		}else if(this.notificationType == NotificationType.PLAN_REMOVED_USER) {
			return NotificationMessage.PLAN_REMOVED;
		}else if(this.notificationType == NotificationType.PLAN_REVIEW) {
			return NotificationMessage.PLAN_REVIEW;
		}
		return "has a new notification for you";
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Notification(User notificationFrom, User notificationTo, NotificationType notificationType, long referenceId,
			String message) {
		super();
		this.notificationFrom = notificationFrom;
		this.notificationTo = notificationTo;
		this.notificationType = notificationType;
		this.referenceId = referenceId;
		this.message = message;
	}

	public Notification() {
	}

	public Notification(Long id, Date createdOn, Date lastModifiedOn) {
		super(id, createdOn, lastModifiedOn);
		// TODO Auto-generated constructor stub
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}
