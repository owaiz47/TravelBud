package com.travelbud.services;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.travelbud.dao.NotificationDao;
import com.travelbud.entities.LastOnline;
import com.travelbud.entities.Notification;
import com.travelbud.entities.Post;
import com.travelbud.entities.User;
import com.travelbud.errors.CommonErrorMessages;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationDao notificationDao;
	
	@Autowired
	private LastOnlineService lastOnlineService;

	@Autowired
	private UserService userService;

	private final int MAX_NOTIFICATION_LENGTH = 20;

	@Override
	public Notification saveNotification(Notification notification) throws NotReadablePropertyException, AccessDeniedException {
		checkNotificationAccessibility(notification);
		return notificationDao.saveAndFlush(notification);
	}

	@Override
	public List<Notification> getNotifications(long id) throws AccessDeniedException {
		/*return notificationDao.findByNotificationToId(userService.getAuthenticatedUser().getId(),
				PageRequest.of(page, MAX_NOTIFICATION_LENGTH, Sort.by("id").descending()));*/
		User authUser = userService.getAuthenticatedUser();
		/*if(id == 0)return notificationDao.findByNotificationToId(authUser.getId(), PageRequest.of(0, MAX_NOTIFICATION_LENGTH, Sort.by("id").descending()));
		List<Notification> ns = notificationDao.findByNotificationToIdAndIdBefore(authUser.getId(), id, Sort.by("id").descending());//, PageRequest.of(page, MAX_NOTIFICATION_LENGTH, Sort.by("id").descending())
		*/
		List<Notification> ns = new ArrayList<>();
		if(id == 0) {
			List<Long> ids = notificationDao.getNotificationsOf(authUser.getId());
			ns = notificationDao.findAllById(ids);
		}
		else ns = notificationDao.findAllById(notificationDao.getNotificationsBefore(authUser.getId(), id));
		Collections.reverse(ns);
		return ns;
	}

	private void checkNotificationAccessibility(Notification notification)
			throws AccessDeniedException, NotReadablePropertyException {
		User currentUser = userService.getAuthenticatedUser();
		if (notification == null) {
			throw new NotReadablePropertyException(Post.class, CommonErrorMessages.NULL_ERROR);
		} else if (notification.getNotificationFrom().getId() == null
				|| notification.getNotificationFrom().getId() != currentUser.getId()) {
			throw new AccessDeniedException(CommonErrorMessages.ACCESS_DENIED);
		}
	}

	@Override
	public List<Notification> saveNotifications(List<Notification> notifications) throws Exception {
		checkNotificationAccessibility(notifications.get(0));
		return notificationDao.saveAllAndFlush(notifications);
	}

	@Override
	public List<Notification> getPendingNotifications() throws Exception {
		User authUser = userService.getAuthenticatedUser();
		LastOnline lastOnline = lastOnlineService.getLastOnline(authUser.getId());
		if(lastOnline != null) {
			List<Notification> ns = notificationDao.getPendingTransactions(authUser.getId(), lastOnline.getLastOnline());
			return ns;
		}
		return new ArrayList<Notification>();
	}

}
