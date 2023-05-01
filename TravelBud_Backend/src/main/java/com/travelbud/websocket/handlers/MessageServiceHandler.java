package com.travelbud.websocket.handlers;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NotReadablePropertyException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelbud.dto.MessageStatus;
import com.travelbud.dto.NotificationType;
import com.travelbud.dto.WSCarrier;
import com.travelbud.entities.Message;
import com.travelbud.entities.Notification;
import com.travelbud.services.MessageService;
import com.travelbud.services.UserService;
import com.travelbud.websocket.helper.WSHelper;
import com.travelbud.websocket.helper.WebSocketData;

public class MessageServiceHandler extends AbstractServiceHandler implements HandlerAPI {

	private Logger log = LoggerFactory.getLogger(MessageServiceHandler.class);
	
	private MessageService messageService;
	
	@Override
	public AbstractServiceHandler construct(UserService userService, WSCarrier wsCarrier, Object... services)
			throws Exception {
		setUserService(userService);
		this.messageService = (MessageService) services[0];
		setWsCarrier(wsCarrier);
		log.info("Initialized MessageServiceHandler");
		continueProcessing(wsCarrier);
		return this;
	}

	@Override
	public void continueProcessing(WSCarrier wsCarrier) throws Exception {
		String name = wsCarrier.getName();
		if(name.equalsIgnoreCase(WebSocketData.MSG_SEND)) {
			Message message = getObjectMapper().readValue(wsCarrier.getContent(), Message.class);
			saveMessage(message);
			log.info("Continuing processing " + wsCarrier.getName());
		}else if(name.equalsIgnoreCase(WebSocketData.MSG_READ)) {
			WSHelper wsHelper = getPlanHelper(wsCarrier.getContent());
			readMessage(wsHelper.getReferenceId());
		}else if(name.equalsIgnoreCase(WebSocketData.MSGS_READ)) {
			WSHelper wsHelper = getPlanHelper(wsCarrier.getContent());
			readMessages(wsHelper.getUserId());
		}
	}

	private void saveMessage(Message message) throws NotReadablePropertyException, AccessDeniedException, JsonProcessingException {
		log.info("saving message...");	
		message.setMsgStatus(MessageStatus.SENT);//Only new messages get saved here so status sent
		message = messageService.saveMessage(message);
		
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_UPDATE_MESSAGE);
		ws.setContent(getObjectMapper().writeValueAsString(message));
		ws.setAlsoSendToUserId(message.getMessageTo().getId());
		
		WSCarrier alsoWs = new WSCarrier();
		alsoWs.setItem(WebSocketData.ITEM_MESSAGE);
		alsoWs.setContent(getObjectMapper().writeValueAsString(message));
		
		ws.setAlsoUsersWs(alsoWs);
		
		
		Notification notification = new Notification();
		notification.setNotificationFrom(getUserService().getAuthenticatedUser());
		notification.setNotificationTo(message.getMessageTo());
		notification.setReferenceId(message.getId());
		notification.setMessage(message.getMessage());
		notification.setNotificationType(NotificationType.MESSAGE);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		setReturns(ws, notifications);
	}
	
	private void readMessages(long userId) throws AccessDeniedException, JsonProcessingException {
		List<Message> msgs = messageService.messagesRead(userId);
		if(msgs == null || msgs.size() < 1) {
			setReturns(null, null);
			return;
		}
		
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_MESSAGE_LIST);
		ws.setContent(getObjectMapper().writeValueAsString(msgs));
		ws.setAlsoSendToUserId(userId);
		
		setReturns(ws, null);
	}
	
	private void readMessage(long msgId) throws AccessDeniedException, JsonProcessingException {
		Message msg = messageService.messageRead(msgId);
		if(msg == null) {
			setReturns(null, null);
		}
		
		WSCarrier ws = new WSCarrier();
		ws.setItem(WebSocketData.ITEM_UPDATE_MESSAGE);
		ws.setContent(getObjectMapper().writeValueAsString(msg));
		ws.setAlsoSendToUserId(msg.getMessageFrom().getId());
		
		setReturns(ws, null);
	}
	
	private WSHelper getPlanHelper(String content) throws JsonMappingException, JsonProcessingException {
		return getObjectMapper().readValue(content, WSHelper.class);
	}

}
