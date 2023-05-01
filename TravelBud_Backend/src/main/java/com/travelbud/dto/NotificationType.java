package com.travelbud.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {
	FOLLOW, MESSAGE, LIKE, COMMENT, RATING, DONATE, PLAN_COWAN_REQ, PLAN_ACCEPT_COWNA, PLAN_REJECT_COWNA, PLAN_JOIN_REQ, PLAN_JOIN_ACPT, PLAN_JOIN_RJCT, PLAN_EXIT_REQ, PLAN_EXIT_ACPT, PLAN_EXIT_RJCT, PLAN_REMOVED_USER, SPONSOR, PLAN_REVIEW;
	
	@JsonValue
	public int toValue() {
		return ordinal();
	}
}
