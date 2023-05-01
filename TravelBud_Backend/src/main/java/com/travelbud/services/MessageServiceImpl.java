package com.travelbud.services;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelbud.dao.MessageDao;
import com.travelbud.dto.MessageInfo;
import com.travelbud.dto.MessageStatus;
import com.travelbud.dto.WSCarrier;
import com.travelbud.entities.Message;
import com.travelbud.entities.Post;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;
import com.travelbud.websocket.helper.WebSocketData;
import com.travelbud.websocket.util.WebSocketUtil;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private UserService userService;
	
	@Autowired
	private WebSocketUtil wsUtil;

	private final int MAX_MSG_SIZE = 30;

	@Override
	public Message saveMessage(Message message) throws NotReadablePropertyException, AccessDeniedException {
		checkMessageAccessibility(message, false);
		return messageDao.saveAndFlush(message);
	}

	@Override
	public List<MessageInfo> getMessages() throws AccessDeniedException {
		User authUser = userService.getAuthenticatedUser();
		List<MessageInfo> msgInfos = new ArrayList<MessageInfo>();
		List<Long> msgIds = messageDao.getMyMessages(authUser.getId());
		for (Message msg : messageDao.findAllById(msgIds)) {
			User msgInfoUser;
			if (msg.getMessageFrom().getId() == authUser.getId()) {
				msgInfoUser = msg.getMessageTo();
			} else {
				msgInfoUser = msg.getMessageFrom();
			}
			MessageInfo msgInfo = new MessageInfo();
			msgInfo.setUser(msgInfoUser);
			msgInfo.setMessages(getConversation(msgInfoUser.getId(), 0));
			String lastMsg = "";
			Date lastOn = null;
			if(!msgInfo.getMessages().isEmpty()) {
				lastMsg = msgInfo.getMessages().get(0).getMessage();
				lastOn = msgInfo.getMessages().get(0).getCreatedOn();
			}
			msgInfo.setLastMsg(lastMsg);
			msgInfo.setLastMsgOn(lastOn);
			msgInfos.add(msgInfo);
		}
		return msgInfos;
	}

	@Override
	public List<Message> getConversation(long userId, int page) throws AccessDeniedException {
		User authUser = userService.getAuthenticatedUser();
		/*return messageDao.findAllByMessageFromIdOrMessageToId(authUser.getId(), userId,
				PageRequest.of(page, MAX_MSG_SIZE, Sort.by("id").descending()));*/
		//List<Message> msgs = messageDao.findAllById(messageDao.getConversation(authUser.getId(), userId, PageRequest.of(page, MAX_MSG_SIZE, Sort.by("id").descending())));
		List<Long> ids = new ArrayList<>();
		ids.add(authUser.getId());
		ids.add(userId);
		List<Message> msgs = messageDao.findAllByMessageFromIdInAndMessageToIdIn(ids, ids, PageRequest.of(page, MAX_MSG_SIZE, Sort.by("id").descending()));
		List<Message> ret = new ArrayList<>();
		for(Message m : msgs) {//int i=msgs.size()-1; i>=0;i--
			//Message m = msgs.get(i);
			if(m.getMessageFrom().getId() == authUser.getId() && m.isSenderDeleted()) {
				continue;
			}else if(m.isReceiverDeleted()) {
				continue;
			}
			ret.add(m);
		}
		return ret;
	}

	@Override
	public Message getMessage(long id) {
		Optional<Message> message = messageDao.findById(id);
		if (message == null || !message.isPresent()) {
			throw new EntityNotFoundException(CommonErrorMessages.NO_RECORD_WITH_ID);
		}
		return message.get();
	}

	@Override
	public boolean deleteMessages(List<Message> messages) throws NotReadablePropertyException, AccessDeniedException {
		//checkMessageAccessibility(messages.get(0), true);
		User auth = userService.getAuthenticatedUser();
		for(Message msg : messages) {
			if(msg.getMessageFrom().getId() == auth.getId()) {
				msg.setSenderDeleted(true);
			}else if(msg.getMessageTo().getId() == auth.getId()) {
				msg.setReceiverDeleted(true);
			}
		}
		messageDao.saveAll(messages);
		//messageDao.deleteAll(messages);
		return true;
	}

	private void checkMessageAccessibility(Message message, boolean checkBoth)
			throws AccessDeniedException, NotReadablePropertyException {
		User authUser = userService.getAuthenticatedUser();
		if (message == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		} else if (message.getMessageFrom().getId() == null || message.getMessageFrom().getId() != authUser.getId()) {
			if (!checkBoth) {
				throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
			}
			if (message.getMessageTo().getId() == null || message.getMessageTo().getId() != authUser.getId()) {
				throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
			}
		}
	}

	@Override
	public List<Message> messagesRead(long userId) throws AccessDeniedException, JsonProcessingException {
		User authUser = userService.getAuthenticatedUser();
		List<Message> msgs = messageDao.findAllByMessageFromIdAndMessageToId(userId, authUser.getId(), PageRequest.of(0, 50, Sort.by("id").descending()));
		List<Message> forwaredMsgs = new ArrayList<>();
		for(Message msg : msgs) {
			if(msg.getMsgStatus() != MessageStatus.READ) {
				msg.setMsgStatus(MessageStatus.READ);
				forwaredMsgs.add(msg);
			}
		}
		
		if(forwaredMsgs.size() > 0)messageDao.saveAll(forwaredMsgs);
		
		return forwaredMsgs;
	}

	@Override
	public Message messageRead(long messageId) throws AccessDeniedException {
		User authUser = userService.getAuthenticatedUser();
		Optional<Message> msgOpt = messageDao.findById(messageId);
		if(!msgOpt.isPresent())return null;
		Message message = msgOpt.get();
		if(authUser.getId() != message.getMessageTo().getId())return null;
		message.setMsgStatus(MessageStatus.READ);
		messageDao.save(message);
		return message;
		
	}
}
