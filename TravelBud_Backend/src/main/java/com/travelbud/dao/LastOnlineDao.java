package com.travelbud.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.LastOnline;

@Repository
public interface LastOnlineDao extends JpaRepository<LastOnline, Long> {
	public LastOnline findByUserId(long userId);
}
