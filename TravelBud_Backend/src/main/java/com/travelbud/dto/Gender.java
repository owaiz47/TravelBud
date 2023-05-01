package com.travelbud.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
	MALE, FEMALE, OTHER;

	@JsonValue
	public int toValue() {
		return ordinal();
	}
}
