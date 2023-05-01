package com.travelbud.controllers;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelbud.configs.WSWebAuthenticationDetailsSource;
import com.travelbud.dto.MessageInfo;
import com.travelbud.entities.Message;
import com.travelbud.entities.Post;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;
import com.travelbud.services.MessageService;
import com.travelbud.services.UserService;

@RestController
@RequestMapping("/messages")
public class MessageControllerImpl implements MessageController {

	@Autowired
	private MessageService messageService;

	@PostMapping("")
	@Override
	public Message saveMessage(@RequestBody Message message) throws NotReadablePropertyException, AccessDeniedException {
		return messageService.saveMessage(message);
	}

	@GetMapping("")
	public List<MessageInfo> getMessages() throws AccessDeniedException {
		return messageService.getMessages();
	}

	@GetMapping("/{userId}/page/{page}")
	@Override
	public List<Message> getConversation(@PathVariable long userId, @PathVariable int page) throws AccessDeniedException {
		return messageService.getConversation(userId, page);
	}

	@GetMapping("/{id}")
	@Override
	public Message getMessage(@PathVariable long id) {
		return messageService.getMessage(id);
	}

	@PostMapping("/delete/")
	@Override
	public boolean deleteMessages(@RequestBody List<Message> messages) throws NotReadablePropertyException, AccessDeniedException {
		return messageService.deleteMessages(messages);
	}

	@Override
	public List<Message> messagesRead(long userId) {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public Message messageRead(long messageId) throws AccessDeniedException {
		return null;
		// TODO Auto-generated method stub
		
	}

}
