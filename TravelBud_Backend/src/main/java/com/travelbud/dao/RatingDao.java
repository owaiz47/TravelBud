package com.travelbud.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelbud.entities.Rating;

@Repository
public interface RatingDao extends JpaRepository<Rating, Long>{
	public Rating findByUserIdAndPostId(long userId, long postId);
}
