package com.travelbud.dto;

public enum ItemType {
	USER, POST, PLAN, MESSAGE, SPONSOR;
	
	public String getType() {
		if(this == USER) {
			return "user";
		}else if(this == POST) {
			return "post";
		}else if(this == PLAN) {
			return "plan";
		}else if(this == MESSAGE) {
			return "message";
		}else if(this == SPONSOR) {
			return "sponsor";
		}
		return null;
	}
}
