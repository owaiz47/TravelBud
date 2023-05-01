package com.travelbud.apis;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.NotReadablePropertyException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travelbud.dto.MessageInfo;
import com.travelbud.entities.Message;

public interface MessageAPI {
	public Message saveMessage(Message message) throws NotReadablePropertyException, AccessDeniedException;
	public List<MessageInfo> getMessages() throws AccessDeniedException;
	public List<Message> getConversation(long userId, int page) throws AccessDeniedException;
	public Message getMessage(long id);
	public boolean deleteMessages(List<Message> messages) throws NotReadablePropertyException, AccessDeniedException;
	public List<Message> messagesRead(long userId) throws AccessDeniedException, JsonProcessingException;
	public Message messageRead(long messageId) throws AccessDeniedException;
}
