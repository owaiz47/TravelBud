package com.travelbud.websocket.handlers;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NotReadablePropertyException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.travelbud.dao.LikeDao;
import com.travelbud.dto.NotificationType;
import com.travelbud.dto.WSCarrier;
import com.travelbud.entities.Like;
import com.travelbud.entities.Notification;
import com.travelbud.entities.Post;
import com.travelbud.services.LikeService;
import com.travelbud.services.PostService;
import com.travelbud.services.UserService;
import com.travelbud.websocket.helper.WebSocketData;

public class LikeServiceHandler extends AbstractServiceHandler implements HandlerAPI{
	
	private Logger log = LoggerFactory.getLogger(LikeServiceHandler.class);

	private LikeService likeService;
	private PostService postService;
	
	@Override
	public AbstractServiceHandler construct(UserService userService, WSCarrier wsCarrier, Object... services)
			throws Exception {
		this.likeService = (LikeService) services[0];
		this.postService = (PostService) services[1];
		setUserService(userService);
		setWsCarrier(wsCarrier);
		log.info("Initialized PostServiceHandler");
		continueProcessing(wsCarrier);
		return this;
	}

	@Override
	public void continueProcessing(WSCarrier wsCarrier) throws Exception {
		String name = wsCarrier.getName();
		if(name.equalsIgnoreCase(WebSocketData.LIKE_METHOD)) {
			Like like = getLike(wsCarrier.getContent());
			saveLike(like);
		}else if(name.equalsIgnoreCase(WebSocketData.DISLIKE_METHOD)) {
			Like like = getLike(wsCarrier.getContent());
			dislike(like);
		}
	}

	private void saveLike(Like like) throws AccessDeniedException, JsonProcessingException {
		likeService.saveLike(like);
		Post post = postService.getPost(like.getPostId());
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_POST);
		ws.setContent(postToJson(post));
		
		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(post.getPostedBy());
		notification.setReferenceId(post.getId());
		notification.setNotificationType(NotificationType.LIKE);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
	}
	
	private void dislike(Like like) throws NotReadablePropertyException, AccessDeniedException {
		likeService.deleteLike(like);
	}

	private String postToJson(Post post) throws JsonProcessingException {
		return getObjectMapper().writeValueAsString(post);
	}

	private Like getLike(String content) throws JsonMappingException, JsonProcessingException {
		return getObjectMapper().readValue(content, Like.class);
	}

}
