package com.travelbud.websocket.handlers;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travelbud.dto.NotificationType;
import com.travelbud.dto.WSCarrier;
import com.travelbud.entities.Notification;
import com.travelbud.entities.User;
import com.travelbud.services.FollowService;
import com.travelbud.services.UserService;
import com.travelbud.websocket.helper.WebSocketData;

public class FollowServiceHandler extends AbstractServiceHandler implements HandlerAPI {

	private Logger log = LoggerFactory.getLogger(FollowServiceHandler.class);
	
	private FollowService followService;

	@Override
	public AbstractServiceHandler construct(UserService userService, WSCarrier wsCarrier, Object... services)
			throws Exception {
		setUserService(userService);
		this.followService = (FollowService) services[0];
		setWsCarrier(wsCarrier);
		log.info("Initialized FollowServiceHandler");
		continueProcessing(wsCarrier);
		return this;
	}

	@Override
	public void continueProcessing(WSCarrier wsCarrier) throws Exception {
		String name = wsCarrier.getName();
		if(name.equalsIgnoreCase(WebSocketData.FOLLOW_METHOD)) {
			long userId = Long.valueOf(wsCarrier.getContent());
			saveFollow(userId);
			log.info("Continuing processing " + wsCarrier.getName());
		}
	}

	private void saveFollow(long userId) throws JsonProcessingException, AccessDeniedException {
		followService.follow(userId);
		
		User to = getUserService().getUserById(userId);

		log.info("Following user...." + to.getUsername());
		
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_USER);
		ws.setContent(getObjectMapper().writeValueAsString(to));
		
		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(to);
		notification.setReferenceId(to.getId());
		notification.setNotificationType(NotificationType.FOLLOW);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
	}

}
