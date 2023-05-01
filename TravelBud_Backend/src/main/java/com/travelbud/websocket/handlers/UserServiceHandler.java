package com.travelbud.websocket.handlers;

import java.nio.file.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travelbud.dto.WSCarrier;
import com.travelbud.services.UserService;
import com.travelbud.websocket.helper.WebSocketData;

public class UserServiceHandler extends AbstractServiceHandler implements HandlerAPI{

	private Logger log = LoggerFactory.getLogger(UserServiceHandler.class);
	
	public UserServiceHandler() {
	}
	
	@Override
	public AbstractServiceHandler construct(UserService userService, WSCarrier wsCarrier, Object... services) throws Exception{
		setUserService(userService);
		setWsCarrier(wsCarrier);
		log.info("Initialized UserServiceHandler ");
		continueProcessing(wsCarrier);
		return this;
	}

	@Override
	public void continueProcessing(WSCarrier wsCarrier) throws AccessDeniedException, JsonProcessingException {
		String name = wsCarrier.getName();
		if(name.equalsIgnoreCase(WebSocketData.AUTHENTICATED_USER)) {
			WSCarrier ws = new WSCarrier();
			ws.setName(WebSocketData.AUTHENTICATED_USER);
			ws.setItem(WebSocketData.ITEM_USER);
            String jsonStr = getObjectMapper().writeValueAsString(getUserService().getAuthenticatedUser());
			ws.setContent(jsonStr);
			setReturns(ws, null);
			log.info("Returning Authenticated User details..");
		}
		
	}
	
}
