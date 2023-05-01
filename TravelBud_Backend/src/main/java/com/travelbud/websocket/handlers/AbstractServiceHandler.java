package com.travelbud.websocket.handlers;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelbud.dto.WSCarrier;
import com.travelbud.entities.Message;
import com.travelbud.entities.Notification;
import com.travelbud.services.UserService;

public class AbstractServiceHandler {
	private WSCarrier wsCarrier;
	private WSCarrier returningObject;
	private List<Notification> notifications;
	private UserService userService;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public WSCarrier getWsCarrier() {
		return wsCarrier;
	}
	public void setWsCarrier(WSCarrier wsCarrier) {
		this.wsCarrier = wsCarrier;
	}
	public WSCarrier getReturningObject() {
		return returningObject;
	}
	public void setReturningObject(WSCarrier returningObject) {
		this.returningObject = returningObject;
	}
	public List<Notification> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	
	public void setReturns(WSCarrier wsCarrier, List<Notification> notifications) {
		setReturningObject(wsCarrier);
		setNotifications(notifications);
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	public void setObjectMapper(ObjectMapper obj) {
		this.objectMapper = obj;
	}
}
