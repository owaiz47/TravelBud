package com.travelbud.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Message;

@Repository
public interface MessageDao extends JpaRepository<Message, Long> {
	public List<Message> findAllByMessageFromIdOrMessageToId(long messageFromId, long messageToId, Pageable pageable);
	
	public List<Message> findAllByMessageFromIdInAndMessageToIdIn(Collection<Long> ids, Collection<Long> ids2, Pageable pageable);
	
	@Query(nativeQuery = true, value = "select * from travelbud.message m where (m.message_from = ?1 and m.message_to = ?2) or (m.message_to =?1 and m.message_from =?2) order by m.id desc")
	public List<Long> getConversation(long userId, long messageToId, Pageable pageable);
	
	@Query(nativeQuery = true, value = "SELECT t1.id FROM travelbud.message AS t1 INNER JOIN ( SELECT LEAST(message_from, message_to) AS message_from, GREATEST(message_from, message_to) AS message_to, MAX(id) AS max_id FROM travelbud.message GROUP BY LEAST(message_from, message_to), GREATEST(message_from, message_to) ) AS t2 ON LEAST(t1.message_from, t1.message_to) = t2.message_from AND GREATEST(t1.message_from, t1.message_to) = t2.message_to AND t1.id = t2.max_id WHERE t1.message_from = ?1 OR t1.message_to = ?1 ORDER BY id DESC")
	public List<Long> getMyMessages(long userId);
	
	public List<Message> findAllByMessageFromIdAndMessageToId(long messageFromId, long messageToId, Pageable pageable);
}
