package com.travelbud.websocket.handlers;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NotReadablePropertyException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travelbud.dto.NotificationType;
import com.travelbud.dto.WSCarrier;
import com.travelbud.entities.Notification;
import com.travelbud.entities.Post;
import com.travelbud.entities.Rating;
import com.travelbud.entities.User;
import com.travelbud.services.PostService;
import com.travelbud.services.RatingService;
import com.travelbud.services.UserService;
import com.travelbud.websocket.helper.WebSocketData;

public class RatingServiceHandler extends AbstractServiceHandler implements HandlerAPI {
	
	private Logger log = LoggerFactory.getLogger(RatingServiceHandler.class);

	private RatingService ratingService;
	
	private PostService	  postService;
	
	@Override
	public AbstractServiceHandler construct(UserService userService, WSCarrier wsCarrier, Object... services)
			throws Exception {
		setUserService(userService);
		this.ratingService = (RatingService) services[0];
		this.postService = (PostService) services[1];
		setWsCarrier(wsCarrier);
		log.info("Initialized RatingServiceHandler");
		continueProcessing(wsCarrier);
		return this;
	}

	@Override
	public void continueProcessing(WSCarrier wsCarrier) throws Exception {
		String name = wsCarrier.getName();
		if(name.equalsIgnoreCase(WebSocketData.RATE_METHOD)) {
			Rating rating = getObjectMapper().readValue(wsCarrier.getContent(), Rating.class);
			saveRating(rating);
			log.info("Continuing processing " + wsCarrier.getName());
		}
	}

	private void saveRating(Rating rating) throws JsonProcessingException, NotReadablePropertyException, AccessDeniedException {
		log.info("Saving the rating");
		ratingService.saveRating(rating);
		
		Post post = postService.getPost(rating.getPostId());
		
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_POST);
		ws.setContent(getObjectMapper().writeValueAsString(post));
		
		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		User user = new User();
		user.setId(rating.getUserId());
		notification.setNotificationTo(post.getPostedBy());
		notification.setReferenceId(post.getId());
		notification.setNotificationType(NotificationType.RATING);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
	}

}
