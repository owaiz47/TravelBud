package com.travelbud.apis;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.aspectj.weaver.ast.Not;
import org.springframework.beans.NotReadablePropertyException;

import com.travelbud.entities.Notification;

public interface NotificationAPI {
	public Notification saveNotification(Notification notification) throws NotReadablePropertyException, AccessDeniedException;
	public List<Notification> getPendingNotifications() throws Exception;
	public List<Notification> saveNotifications(List<Notification> notifications) throws Exception;
	public List<Notification> getNotifications(long id) throws AccessDeniedException;//if id is not null get notifications previous to id
}
