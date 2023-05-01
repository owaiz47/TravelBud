package com.travelbud.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Like;

@Repository
public interface LikeDao extends JpaRepository<Like, Long>{
	public Like findByPostIdAndUserId(long postId, long userId);
}
