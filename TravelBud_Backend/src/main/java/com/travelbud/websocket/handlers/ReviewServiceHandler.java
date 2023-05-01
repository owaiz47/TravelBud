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
import com.travelbud.entities.Review;
import com.travelbud.services.ReviewService;
import com.travelbud.services.UserService;
import com.travelbud.websocket.helper.WebSocketData;

public class ReviewServiceHandler extends AbstractServiceHandler implements HandlerAPI {

	private Logger log = LoggerFactory.getLogger(ReviewServiceHandler.class);

	private ReviewService reviewService; 
	
	@Override
	public AbstractServiceHandler construct(UserService userService, WSCarrier wsCarrier, Object... services)
			throws Exception {
		setUserService(userService);
		this.reviewService = (ReviewService) services[0];
		setWsCarrier(wsCarrier);
		log.info("Initialized ReviewServiceHandler");
		continueProcessing(wsCarrier);
		return this;
	}

	@Override
	public void continueProcessing(WSCarrier wsCarrier) throws Exception {
		String name = wsCarrier.getName();
		if(name.equalsIgnoreCase(WebSocketData.RATE_METHOD)) {
			Review review = getObjectMapper().readValue(wsCarrier.getContent(), Review.class);
			saveReview(review);
			log.info("Continuing processing " + wsCarrier.getName());
		}
	}

	private void saveReview(Review review) throws AccessDeniedException, JsonProcessingException {
		log.info("Saving the review");
		review = reviewService.saveReview(review);
		
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_PLAN);
		ws.setContent(getObjectMapper().writeValueAsString(review.getPlan()));
		
		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(review.getPlan().getPlanBy());
		notification.setReferenceId(review.getPlan().getId());
		notification.setNotificationType(NotificationType.PLAN_REVIEW);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
	}

}
