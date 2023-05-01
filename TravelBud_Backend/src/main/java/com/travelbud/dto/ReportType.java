package com.travelbud.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReportType {
	POST, COMMENT, PLAN, USER;
	
	@JsonValue
	public int toValue() {
		return ordinal();
	}
}
