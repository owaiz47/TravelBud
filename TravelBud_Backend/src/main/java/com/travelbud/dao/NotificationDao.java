package com.travelbud.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Notification;

@Repository
public interface NotificationDao extends JpaRepository<Notification, Long> {
	public List<Notification> findByNotificationToId(long userId, Pageable pageable);
	
	public List<Notification> findByNotificationToIdAndIdBefore(long userId, long id, Sort sort);
	
	@Query(nativeQuery = true, value = "select * from travelbud.notification n where notification_type != 1 and n.notification_to = ?1 order by n.id desc limit 30")
	public List<Long> getNotificationsOf(long userId);
	
	@Query(nativeQuery = true, value = "select * from travelbud.notification n where notification_type != 1 and n.notification_to = ?1 and n.id < ?2 order by n.id desc limit 30")
	public List<Long> getNotificationsBefore(long userId, long id);
	
	@Query(nativeQuery = true, value = "select * FROM travelbud.notification WHERE notification.notification_to = ?1 AND notification.created_on >= ?2 ORDER BY id DESC")
	public List<Notification> getPendingTransactions(long userId, Date lastOnline);
}
