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
import com.travelbud.entities.Sponsor;
import com.travelbud.services.SponsorService;
import com.travelbud.services.UserService;
import com.travelbud.websocket.helper.WebSocketData;

public class SponsorServiceHandler extends AbstractServiceHandler implements HandlerAPI {
	
	private Logger log = LoggerFactory.getLogger(SponsorServiceHandler.class);

	private SponsorService sponsorService;
	
	@Override
	public AbstractServiceHandler construct(UserService userService, WSCarrier wsCarrier, Object... services)
			throws Exception {
		this.sponsorService = (SponsorService) services[0];
		setUserService(userService);
		setWsCarrier(wsCarrier);
		log.info("Initialized PostServiceHandler");
		continueProcessing(wsCarrier);
		return this;
	}

	@Override
	public void continueProcessing(WSCarrier wsCarrier) throws Exception {
		String name = wsCarrier.getName();
		log.info("Processing " + name);
		if(name.equalsIgnoreCase(WebSocketData.SPONSOR_SAVE_METHOD)) {
			saveSponsor(getObjectMapper().readValue(wsCarrier.getContent(), Sponsor.class));
		}
	}

	private void saveSponsor(Sponsor sponsor) throws NotReadablePropertyException, AccessDeniedException, JsonProcessingException {
		sponsor = sponsorService.saveSponsor(sponsor);
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_SPONSOR);
		ws.setContent(getObjectMapper().writeValueAsString(sponsor));
		
		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(sponsor.getSponsoredTo());
		notification.setReferenceId(sponsor.getId());
		notification.setNotificationType(NotificationType.SPONSOR);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
	}

}
