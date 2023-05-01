package com.travelbud.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageStatus {
	SENT, RECEIVED, READ;
	
	@JsonValue
	public int toValue() {
		return ordinal();
	}
}
