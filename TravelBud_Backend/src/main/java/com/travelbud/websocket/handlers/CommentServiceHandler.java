package com.travelbud.websocket.handlers;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NotReadablePropertyException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.travelbud.dto.NotificationType;
import com.travelbud.dto.WSCarrier;
import com.travelbud.entities.Comment;
import com.travelbud.entities.Notification;
import com.travelbud.services.CommentService;
import com.travelbud.services.UserService;
import com.travelbud.websocket.helper.WebSocketData;

public class CommentServiceHandler extends AbstractServiceHandler implements HandlerAPI {

	private Logger log = LoggerFactory.getLogger(CommentServiceHandler.class);

	private CommentService commentService;
	
	@Override
	public AbstractServiceHandler construct(UserService userService, WSCarrier wsCarrier, Object... services)
			throws Exception {
		this.commentService = (CommentService) services[0];
		setUserService(userService);
		setWsCarrier(wsCarrier);
		log.info("Initialized CommentServiceHandler");
		continueProcessing(wsCarrier);
		return this;
	}

	@Override
	public void continueProcessing(WSCarrier wsCarrier) throws Exception {
		log.info("Continuing to process calling " + wsCarrier.getName());
		String name = wsCarrier.getName();
		if(name.equalsIgnoreCase(WebSocketData.COMMENT_SAVE_METHOD)) {
			Comment comment = getComment(wsCarrier.getContent());
			saveComment(comment);
			log.info("Continuing processing " + wsCarrier.getName());
		}
	}

	private void saveComment(Comment comment) throws NotReadablePropertyException, AccessDeniedException, JsonProcessingException {
		comment = commentService.saveComment(comment);
		
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_POST);
		ws.setContent(getObjectMapper().writeValueAsString(comment.getPost()));
		
		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(comment.getPost().getPostedBy());
		notification.setReferenceId(comment.getPost().getId());
		notification.setNotificationType(NotificationType.COMMENT);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
	}

	private Comment getComment(String content) throws JsonMappingException, JsonProcessingException {
		return getObjectMapper().readValue(content, Comment.class);
	}

}
