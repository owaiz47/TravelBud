package com.travelbud.controllers;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelbud.entities.Notification;
import com.travelbud.services.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationControllerImpl implements NotificationController {

	@Autowired
	private NotificationService notificationService;
	
	@PostMapping("")
	@Override
	public Notification saveNotification(@RequestBody Notification notification) throws NotReadablePropertyException, AccessDeniedException {
		return notificationService.saveNotification(notification);
	}

	@GetMapping("/{id}")
	@Override
	public List<Notification> getNotifications(@PathVariable long id) throws AccessDeniedException {
		return notificationService.getNotifications(id);
	}

	@Override
	public List<Notification> saveNotifications(@RequestBody List<Notification> notifications) throws Exception {
		return notifications;
		// TODO Auto-generated method stub
		
	}

	@GetMapping("/pending")
	@Override
	public List<Notification> getPendingNotifications() throws Exception {
		return notificationService.getPendingNotifications();
	}

}
