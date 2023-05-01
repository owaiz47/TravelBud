package com.travelbud.dto;

public class WSCarrier {
	private String service;
	private String name;
	private String content;
	private String item;
	private Long alsoSendToUserId;
	private WSCarrier alsoUsersWs;
	
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public Long getAlsoSendToUserId() {
		return alsoSendToUserId;
	}
	public void setAlsoSendToUserId(Long alsoSendToUserId) {
		this.alsoSendToUserId = alsoSendToUserId;
	}
	public WSCarrier getAlsoUsersWs() {
		return alsoUsersWs;
	}
	public void setAlsoUsersWs(WSCarrier alsoUsersWs) {
		this.alsoUsersWs = alsoUsersWs;
	}
}
